<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="cl" value="${ pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>채팅 화면</title>
</head>
<body>
    <h2 class="text-black text-center text-2xl m-4">채팅 연습하기</h2>

	<!-- 사용자 이름 입력 모달 -->
	<div id="usernameModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50 hidden">
	    <div class="bg-gray-800 p-6 rounded-md text-white" style="width: 70%; max-width: 500px; min-width: 300px;">
	        <h3 class="text-2xl mb-4">사용자 이름을 <br> 입력하세요</h3>
	        <input type="text" id="usernameInput" class="w-full p-2 mb-4 rounded-md bg-gray-700 text-white placeholder-gray-400" placeholder="이름을 입력하세요">
	        <button onclick="enterChatRoom()" class="bg-gradient-to-r from-cyan-500 to-blue-500 w-full p-2 rounded-md text-white">입장</button>
	    </div>
	</div>
	
	<!-- 채팅 메시지 표시 영역 -->
	<div id="messageContainer" class="bg-white border border-black text-white w-full h-[500px] overflow-y-auto p-4 mb-4">
	    <!-- 메시지가 여기에 표시됩니다 -->
	</div>
	
	<!-- 채팅방 나가기 버튼 -->
	<div id="exitButton" class="bg-red-500 text-white p-2 w-40 text-center rounded-md mt-4 hidden cursor-pointer absolute right-4 top-0" onclick="exitChatRoom()">채팅방 나가기</div>
	
	<!-- 메시지 입력 필드 -->
	<div class="fixed bottom-0 w-full bg-gray-100 border p-4 flex items-center space-x-2">
	    <input type="text" id="messageInput" class="w-11/12 p-2 rounded-md bg-white-900 border text-black placeholder-gray-400" placeholder="메시지를 입력하세요...">
	    <button onclick="sendMessage();" class="bg-gradient-to-r from-cyan-500 to-blue-500 text-white p-2 rounded-md" style="width: 150px;">전송</button>
	</div>
	<script src="https://cdn.tailwindcss.com"></script>
	<script src="${ cl }/js/script.js"></script>
</body>
</html>
