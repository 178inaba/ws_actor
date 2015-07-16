package controller;

import model.Match;
import model.MessageA;
import model.MessageB;
import play.Logger;
import play.mvc.Controller;
import play.mvc.WebSocket;

import java.util.Map;

public class Application extends Controller {

	public Application() {
		Logger.info("Application: Application()");
	}

	public WebSocket<String> socket() {
		Logger.info("request(): " + request().toString());
		Logger.info("request().body(): " + request().body());

		for (Map.Entry<String, String[]> header : request().headers().entrySet()) {
			Logger.info("header: [k: " + header.getKey() + "]");

			for (String v : header.getValue()) {
				Logger.info("\theader: [v: " + v + "]");
			}
		}

		return WebSocket.withActor(Match::props);
	}

	public WebSocket<String> msga() {
		return WebSocket.withActor(MessageA::props);
	}

	public WebSocket<String> msgb() {
		return WebSocket.withActor(MessageB::props);
	}
}
