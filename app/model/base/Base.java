package model.base;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import play.Logger;

public abstract class Base extends UntypedActor {
	private final ActorRef out;

	public Base(ActorRef out) {
		Logger.info("Base: Base()");

		this.out = out;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		Logger.info("Base: onReceive(): [message: " + message.toString() + "] [" + getInt() + "]");

		if (message instanceof String) {
			Logger.info("Base: onReceive(): message instanceof String");
		}
	}

	@Override
	public void postStop() throws Exception {
		Logger.info("Base: postStop()");
	}

	protected abstract int getInt();
}
