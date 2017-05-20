package messages;

public class Message {
	public enum MessageType {CONTROL, DATA, ERROR}
	public MessageHeader header;
	public MessageBody body;
	
	public Message(MessageType type, Integer messageID, MessageBody bdy) throws Exception
	{		
		if ( 	(type != MessageType.CONTROL && bdy instanceof ControlMessage) ||
				(type != MessageType.DATA && bdy instanceof DataMessage) ||
				(type != MessageType.ERROR && bdy instanceof ErrorMessage)
				)
		{
			String err = "Conflicting message type and message body." + System.lineSeparator() + "Message type: " + type + System.lineSeparator() + "Message body: " + bdy.getClass();
			throw new Exception(err);
		}
		
		this.header = new MessageHeader(type, messageID, bdy);
		this.body = bdy;
	}
}
