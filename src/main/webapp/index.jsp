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

<b>get key value</b>
GET /general/keyvalue
Get value for key
-params: key
<form action="general/keyvalue" method="GET">
key: <input type="text" name="key">
<input type="submit" value="Submit">
</form>

<b>store key value</b>
POST /general/keyvalue
Set value for key
-params: key, value
<form action="general/keyvalue" method="POST">
key: <input type="text" name="key">
value: <input type="text" name="value">
<input type="submit" value="Submit">
</form>

<b>get s3</b>
GET /general/s3
Get a file
-params: file
<form action="general/s3" method="GET">
file: <input type="text" name="file">
<input type="submit" value="Submit">
</form>

<b>store s3</b>
POST /general/s3
Set content for a file
-params: file, content
<form action="general/s3" method="POST">
file: <input type="text" name="file">
content: <input type="text" name="content">
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
