package model;

import akka.actor.ActorRef;
import akka.actor.Props;
import model.base.Base;
import play.Logger;

public class MessageA extends Base {
	public MessageA(ActorRef out) {
		super(out);
	}

	public static Props props(ActorRef out) {
		Logger.info("MessageA: props()");

		return Props.create(MessageA.class, out);
	}

	@Override
	protected int getInt() {
		return 22;
	}
}
