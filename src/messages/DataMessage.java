package messages;

public class DataMessage extends MessageBody {
	public enum DataType { IMAGE, VIDEO, AUDIO, NAVIGATION; }
	
	public DataType type;
	public byte flag;
	public byte[] data;
	
	public DataMessage(DataType type, byte flag, byte[] data) throws Exception {
		if (data.length > 127) {
			String err = "Size of data is too large to send message.";
			throw new Exception(err);
		}
		
		this.type = type; 
		this.flag = flag;
		this.data = data;
	}
	
	@Override
	public int length() {
		return 1 + 1 + data.length;
	}

	@Override
	public byte[] buildMessageBody() {
		byte[] body = new byte[this.length()];
		body[0] = (byte) type.ordinal();
		body[1] = flag;
		for (int i = 0; i < data.length; i++) {
			body[i+2] = data[i];
		}
		return body;
	}	
}
//
