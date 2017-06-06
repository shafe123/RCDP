package messages;

public class ErrorMessage extends MessageBody {
	public enum ErrorType { CANNOT_FIND_DRONE, CONNECTION_ERROR, AUTHENTICATION_ERROR, 
		INVALID_CONTROL, INVALID_DATA, DRONE_LOW_BATTERY, CONTROLLER_LOW_BATTERY, 
		WEAK_SIGNAL, LOST_SIGNAL; }
	
	
	public ErrorType error_code;
	
	public ErrorMessage(ErrorType err) {
		this.error_code = err;
	}
	
	public ErrorMessage(byte[] hdr) {
		this.error_code = ErrorType.values()[hdr[0]];
	}

	@Override
	public int length() {
		return 1;
	}

	@Override
	public byte[] toByteArray() {
		return new byte[] { (byte) error_code.ordinal() };
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ErrorMessage) {
			ErrorMessage other = (ErrorMessage) obj;
			return (this.error_code == other.error_code);
		}
		return false;
	}

	public static MessageBody fromByteArray(byte[] bdy) {
		return new ErrorMessage(bdy);
	}
	
	@Override
	public String toString() {
		return "Error Code: " + this.error_code;
	}
}
