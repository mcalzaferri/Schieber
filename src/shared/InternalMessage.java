package shared;

import java.net.InetSocketAddress;

import ch.ntb.jass.common.proto.Message;

public class InternalMessage {
	public InetSocketAddress senderAddress = null;
	public Message message = null;
}
