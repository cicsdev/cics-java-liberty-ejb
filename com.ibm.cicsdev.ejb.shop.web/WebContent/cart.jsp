<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>
<title>CICS EJB Sample</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="styles/bootstrap.min.css" />
<link rel="stylesheet" href="styles/bootstrap-theme.min.css" />
<link rel="stylesheet" href="styles/cics.css" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
</head>
<body class="container">
	<f:view>
		<div class="masthead">
			<header>
				<h3>CICS EJB Sample</h3>
			</header>
			<nav>
				<ul class="nav nav-pills">
					<li><a href="index.html">Shop</a></li>
					<li class="active"><a href="#">Cart (#{ cart.items.size()})</a>
					<li>
				</ul>
			</nav>
		</div>
		
		<section class="jumbotron">
			<img class="img-responsive"
				src="http://cicsdev.github.io/imgs/cicsdevGithub.png" />
			<div class="text-center">
				<p class=lead>
					A sample for using <abbr title="Enterprise JavaBeans"
						class="initialism">EJB</abbr> session beans to provide a simple
					web shop backed by CICS.
				</p>
			</div>
		</section>
		
		<section>
			<h2>Cart</h2>
			<h:form>
				<h:messages globalOnly="false" layout="table"
					infoClass="alert alert-success" warnClass="alert alert-warning"
					errorClass="alert alert-danger" fatalClass="alert alert-danger" />

				<table class="table table-hover">
					<thead>
						<tr>
							<th class="col-md-2">ID</th>
							<th class="col-md-9">Name</th>
							<th class="col-md-1"></th>
						</tr>
					</thead>
					<tbody id="cart">
						<c:forEach var="item" items="#{ cart.items }">
							<tr>
								<td>#{ item.id }</td>
								<td>#{ item.name }</td>
								<td><h:commandButton styleClass="btn btn-danger"
										value="Remove" action="#{ cart.remove(item) }" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</h:form>

			<h:form>
				<h:commandButton value="Purchase" styleClass="btn btn-primary"
					action="#{ cart.purchase() }"></h:commandButton>
			</h:form>
		</section>
		<hr />
		<footer class="footer">&copy;IBM&reg; 2017</footer>
	</f:view>
</body>
</html>