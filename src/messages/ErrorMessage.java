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
* File name: ErrorMessage.java
*
* Description:
* Defines the protocol various error codes. All message adhere to the documentation
* of size 8-bits even thought the language does not support bit data types
* we used bytes instead.
*
* Requirements (Additional details can be found in the file below):
* -STATEFUL, SERVICE
*
*=================================================================================
* */

package messages;

public class ErrorMessage extends MessageBody {
	public enum ErrorType { CANNOT_FIND_DRONE, CONNECTION_ERROR, AUTHENTICATION_ERROR, 
		INVALID_CONTROL, INVALID_DATA, DRONE_LOW_BATTERY, CONTROLLER_LOW_BATTERY, 
		WEAK_SIGNAL, LOST_SIGNAL, VERSION_ERROR, INVALID_RANDOM_NUMBER, INVALID_COMMAND, INVALID_MESSAGE, 
		JSON_PARAMETER_ERROR, INVALID_STATE;}
	
	
	public ErrorType error_code;

	/**
	 * Default constructor sets the error code
 	 * @param err an error code of enumerate type
	 */
	public ErrorMessage(ErrorType err) {
		this.error_code = err;
	}

	/**
	 * Construct the error message based on a byte array header
 	 * @param hdr the header of type byte array
	 */
	public ErrorMessage(byte[] hdr) {
		this.error_code = ErrorType.values()[hdr[0]];
	}

	/**
	 * @return the length of the error message
	 */
	@Override
	public int length() {
		return 1;
	}

	/**
	 * @return the error message as a byte array
	 */
	@Override
	public byte[] toByteArray() {
		return new byte[] { (byte) error_code.ordinal() };
	}

	/**
	 * Compares the error message to see if they contain the same error code
 	 * @param obj a type object to compare
	 * @return ture if the error codes match otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ErrorMessage) {
			ErrorMessage other = (ErrorMessage) obj;
			return (this.error_code == other.error_code);
		}
		return false;
	}

	/**
	 * Creates a static error message of type byte array and returns error message as a MessageBody object
	 * @param bdy the errror message as a type byte array
	 * @return the error message as a type MessageBody
	 */
	public static MessageBody fromByteArray(byte[] bdy) {
		return new ErrorMessage(bdy);
	}

	/**
	 * @return an error message as a string
	 */
	@Override
	public String toString() {
		return "Error Code: " + this.error_code;
	}
}
