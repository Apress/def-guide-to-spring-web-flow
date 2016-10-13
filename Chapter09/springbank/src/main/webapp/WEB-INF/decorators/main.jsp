<%@ include file="/WEB-INF/jspf/declarations.jspf" %>

<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><decorator:title default="Spring Bank"/></title>

		<link rel="StyleSheet" href="<c:url value="/css/style.css"/>" type="text/css" />
		
		<meta http-equiv="Pragma" content="no-cache"/>
		<meta http-equiv="Cache-Control" content="max-age=0"/>
		<meta http-equiv="Expires" content="0"/>
		
		<decorator:head/>
	</head>
	<body>
		<div class="containerTop">
			<span style="color: green; font-size: 20pt; display: block">Spring Bank</span>
		</div>
		
		<div class="container">
			<decorator:body/>
		</div>

		<div class="containerBottom">
			<span style="color: green; font: 8pt">&copy; Spring Bank</span>
		</div>
	</body>
</html>