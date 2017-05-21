package messages;

class MessageHeader {
	//The three types of Messages are "CONTROL", "DATA", AND "ERROR".
	//The type will be encoded into a 2-bit flag based off of the enumeration.
	//http://stackoverflow.com/questions/4032327/java-working-with-bits	
	public Message.MessageType type;
	public byte messageID;
	public byte length;
	
	public MessageHeader(Message.MessageType type, int id, MessageBody bdy) {
		this.type = type;
		this.messageID = (byte) id;
		//do we want this to be the entire message length, or just the length of the body?
		this.length = (byte) bdy.length();
	}
	
	public byte[] buildMessageHeader() {
		byte[] header = new byte[] { (byte) type.ordinal(), messageID, length };
		return header;
	}
}
