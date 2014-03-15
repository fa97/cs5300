<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CS 5300 - Session State Management</title>
</head>
<script type="text/javascript">
	function setButtonValue(id)
	{
		buttonValue = document.getElementById(id).value;
		document.getElementById("buttonValue").value = buttonValue;
		
	}
</script>
<body>

<h1>${message} </h1>
<form method="post">
	<div style="padding-top: 50px;">
		<input type="submit" id="replace" name="replace" value="Replace" onClick="setButtonValue(this.id)">
		<input type="text" id="message" name="message" value="" maxlength="200">
	</div>
	<div style="padding-top: 10px;">
		<input type="submit" id="refresh" name="refresh" value="Refresh" onClick="setButtonValue(this.id)">
	</div>
	<div style="padding-top: 10px;">
		<input type="submit" id="logout" name="logout" value="Logout" onClick="setButtonValue(this.id)">
	</div>
	
	
	<input id="buttonValue" name="buttonValue" type="hidden" value=""/>
</form>

<div style="padding-top: 10px;">
	<p>Cookie Value(versionNumber_sessionID_locationMetadata): ${cookie_value}</p>
	<p>Session Expiration TimeStamp: ${expiration_timestamp}</p>
</div>
</body>
</html>