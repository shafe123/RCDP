/*================================================================================
* CS544 - Computer Networks
* Drexel University, Spring 2017
* Protocol Implementation: Remote Control Drone Protocol
* Team 4:
* - Ajinkya Dhage
* - Ethan Shafer
* - Brent Varga
* - Xiaxin Xin
* --------------------------------------------------------------------------------
* File name: MessageHeader.java
*
* Description:
* Defines the header of the message, types, and control mechanisms
*
* Requirements (Additional details can be found in the file below):
* -STATEFUL, SERVICE
*
*=================================================================================
* */

package Messages;

public class MessageHeader {

	/**
	 * The three types of Messages are "CONTROL", "DATA", AND "ERROR".
	 * The type will be encoded into a 2-bit flag based off of the enumeration.
	 * http://stackoverflow.com/questions/4032327/java-working-with-bits
	 */
	public Message.MessageType type;
	public int messageID;
	public int length;

	/**
	 * Constructs the message header based type, id, and body of the message
 	 * @param type of message of type MessageType
	 * @param id a unique identifier of type int
	 * @param bdy the body length of type int
	 */
	public MessageHeader(Message.MessageType type, int id, MessageBody bdy) {
		this.type = type;
		this.messageID = id;
		this.length = bdy.length(); //Entire message length, or the length of the body?
	}

	/**
	 * Construcst he message header based on a message in the form of a byte array
	 * @param hdr the header byte array
	 */
	public MessageHeader(byte[] hdr) {
		//get the MessageType from the first byte
		this.type = Message.MessageType.values()[hdr[0]];
		
		//coerce the second byte to int
		this.messageID = (int) hdr[1];
		
		//coerce the third byte to int
		this.length = (int) hdr[2];
	}

	/**
     * Implements the toByteArray returning the header of the message
	 * @return a message in byte array
	 */
	public byte[] toByteArray() {
		byte[] header = new byte[] { (byte) type.ordinal(), (byte) messageID, (byte) length };
		return header;
	}

	/**
	 * Takes in a message of type byte array but use the public constructor
	 * to return the MessageHeader
	 * @return a the header of the message of type Message
	 */
	public static MessageHeader fromByteArray(byte[] hdr) {
		return new MessageHeader(hdr);
	}

	/**
	 * Compares the equality of hte message header
	 * @param obj a message of Object type
	 * @return a boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MessageHeader) {
			MessageHeader other = (MessageHeader) obj;
			return (this.type == other.type && this.messageID == other.messageID && this.length == other.length);
		}
		
		return false;
	}

	/**
	 * Returns the message header as a string value
	 * @return a string value
	 */
	@Override
	public String toString() {
		String result = 
				"Type: " + this.type.toString() + System.lineSeparator() + 
				"MessageID: " + this.messageID + System.lineSeparator() + 
				"Length: " + this.length + System.lineSeparator() +
				"--------------------";
		return result;
	}
}
