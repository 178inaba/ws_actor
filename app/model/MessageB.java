package model;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;

public class MessageB extends UntypedActor {

	private final ActorRef out;

	private String messageBox = "";

	public static Props props(ActorRef out) {
		Logger.info("MessageB: props()");

		return Props.create(MessageB.class, out);
	}

	public MessageB(ActorRef out) {
		Logger.info("MessageB: MessageB()");

		this.out = out;

		// send
		ObjectNode msg = Json.newObject();
		msg.put("message", "MessageB start");
		out.tell(msg.toString(), self());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		Logger.info("MessageB: onReceive(): [obj: " + message.toString() + "] [this: " + this.toString() + "]");

		if (message instanceof String) {
			Logger.info("MessageB: onReceive(): message instanceof String");
		}

		// update
		String before = messageBox;
		messageBox = message.toString();

		// send
		ObjectNode msg = Json.newObject();
		msg.put("message", "update messageBox");
		msg.put("before", before);
		msg.put("after", messageBox);
		out.tell(msg.toString(), self());
	}

	@Override
	public void postStop() throws Exception {
		Logger.info("MessageB: postStop()");
	}
}
