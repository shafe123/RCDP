package messages;

import org.json.simple.*;

public class ControlMessage extends MessageBody {
	public enum ControlType { UNIVERSAL, GROUNDED, PREFLIGHT, FLYING, AUTONOMOUS, BEACON; }
	
	public ControlType type;
	public CommandTypes command;
	public String JSON;
	
	
	public ControlMessage(ControlType type/*, CommandType cmd*/, JSONObject json) throws Exception {
		if(json.toString().getBytes().length > 128) {
			String err = "Size of parameters is too large to send message.";
			throw new Exception(err);
		}
		
		this.type = type;
		this.JSON = json.toString();
		
	}
	
	
	@Override
	public int length() {
		return JSON.getBytes().length;
	}

	@Override
	public byte[] buildMessageBody() {
		byte[] body = new byte[this.length()];
		body[0] = (byte) type.ordinal();
		body[1] = 0;
		
		byte[] json = JSON.getBytes();
		for (int i = 0; i < json.length; i++)
		{
			body[i+2] = json[i];
		}
		
		return body;
	}
	
}
