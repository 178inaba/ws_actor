var ws = new WebSocket("ws://" + location.host + "/match_sock");
var id = 0;

// set event handler
ws.onopen = onOpen;
ws.onmessage = onMessage;
ws.onclose = onClose;
ws.onerror = onError;

// open event
function onOpen(event) {
	console.log("onOpen()");
	console.log(event);
}

// receive data event
function onMessage(event) {
	console.log("onMessage()");

	if(event && event.data){
		console.log(event);
		var data = JSON.parse(event.data);
		var result = "";
		switch (data.cmd) {
			case "authResult":
				console.log("onMessage(): cmd is authResult");
				id = data.id;
				result = "id is " + id;
				break;
			case "matchResult":
				console.log("onMessage(): cmd is matchResult");
				result = "state is " + data.state;
				break;
		}

		document.getElementById("res").innerHTML =
			"result: " + result + "<br>" +
			document.getElementById("res").innerHTML;
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

// send data
function match() {
	console.log("sendData()");

	var data = {
		cmd: "match",
		id:  id
	};
	var json = JSON.stringify(data);

	console.log("data: " + data);
	console.log("json: " + json);
	ws.send(json);
}
