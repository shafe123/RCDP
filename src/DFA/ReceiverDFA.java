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
* File name: ReceiverDFA.java
*
* Description:
* This class provides functionality related to Receiver by extending the DFA class
*
* Requirements (Additional details can be found in the file below):
* - STATEFUL, CLIENT
*
*=================================================================================
* */

package DFA;

import org.json.simple.JSONObject;

import messages.ControlMessage;
import messages.ErrorMessage;
import messages.Message;
import messages.MessageBody;
import messages.ControlMessage.ControlType;
import messages.ErrorMessage.ErrorType;
import messages.Message.MessageType;

public class ReceiverDFA extends DFA {
	private String password;
	private String version;
	private String randomNumber;

	/**
	 * Constructs a receiver based on the parameters passed
	 * @param password of type String, that represents the password
	 * @param version of type String, that represents the protocol version
	 * @param randomNumber of type String, represents the unique number for each message
	 */
	public ReceiverDFA(String password, String version, String randomNumber) {
		this.password = password;
		this.version = version;
		this.randomNumber = randomNumber;
	}

	/**
	 * Authenticate determines if the message sent to the receiver is valid based on the
	 * the DFA. This method throws an exception and may return an error the message
	 * is invalid.
	 * @param message of type Message, used to enable the receiver to perform an action
	 * @return type of DFAResponse object
	 * @throws Exception an IOException
	 */
	@Override
	public DFAResponse authenticate(Message message) throws Exception {
		DFAResponse response = null;
		MessageBody messageBody = message.body;
		Message errorMessage = null;
		int messageID = message.header.messageID;

		/**
		 * Checks to see if the message is a control message
		 */
		if(messageBody instanceof ControlMessage){
			ControlMessage controlMessage = (ControlMessage) messageBody;
			JSONObject params = controlMessage.params;
			byte command = controlMessage.command;

			/**
			 * Checks the message against the current state
			 */
			switch (command) {
			case 0x01:
						if(!params.containsKey("version")){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.VERSION_ERROR));
							response = new DFAResponse(errorMessage, true, "version field not present in message params");
							return response;
						}
						
						//set minimum of receiverVersion and droneVersion as final version.
						double receiverVersion = Double.parseDouble(String.valueOf(params.get("version")));
						double droneVersion = Double.parseDouble(version);
						String version = String.valueOf(receiverVersion < droneVersion ? receiverVersion : droneVersion);
						params.put("version", version);
						
						if(!params.containsKey("random number A")){//Check if random number from receiver is present
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number A field not present in message params");
							return response;
						}
						
						//Verify random number A sent by drone
						String randomNumberA = String.valueOf(params.get("random number A"));
						if(Utility.isEmpty(randomNumberA)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number A is null or empty");
							return response;
						}
						
						if(Utility.isEmpty(randomNumber)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number for receiver is null or empty");
							return response;
						}
						
						if(!randomNumberA.equals(randomNumber)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number A sent by drone is invalid");
							return response;
						}
						
						//Verify random number B sent by drone 
						if(!params.containsKey("random number B")){//Check if random number from receiver is present
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number B field not present in message params");
							return response;
						}
						String randomNumberB = String.valueOf(params.get("random number B"));
						if(Utility.isEmpty(randomNumberB)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number B is null or empty");
							return response;
						}
						controlMessage.command = 0x02;
						
						ControlMessage responseControlMessage = new ControlMessage(controlMessage.type, controlMessage.command, params);
						Message responseMessage = new Message(MessageType.CONTROL, 3, responseControlMessage);
						response = new DFAResponse(responseMessage, false, null);
						return response;
			default:
						errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_COMMAND));
						return new DFAResponse(errorMessage, true, "Invalid Command Byte");
			}
		}
		/**
		 * If not, returns an error message
		 */
		errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_MESSAGE));
		return new DFAResponse(errorMessage, true, "Message is not of type Control Message");
	}
}
