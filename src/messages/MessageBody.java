package messages;

public abstract class MessageBody {
	//returns the number of bytes used to represent the body
	public abstract int length();
	
	//returns the actual message body as an array of bytes
	public abstract byte[] buildMessageBody();
}
