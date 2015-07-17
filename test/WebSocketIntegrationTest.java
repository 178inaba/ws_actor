import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controller.Application;
import org.junit.Test;
import play.Logger;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.WebSocket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static play.test.Helpers.*;

public class WebSocketIntegrationTest {

	public static final BlockingQueue<String> msgQ = new LinkedBlockingQueue<>();

	@Test
	public void test() {
		Application app = new Application();
		WebSocket<String> ws = app.matchSock();

		running(fakeApplication(inMemoryDatabase()), () -> {
			Props wsProps = ws.actorProps(Akka.system().actorOf(MockActor.props));
			ActorRef wsRef = Akka.system().actorOf(wsProps);
			int id = 0;
			boolean end = false;
			try {
				while (true) {
					String msg = read();
					Logger.info("read(): " + msg);

					JsonNode json = Json.parse(msg);
					switch (json.get("cmd").asText()) {
						case "authResult":
							Logger.info("cmd: authResult");
							id = json.get("id").asInt();
							Logger.info("id is " + id);

							ObjectNode match = Json.newObject();
							match.put("cmd", "match");
							match.put("id", id);
							wsRef.tell(match.toString(), null);
							Logger.info("cmd: authResult");
							break;
						case "matchResult":
							Logger.info("cmd: matchResult");
							Logger.info("state is " + json.get("state").asText());
							end = true;
							break;
					}

					if (end) {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public String read() throws InterruptedException {
		return msgQ.poll(1, TimeUnit.SECONDS);
	}
}
