package controller;

import model.Match;
import play.mvc.Controller;
import play.mvc.WebSocket;

public class Application extends Controller {
	public WebSocket<String> socket() {
		return WebSocket.withActor(Match::props);
	}
}
