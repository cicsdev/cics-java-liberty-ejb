package com.ibm.cicsdev.ejb;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.InvalidQueueIdException;
import com.ibm.cics.server.ItemHolder;
import com.ibm.cics.server.LengthErrorException;
import com.ibm.cics.server.TSQ;

/**
 * A bean which handles the loading and saving of the shop catalogue.
 * 
 * @author Alexander Brown
 */
@Singleton
@DeclareRoles("Administrator")
public class CatalogueBean
{
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
            catch (CicsException e)
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
    
    @RolesAllowed("Administrator")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Item getItem(int id) throws IOException
    {
        TSQ dataSource = getDataSource();
        ItemHolder holder = new ItemHolder();
        
        try
        {
            dataSource.readItem(id, holder);
        }
        catch(CicsException e)
        {
            throw new IOException("Failed to read item " + id + " from CICS.", e);
        }
        
        return createItem(id, holder.getStringValue());
    }

    @RolesAllowed("Administrator")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Item addItem(String name, int stock) throws IOException
    {
        TSQ dataSource = getDataSource();
        
        try
        {
            int id = dataSource.writeString(name + "," + stock);
            
            return new Item(this, id, name, stock);
        }
        catch(CicsException e)
        {
            throw new IOException("Failed to create item.", e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @RolesAllowed("Administrator")
    public Item addStock(int id, int ammount) throws IOException
    {
        if(ammount <= 0)
        {
            throw new IOException("Cannot add a negative ammount of stock.");
        }
        
        return changeStock(id, ammount);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void purchase(int id) throws IOException
    {
        changeStock(id, -1);
    }
    
    private TSQ getDataSource() {
        TSQ dataSource = new TSQ();
        dataSource.setName("CATALOGUE");
        
        return dataSource;
    }

    private Item createItem(int id, String itemStr)
    {
        // The item string comma separated list of the item attributes
        String[] itemParts = itemStr.split(",");
        String name = itemParts[0];
        int stock = Integer.parseInt(itemParts[1]);
        
        return new Item(this, id, name, stock);
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    private Item changeStock(int id, int ammount) throws IOException
    {
        TSQ dataSource = getDataSource();
        
        try
        {
            Item item = getItem(id);
            
            // Update the stock
            int currentStock = item.getStock();
            int newStock = currentStock + ammount;
            item.setStock(newStock);
            
            dataSource.rewriteItem(item.getId(), getData(item));
            
            return item;
        }
        catch(CicsException e)
        {
            throw new IOException("Failed to update item " + id, e);
        }
    }
    
    private byte[] getData(Item item) {
        return (item.getName() + "," + item.getStock()).getBytes(Charset.forName("IBM1047"));
    }
}
