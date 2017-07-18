package com.ibm.cicsdev.ejb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.ibm.cics.server.CicsException;

/**
 * Business logic for controlling an online shopping cart.
 * 
 * @author Alexander Brown
 */
@Stateful
public class CartBean
{
    /** The items currently in the cart */
    private List<Item> items = new ArrayList<>();
    
    /**
     * Add an item to the cart.
     * 
     * @param item
     *            The item to add
     * @return <code>true</code> if the item was added. <code>false</code> if it
     *         could potentially be out of stock
     */
    public boolean add(Item item)
    {
        // Add the item to the cart
        return this.items.add(item);
    }
    
    /**
     * Remove an item from the cart.
     * 
     * @param item
     *            The item to remove
     * @return <code>true</code> if the item was removed.
     */
    public boolean remove(Item item)
    {
        // Remove the item
        return this.items.remove(item);
    }
    
    /**
     * @return The items in the cart
     */
    public List<Item> getItems()
    {
        return this.items;
    }
    
    /**
     * Purchase the items currently in the cart.
     * 
     * @return <code>true</code> if the cart was purchased successfully.
     * @throws CicsException
     * @throws IOException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean purchase() throws IOException
    {
        // If the cart is empty nothing can be purchased
        if (items.isEmpty())
        {
            return false;
        }
        
        // Purchase every item
        for (Item item : items)
        {
            item.purchase();
        }
        
        // Clear the cart
        clear();
        
        return true;
    }
    
    /**
     * Clear the cart
     */
    @Remove
    public void clear()
    {
        // Clear the array
        this.items.clear();
    }
}
