package model;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;

public class MsgBoard extends UntypedActor {

	private final ActorRef out;

	private String messageBoard = "";

	public static Props props(ActorRef out) {
		Logger.info("MsgBoard: props()");

		return Props.create(MsgBoard.class, out);
	}

	public MsgBoard(ActorRef out) {
		Logger.info("MsgBoard: MsgBoard()");

		this.out = out;

		// send
		ObjectNode msg = Json.newObject();
		msg.put("message", "MsgBoard start");
		out.tell(msg.toString(), self());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		Logger.info("MsgBoard: onReceive(): [obj: " + message.toString() + "] [this: " + this.toString() + "]");

		if (message instanceof String) {
			Logger.info("MsgBoard: onReceive(): message instanceof String");
		}

		// update
		String before = messageBoard;
		messageBoard = message.toString();

		// send
		ObjectNode msg = Json.newObject();
		msg.put("message", "update messageBoard");
		msg.put("before", before);
		msg.put("after", messageBoard);
		out.tell(msg.toString(), self());
	}

	@Override
	public void postStop() throws Exception {
		Logger.info("MsgBoard: postStop()");
	}
}
