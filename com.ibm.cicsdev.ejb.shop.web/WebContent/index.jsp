<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<!-- Licensed Materials - Property of IBM                             -->
<!--                                                                  -->
<!-- SAMPLE                                                           -->
<!--                                                                  -->
<!-- (c) Copyright IBM Corp. 2017 All Rights Reserved                 -->
<!--                                                                  -->
<!-- US Government Users Restricted Rights - Use, duplication or      -->
<!-- disclosure restricted by GSA ADP Schedule Contract with IBM Corp -->
<html>
<head>
<title>CICS EJB Sample</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="styles/cics.css" />

<!-- Bootstrap CDN -->
<link rel="stylesheet" href="styles/bootstrap.min.css" />

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
</head>
<body class="container">
	<f:view>
		<header class="header clearfix">
			<nav>
				<ul class="nav nav-pills pull-right">
					<li  class="active"><a href="#">Shop</a></li>
					<li><a href="cart.html">Cart (#{ cart.items.size()})</a>
					<li>
				</ul>
			</nav>
			<h1>CICS EJB Sample</h1>
			<hr />
		</header>

		<section class="row">
			<div class="col-lg-6 col-lg-offset-3 center">
				<img src="img/cics.png" style="width: 30em" />

				<h2>CICS EJB Store</h2>
				<p>
					A sample for using <abbr title="Enterprise JavaBeans"
						class="initialism">EJB</abbr> session beans to provide a simple
					web shop backed by CICS.
				</p>
			</div>
		</section>

		<section>
			<h2>Store</h2>

			<h:messages globalOnly="false" layout="table"
				infoClass="alert alert-success" warnClass="alert alert-warning"
				errorClass="alert alert-danger" fatalClass="alert alert-danger">
			</h:messages>

			<h:form>
				<table class="table table-hover">
					<thead>
						<tr>
							<th class="col-md-2">ID</th>
							<th class="col-md-9">Name</th>
							<th class="col-md-1"></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="#{ catalogue.items }">
							<tr>
								<td>#{ item.id }</td>
								<td>#{ item.name }</td>
								<c:if test="${ item.stock > 0 }">
									<td><h:commandButton
											styleClass="btn btn-default col-md-12" value="Add to Cart"
											action="#{ cart.add(item) }" /></td>
								</c:if>
								<c:if test="${item.stock <= 0 }">
									<td><span class="btn btn-default disabled col-md-12">Out
											of stock</span></td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</h:form>
		</section>
		<hr />
		<footer class="footer">&copy;IBM&reg; 2017</footer>
	</f:view>
</body>
</html>