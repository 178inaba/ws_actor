var ws;

function start() {
	console.log("start()");

	var data = {
		cmd:      "start",
		limit:    document.getElementById("limit").value,
		interval: document.getElementById("interval").value,
	};

	var json = JSON.stringify(data);

	console.log("data: " + data);
	console.log("json: " + json);

	ws.send(json);
}

function reset() {
	console.log("reset()");
	document.getElementById("limit").value = "";
	interval: document.getElementById("interval").value = "";
}

// open event
function onOpen(event) {
	console.log("onOpen()");
	console.timeEnd("WebSocket connect");
	console.log(event);
}

// receive data event
function onMessage(event) {
	console.log("onMessage()");

	if (event && event.data) {
		console.log(event);
		var data = JSON.parse(event.data);

		document.getElementById("res").innerHTML = data.timeLeft;
	}
}

// error event
function onError(event) {
	console.log("onError()");
	console.log(event);
}

// close event
function onClose(event) {
	console.log("onClose()");
	console.log(event);
}

function connect() {
	console.log("connect()");

	console.time("WebSocket connect");
	ws = new WebSocket("ws://" + location.host + "/cd_sock");

	// set event handler
	ws.onopen = onOpen;
	ws.onmessage = onMessage;
	ws.onclose = onClose;
	ws.onerror = onError;
}

connect();
