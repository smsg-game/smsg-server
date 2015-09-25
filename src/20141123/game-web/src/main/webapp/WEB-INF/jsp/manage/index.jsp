<%@ page contentType="text/html;charset=utf8"%> 
<%@ taglib uri="http://www.rapid-framework.org.cn/rapid" prefix="rapid" %>  
<rapid:override name="head">  
 <title>WEB API管理登陆界面</title>
</rapid:override>
<rapid:override name="content">  
 <div class="form">
	<form action="/manage/login.do" method="post">
		<div class="form-item">
			<span class="form-label">账号</span>
			<span class="form-input"><input type="text" name="username"/></span>
		</div>
		<div class="form-item">
			<span class="form-label">密码</span>
			<span class="form-input"><input type="password" name="password"/></span>
		</div>
		<div class="form-btn">
			<input type="submit" name="button" value="登陆"/>
		</div>
	</form>
</div>
</rapid:override>
<%@ include file="/WEB-INF/jsp/base.jsp" %> 	