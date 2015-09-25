<%@ page contentType="text/html;charset=utf8"%> 
<%@ taglib uri="http://www.rapid-framework.org.cn/rapid" prefix="rapid" %>  
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<rapid:override name="head">  
 <title>版本管理</title>
</rapid:override>

<rapid:override name="header">  
<ul class="menu">
	<li class="cur">版本管理</li>
	<li><a href="/manage/notice.do">公告管理</a></li>
</ul>
</rapid:override>
		
<rapid:override name="content">  
 <div class="form">
	<form action="/manage/uploadStaticPackage.do" method="post" enctype="multipart/form-data">
		<div class="form-item">
			<span class="form-label">版本号</span>
			<span class="form-input"><input type="text" name="version"/></span>
		</div>
		<div class="form-item">
			<span class="form-label">是否是测试版</span>
			<span class="form-input">
				<select name="isTest">
					<option value="0">否</option>
					<option value="1" selected>是</option>
				</select>
			</span>
		</div>
		<div class="form-item">
			<span class="form-label">白名单</span>
			<span class="form-input">
			<textarea name="whiteList" col="10" rows="3"></textarea>
			</span>
		</div>
		<div class="form-item">
			<span class="form-label">静态资源包</span>
			<span class="form-input"><input type="file" name="staticPackage"/></span>
		</div>
		<div class="form-item">
			<span class="form-label">升级描述</span>
			<span class="form-input">
			<textarea name="describe" col="10" rows="10"></textarea>
			</span>
		</div>
		<div class="form-btn">
			<input type="submit" name="button" value="提交"/>
		</div>
	</form>
</div>
</rapid:override>
<%@ include file="/WEB-INF/jsp/base.jsp" %> 	