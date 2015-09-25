<%@ page contentType="text/html;charset=utf8"%> 
<%@ taglib uri="http://www.rapid-framework.org.cn/rapid" prefix="rapid" %>  
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<rapid:override name="head">  
 <title>版本管理</title>
</rapid:override>

<rapid:override name="header">  
<ul class="menu">
	<li><a href="/manage/version.do">版本管理</a></li>
	<li class="cur">公告管理</li>
</ul>
</rapid:override>
<rapid:override name="script">  
<script language="javascript" type="text/javascript">
function change(op){
	var serverId = document.getElementById("serverId").value;
	if(serverId == "all"){
		op = "all";
	}
	location.href="/manage/notice.do?serverId=" + serverId + "&partnerId=" + op;
}
</script>
</rapid:override>
<rapid:override name="content">  
 <div class="form">
	<form action="/manage/updateNotice.do" method="post" enctype="multipart/form-data">
		<div>
			<select id="serverId" name="serverId">
				<option value="0">选择</option>
				<c:forEach items="${servers}" var="it">
					<c:choose>
						<c:when test="${!empty notice && it.serverId==notice.serverId}">
							<option value="${it.serverId}" selected><c:out value="${it.serverName}"></c:out></option>
						</c:when>
						<c:otherwise>
							<option value="${it.serverId}"><c:out value="${it.serverName}"></c:out></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
			<div id="partnerBox">
				<input type="radio" name="partnerId" onclick="change(this.value);" <c:if test="${notice.partnerId == 'all'}">checked</c:if> value="all"/>全服
				<c:forEach items="${partners}" var="entity">
					<input type="radio" name="partnerId" onclick="change(this.value);" <c:if test="${!empty partners && notice.partnerId == entity.pNum}">checked</c:if> value="${entity.pNum}"/>${entity.pName}
				</c:forEach>		
			</div>
		</div>
		<div>
			<span>
			<c:choose>
				<c:when test="${!empty notice && notice.isEnable == 1}">
					<input type="radio" name="isEnable" checked value="1"/>启用 <input type="radio" name="isEnable" value="0"/>关闭
				</c:when>
				<c:otherwise>
					<input type="radio" name="isEnable" value="1"/>启用 <input type="radio" name="isEnable" checked value="0"/>关闭
				</c:otherwise>
			</c:choose>
			</span>
		</div>
		<div class="form-item">
			<span class="form-label">标题</span>
			<span class="form-input"><input type="text" name="title" value="<c:out value="${notice.title}"></c:out>"/></span>
		</div>
		<div class="form-item">
			<span class="form-label">公告内容</span>
			<span class="form-input">
			<textarea name="content" col="10" rows="10"><c:out value="${notice.content}"></c:out></textarea>
			</span>
		</div>
		<div class="form-btn">
			<input type="submit" name="button" value="提交"/>
		</div>
	</form>
</div>
</rapid:override>
<%@ include file="/WEB-INF/jsp/base.jsp" %> 	