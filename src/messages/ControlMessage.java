package messages;

public class ControlMessage extends MessageBody {
	public enum ControlType { UNIVERSAL, GROUNDED, PREFLIGHT, FLYING, AUTONOMOUS, BEACON; }
	
	public ControlMessage(ControlType type/*, CommandType cmd, JsonValue json*/) 
	{
		
	}
	
	
	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] buildMessageBody() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
