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
* File name: DFAState.java
*
* Description:
* This class provides static methods to process DFA States and checks the current
* state against and acceptable message and returns an error message.
*
* Requirements (Additional details can be found in the file below):
* - STATEFUL
*
*=================================================================================
* */

package DFA;

import org.json.simple.JSONObject;

import messages.ControlMessage;
import messages.ErrorMessage;
import messages.ControlMessage.ControlType;
import messages.ErrorMessage.ErrorType;
import messages.Message.MessageType;
import messages.Message;
import messages.MessageBody;

public class DFAState {

	static DFAResponse nextState;

	/**
	 * This method returns a object type of DFAResponse based on the message, control type, and current state.
	 * If an invalid command is sent the function will return an error message.
	 * @param message of type Message the current message to be processed
	 * @param currentState of type ControlType which is the current state of the message
	 * @return a DFAResponse for the next state
	 * @throws Exception an IOException
	 */
	public static DFAResponse getNextState(Message message, ControlType currentState) throws Exception {
		DFAResponse response = null;
		MessageBody messageBody = message.body;
		Message errorMessage = null;
		int messageID = message.header.messageID;
		ControlMessage controlMessage = null;
		JSONObject params = null;

		/**
		 * Checks to see if the message is a control message
		 */
		if(messageBody instanceof ControlMessage){
			controlMessage = (ControlMessage) messageBody;
			params = controlMessage.params;
			byte command = controlMessage.command;

			/**
			 * Checks the message against the current state
			 */
			switch (currentState) {
				case PREFLIGHT:
								switch (command) {
									case 0x00:
												controlMessage.type = ControlType.PREFLIGHT;
												break;
									case 0x01:	
												controlMessage.type = ControlType.PREFLIGHT;
												break;
									case 0x02:
												controlMessage.type = ControlType.FLYING;
												break;
									default:
												errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_COMMAND));
												return new DFAResponse(errorMessage, true, "Invalid Command Byte");
									}
								break;
				case FLYING:	
								switch (command) {
									case 0x00:
												controlMessage.type = ControlType.FLYING;
												break;
									case 0x01:
												controlMessage.type = ControlType.FLYING;
												break;
									case 0x02:
												controlMessage.type = ControlType.AUTONOMOUS;
												break;
									case 0x03:
												controlMessage.type = ControlType.PREFLIGHT;
												break;
									default:
												errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_COMMAND));
												return new DFAResponse(errorMessage, true, "Invalid Command Byte");
								}
								break;
				case AUTONOMOUS:
								switch (command) {
									case 0x00:
												controlMessage.type = ControlType.FLYING;
												break;
									case 0x01:
												controlMessage.type = ControlType.FLYING;
												break;
									case 0x02:
												controlMessage.type = ControlType.PREFLIGHT;
												break;
									default:
												errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_COMMAND));
												return new DFAResponse(errorMessage, true, "Invalid Command Byte");
								}
								break;
				case BEACON:	
							switch (command) {
								case 0x00:
											if(!params.containsKey("to_mode")){
												errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.JSON_PARAMETER_ERROR));
												response = new DFAResponse(errorMessage, true, "to_mode field is "
																						+ "not present in message params");
												return response;
											}
											
											String to_mode = String.valueOf(params.get("to_mode"));
											if(Utility.isEmpty(to_mode)){
												errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.JSON_PARAMETER_ERROR));
												response = new DFAResponse(errorMessage, true, "to_mode field in message "
																						+ "params is null or empty");
												return response;
											}
											
											if(to_mode.equalsIgnoreCase(String.valueOf(ControlType.GROUNDED))){
												controlMessage.type = ControlType.GROUNDED;
											}
											else if(to_mode.equalsIgnoreCase(String.valueOf(ControlType.PREFLIGHT))){
												controlMessage.type = ControlType.PREFLIGHT;
											}
											else{
												errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.JSON_PARAMETER_ERROR));
												response = new DFAResponse(errorMessage, true, "value of to_mode field in message "
																						+ "params is invalid");
												return response;
											}
											break;
								case 0x01:
											controlMessage.type = ControlType.BEACON;
											break;
								default:
											errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_COMMAND));
											return new DFAResponse(errorMessage, true, "Invalid Command Byte");
							}
							break;
				default:
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_STATE));
							return new DFAResponse(errorMessage, true, "Current State is Invalid");
			}
		}

		/**
		 * If not, returns an error message
		 */
		else{
			errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_MESSAGE));
			return new DFAResponse(errorMessage, true, "Invalid messageBody type");
		}
		ControlMessage responseControlMessage = new ControlMessage(controlMessage.type, controlMessage.command, params);
		Message responseMessage = new Message(MessageType.CONTROL, 3, responseControlMessage);
		nextState = new DFAResponse(responseMessage, false, null);
		return nextState;
	}
}
