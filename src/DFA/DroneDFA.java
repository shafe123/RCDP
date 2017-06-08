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
* File name: DroneDFA.java
*
* Description:
* This class provides functionality related to Drone that extends the DFA class
* and provides drone actions based on the the different states of the protocol.
*
* Requirements (Additional details can be found in the file below):
* - STATEFUL, SERVICE, CONCURRENT
*
*=================================================================================
* */

package DFA;

import org.json.simple.JSONObject;

import messages.ControlMessage;
import messages.ErrorMessage;
import messages.ErrorMessage.ErrorType;
import messages.Message;
import messages.Message.MessageType;
import messages.MessageBody;

public class DroneDFA extends DFA {
	private String password;
	private String version;
	private String randomNumber;
	private String droneID;

	/**
	 * Constructs a drone based on the parameters passed
	 * @param password of type String, that represents the password
	 * @param version of type String, that represents the protocol version
	 * @param randomNumber of type String, represents the unique number for each message
	 * @param droneID of type String, represents a unique number identifier for the drone
	 */
	public DroneDFA(String password, String version, String randomNumber, String droneID) {
		this.password = password;
		this.version = version;
		this.randomNumber = randomNumber;
		this.droneID = droneID;
	}

	/**
	 * Authenticate determines if the message sent to the drone is valid based on the
	 * the DFA. This method throws an exception and may return an error the message
	 * is invalid.
	 * @param message of type Message, used to enable the drone to perform an action
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
			case 0x00:
						if(!params.containsKey("password")){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.AUTHENTICATION_ERROR));
							response = new DFAResponse(errorMessage, true, "password field not present in message params");
							return response;
						}
						//Authenticate Password
						String receiverPassword = (String) params.get("password");
						String dronePassword = password;
						if(Utility.isEmpty(receiverPassword)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.AUTHENTICATION_ERROR));
							response = new DFAResponse(errorMessage, true, "Receiver Password is null or empty");
							return response;
						}
						
						if(Utility.isEmpty(dronePassword)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.AUTHENTICATION_ERROR));
							response = new DFAResponse(errorMessage, true, "Drone Password is null or empty");
							return response;
						}
						
						if(!receiverPassword.equals(dronePassword)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.AUTHENTICATION_ERROR));
							response = new DFAResponse(errorMessage, true, "Incorrect Password");
							return response;
						}
						
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
						
						if(Utility.isEmpty(String.valueOf(params.get("random number A")))){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number A is null or empty");
							return response;
						}
						
						//Add random number from drone
						params.put("random number B", randomNumber);
						controlMessage.command = 0x01;
						ControlMessage responseControlMessage = new ControlMessage(controlMessage.type, controlMessage.command, params);
						Message responseMessage = new Message(MessageType.CONTROL, 3, responseControlMessage);
						response = new DFAResponse(responseMessage, false, null);
						return response;
			case 0x02:
						//Verify Version
						if(!params.containsKey("version")){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.VERSION_ERROR));
							response = new DFAResponse(errorMessage, true, "version field not present in message params");
							return response;
						}
						
						version = String.valueOf(params.get("version"));
						if(Utility.isEmpty(version)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.VERSION_ERROR));
							response = new DFAResponse(errorMessage, true, "Version is null or empty");
							return response;
						}
						
					    if(!version.equals(version)){
					    	errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.VERSION_ERROR));
					    	response = new DFAResponse(errorMessage, true, "Version is invalid");
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
						
						if(Utility.isEmpty(randomNumber)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number for drone is null or empty");
							return response;
						}
						
						if(!randomNumberB.equals(randomNumber)){
							errorMessage = new Message(MessageType.ERROR, messageID, new ErrorMessage(ErrorType.INVALID_RANDOM_NUMBER));
							response = new DFAResponse(errorMessage, true, "random number B sent by drone is invalid");
							return response;
						}
						
						responseControlMessage = new ControlMessage(controlMessage.type, controlMessage.command, params);
						responseMessage = new Message(MessageType.CONTROL, 3, responseControlMessage);
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
