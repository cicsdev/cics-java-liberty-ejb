package com.ibm.cicsdev.ejb.stock.web;

/**
 * Class representing a request to create an item in the catalogue.
 * 
 * @author Alexander Brown
 */
public class StockCreateRequest
{
    private String name;
    private int stock;
    
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
        return stock;
    }
    
    public void setStock(int stock)
    {
        this.stock = stock;
    }
    
}
