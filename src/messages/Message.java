package messages;

public class Message {
	public enum MessageType {CONTROL, DATA, ERROR}
	public MessageHeader header;
	public MessageBody body;
	
	public Message(MessageType type, Integer messageID, MessageBody bdy)
	{
		this.body = bdy;
		this.header = new MessageHeader(type, messageID, bdy);
	}
}
