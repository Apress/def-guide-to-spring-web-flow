<%@ include file="/WEB-INF/jspf/declarations.jspf" %>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><fmt:message key="selectDebitAccount"/></title>
	</head>
	<body>
		<div class="operations">
			<a href="<c:url value="/session/logout.html"/>"><fmt:message key="logout"/></a>
		</div>

		<h1><fmt:message key="selectDebitAccount"/></h1>
		
		<form action="<c:url value="flows.html"/>">
			<display:table name="accounts" id="account" requestURI="/flows.html" style="width: 100%;">
				<display:column titleKey="holder" sortable="true" property="holder.name"/>
				<display:column titleKey="accountNumber" sortable="true">
					<c:url value="/flows.html" var="nextUrl">
						<c:param name="_flowExecutionKey">${flowExecutionKey}</c:param>
						<c:param name="_eventId">next</c:param>
						<c:param name="debitAccount">${account.number}</c:param>
					</c:url>
					<a href="${nextUrl}">${account.number}</a>
				</display:column>
				<display:column titleKey="balance" style="text-align: right;">
					<fmt:formatNumber value="${account.balance}" pattern="0.00"/> &euro;
				</display:column>
			</display:table>

			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
			<input type="submit" name="_eventId_cancel" value="<fmt:message key="cancel"/>"/>
		</form>
	</body>
</html>