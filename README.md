# cics-java-liberty-ejb
Java EE EJB sample application to simulate a simple web shop to follow the article in the CICS developer center

## Supporting Files
* com.ibm.cicsdev.ejb - EJB project containing the session beans
* com.ibm.cicsdev.ejb.app - EAR project which combines all other projects
* com.ibm.cicsdev.ejb.bundle - CICS bundle project
* com.ibm.cicsdev.ejb.config - Sample configuration files
* com.ibm.cicsdev.ejb.shop.web - WAR project containing the web store JSF front-end
* com.ibm.cicsdev.ejb.stock.web - WAR project containing the stock management JAX-RS webservice

## Java Code
* com.ibm.cicsdev.ejb
  * CartBean.java - Session bean representing a shopping cart
  * CatalogueBean.java - Session bean responsible for managing the shop catalogue
  * Item.java - Java class representing a shop item
* com.ibm.cicsdev.ejb.shop.web
  * Cart.java - JSF managed bean proxying the CartBean
  * Catalogue.java - JSF managed bean proxying the CatalogueBean
* com.ibm.cicsdev.ejb.stock.web
  * StockApplication.java - JAX-RS application defining the REST endpoints
  * StockCreateRequest.java - Java class representing a request to create a new item in stock
  * StockUpdateRequest.java - Java class representing a request to update existing stock

## Pre-requirements
* CICS TS for z/OS V5.3 or later
* Java SE 1.7 or later on the z/OS System

### Optional Requirements
* IBM CICS Explorer - Local development and bundle deployment
* z/OS Provisioning Toolkit - Provisioning and deployment

