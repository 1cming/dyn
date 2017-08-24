<html><head><title>
Service /com/gome/commerce/Interface/ElectronTicket/service/ElectronTicketManager/
</title>
<link rel="stylesheet" type="text/css" href="${project}/dyn/admin/resources/admin.css" title="Standard">
<script type="text/javascript" src="${project}/dyn/admin/resources/admin.js"></script>
</head>
<body text=00214A bgcolor=ffffff link=E87F02 vlink=637DA6>
<#include "header.ftl" >
<hr>
<h1>Invoke Method <code>hashCode</code>?</h1>Warning: invoking methods should only be performed by developers aware of potential side effects that this method might have.  You should never invoke a method on a live site unless you have tested this operation before.
<form action="${project}/dyn/admin/spring/method/invoke" method=POST>
<input type=hidden name="invokeMethod" value="${methodName}">
<input type=submit name=submit value="Invoke Method">
<input type=button name=cancelMethodInvocation onclick='history.go(-1)' value="Cancel">
<input type=hidden name=id value="${id}">
</form>
</body>
<FOOTER>
 	<span style="font-weight:bold;"> 
  		<p align="center">Powered by: <a href="mailto:conning@qq.com">Conning</a></p>
	</span>
</FOOTER>
</html>