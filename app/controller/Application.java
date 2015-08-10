package controller;

import model.Child;
import model.CountdownTimer;
import model.Match;
import model.MsgBoard;
import play.Logger;
import play.mvc.Controller;
import play.mvc.WebSocket;

import java.util.Map;

public class Application extends Controller {

	public Application() {
		Logger.info("Application: Application()");
	}

	public WebSocket<String> matchSock() {
		try {
			Logger.info("request(): " + request().toString());
			Logger.info("request().body(): " + request().body());

			for (Map.Entry<String, String[]> header : request().headers().entrySet()) {
				Logger.info("header: [k: " + header.getKey() + "]");

				for (String v : header.getValue()) {
					Logger.info("\theader: [v: " + v + "]");
				}
			}
		} catch (Exception e) {
			Logger.info("log exception: " + e.toString());
		}

		return WebSocket.withActor(Match::props);
	}

	public WebSocket<String> child() {
		return WebSocket.withActor(Child::props);
	}

	public WebSocket<String> msgBoard() {
		return WebSocket.withActor(MsgBoard::props);
	}

	public WebSocket<String> countdownTimer() {
		return WebSocket.withActor(CountdownTimer::props);
	}
}
