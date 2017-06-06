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
* File name: DFAResponse.java
*
* Description:
* This class defines the ideal response that DFA should give.
* DFA Response has a response message, error flag and error message.
* We can add extra fields in response if needed in future versions.
*
* Requirements (Additional details can be found in the file below):
* - STATEFUL
*
*=================================================================================
* */
package DFA;

import messages.Message;

public class DFAResponse {
	Message message = null;
	boolean errorFlag = false;
	String errorMessage = null;

	/**
	 * Initial constructor that accepts a message, error flag, and error message
	 * @param message of type Message, the main message body
	 * @param errorFlag of type boolean
	 * @param errorMessage of type String
	 */
	public DFAResponse(Message message, boolean errorFlag, String errorMessage) {
		this.message = message;
		this.errorFlag = errorFlag;
		this.errorMessage = errorMessage;
	}

	/**
	 * @return a message of type Message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * @param message takes a type Message and sets
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
	 * @return an errorFlag of type boolean
	 */
	public boolean isErrorFlag() {
		return errorFlag;
	}

	/**
	 * @param errorFlag takes a type boolean and sets errorFlag
	 */
	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	/**
	 * @return an errorMessage of type String
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage takes a type errorMessage of type string to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
