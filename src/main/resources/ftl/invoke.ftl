<html><head><title>
Service /com/gome/commerce/Interface/ElectronTicket/service/ElectronTicketManager/
</title>
<link rel="stylesheet" type="text/css" href="${project}/dyn/admin/resources/admin.css" title="Standard">
<script type="text/javascript" src="${project}/dyn/admin/resources/resources/admin.js"></script>
</head>
<body text=00214A bgcolor=ffffff link=E87F02 vlink=637DA6>
<#include "header.ftl" >
<hr>
<h1>Method <code>${methodName}</code> invoked</h1>



<#if errMsg == "">
    <h3>Returned Object</h3>
    <table border style="width:1000px">
<tr><th>Value</th><th>Class</th></tr>
<tr><td>${returnValue}</td><td>${returnClass}</td></tr>
<#else>
 <h3>Invoke error</h3>
  <div style="margin:20 0 0 100;color:red"> ${errMsg} </div>
</table> 
</#if>

</body>
<FOOTER>
 	<span style="font-weight:bold;"> 
  		<p align="center">Powered by: <a href="mailto:conning@qq.com">Conning</a></p>
	</span>
</FOOTER>
</html>