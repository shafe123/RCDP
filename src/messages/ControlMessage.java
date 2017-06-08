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
* File name: ControlMessage.java
*
* Description:
* Defines the protocol current state using 3-bit state code as indicated in the
* RCDP documentation. This class extends Message body and implements
* common functionality of the message class.
*
* Requirements (Additional details can be found in the file below):
* -STATEFUL, SERVICE
*
*=================================================================================
* */

package messages;

import java.util.Arrays;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ControlMessage extends MessageBody {
	public enum ControlType { UNIVERSAL, GROUNDED, PREFLIGHT, FLYING, AUTONOMOUS, BEACON; }
	
	public ControlType type;
	public byte command;
	public JSONObject params;

	/**
	 * Constructs a control message provided the following parameters and ensures the
	 * size of the parameters does not exceed 128 bytes. If so an error will be thrown
 	 * @param type the type of control message of type ControlType
	 * @param cmd the control message in byte format
	 * @param parameters reference to a json object that contains the parameters
	 * @throws Exception parameter length is over 128 bytes
	 */
	public ControlMessage(ControlType type, byte cmd, JSONObject parameters) throws Exception {
		if(parameters.toString().getBytes().length > 128) {
			String err = "Size of parameters is too large to send message.";
			throw new Exception(err);
		}
		
		this.type = type;
		this.command = cmd;
		this.params = parameters;
	}

	/**
	 * Constructs a control message based on a message body as a type of byte array
 	 * @param bdy that represents the bod of the message as a byte array
	 * @throws ParseException if the message body can not be parsed
	 */
	public ControlMessage(byte[] bdy) throws ParseException {
		this.type = ControlType.values()[bdy[0]];
		switch (this.type) {
		/*case AUTONOMOUS:
			break;
		case BEACON:
			break;
		case FLYING:
			break;
		case GROUNDED:
			break;
		case PREFLIGHT:
			break;
		case UNIVERSAL:
			break;*/
		default:
			this.command = bdy[1];
			break;
		}
		
		//convert the last bytes into the JSON Object parameters
		byte[] byteParams = Arrays.copyOfRange(bdy, 2, bdy.length);
		JSONParser parser = new JSONParser();
		String stringParams = new String(byteParams);
		this.params = (JSONObject) parser.parse(stringParams);
	}

	/**
 	 * @return the current length of the control message of type int
	 */
	@Override
	public int length() {
		return 2 + params.toString().getBytes().length;
	}

	/**
	 * @return the control message as a type of byte array
	 */
	@Override
	public byte[] toByteArray() {
		byte[] body = new byte[this.length()];
		body[0] = (byte) type.ordinal();
		body[1] = 0;
		
		byte[] json = params.toString().getBytes();
		for (int i = 0; i < json.length; i++)
		{
			body[i+2] = json[i];
		}
		
		return body;
	}

	/**
 	 * @param obj a type object to compare
	 * @return returns true if objects are the same based on the PDU's type, command, and params
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ControlMessage) {
			ControlMessage other = (ControlMessage) obj;
			return (this.type == other.type && this.command == other.command && this.params.equals(other.params));
		}
		return false;
	}

	/**
	 * Converts the control message using the message body returning a MessageBody object
	 * @param bdy the message body of type byte array
	 * @return the control message body of type MessageBody
	 * @throws ParseException if the message body of type byte array an exception will be thrown
	 */
	public static MessageBody fromByteArray(byte[] bdy) throws ParseException {
		return new ControlMessage(bdy);
	}

	/**
 	 * @return the control message as a string
	 */
	@Override
	public String toString() {
		String result = 
				"Control type: " + this.type + System.lineSeparator() +
				"Command: " + (int) this.command + System.lineSeparator() +
				"Params: " + this.params.toString();
		return result;
	}
}
