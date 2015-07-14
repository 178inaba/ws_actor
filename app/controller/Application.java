package controller;

import model.Match;
import model.MessageA;
import play.mvc.Controller;
import play.mvc.WebSocket;

public class Application extends Controller {
	public WebSocket<String> socket() {
		return WebSocket.withActor(Match::props);
	}

	public WebSocket<String> msga() {
		return WebSocket.withActor(MessageA::props);
	}
}
