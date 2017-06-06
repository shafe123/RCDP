package Receiver;

import java.io.*;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;

import messages.AckMessage;
import messages.ControlMessage;
import messages.ErrorMessage;
import messages.Message;
import messages.ControlMessage.ControlType;
import messages.ErrorMessage.ErrorType;
import messages.Message.MessageType;

public class SystemDemo {

   public static void main(String[] args) throws Exception {

	   
		byte m3Command = 0x01;
		JSONObject json = new JSONObject();
		json.put("unit_type", "receiver");
		json.put("unit_id", "abcdef");
		json.put("unit_ipaddress", "123.123.123.123");
		json.put("version", 1.0);
		ControlMessage m3bdy = new ControlMessage(ControlType.GROUNDED, m3Command, json);

		Message m5 = new Message(MessageType.CONTROL, 5, m3bdy);
		byte [] temp = Message.toByteArray(m5);
		Message m55 = Message.fromByteArray(temp);
		
		System.out.println(Message.toByteArray(m5));
		System.out.println(Message.fromByteArray(Message.toByteArray(m5)));
		
   }
} 