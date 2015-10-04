<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>API</title>
</head>
<body>
<pre>
<h1>API</h1>
<h2>Links</h2>
<a href="version.jsp">version</a>
<a href="#general">General section</a>
<a href="#servlet">Servlet section</a>

<a id="general"><h2>General</h2></a>

<b>key value</b>
GET /general/keyvalue
Get value for key
-params: key
<form action="general/keyvalue" method="GET">
key: <input type="text" name="key">
<input type="submit" value="Submit">
</form>

<b>email signup</b>
GET /general/emailsignup
Signup an email address
-params: email
<form action="general/emailsignup" method="POST">
email: <input type="text" name="email">
<input type="submit" value="Submit">
</form>

<a id="servlet"><h2>Servlet</h2></a>

<b>echo</b>
GET /servlet/echo
Echo a parameter
-params: param
<form action="servlet/echo" method="GET">
param: <input type="text" name="param">
<input type="submit" value="Submit">
</form>

</pre>
</body>
</html>
