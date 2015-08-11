package model;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Akka;
import play.libs.Json;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class CountdownTimer extends UntypedActor {
	private final ActorRef out;

	public static Props props(ActorRef out) {
		Logger.info("CountdownTimer: props()");
		return Props.create(CountdownTimer.class, out);
	}

	public CountdownTimer(ActorRef out) {
		Logger.info("CountdownTimer: CountdownTimer()");
		this.out = out;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			// javascript
			Logger.info("CountdownTimer: onReceive(): String");
			Logger.info("CountdownTimer: onReceive(): message: " + message);
			JsonNode json = Json.parse(message.toString());
			String cmd = json.get("cmd").asText();
			Logger.info("CountdownTimer: onReceive(): cmd: " + cmd);
			if ("start".equals(cmd)) {
				countdownScheduler(json.get("limit").asInt(), json.get("interval").asInt(), self());
			}
		} else if (message instanceof Integer) {
			// scheduler
			Logger.info("CountdownTimer: onReceive(): Integer");
			Logger.info("CountdownTimer: onReceive(): message: " + message);
			ObjectNode notify = Json.newObject();
			notify.put("cmd", "notify");
			notify.put("timeLeft", (int) message);
			out.tell(notify.toString(), self());
		}
	}

	public static Cancellable countdownScheduler(int limit, int interval, ActorRef target) {
		final long lmtDate = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + limit;

		Cancellable c = Akka.system().scheduler().schedule(Duration.Zero(),
				Duration.create(interval, TimeUnit.SECONDS),
				() -> {
					Logger.info("tell schedule target: " + target.toString());
					int timeLeft = (int) (lmtDate - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
					target.tell(timeLeft, ActorRef.noSender());
				},
				Akka.system().dispatcher());

		Akka.system().scheduler().scheduleOnce(Duration.create(limit, TimeUnit.SECONDS),
				() -> {
					if (!c.isCancelled()) {
						Logger.info("cancel schedule: " + target.toString());
						c.cancel();
					} else {
						Logger.info("already cancel schedule: " + target.toString());
					}
				},
				Akka.system().dispatcher());

		return c;
	}
}
