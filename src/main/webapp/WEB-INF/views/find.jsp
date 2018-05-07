<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Find page</title>
</head>

<body>
<div class="login-form">
    <c:url var="saveUrl" value="/find"/>
    <form action="${saveUrl}" method="get" class="form-horizontal">
        <div class="input-group input-sm">
            <label class="input-group-addon" for="email"><i class="fa fa-user"></i></label>
            <input type="text" class="form-control" id="email" name="email" placeholder="Enter Email" required>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="form-actions">
            <input type="submit" class="btn btn-block btn-primary btn-default" value="Find">
        </div>
    </form>
</div>
<div class="text">
    <c:out value='${result}'/>
</div>
</body>
</html>