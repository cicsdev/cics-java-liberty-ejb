package com.ibm.cicsdev.ejb.shop.web;

import java.io.IOException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.ibm.cicsdev.ejb.CartBean;
import com.ibm.cicsdev.ejb.Item;

@ManagedBean
@SessionScoped
public class Cart {
	@EJB
	private CartBean cart;
	
	public Collection<Item> getItems() {
		return cart.getItems();
	}
	
	public void add(Item item) {
		if(!cart.add(item)) {
			FacesContext.getCurrentInstance().addMessage("remove", new FacesMessage(FacesMessage.SEVERITY_WARN, "Reserved by another", "The item is currently reserved by another. Try again later."));
		}
	}
	
	public void remove(Item item) {
		if(!cart.remove(item)) {
			FacesContext.getCurrentInstance().addMessage("remove", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not remove item", "Failed to remove item"));
		}
	}
	
	public void purchase() {
		try {
			if(cart.purchase()) {
				FacesContext.getCurrentInstance().addMessage("purchase", new FacesMessage(FacesMessage.SEVERITY_INFO, "Purchase successful", "All items purchased successfully."));
			} else {
				FacesContext.getCurrentInstance().addMessage("purchase", new FacesMessage(FacesMessage.SEVERITY_WARN, "No items in cart", "The cart contains no items."));
			}
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage("purchase", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Purchase failed", e.getLocalizedMessage()));
		}
	}
}
