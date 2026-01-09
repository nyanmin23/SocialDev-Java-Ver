let stompClient = null;
let currentUser = null;

function connect() {
  currentUser = document.getElementById("username").value;

  const socket = new SockJS('/ws-socialdev');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    const messagesList = document.getElementById("messages");

    stompClient.subscribe('/topic/public', (frame) => {
      const msg = JSON.parse(frame.body);
      const li = document.createElement("li");
      li.textContent = `${msg.senderName}: ${msg.content}`;
      messagesList.appendChild(li);
    });
  });
}

function sendMessage() {
  const messageText = document.getElementById("message").value;

  stompClient.send(
    "/app/message",
    {},
    JSON.stringify({ senderName: currentUser, content: messageText })
  );
}

document.getElementById("connect").onclick = connect;
document.getElementById("send").onclick = sendMessage;
