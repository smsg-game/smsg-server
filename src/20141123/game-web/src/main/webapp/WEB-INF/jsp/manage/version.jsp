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
	<li><a href="/manage/staticPackage.do">提交静态文件</a>		
	
</ul>
</rapid:override>

<rapid:override name="content">  
 <div class="form">
	<form action="/manage/uploadPackage.do" method="post" enctype="multipart/form-data">
		<div class="form-item">
			<span class="form-label">当前版本号</span>
			<span class="form-input"><input type="text" name="version"/></span>
		</div>
		<div>
			<c:forEach items="${partners}" var="it">
				<span><input type="radio" name="partnerId" value="${it.pNum}"/><c:out value="${it.pName}"></c:out></span>&nbsp;
			</c:forEach>
		</div>
		<div class="form-item">
			<span class="form-label">兼容版本号</span>
			<span class="form-input"><input type="text" name="versions"/></span>
			<span class="form-desc">(逗号隔开多个版本号)</span>
		</div>
		<div class="form-item">
			<span class="form-label">灰度测试IMEI号</span>
			<span class="form-input"><input type="text" name="frs"/></span>
			<span class="form-desc">(逗号隔开多个号)</span>
		</div>
		<div class="form-item">
			<span class="form-label">是否灰度测试</span>
			<span class="form-input">
				<select name="isTest">
					<option value="0">否</option>
					<option value="1" selected>是</option>
				</select>
			</span>
		</div>
		<div class="form-item">
			<span class="form-label">类型</span>
			<span class="form-input">
				<select name="pkgType">
					<option value="1" selected>资源包</option>
					<option value="0">APK包</option>
				</select>
			</span>
		</div>
		<div class="form-item">
			<span class="form-label">升级包</span>
			<span class="form-input"><input type="file" name="upgradePackage"/></span>
		</div>
		<div class="form-item">
			<span class="form-label">完整包</span>
			<span class="form-input"><input type="file" name="fullPackage"/></span>
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