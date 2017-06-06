package messages;

import java.util.Arrays;

import org.json.simple.parser.ParseException;

public class Message {
	public enum MessageType {CONTROL, DATA, ERROR, ACK}
	
	public MessageHeader header;
	public MessageBody body;
	
	public Message(MessageType type, Integer messageID, MessageBody bdy) throws Exception {		
		if ( 	(type != MessageType.CONTROL && bdy instanceof ControlMessage) ||
				(type != MessageType.DATA && bdy instanceof DataMessage) ||
				(type != MessageType.ERROR && bdy instanceof ErrorMessage) ||
				(type != MessageType.ACK && bdy instanceof AckMessage)
				) {
			String err = "Conflicting message type and message body." + System.lineSeparator() + "Message type: " + type + System.lineSeparator() + "Message body: " + bdy.getClass();
			throw new Exception(err);
		}
		
		this.header = new MessageHeader(type, messageID, bdy);
		
		this.body = bdy;
	}
	
	public Message(byte[] msg) throws Exception {
		//the header should simply be the first three bytes
		byte[] hdr = new byte[] { msg[0], msg[1], msg[2] };
		
		this.header = MessageHeader.fromByteArray(hdr);
		
		byte[] bdy = Arrays.copyOfRange(msg, hdr.length, this.header.length + hdr.length);
		
		switch (this.header.type) {
		case CONTROL:
			this.body = ControlMessage.fromByteArray(bdy);
			break;
		case DATA:
			this.body = DataMessage.fromByteArray(bdy);
			break;
		case ERROR:
			this.body = ErrorMessage.fromByteArray(bdy);
			break;
		case ACK:
			this.body = AckMessage.fromByteArray(bdy);
			break;
		default:
			throw new Exception("Could not parse message");
		}
	}
	
	public static byte[] toByteArray(Message msg) {
		byte[] hdr = msg.header.toByteArray();
		byte[] bdy = msg.body.toByteArray();
		
		//make an array of length header + body, and copy header into the beginning positions of the array
		byte[] result = Arrays.copyOf(hdr, hdr.length + bdy.length);
		
		//copy the header from position 0 into "result" starting at the end of the header and ending with the end of the body
		System.arraycopy(bdy, 0, result, hdr.length, bdy.length);
		
		return result;
	}
	
	public static Message fromByteArray(byte[] msg) throws Exception {
		return new Message(msg);
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Message) {
			Message other = (Message) obj;
			return (this.header.equals(other.header) && this.body.equals(other.body));
		}
		
		return false;
	}
}
