package model.base;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import play.Logger;

public abstract class Base extends UntypedActor {
	private final ActorRef out;

	public static Props props(ActorRef out) {
		Logger.info("Base: props()");

		return Props.create(Base.class, out);
	}

	public Base(ActorRef out) {
		Logger.info("Base: Base()");

		this.out = out;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		Logger.info("Base: onReceive(): [message: " + message.toString() + "]");

		if (message instanceof String) {
			Logger.info("Base: onReceive(): message instanceof String");
		}
	}

	@Override
	public void postStop() throws Exception {
		Logger.info("Base: postStop()");
	}
}
