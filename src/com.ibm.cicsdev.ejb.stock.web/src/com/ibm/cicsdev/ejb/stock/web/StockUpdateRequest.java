package com.ibm.cicsdev.ejb.stock.web;

/**
 * Class representing a request to update the stock of an item.
 * 
 * @author Alexander Brown
 */
public class StockUpdateRequest
{
    private int ammount;
    
    public int getAmmount()
    {
        return ammount;
    }
    
    public void setAmmount(int ammount)
    {
        this.ammount = ammount;
    }
    
}
