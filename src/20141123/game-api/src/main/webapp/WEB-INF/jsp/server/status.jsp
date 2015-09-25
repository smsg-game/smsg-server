<%@ page language="java" contentType="text/html;charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	连接数：<c:out value="${connCount}"></c:out>
	服务器连接数：<c:out value="${serverConnCount}"></c:out>
	<br/>
	<br/>
	<br/>
	服务器结构：
	<br/>
	<c:forEach items="${serverStruts}" var="entry">
		&nbsp;&nbsp;&nbsp;<c:out value="${entry.key}" />：<br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<c:forEach items="${entry.value}" var="its">
		   <c:out value="${its}" />
		</c:forEach>  
	</c:forEach>  
</html>
