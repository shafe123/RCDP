package messages;

import java.util.Arrays;

public class DataMessage extends MessageBody {
	public enum DataType { IMAGE, VIDEO, AUDIO, NAVIGATION; }
	
	public DataType type;
	public byte flag;
	public byte[] data;
	
	public DataMessage(DataType type, byte flag, byte[] data) throws Exception {
		if (data.length > 125) {
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
		this.data = Arrays.copyOfRange(bdy, 2, bdy.length);
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DataMessage) {
			DataMessage other = (DataMessage) obj;
			return (this.flag == other.flag && this.type == other.type && Arrays.equals(this.data, other.data));
		}
		
		return false;
	}

	public static MessageBody fromByteArray(byte[] bdy) {
		return new DataMessage(bdy);
	}	
	
	@Override
	public String toString() {
		String result = 
				"Data type: " + this.type + System.lineSeparator() +
				"Flag: " + (int) this.flag + System.lineSeparator() +
				"Data length: " + this.data.length;
		return result;
	}
}
//
