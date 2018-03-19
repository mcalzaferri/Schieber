package shared;

import java.net.InetAddress;
import ch.ntb.jass.common.proto.Message;

public class InternalMessage extends Message {
	public InetAddress SenderAddress = null;
	public Message message = null;
}
