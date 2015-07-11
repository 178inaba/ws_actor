package model;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match extends UntypedActor {

	private final ActorRef out;

	public static int idCnt = 0;
	public static Map<Integer, ActorRef> outMap = new HashMap<>();
	public static List<Integer> waitList = new ArrayList<>();

	public static Props props(ActorRef out) {
		Logger.info("Match: props()");
		return Props.create(Match.class, out);
	}

	public Match(ActorRef out) {
		Logger.info("Match: Match()");
		this.out = out;

		idCnt++;

		outMap.put(idCnt, out);

		// send id
		ObjectNode authResult = Json.newObject();
		authResult.put("cmd", "authResult");
		authResult.put("id", idCnt);
		out.tell(authResult.toString(), self());
	}

	@Override
	public void onReceive(Object obj) throws Exception {
		Logger.info("Match: onReceive(): [obj: " + obj.toString() + "]");
		if (obj instanceof String) {
			JsonNode json = Json.parse(obj.toString());
			String cmd = json.get("cmd").asText();
			if (cmd.equals("match")) {
				if (waitList.size() == 0) {
					waitList.add(json.get("id").asInt());

					ObjectNode matchResult = Json.newObject();
					matchResult.put("cmd", "matchResult");
					matchResult.put("state", "wait");

					out.tell(matchResult.toString(), self());
				} else {
					int enemyId = waitList.get(0);
					waitList.remove(0);

					ActorRef enemyOut = outMap.get(enemyId);

					ObjectNode matchResult = Json.newObject();
					matchResult.put("cmd", "matchResult");
					matchResult.put("state", "match");
					matchResult.put("gameServerUrl", "ws://xxx/");
					matchResult.put("enemyId", enemyId);

					out.tell(matchResult.toString(), self());

					matchResult.put("enemyId", json.get("id").asInt());

					enemyOut.tell(matchResult.toString(), self());
				}
			} else {
				out.tell("unknown command: " + cmd, self());
			}
		}
	}

	@Override
	public void postStop() throws Exception {
		Logger.info("Match: postStop()");
	}
}
