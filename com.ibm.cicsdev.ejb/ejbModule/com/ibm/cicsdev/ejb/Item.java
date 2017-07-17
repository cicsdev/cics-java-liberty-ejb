package com.ibm.cicsdev.ejb;

import java.io.IOException;

public class Item
{
    private CatalogueBean catalogue;
    
    private int id;
    private String name;
    
    private int stock;
    
    private int reseved = 0;
    
    public Item(CatalogueBean catalogue, int id, String name, int stock)
    {
        this.catalogue = catalogue;
        this.id = id;
        this.name = name;
        this.stock = stock;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getStock()
    {
        synchronized (this)
        {
            return this.stock;
        }
    }
    
    public void setStock(int stock)
    {
        this.stock = stock;
    }
    
    public int getReseved()
    {
        return reseved;
    }
    
    public boolean reserve()
    {
        synchronized (this)
        {
            if (reseved >= stock)
            {
                return false;
            }
            
            reseved++;
            return true;
        }
    }
    
    public void unreserve()
    {
        synchronized (this)
        {
            reseved--;
        }
    }
    
    public void purchase() throws IOException
    {
        this.catalogue.purchase(this.id);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Item)
        {
            Item other = (Item) o;
            
            return other.id == this.id;
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return id;
    }
}
