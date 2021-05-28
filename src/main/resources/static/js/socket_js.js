const url = 'http://localhost:8080';
let stompClient;

function connectToSocket(paymentId) {
    console.log("connecting to the payment");
    let socket = new SockJS(url + "/payment-status");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/payment/" + paymentId, function (response) {
            console.log("/topic/payment/" + paymentId);
            console.log(JSON.parse(response.body));
            console.log("received");
        })
    })
}

function watch_payment() {
    let paymentId = document.getElementById("paymentId").value;
    if (paymentId == null || paymentId === '') {
        alert("Please enter the payment id");
    } else {
        connectToSocket(paymentId);
        alert("You are now tracking the payment " + paymentId);
    }
}