<html>
<head><title>
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
<h1><a name="properties"></a>Properties</h1>
<table border>
    <tr>
        <th>Name</th>
        <th>Value</th>
        <th>Type</th>
    </tr>
<#list fieldList as item>
    <#if item_index%2 == 1>
    <tr class="even">
    <#else>
    <tr class="odd">
    </#if>
    <td><a href="${project}/dyn/admin/spring/prop?propertyName=${item.name}&id=${id}">${item.name}</a></td>
    <td><span style='white-space:pre'><xmp>${item.value}</xmp></span></td>
    <td>${item.type}</td></tr>
</#list>
</table>
<h1>Methods</h1>
<table border>
    <tr>
        <th>Name</th>
        <th>Return Type</th>
        <th>Declaring Class</th>
    </tr>
<#list methodList as item>
    <#if item_index%2 == 1>
    <tr class="even">
    <#else>
    <tr class="odd">
    </#if>
    <td><a href="${project}/dyn/admin/spring/method/comfirmInvoke?methodName=${item.name}&id=${id}">${item.name}</a>
    </td>
    <td><span style='white-space:pre'>${item.returnType}</span></td>
    <td>${item.declaringClass}</td></tr>
</#list>
</table>

<#if monitData??>
<script>
    function selectTheme(v){
        var tables = document.getElementsByTagName("table");
        for(var i = 0;i<tables.length;i++){
            if(tables[i].attributes['id']){
                if(v == tables[i].attributes['id'].value){
                    var trs = tables[i].childNodes[1].children;
                    for(var j = 0;j<trs.length;j++){
                        for(k = 0;k<trs[j].children.length;k++){
                            trs[j].children[k].style.display = "";
                        }

                    }
                    tables[i].style.display = "";
                }else{
                    tables[i].style.display = "none";
                }
            }
        }
    }

    function hide(index,id){
        var table =  document.getElementById(id);
        var trs = table.childNodes[1].children;
        for(var i = 0;i<trs.length;i++){
            trs[i].children[index].style.display = "none";
        }

    }
</script>
<h1>Monito Data</h1>
Theme : <select onchange="selectTheme(this.options[this.options.selectedIndex].value)">
    <option selected>请选择...</option>
    <#list monitData['themes'] as item>
        <option>${item}</option>
    </#list>
</select><br/>

    <#list monitData['data']?keys as key>
    <table border style="display:none" id="${key}" >
        <tr class="even">
            <#list monitData['themeProperties'][key] as prop>
                <th><a alt="隐藏" title="隐藏" href="javascript:hide(${prop_index},'${key}')">${prop}</a></th>
            </#list>
        </tr>

        <#list monitData['data'][key] as obj>
            <#if obj_index%2 == 1>
            <tr class="odd">
            <#else>
            <tr class="even">
            </#if>
            <#list monitData['themeProperties'][key] as prop1>
                <td>${obj[prop1]!}</td>
            </#list>
        </tr>
        </#list>

    </table>
    </#list>
</#if>
<h1>String Value</h1>
${stringValue}
</body>
</html>