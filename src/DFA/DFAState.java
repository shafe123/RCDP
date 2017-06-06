package DFA;

import org.json.simple.JSONObject;

import messages.ControlMessage;
import messages.ControlMessage.ControlType;
import messages.Message;
import messages.MessageBody;

/**
 * 
 * @author Ajinkya
 * This class provides static methods to process DFA States 
 */
public class DFAState {
	static DFAResponse nextState;

	public static DFAResponse getNextState(Message message, ControlType currentState) {
		DFAResponse response = null;
		MessageBody messageBody = message.body;
		if(messageBody instanceof ControlMessage){
			ControlMessage controlMessage = (ControlMessage) messageBody;
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
												return new DFAResponse(message, true, "Invalid Command Byte");
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
												return new DFAResponse(message, true, "Invalid Command Byte");
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
												return new DFAResponse(message, true, "Invalid Command Byte");
								}
								break;
				case BEACON:	
							switch (command) {
								case 0x00:
											JSONObject params = controlMessage.params;
											if(!params.containsKey("to_mode")){
												response = new DFAResponse(message, true, "to_mode field is "
																						+ "not present in message params");
												return response;
											}
											
											String to_mode = String.valueOf(params.get("to_mode"));
											if(Utility.isEmpty(to_mode)){
												response = new DFAResponse(message, true, "to_mode field in message "
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
												response = new DFAResponse(message, true, "value of to_mode field in message "
																						+ "params is invalid");
												return response;
											}
											break;
								case 0x01:
											controlMessage.type = ControlType.BEACON;
											break;
								default:
											return new DFAResponse(message, true, "Invalid Command Byte");
							}
							break;
				default:
							return new DFAResponse(message, true, "Current State is Invalid");
			}
		}
		else{
			return new DFAResponse(message, true, "Invalid messageBody type");
		}
		nextState = new DFAResponse(message, false, null);
		return nextState;
	}
}
