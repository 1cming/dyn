**DYN
** 
### 声明：

### 此插件借鉴ATG思路，通过spring实现，提供管控界面。  

#### 一、适用范围：
Java + Spring搭建的web应用

#### 二、功能原理
spring bean + java reflect

#### 三、功能概述

用于在应用在线时获取spring容器中的bean，可查看bean class的相关定义，如methods 、fileds、properties等在运行时的数值。

对于Properties：即bean class定义的字段，对于非私有（private）的，可动态修改其数值；  
对于Methods：即bean class定义的函数，可执行；

#### 四、适用场景：


本工具侧重于内部维护系统数据，因此建议屏蔽外网访问，权限相关可自行继承 com.conning.compents.servlet.DynServlet 扩展。

**几个典型的应用场景：**  
处理客诉问题，如修改数据状态、删除数据；（开发可绕过DBA或相关繁琐流程直接操作数据，需谨慎开发相关功能）；
调试相关功能（部分公司多环境下调试本地逻辑较为繁琐）；

#### 五、接入流程：

###### 1.安装maven

###### 2.clone工程到xxx/dyn目录下

###### 3.在xxx/dyn下执行：mvn clean compile install编译jar包

###### 4.在需要操作的工程中引入jar

`相关依赖说明：

spring > 3.2.3.RELEASE   
aspectjweaver > 1.7.2   
org.freemarker > 2.3.20   
fastjson > 1.2.28 `


###### 5、web.xml引入配置  


````
<servlet>  
	<servlet-name>dyn</servlet-name>
	<servlet-class>com.conning.compents.servlet.DynServlet</servlet-class>
</servlet>
<servlet-mapping>
	<servlet-name>dyn</servlet-name>
	<url-pattern>/dyn/admin/*</url-pattern>
</servlet-mapping>
````
###### 6、新建一个Demo.java：  

````
@Component  
public class Demo {  
	private String isOk;  

	public String getIsOk() {  
		return isOk;
	}

	public void setIsOk(String isOk) {
		this.isOk = isOk;
	}

	public String areYouOk() {
		StringBuilder sb = new StringBuilder("<p>\\t step into method areYouOk </p>");
		return sb.append(isOk).toString();
	}
}
````



###### 7、启动应用   
以ip为127.0.0.1,port为8080示例,访问dyn控制界面地址为:127.0.0.0:8080/dyn/admin
注:
方法不能含有入参,参数以全局变量传递
输出支持html标记语言

###### 8、操作步骤：

进入127.0.0.0:8080/dyn/admin
输入帐号密码 默认均为admin

点击进入Component Browser

找到刚刚写好的Demo点击进去

在Properties一栏点击类属性isOk

在New value下面的输入框输入你想要输入的文字然后点击Change Value按钮

返回到之前Demo的页面

在Methods下面找到你刚刚定义的方法areYouOk

点击Invoke Method执行该方法获得执行结果




