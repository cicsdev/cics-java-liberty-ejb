# cics-java-liberty-ejb
A Java EE EJB sample application to simulate a simple web shop to follow the article in the CICS developer center

## Supporting Files
* [com.ibm.cicsdev.ejb](projects/com.ibm.cicsdev.ejb) - EJB project containing the session beans
* [com.ibm.cicsdev.ejb.app](projects/com.ibm.cicsdev.ejb.app) - EAR project which combines all other projects
* [com.ibm.cicsdev.ejb.config](etc/config) - Sample configuration files
* [com.ibm.cicsdev.ejb.shop.web](projects/com.ibm.cicsdev.shop.web) - WAR project containing the web store JSF front-end
* [com.ibm.cicsdev.ejb.stock.web](projects/com.ibm.cicsdev.ejb.stock.web) - WAR project containing the stock management JAX-RS webservice
* [com.ibm.cicsdev.ejb.bundle](etc/com.ibm.cicsdev.ejb.bundle) - CICS bundle project

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

## Requirements
* CICS TS for z/OS V5.3 with APAR [PI63877](http://www-01.ibm.com/support/docview.wss?uid=swg1PI63877) applied, or CICS TS for z/OS V5.4, or later.

### Optional Requirements
* Eclipse with WebSphere Developer Tools and CICS Explorer SDK installed - Local development and bundle deployment

## Deploying the Sample

To deploy the sample you will need to import the projects into CICS Explorer. 

To install the sample as a CICS bundle:

1. Export the CICS bundle from Eclipse by selecting the project com.ibm.cicsdev.ejb.bundle > Export Bundle Project to z/OS UNIX File System. 
2. Define and install a JVMSERVER resource named `DFHWLP` in the CICS region.
3. Add the features `ejbLite-3.2`, `jsf-2.2` and `jaxrs-2.0` the the `featureManager` element in the Liberty JVM server's server.xml configuration file.
4. Define and install a BUNDLE resource.

To install the sample through Liberty configuration
1. Export the EAR project from Eclipse by selecting the project com.ibm.cicsdev.ejb.app > File > Export > EAR file > Next > choose a destination > Finish.
2. Copy the EAR file in binary to the `apps` directory in the Liberty configuration directory on zFS.
3. Replace the Liberty configuration file `server.xml` or update elements featureManager, safRegistry and application using [server.xml](etc/config/server.xml) as a basis.
4. Install a JVMSERVER resource in the CICS region.

If the sample is correctly deployed, you should see the following messages in the Liberty logs:

```
A CWWKT0016I: Web application available (default_host): http://mvs.example.ibm.com/shop/
...
A CWWKT0016I: Web application available (default_host): http://mvs.example.ibm.com/stock/
```

If you use CICS bundle deployment, you will also need to define a RACF profile for users to access the stock REST API.

```
RDEFINE EJBROLE BBGZDFLT.com.ibm.cicsdev.ejb.Administrator UACC(NONE) 
PERMIT BBGZDFLT.com.ibm.cicsdev.ejb.Administrator CLASS(EJBROLE) ACCESS(READ) ID(WEBUSER) 
```

## Running the Sample
To create new items in the store send a HTTP request to the stock API:

```http
POST /stock/api/items HTTP/1.1
Host: mvs.example.ibm.com
Content-Type: application/json
Authentication: BASIC <base64 encoded username,password>

{ "name": "CICS TS for z/OS", "stock": 2 }
```
The response returned will be similar to:

```http
HTTP/1.1 200 OK
Content-Type: application/json

{"id":1,"name":"CICS TS for z/OS","stock":2}
```

**Note:** We use BASIC authentication here, but any form of HTTP or HTTPS authentication would work.

You can use this request using the command line tool cURL:

```shell
curl mvs.example.ibm.com/stock/api/items/ -X POST -d '{ "name": "CICS TS for z/OS", "stock": 2 }' -H 'Content-Type: application/json' --user MVSUSER1
```

Once one or more items have been created, you can then use a browser to navigate to http://mvs.example.ibm.com/shop/ and use the shop.

More stock can be added to an item through the following request (in this case we update item with the ID `1`):

```http
PUT /stock/api/items/1
Host: mvs.example.ibm.com
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
Host: mvs.example.ibm.com
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

1. In Eclipse select *File* > *Import* > *General/Existing Projects into Workspace*
2. *Browse* to the `projects` directory.
3. Ensure all 4 projects are checked
4. *Finish* to import the source projects.

Optionally, you can import the CICS bundle project by following the same steps, but with the `etc` directory.

## Notes
[Twitter Bootstrap](http://getbootstrap.com/) is linked to for styling of the JSF web pages. This is pulled from a content delivery network (CDN) online. If external links are blocked, these web pages will continue to work without Bootstrap, but the Bootstrap CSS can also be downloaded and added by changing the `link` element:

```html
<!-- Replace the CDN Boostrap CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous" />

<!-- With the downloaded Bootstrap CSS -->
<link rel="stylesheet" href="path/to/bootstrap.min.css" />
```
