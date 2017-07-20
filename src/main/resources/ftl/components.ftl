<HTML>
<HEAD>

<TITLE>Dynamo Administration</TITLE>

<link rel="stylesheet" type="text/css" href="${project}/dyn/admin/resources/admin.css">
<script type="text/javascript" src="${project}/dyn/admin/resources/admin.js"></script>
</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#00214A" VLINK="#637DA6" LINK="#E87F02">
<#include "header.ftl" >
<a href="${project}/dyn/admin">admin</a></p><p>

<blockquote>
<h1>Dynamo Administration</h1>

<p>
<b>Version SpringPlatform/0.01</b>
</font>
</p>   
<h1>Directory Listing</h1>

<ul>
 <#if data??> 
	<#list data as item>
	<h3><a 
	  <#if item.leaf> 
	  	href="${project}/dyn/admin/springBean?id=${item.id}"
	  <#else>
	    href="${project}/dyn/admin/spring?id=${item.id}"
	  </#if>
	>${item.packageName}/</a></h3>
	<ul>
	   <#if item.getChildren()??> 
		   <#list item.getChildren() as subitem>
		      <a
		     <#if item.leaf> 
	  	href="${project}/dyn/admin/springBean?id=${item.id}"
	  <#else>
	    href="${project}/dyn/admin/spring?id=${item.id}"
	  </#if>
		       >${subitem.packageName}/</a><br>
		   </#list>
	   </#if>
	</ul>
	</#list>
</#if>
</ul>
</blockquote>

</BODY>
</HTML>
