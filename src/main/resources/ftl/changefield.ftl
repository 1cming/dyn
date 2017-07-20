<html><head><title>
Service /com/gome/commerce/Interface/ElectronTicket/service/ElectronTicketManager/
</title>
<link rel="stylesheet" type="text/css" href="${project}/dyn/admin/resources/admin.css" title="Standard">
<script type="text/javascript" src="${project}/dyn/admin/resources/admin.js"></script>
</head>
<body text=00214A bgcolor=ffffff link=E87F02 vlink=637DA6>
<#include "header.ftl" >
<hr>
<h1>Service Info</h1>
<span style='white-space:pre'>${beanName}</span>
<h1>Short Description</h1>
${propertyName}
<h3>Value</h3>
<span style='white-space:pre'><xmp>${propertyValue}</xmp></span>
<h3>New value</h3>
<form action="${project}/dyn/admin/spring/prop" method=POST>
<input type=hidden name="propertyName" value="${propertyName}">
<input type=hidden name="id" value="${id}">
<#if changeAble>
<textarea rows=3 cols=40 name="newValue">
</textarea>
<p><input type=submit name="change" value="Change Value">
<p>(Note that values changed here are not written permanently to the properties files)
</#if>

</form>
<h1><a name="#"></a>Properties</h1>
<table border style="width:800px">
<tr><th>Name</th><th>Value</th><th>Type</th><th>final</th></tr>
<tr class="even"><td>${propertyName}</td><td>${propertyValue}</td><td>${propertyType} </td><td>${finalField} </td></tr>
</table>
</body></html>