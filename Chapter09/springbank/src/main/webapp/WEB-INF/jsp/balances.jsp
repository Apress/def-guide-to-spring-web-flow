<%@ include file="/WEB-INF/jspf/declarations.jspf" %>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><fmt:message key="accountBalances"/></title>
	</head>
	<body>
		<div class="operations">
			<a href="<c:url value="/flows.html?_flowId=enterPayment-flow"/>">
				<fmt:message key="enterPayment"/>
			</a>
			-
			<a href="<c:url value="/session/logout.html"/>"><fmt:message key="logout"/></a>
		</div>

		<h1><fmt:message key="accountBalances"/></h1>
		
		<c:if test="${param.confirmationMessage != null}">
			<div class="confirmation"><fmt:message key="${param.confirmationMessage}"/></div>
		</c:if>
		
		<display:table name="accounts" id="account" requestURI="/balances/show.html" style="width: 100%;">
			<display:column titleKey="holder" sortable="true" property="holder.name"/>
			<display:column titleKey="accountNumber" sortable="true">
				<a href="<c:url value="/balances/entries.html?accountNumber=${account.number}"/>">
					${account.number}
				</a>
			</display:column>
			<display:column titleKey="balance" style="text-align: right;">
				<fmt:formatNumber value="${account.balance}" pattern="0.00"/> &euro;
			</display:column>
		</display:table>
	</body>
</html>