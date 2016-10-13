<%@ include file="/WEB-INF/jspf/declarations.jspf" %>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><fmt:message key="confirmPayment"/></title>
	</head>
	<body>
		<div class="operations">
			<a href="<c:url value="/session/logout.html"/>"><fmt:message key="logout"/></a>
		</div>

		<h1><fmt:message key="confirmPayment"/></h1>
		
		<c:if test="${rootCauseException != null}">
			<div class="error"><fmt:message key="${rootCauseException.code}"/></div>
		</c:if>

		<form action="<c:url value="flows.html"/>">
			<table width="100%" style="margin-bottom: 3px; border: 1px solid black;">
				<col width="50%"/>
				<col width="50%"/>
				<tr>
					<td colspan="2">
						<fmt:message key="amount"/>:
						<fmt:formatNumber value="${payment.amount}" pattern="0.00"/> &euro;
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
									${payment.creditAccount.number}
								</td>
							</tr>
							<tr>
								<td width="55px" valign="top"><fmt:message key="holder"/>:</td>
								<td valign="top">
									${payment.creditAccount.holder.name}<br/>
									${payment.creditAccount.holder.address.street}
									${payment.creditAccount.holder.address.poBox}<br/>
									${payment.creditAccount.holder.address.zipCode}
									${payment.creditAccount.holder.address.city}
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<fmt:message key="message"/>:<br/>
						<c:out value="${payment.message}" escapeXml="true"/>&nbsp;
					</td>
				</tr>
			</table>

			<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
			<input type="submit" name="_eventId_submit" value="<fmt:message key="submit"/>"/>
			<input type="submit" name="_eventId_cancel" value="<fmt:message key="cancel"/>"/>
		</form>
	</body>
</html>