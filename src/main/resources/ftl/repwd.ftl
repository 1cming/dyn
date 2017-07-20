<html><head><title>
Service /com/gome/commerce/Interface/ElectronTicket/service/ElectronTicketManager/
</title>
<link rel="stylesheet" type="text/css" href="${project}/dyn/admin/resources/admin.css" title="Standard">
<script type="text/javascript" src="${project}/dyn/admin/resources/admin.js"></script>
</head>
<body text=00214A bgcolor=ffffff link=E87F02 vlink=637DA6>
<#include "header.ftl" >
<div style="text-align:ceter;width:600px;margin:20 0 0 20">
	<form method="post" action="${project}/dyn/admin/login/rePwd">
	  <table><tr><td>
		<lable>User</lable></td><td><input type="text" readonly name="userName" value="${userName}"/></td></tr>
		<tr><td><lable>Old Password</lable></td><td><input type="password" name="oldpassword"/></td></tr>
		<tr><td><lable>New Password</lable></td><td><input type="password" name="newpassword"/></td></tr>
		<tr><td><input type="submit"/></td></tr>
		</table>
		<div style="color:red;margin:5 0 0 50;">
			<#if errorMsg??>
			    ${errorMsg}
			</#if>
		</div>
	</form>
</div>
</body>
</html>