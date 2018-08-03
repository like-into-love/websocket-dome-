<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	pageContext.setAttribute("app", request.getContextPath());
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${app }/loginServlet" method="post">
		<div>
			<label>账号:<input type="text" name="username" /></label>
		</div>
		<div>
			<label>密码:<input type="password" name="password" /></label>
		</div>
		<div>
			<input type="submit" value="登录" />
		</div>
	</form>
</body>
</html>