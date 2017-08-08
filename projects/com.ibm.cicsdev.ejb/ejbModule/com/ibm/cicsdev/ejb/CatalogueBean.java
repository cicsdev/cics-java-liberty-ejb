/* Licensed Materials - Property of IBM                               */
/*                                                                    */
/* SAMPLE                                                             */
/*                                                                    */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                   */
/*                                                                    */
/* US Government Users Restricted Rights - Use, duplication or        */
/* disclosure restricted by GSA ADP Schedule Contract with IBM Corp   */
/*                                                                    */
package com.ibm.cicsdev.ejb;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.InvalidQueueIdException;
import com.ibm.cics.server.ItemHolder;
import com.ibm.cics.server.LengthErrorException;
import com.ibm.cics.server.TSQ;

/**
 * A bean which handles the loading and saving of the shop catalogue.
 * 
 * @author Alexander Brown
 */
@Stateless
@DeclareRoles("Administrator")
public class CatalogueBean
{
    /**
     * Loads the catalogue of items from CICS.
     * 
     * @return A list of items contained in the catalogue.
     * 
     * @throws IOException
     */
    public List<Item> getCatalouge() throws IOException
    {
        // Define the TSQ object
        TSQ catalogueQueue = getDataSource();
        
        ItemHolder holder = new ItemHolder();
        
        List<Item> items = new ArrayList<>();
        
        // Loop through every item in the TSQ
        int size = 1;
        for (int i = 1; i <= size; i++)
        {
            try
            {
                // Read the next item
                size = catalogueQueue.readItem(i, holder);
            }
            catch (LengthErrorException | InvalidQueueIdException e)
            {
                // LENGERR means we've read beyond the TSQ
                // QIDERR means the TSQ doesn't actually exist
                // Both of these mean that the catalogue is empty
                break;
            }
            catch (CicsConditionException e)
            {
                // Other exceptions mean something more serious is going on
                throw new IOException("Failed to read item from CICS temporary storage", e);
            }
            
            // Create the item object and add to the list
            Item item = createItem(i, holder.getStringValue());
            items.add(item);
        }
        return items;
    }
    
    /**
     * Gets a single item by it's ID.
     * 
     * @param id
     *            The ID of the item.
     * @return The loaded item.
     * @throws IOException
     */
    @RolesAllowed("Administrator")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Item getItem(int id) throws IOException
    {
        // Define the TSQ object
        TSQ dataSource = getDataSource();
        ItemHolder holder = new ItemHolder();
        
        // Read the item from the TSQ
        try
        {
            dataSource.readItem(id, holder);
        }
        catch (CicsConditionException e)
        {
            throw new IOException("Failed to read item " + id + " from CICS.", e);
        }
        
        // Create the item
        return createItem(id, holder.getStringValue());
    }
    
    /**
     * Adds an item to the catalogue. Can only be run by users in the
     * Administrator role.
     * 
     * @param name
     *            The name of the item
     * @param stock
     *            The ammount of stock the item has
     * @return The details of the created item.
     * @throws IOException
     */
    @RolesAllowed("Administrator")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Item addItem(String name, int stock) throws IOException
    {
        // Define the TSQ object
        TSQ dataSource = getDataSource();
        
        try
        {
            // Write the item to the TSQ and record the position in the TSQ
            int id = dataSource.writeString(name + "," + stock);
            
            // Return the details of the item
            return new Item(this, id, name, stock);
        }
        catch (CicsConditionException e)
        {
            throw new IOException("Failed to create item.", e);
        }
    }
    
    /**
     * Adds more stock to an existing item.
     * 
     * @param id
     *            The ID of the item
     * @param ammount
     *            The ammount of stock to add
     * @return The details of the updated item
     * @throws IOException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @RolesAllowed("Administrator")
    public Item addStock(int id, int ammount) throws IOException
    {
        // Ensure the stock ammount is positive
        if (ammount <= 0)
        {
            throw new IOException("Cannot add a negative ammount of stock.");
        }
        
        // Update the stock of the item
        return changeStock(id, ammount);
    }
    
    /**
     * Purchase an item.
     * 
     * @param id
     *            The ID of the item.
     * @throws IOException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void purchase(int id) throws IOException
    {
        // Remove one of the item.
        changeStock(id, -1);
    }
    
    /**
     * @return The data source used to store the catalogue. In this case a CICS
     *         TSQ.
     */
    private TSQ getDataSource()
    {
        // Create the TSQ item and set the name.
        TSQ dataSource = new TSQ();
        dataSource.setName("CATALOGUE");
        
        return dataSource;
    }
    
    /**
     * Create an object representation of an item
     * 
     * @param id
     *            The ID of the item
     * @param itemStr
     *            The string returned from the CICS TSQ in the format:
     *            "name,stock".
     * @return The item object representation.
     */
    private Item createItem(int id, String itemStr)
    {
        // The item string comma separated list of the item attributes
        String[] itemParts = itemStr.split(",");
        String name = itemParts[0];
        int stock = Integer.parseInt(itemParts[1]);
        
        return new Item(this, id, name, stock);
    }
    
    /**
     * Change the stock of an item.
     * 
     * @param id
     *            The ID of the item.
     * @param ammount
     *            The amount to change the stock by.
     * @return The updated item.
     * @throws IOException
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    private Item changeStock(int id, int ammount) throws IOException
    {
        // Get the data source
        TSQ dataSource = getDataSource();
        
        try
        {
            // Load the item from the TSQ.
            Item item = getItem(id);
            
            // Update the stock
            int currentStock = item.getStock();
            int newStock = currentStock + ammount;
            item.setStock(newStock);
            
            // Update the item in the TSQ
            dataSource.rewriteItem(item.getId(), getData(item));
            
            // Return the updated item
            return item;
        }
        catch (CicsConditionException e)
        {
            throw new IOException("Failed to update item " + id, e);
        }
    }
    
    /**
     * Gets an item in byte array form to easily write to a TSQ.
     * 
     * @param item
     *            The item to get in byte array form.
     * @return The byte array.
     */
    private byte[] getData(Item item)
    {
        return (item.getName() + "," + item.getStock()).getBytes(Charset.forName("IBM1047"));
    }
}
