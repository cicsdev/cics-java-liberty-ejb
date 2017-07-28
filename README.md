# cics-java-liberty-ejb
A Java EE EJB sample application to simulate a simple web shop to follow the article in the CICS developer center

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
* CICS TS for z/OS V5.3 with APAR PI63877 applied, CICS TS for z/OS V5.4 or later.
* Java SE 1.7 or later on the z/OS System

### Optional Requirements
* Eclipse with WebSphere Developer Tools and CICS Explorer SDK installed - Local development and bundle deployment
* [z/OS Provisioning Toolkit](https://developer.ibm.com/mainframe/products/zospt/) - Provisioning and deployment

## Deploying the Sample
The simplest deployment strategy is to use `zospt` (z/OS Provisioning Toolkit). Follow these [instructions](etc/zospt/README.md).

Otherwise, import the projects into CICS Explorer. And deploy the CICS bundle to z/FS. Then define and install the bundle in the CICS region, along with a Liberty JVM server. The features `ejbLite-3.2`, `jaxrs-2.0` and `jsf-2.2` need to be added to the Liberty `server.xml` file.

An example [server.xml](etc/config/server.xml) configuration is provided which can be used to deploy the application directly from USS. To take this route, simple FTP a compiled `ear` file to the Liberty configuration directory of the Liberty JVM server, under the `apps` directory.

If the sample is correctly deploy, you should see the following messages:

```
A CWWKT0016I: Web application available (default_host): http://mvs.hursley.ibm.com/shop/
...
A CWWKT0016I: Web application available (default_host): http://mvs.hursley.ibm.com/stock/
```

## Running the Sample
To create new items in the store send a HTTP request to the stock API:

```http
POST /stock/api/items HTTP/1.1
Host: mvs.hursley.ibm.com
Content-Type: application/json
Authentication: BASIC <base64 encoded username,password>

{ "name": "CICS TS for z/OS", "stock": 2 }

```

```http
HTTP/1.1 200 OK
Content-Type: application/json

{"id":1,"name":"CICS TS for z/OS","stock":2}

```

**Note:** We use BASIC authentication here, but any form of HTTP or HTTPS authentication would work.

You can use this request using the command line tool cURL:

```shell
curl mvs.hursley.ibm.com/stock/api/items/ -X POST -d '{ "name": "CICS TS for z/OS", "stock": 2 }' -H 'Content-Type: application/json' --user MVSUSER1
```

Once one or more items have been created, you can then use a browser to navigate to http://mvs.hursley.ibm.com/shop/ and use the shop.

More stock can be added to an item through the following request (in this case we update item with the ID `1`):

```http
PUT /stock/api/items/1
Host: mvs.hurlsey.ibm.com
Content-Type: application/json
Authentication: BASIC <base64 encoded username,password>

{ "ammount": 10 }

```

```http
HTTP/1.1 200 OK
Content-Type: application/json

{"id":1,"name":"CICS TS for z/OS","stock":12}

```

You can view the current state of an item by sending a GET request (in this case we get the item with the ID `1`):
```http
GET /stock/api/items/1
Host: mvs.hurlsey.ibm.com
Accept: application/json
Authentication: BASIC <base64 encoded username,password>


```

```http
HTTP/1.1 200 OK
Content-Type: application/json

{"id":1,"name":"CICS TS for z/OS","stock":12}

```

### Further Configuration
Because TSQs are not recoverable by default, to get the benefit of EJB transactions in the project, you would need to define a TS Model similar to this:


```
DEFINE TSMODEL(JAVAEJB) GROUP(SAMPEJB) PREFIX(CATALOGUE) RECOVERABLE(YES)
```

## Importing the Projects into Eclipse
All the projects with code are Eclipse projects. To import these projects:

1. In Eclipse click *File* -> *Import* -> *General/Existing Projects into Workspace*
2. *Browse* to the `src` directory.
3. Ensure all 4 projects are checked
4. *Finish* to import the source projects.

Optionally, you can import the CICS bundle project by following the same steps, but with the `etc` directory.
