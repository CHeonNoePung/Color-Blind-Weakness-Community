<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<% 
String uid = request.getParameter("id");
if (uid == null){
out.print("�α��� ���� ����");
}
else{
	session.setAttribute("id", uid);
	response.sendRedirect("login.jsp");
}
%>
</body>
</html>