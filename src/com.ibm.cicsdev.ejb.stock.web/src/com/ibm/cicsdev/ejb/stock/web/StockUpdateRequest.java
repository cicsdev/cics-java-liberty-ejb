package com.ibm.cicsdev.ejb.stock.web;

/* Licensed Materials - Property of IBM                               */
/*                                                                    */
/* SAMPLE                                                             */
/*                                                                    */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                   */
/*                                                                    */
/* US Government Users Restricted Rights - Use, duplication or        */
/* disclosure restricted by GSA ADP Schedule Contract with IBM Corp   */
/*                                                                    */

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
