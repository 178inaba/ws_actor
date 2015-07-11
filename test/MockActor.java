import akka.actor.Props;
import akka.actor.UntypedActor;
import play.Logger;

public class MockActor extends UntypedActor {

	public static Props props = Props.create(MockActor.class);

	@Override
	public void onReceive(Object obj) throws Exception {
		String msg = obj.toString();
		Logger.info("MockActor: onReceive(): [msg: " + msg + "]");

		WebSocketIntegrationTest.msgQ.add(msg);
	}
}
