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
* File name: DataMessage.java
*
* Description:
* Defines the protocol data message which consist of a data type, flag, and
* data frames. It is used to communicate information between the receiver and
* the drone in exchange video feed, sensor data, image, and navigation commands.
*
* Requirements (Additional details can be found in the file below):
* -STATEFUL, SERVICE
*
*=================================================================================
* */
package messages;

import java.util.Arrays;

public class DataMessage extends MessageBody {
	public enum DataType { IMAGE, VIDEO, AUDIO, NAVIGATION; }
	
	public DataType type;
	public byte flag;
	public byte[] data;

	/**
	 * Constructs a new data message based on the following parameters and does
	 * exceed 125 bytes in length.
	 * @param type the type of control message of type ControlType
	 * @param flag the flag of he data message of type byte
	 * @param data that is encapsulated in the message of type byte array
	 * @throws Exception parameter length is over 125 bytes
	 */
	public DataMessage(DataType type, byte flag, byte[] data) throws Exception {
		if (data.length > 125) {
			String err = "Size of data is too large to send message.";
			throw new Exception(err);
		}
		
		this.type = type; 
		this.flag = flag;
		this.data = data;
	}

	/**
	 * Constructs a data message based on a message body as a type of byte array
	 * @param bdy that represents the data message as a byte array
	 */
	public DataMessage(byte[] bdy) {
		this.type = DataType.values()[bdy[0]];
		this.flag = bdy[1];
		this.data = Arrays.copyOfRange(bdy, 2, bdy.length);
	}

	/**
	 * @return the current length of the data message
	 */
	@Override
	public int length() {
		return 1 + 1 + data.length;
	}

	/**
	 * @return the data message as a type of byte array
	 */
	@Override
	public byte[] toByteArray() {
		byte[] body = new byte[this.length()];
		body[0] = (byte) type.ordinal();
		body[1] = flag;
		for (int i = 0; i < data.length; i++) {
			body[i+2] = data[i];
		}
		return body;
	}

	/**
	 * Compares the data message to see if they are the same
	 * @param obj a type object to compare
	 * @return true if objects are the same based on the PDU's flag, type, and data
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DataMessage) {
			DataMessage other = (DataMessage) obj;
			return (this.flag == other.flag && this.type == other.type && Arrays.equals(this.data, other.data));
		}
		
		return false;
	}

	/**
	 * Creates a static data message of type byte array and returns data message as a MessageBody object
	 * @param bdy the data message as a type byte array
	 * @return the data message as a type MessageBody
	 */
	public static MessageBody fromByteArray(byte[] bdy) {
		return new DataMessage(bdy);
	}

	/**
	 * @return a data message as a string
	 */
	@Override
	public String toString() {
		String result = 
				"Data type: " + this.type + System.lineSeparator() +
				"Flag: " + (int) this.flag + System.lineSeparator() +
				"Data length: " + this.data.length;
		return result;
	}
}
//
