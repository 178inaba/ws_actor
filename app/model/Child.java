package model;

import akka.actor.ActorRef;
import akka.actor.Props;
import model.base.Base;
import play.Logger;

public class Child extends Base {
	public Child(ActorRef out) {
		super(out);
	}

	public static Props props(ActorRef out) {
		Logger.info("Child: props()");

		return Props.create(Child.class, out);
	}

	@Override
	protected int getInt() {
		return 26;
	}
}
