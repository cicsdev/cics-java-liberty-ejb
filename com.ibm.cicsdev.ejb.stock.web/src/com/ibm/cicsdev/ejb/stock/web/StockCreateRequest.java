package com.ibm.cicsdev.ejb.stock.web;

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
