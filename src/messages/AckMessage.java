package messages;

public class AckMessage extends MessageBody {
	public int MessageID;
	
	public AckMessage(int msgID) {
		this.MessageID = msgID;
	}
	
	public AckMessage(byte[] bdy) {
		this.MessageID = (int) bdy[0];
		
	}
	
	@Override
	public int length() {
		return 1;
	}

	@Override
	public byte[] toByteArray() {
		return new byte[] { (byte) MessageID };
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AckMessage) {
			return this.MessageID == ((AckMessage) obj).MessageID;
		}
		return false;
	}

	public static MessageBody fromByteArray(byte[] bdy) {
		return new ErrorMessage(bdy);
	}
}
