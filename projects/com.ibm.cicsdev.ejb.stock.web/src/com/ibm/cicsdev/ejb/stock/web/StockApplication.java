/* Licensed Materials - Property of IBM                               */
/*                                                                    */
/* SAMPLE                                                             */
/*                                                                    */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                   */
/*                                                                    */
/* US Government Users Restricted Rights - Use, duplication or        */
/* disclosure restricted by GSA ADP Schedule Contract with IBM Corp   */
/*                                                                    */
package com.ibm.cicsdev.ejb.stock.web;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import com.ibm.cicsdev.ejb.CatalogueBean;
import com.ibm.cicsdev.ejb.Item;

/**
 * JAX-RS Application to manage stock.
 * <p>
 * <strong>Note:</strong> JAX-RS allows applications to be defined as EJBs, to
 * make injection easier. Here we define it as stateless and to never have a
 * transaction (as further methods may require a new transaction).
 * 
 * @author Alexander Brown
 */
@ApplicationPath("/api")
@Path("/items")
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class StockApplication extends Application
{
    /** Inject the catalogue session bean */
    @EJB
    private CatalogueBean catalogue;
    
    /**
     * Create an item in the catalogue.
     * 
     * @param item
     *            The item to create.
     * @return The created item.
     * @throws IOException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Item createItem(StockCreateRequest item) throws IOException
    {
        return catalogue.addItem(item.getName(), item.getStock());
    }
    
    /**
     * Get an item in the catalogue
     * 
     * @param id
     *            The ID of the item.
     * @return The item.
     * @throws IOException
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Item getItem(@PathParam("id") int id) throws IOException
    {
        return catalogue.getItem(id);
    }
    
    /**
     * Update an item's stock
     * 
     * @param id
     *            The ID of the item
     * @param update
     *            The request to update the item.
     * @return The updated item
     * @throws IOException
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Item updateStock(@PathParam("id") int id, StockUpdateRequest update) throws IOException
    {
        return catalogue.addStock(id, update.getAmmount());
    }
}
