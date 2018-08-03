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
<script type="text/javascript" src="${app}/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
	var name = "${sessionScope.user }";
	//通过URL请求服务端（chat为项目名称）
	//	var url = "ws://localhost:8080/chatSocket?username=${sessionScope.user}";
	var ws;
	var url = "ws://localhost:8080/websocket-dome/webSocket/chat/" + name;
	//进入聊天页面就是一个通信管道 即浏览器检查 WebSocket 支持
	window.onload = function() {

		if ('WebSocket' in window) {
			ws = new WebSocket(url);
		} else if ('MozWebSocket' in window) {
			ws = new MozWebSocket(url);
		} else {
			alert('WebSocket不受这个浏览器的支持');
			return;
		}
		//监听服务器发送过来的所有信息 event是返回回來的数据内置参数
		ws.onmessage = function(event) {
			//eval函数是强大的数码转换引擎,字符串经eval转换后得到一个javascript对象,
			//var obj = eval("date":null,"alert":"用户12进入聊天室");等效于 var obj = {date: null, alert: "用户12进入聊天室",};
			//console.info(event.data)
			eval("var result=" + event.data);
			//console.info(result)
			//如果后台发过来的alert不为空就显示出来
			if (result.alert != undefined) {
				$("#content").append(result.alert + "<br/>");
			}
			//如果用户列表不为空就显示
			if (result.names != undefined) {
				//刷新用户列表之前清空一下列表，免得会重复，因为后台只是单纯的添加
				$("#userList").html("");
				$(result.names).each(
						function() {
							$("#userList").append(
									"<input  type=checkbox  value='"+this+"'/>"
											+ this + "<br/>");
						});
			}
			//将用户名字和当前时间以及发送的信息显示在页面上
			if (result.from != undefined) {
				$("#content").append(
						result.from + " " + result.date + " 说：<br/>"
								+ result.sendMsg + "<br/>");
			}

		}
	}
	//解决如何处理关闭或刷新页面导致websocket关闭而抛出的异常？
	window.onbeforeunload = function(event) {
		console.log("关闭WebSocket连接！");
		//当浏览器关闭或者浏览器刷新时执行一个页面关闭事件
		ws.close();
	}
	//将消息发送给后台服务器
	function send() {
		//拿到需要单聊的用户名
		//alert("当前登录用户为"+userName);
		var ss = $("#userList input[type=checkbox]:checked");
		//alert("群聊还是私聊"+ss.length);
		//用户名
		var to = $('#userList :checked').val();
		//alert(to);
		if (to == name) {
			alert("你不能给自己发送消息啊");
			return;
		}
		//根据勾选的人数确定是群聊还是单聊
		var value = $("#msg").val();
		alert("消息内容为" + value);
		var object = null;
		if (ss.length == 0) {
			object = {
				msg : value,
				type : 1, //1 广播 2单聊    
			};
		} else {
			object = {
				to : to,
				msg : value,
				type : 2, //1 广播 2单聊    
			};
		}
		//将object转成json字符串发送给服务端
		var json = JSON.stringify(object);
		//alert("str="+json);
		ws.send(json);
		//消息发送后将消息栏清空
		$("#msg").val("");
	}
</script>
</head>
<body>
	<h3>欢迎 ${sessionScope.user }使用本聊天系统！！</h3>

	<div id="content"
		style="border: 1px solid black; width: 400px; height: 300px; float: left; color: #7f3f00;"></div>
	<div id="userList"
		style="border: 1px solid black; width: 120px; height: 300px; float: left; color: #00ff00;"></div>

	<div style="clear: both;" style="color:#00ff00">
		<input id="msg" type="text" />
		<button onclick="send();">发送消息</button>
	</div>
</body>
</html>