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

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
@ApplicationPath("/api")
@Path("/items")
public class StockApplication extends Application
{
    @EJB private CatalogueBean catalogue;
    
    @GET
    public String test() {
        return "Test";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Item createItem(StockCreateRequest item) throws IOException
    {
        return catalogue.addItem(item.getName(), item.getStock());
    }
    
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Item getItem(@PathParam("id") int id) throws IOException {
        return catalogue.getItem(id);
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Item updateStock(@PathParam("id") int id, StockUpdateRequest update) throws IOException
    {
        return catalogue.addStock(id, update.getAmmount());
    }
}
