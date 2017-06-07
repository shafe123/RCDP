package DFA;

import org.json.simple.JSONObject;

import messages.ControlMessage;
import messages.ErrorMessage;
import messages.ControlMessage.ControlType;
import messages.ErrorMessage.ErrorType;
import messages.Message.MessageType;
import messages.Message;
import messages.MessageBody;

/**
 * 
 * @author Ajinkya
 * This class provides static methods to process DFA States 
 */
public class DFAState {
	static DFAResponse nextState;

	public static DFAResponse getNextState(Message message, ControlType currentState) throws Exception {
		DFAResponse response = null;
		MessageBody messageBody = message.body;
		Message errorMessage = null;
		int messageID = message.header.messageID;
		ControlMessage controlMessage = null;
		JSONObject params = null;
		if(messageBody instanceof ControlMessage){
			controlMessage = (ControlMessage) messageBody;
			params = controlMessage.params;
			byte command = controlMessage.command;
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
									case 0x03:
												controlMessage.type = ControlType.FLYING;
												break;
									case 0x04:
												controlMessage.type = ControlType.FLYING;
												break;
									case 0x05:
												controlMessage.type = ControlType.AUTONOMOUS;
												break;
									case 0x06:
												controlMessage.type = ControlType.PREFLIGHT;
												break;
									default:
												errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_COMMAND));
												return new DFAResponse(errorMessage, true, "Invalid Command Byte");
								}
								break;
				case AUTONOMOUS:
								switch (command) {
									case 0x07:
												controlMessage.type = ControlType.FLYING;
												break;
									case 0x08:
												controlMessage.type = ControlType.FLYING;
												break;
									case 0x09:
												controlMessage.type = ControlType.PREFLIGHT;
												break;
									default:
												errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_COMMAND));
												return new DFAResponse(errorMessage, true, "Invalid Command Byte");
								}
								break;
				case BEACON:	
							switch (command) {
								case 0x0A:
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
								case 0x0B:
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
