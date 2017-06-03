package messages;

class MessageHeader {
	//The three types of Messages are "CONTROL", "DATA", AND "ERROR".
	//The type will be encoded into a 2-bit flag based off of the enumeration.
	//http://stackoverflow.com/questions/4032327/java-working-with-bits	
	public Message.MessageType type;
	public int messageID;
	public int length;
	
	public MessageHeader(Message.MessageType type, int id, MessageBody bdy) {
		this.type = type;
		this.messageID = id;
		//do we want this to be the entire message length, or just the length of the body?
		this.length = bdy.length();
	}
	
	public MessageHeader(byte[] hdr) {
		//get the MessageType from the first byte
		this.type = Message.MessageType.values()[hdr[0]];
		
		//coerce the second byte to int
		this.messageID = (int) hdr[1];
		
		//coerce the third byte to int
		this.length = (int) hdr[2];
	}
	
	public byte[] toByteArray() {
		byte[] header = new byte[] { (byte) type.ordinal(), (byte) messageID, (byte) length };
		return header;
	}
	
	public static MessageHeader fromByteArray(byte[] hdr) {
		//use the public constructor to return the MessageHeader
		return new MessageHeader(hdr);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MessageHeader) {
			MessageHeader other = (MessageHeader) obj;
			return (this.type == other.type && this.messageID == other.messageID && this.length == other.length);
		}
		
		return false;
	}
}
