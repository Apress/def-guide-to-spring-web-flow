<%@ include file="/WEB-INF/jspf/declarations.jspf" %>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><fmt:message key="accountEntries"/></title>
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

		<h1><fmt:message key="accountEntriesFor"><fmt:param value="${param.accountNumber}"/></fmt:message></h1>
		
		<display:table name="entries" id="entry" requestURI="/balances/entries.html" style="width: 100%;"
			export="true" pagesize="10">
			<display:column titleKey="date" sortable="true" style="width: 25%">
				<fmt:formatDate value="${entry.transaction.date}" type="both"/>
			</display:column>
			<display:column titleKey="amount" style="width: 20%; text-align: right;">
				<fmt:formatNumber value="${entry.appliedAmount}" pattern="0.00"/> &euro;
			</display:column>
			<display:column titleKey="message" property="transaction.message" style="width: 55%"/>
		</display:table>
	</body>
</html>