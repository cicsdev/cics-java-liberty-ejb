package com.ibm.cicsdev.ejb.shop.web;

import java.io.IOException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import com.ibm.cicsdev.ejb.CatalogueBean;
import com.ibm.cicsdev.ejb.Item;

@ManagedBean
public class Catalogue {
	@EJB private CatalogueBean catalogue;
	
	public Collection<Item> getItems() throws IOException {
		return this.catalogue.getCatalouge();
	}
}
