<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<img src="${project}/dyn/admin/resources/comp-banner.gif" alt="Dynamo Component Browser" align=top width=585 height=37 border=0><p>
<p style="display: inline;"><a href="${project}/dyn/admin">admin</a>/Dynamo Component Browser
&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="toggle_visibility_inline('search'); set_focus('searchField'); return false;">Search</a>
<form action="${project}/dyn/admin/spring" style="padding:0; margin:0; display:none;" id="search">By Name <input type="text" name="beanName" id="searchField"></form>
 <#if beanUrl??> 
<h1> ${beanUrl}</h1>
</#if>