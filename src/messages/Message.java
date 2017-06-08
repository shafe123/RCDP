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
* File name: Message.java
*
* Description:
* Defines all message types in the protocol that are encapsulated based on this
* message class.
*
* Requirements (Additional details can be found in the file below):
* -STATEFUL, SERVICE
*
*=================================================================================
* */

package messages;

import java.util.Arrays;

import org.json.simple.parser.ParseException;

public class Message {

	/**
	 * The four main message types with an additional TEST message type for testing purposes
	 */
	public enum MessageType {CONTROL, DATA, ERROR, ACK, TEST}
	
	public MessageHeader header;
	public MessageBody body;

	/**
	 * Constructs a Message based on the type, message id, and message body
	 * @param type of type MessageType
	 * @param messageID of type Integer, a unique value id
	 * @param bdy of type MessageBody
	 * @throws Exception if message cannot be parsed error message will be thrown
	 */
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

	/**
	 * Constructs a Message based on a byte array
	 * @param msg a message that is represented as a byte array
	 * @throws Exception if message cannot be parsed an error message is thrown
	 */
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

	/**
	 * Takes in a message of type Message object and converts it to a byte array
	 * @param msg the message to be converted of type Message
	 * @return a message in byte array
	 */
	public static byte[] toByteArray(Message msg) {
		byte[] hdr = msg.header.toByteArray();
		byte[] bdy = msg.body.toByteArray();
		
		//make an array of length header + body, and copy header into the beginning positions of the array
		byte[] result = Arrays.copyOf(hdr, hdr.length + bdy.length);
		
		//copy the header from position 0 into "result" starting at the end of the header and ending with the end of the body
		System.arraycopy(bdy, 0, result, hdr.length, bdy.length);
		
		return result;
	}

	/**
	 * Takes in a message of type byte array and returns a message of type Message
	 * @param msg the message to be converted of type byte array
	 * @return a message of type Message
	 * @throws Exception error message
	 */
	public static Message fromByteArray(byte[] msg) throws Exception {
		return new Message(msg);
	}

	/**
	 * Compares the equality of the message
	 * @param obj a message of Object type
	 * @return a boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Message) {
			Message other = (Message) obj;
			return (this.header.equals(other.header) && this.body.equals(other.body));
		}
		return false;
	}

	/**
	 * Returns the message as a string value
	 * @return a string value
	 */
	@Override
	public String toString() {
		return this.header.toString() + System.lineSeparator() + this.body.toString();
	}
}
