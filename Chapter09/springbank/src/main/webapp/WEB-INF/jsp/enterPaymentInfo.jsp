<%@ include file="/WEB-INF/jspf/declarations.jspf" %>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><fmt:message key="enterPaymentInfo"/></title>
	</head>
	<body>
		<div class="operations">
			<a href="<c:url value="/session/logout.html"/>"><fmt:message key="logout"/></a>
		</div>

		<h1><fmt:message key="enterPaymentInfo"/></h1>

		<form:form action="flows.html" commandName="payment">
			<table width="100%" style="margin-bottom: 3px; border: 1px solid black;">
				<col width="50%"/>
				<col width="50%"/>
				<tr>
					<td colspan="2">
						<fmt:message key="amount"/>: <form:input path="amount" /> &euro;
						<form:errors cssClass="error" path="amount"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<span style="font-weight: bold;"><fmt:message key="debitAccount"/>:</span>
						<table width="100%">
							<tr>
								<td width="55px" valign="top"><fmt:message key="number"/>:</td>
								<td valign="top">${payment.debitAccount.number}</td>
							</tr>
							<tr>
								<td width="55px" valign="top"><fmt:message key="holder"/>:</td>
								<td valign="top">
									${payment.debitAccount.holder.name}<br/>
									${payment.debitAccount.holder.address.street}
									${payment.debitAccount.holder.address.poBox}<br/>
									${payment.debitAccount.holder.address.zipCode}
									${payment.debitAccount.holder.address.city}
								</td>
							</tr>
						</table>
					</td>
					<td valign="top">
						<span style="font-weight: bold;"><fmt:message key="creditAccount"/>:</span>
						<table width="100%">
							<tr>
								<td width="55px" valign="top"><fmt:message key="number"/>:</td>
								<td valign="top">
									<form:input path="creditAccount.number"/>
									<input type="submit" name="_eventId_selectBeneficiary" value="<fmt:message key="select"/>"/>
									<form:errors cssClass="error" path="creditAccount.number"/>
								</td>
							</tr>
							<tr>
								<td width="55px" valign="top"><fmt:message key="holder"/>:</td>
								<td valign="top">
									<form:input path="creditAccount.holder.name" cssStyle="width: 290px;"/><br/>
									<form:input path="creditAccount.holder.address.street" cssStyle="width: 200px;"/>
									<form:input path="creditAccount.holder.address.poBox"cssStyle="width: 80px;"/><br/>
									<form:input path="creditAccount.holder.address.zipCode" cssStyle="width: 70px;"/>
									<form:input path="creditAccount.holder.address.city" cssStyle="width: 210px;"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<fmt:message key="message"/>:<br/>
						<form:input path="message" size="100"/>
					</td>
				</tr>
			</table>

			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
			<input type="submit" name="_eventId_next" value="<fmt:message key="next"/>"/>
			<input type="submit" name="_eventId_cancel" value="<fmt:message key="cancel"/>"/>
		</form:form>
	</body>
</html>