<%@ include file="/WEB-INF/jspf/declarations.jspf" %>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><fmt:message key="error"/></title>
	</head>
	<body>
		<div class="operations">
			<a href="<c:url value="/balances/show.html"/>"><fmt:message key="balances"/></a>
			-
			<a href="<c:url value="/flows.html?_flowId=enterPayment-flow"/>">
				<fmt:message key="enterPayment"/>
			</a>
			-
			<a href="<c:url value="/session/logout.html"/>"><fmt:message key="logout"/></a>
		</div>

		<h1><fmt:message key="error"/></h1>
			
		<c:if test="${exception!=null}">
			<span class="error">
				<fmt:message key="error.transactionExpired"/>
			</span>
		</c:if>
	</body>
</html>
