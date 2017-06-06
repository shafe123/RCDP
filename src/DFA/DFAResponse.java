package DFA;

import messages.Message;

/**
 * 
 * @author Ajinkya
 * This class defines the ideal response that DFA should give. 
 * DFA Response has a response message, error flag and error message.
 * We can add extra fields in response if needed in future versions.
 * 
 */
public class DFAResponse {
	Message message = null;
	boolean errorFlag = false;
	String errorMessage = null;
	
	public DFAResponse(Message message, boolean errorFlag, String errorMessage) {
		this.message = message;
		this.errorFlag = errorFlag;
		this.errorMessage = errorMessage;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public boolean isErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
