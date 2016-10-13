<%@ include file="/WEB-INF/jspf/declarations.jspf" %>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><fmt:message key="beneficiaries"/></title>
	</head>
	<body>
		<div class="operations">
			<a href="<c:url value="/session/logout.html"/>"><fmt:message key="logout"/></a>
		</div>

		<h1><fmt:message key="beneficiaries"/></h1>
		
		<form action="<c:url value="flows.html"/>">
			<h2><fmt:message key="yourAccounts"/></h2>	

			<display:table name="accounts" id="account" requestURI="/flows.html" style="width: 100%;">
				<display:column titleKey="holder" sortable="true" property="holder.name"/>
				<display:column titleKey="accountNumber" sortable="true">
					<c:choose>
						<c:when test="${payment.debitAccount.number == account.number}">
							${account.number}
						</c:when>
						<c:otherwise>
							<c:url value="flows.html" var="selectUrl">
								<c:param name="_flowExecutionKey">${flowExecutionKey}</c:param>
								<c:param name="_eventId">select</c:param>
								<c:param name="accountNumber">${account.number}</c:param>
							</c:url>
							<a href="${selectUrl}">${account.number}</a>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column titleKey="balance" style="text-align: right;">
					<fmt:formatNumber value="${account.balance}" pattern="0.00"/> &euro;
				</display:column>
			</display:table>
			
			<h2><fmt:message key="predefinedBeneficiaries"/></h2>
			
			<display:table name="beneficiaries" id="beneficiary" requestURI="/flows.html" style="width: 100%;">
				<display:column titleKey="beneficiary" sortable="true" property="holder.name"/>
				<display:column titleKey="accountNumber" sortable="true">
					<c:url value="flows.html" var="selectUrl">
						<c:param name="_flowExecutionKey">${flowExecutionKey}</c:param>
						<c:param name="_eventId">select</c:param>
						<c:param name="accountNumber">${beneficiary.number}</c:param>
					</c:url>
					<a href="${selectUrl}">${beneficiary.number}</a>
				</display:column>
			</display:table>

			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
			<input type="submit" name="_eventId_cancel" value="<fmt:message key="cancel"/>"/>
		</form>
	</body>
</html>