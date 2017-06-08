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
* File name: AckMessage.java
*
* Description:
* Defines the ack message and related functionality
*
* Requirements (Additional details can be found in the file below):
* -STATEFUL, SERVICE
*
*=================================================================================
* */

package Messages;

public class AckMessage extends MessageBody {
	public int MessageID;

	/**
     * Constructs the ack message based on a message id
	 * @param msgID that represents the unique identifier of type int
	 */
	public AckMessage(int msgID) {
		this.MessageID = msgID;
	}

	/**
     * Constructs the ack message based on a byte array
	 * @param bdy a message body that is represented as a byte array
	 */
	public AckMessage(byte[] bdy) {
		this.MessageID = (int) bdy[0];
		
	}

	/**
 	 * @return an int value of 1
	 */
	@Override
	public int length() {
		return 1;
	}

	/**
	 * Implements the tobytearray that returns the ack as a byte array
	 * @return  byte array
	 */
	@Override
	public byte[] toByteArray() {
		return new byte[] { (byte) MessageID };
	}

	/**
	 * Implements equality on a ack message type
	 * @param obj a type object to compare
	 * @return a boolean value, if equal then return true otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AckMessage) {
			return this.MessageID == ((AckMessage) obj).MessageID;
		}
		return false;
	}

	/**
	 * Converts a byte array to a message object
	 * @param bdy the body of the message as a byte array
	 * @return the body message as a type of message
	 */
	public static MessageBody fromByteArray(byte[] bdy) {
		return new AckMessage(bdy);
	}

	/**
 	 * @return the ack message as a type String
	 */
	@Override
	public String toString() {
		String result = "ID Ack'd: " + this.MessageID;
		return result;
	}
}
