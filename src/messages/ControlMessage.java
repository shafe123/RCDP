package messages;

import org.json.simple.*;

public class ControlMessage extends MessageBody {
	public enum ControlType { UNIVERSAL, GROUNDED, PREFLIGHT, FLYING, AUTONOMOUS, BEACON; }
	
	public ControlType type;
	public CommandTypes command;
	public String params;
	
	
	public ControlMessage(ControlType type/*, CommandType cmd*/, JSONObject parameters) throws Exception {
		if(parameters.toString().getBytes().length > 128) {
			String err = "Size of parameters is too large to send message.";
			throw new Exception(err);
		}
		
		this.type = type;
		this.params = parameters.toString();
		
	}
	
	
	@Override
	public int length() {
		return params.getBytes().length;
	}

	@Override
	public byte[] buildMessageBody() {
		byte[] body = new byte[this.length()];
		body[0] = (byte) type.ordinal();
		body[1] = 0;
		
		byte[] json = params.getBytes();
		for (int i = 0; i < json.length; i++)
		{
			body[i+2] = json[i];
		}
		
		return body;
	}
	
}
