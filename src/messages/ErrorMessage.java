package messages;

public class ErrorMessage extends MessageBody {
	public enum Error { CANNOT_FIND_DRONE, CONNECTION_ERROR, AUTHENTICATION_ERROR, 
		INVALID_CONTROL, INVALID_DATA, DRONE_LOW_BATTERY, CONTROLLER_LOW_BATTERY, 
		WEAK_SIGNAL, LOST_SIGNAL; }
	public byte[] error_code;
	
	public ErrorMessage(Error err) {
		this.error_code = new byte[] { (byte) err.ordinal() };
	}

	@Override
	public int length() {
		return error_code.length;
	}

	@Override
	public byte[] buildMessageBody() {
		return error_code;
	}
}
