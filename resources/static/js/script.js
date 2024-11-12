let websocket;
    let username;

    window.onload = () => {
        document.getElementById("usernameModal").classList.remove("hidden");
    };

    const enterChatRoom = () => {
        username = document.getElementById("usernameInput").value.trim();
        if (username) {
            document.getElementById("usernameModal").classList.add("hidden");
            openChatting();
        } else {
            alert("사용자 이름을 입력하세요!");
        }
    };

    const openChatting = () => {
        websocket = new WebSocket("ws://localhost:8085/chat");

        websocket.onopen = () => {
            console.log("WebSocket 연결됨");
            const msg = new Message("enter", username, '', '');  // 입장 메시지 보내기
            websocket.send(JSON.stringify(msg));
            document.getElementById("exitButton").classList.remove("hidden");  // 채팅방 나가기 버튼 표시
        };

        websocket.onmessage = (msg) => {
            const message = JSON.parse(msg.data);
            switch (message.type) {
                case "enter":
                case "chat":
                    appendMessage(message);
                    break;
                case "exit":
                    appendMessage({sender: "System", data: message.data, type: "chat"});
                    break;
            }
        };
    };

	function appendMessage(message) {
	    const $container = document.getElementById("messageContainer");
	    const $div = document.createElement("div");

	    // 보낸 사람과 받은 사람의 구별을 위한 flex 및 정렬 클래스
	    $div.classList.add("flex", "mb-2", message.sender === username ? "justify-end" : "justify-start");

	    const $p = document.createElement("p");

	    // 메시지의 스타일: 보낸 사람일 때는 그라디언트 배경, 그렇지 않으면 다른 배경
	    if (message.sender === username) {
	        $p.classList.add("p-2", "rounded-md", "max-w-xs", "bg-gradient-to-r", "from-cyan-500", "to-blue-500", "text-white");
	    } else {
	        $p.classList.add("p-2", "rounded-md", "max-w-xs", "bg-black", "text-white");
	    }

	    $p.innerText = message.data;
	    $div.appendChild($p);
	    $container.appendChild($div);

	    // 새로운 메시지가 추가되면 자동으로 스크롤 내리기
	    $container.scrollTop = $container.scrollHeight;
	}

    function sendMessage() {
        const messageInput = document.getElementById("messageInput");
        const messageText = messageInput.value.trim();

        if (messageText) {
            const msg = new Message("chat", username, '', messageText);
            websocket.send(JSON.stringify(msg));
            messageInput.value = ''; // 메시지 전송 후 입력 필드 초기화
        }
    }

    function exitChatRoom() {
        const msg = new Message("exit", username, '', '');
        websocket.send(JSON.stringify(msg));
        websocket.close();
        document.getElementById("messageContainer").innerHTML = '';  // 메시지 초기화
        document.getElementById("exitButton").classList.add("hidden");  // 나가기 버튼 숨기기
    }

    class Message {
        constructor(type, sender, receiver, data) {
            this.type = type;
            this.sender = sender;
            this.receiver = receiver;
            this.data = data;
        }
    }