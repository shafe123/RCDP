package messages;

import java.util.Arrays;

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
	
	public DataMessage(byte[] bdy) {
		this.type = DataType.values()[bdy[0]];
		this.flag = bdy[1];
		this.data = Arrays.copyOfRange(bdy, 2, bdy.length-2);
	}
	
	@Override
	public int length() {
		return 1 + 1 + data.length;
	}

	@Override
	public byte[] toByteArray() {
		byte[] body = new byte[this.length()];
		body[0] = (byte) type.ordinal();
		body[1] = flag;
		for (int i = 0; i < data.length; i++) {
			body[i+2] = data[i];
		}
		return body;
	}

	public static MessageBody fromByteArray(byte[] bdy) {
		return new DataMessage(bdy);
	}	
}
//
