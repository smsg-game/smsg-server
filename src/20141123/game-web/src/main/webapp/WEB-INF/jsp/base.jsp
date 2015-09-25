<%@ page contentType="text/html;charset=utf8"%> 
<%@ taglib uri="http://www.rapid-framework.org.cn/rapid" prefix="rapid" %>
<html>  
<head>
   <rapid:block name="head">
        base_head_content
   </rapid:block>
   
   <rapid:block name="script">
        script
   </rapid:block>
</head>  
<body>
    <div id="header">
		<rapid:block name="header">
		</rapid:block>  
	</div>
   <div id="content">
		<rapid:block name="content">
		</rapid:block>  
	</div>
	<div id="footer">
		<rapid:block name="footer">
		</rapid:block>  
	</div>
</body>  
</html>