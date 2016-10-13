<%@ include file="/WEB-INF/jspf/declarations.jspf" %>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><fmt:message key="login"/></title>
	</head>
	<body>
		<div class="operations">
		</div>

		<h1><fmt:message key="login"/></h1>
		
		<c:if test="${param.loginError == '1'}">
			<span class="error">
				<fmt:message key="error.loginFailed"/>
			</span>
		</c:if>
		
		<form action="<c:url value="/j_spring_security_check"/>" method="post">
			<table>
				<tr>
					<td><fmt:message key="userName"/>:</td>
					<td><input type="text" name="j_username"/></td>
				</tr>
				<tr>
					<td><fmt:message key="password"/>:</td>
					<td><input type="password" name="j_password"/></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="<fmt:message key="login"/>"/></td>
				</tr>
			</table>
		</form>
	</body>
</html>