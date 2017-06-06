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

	   
	   

		AckMessage m5bdy = new AckMessage(3);
		Message m5 = new Message(MessageType.ACK, 5, m5bdy);
		System.out.println(Message.toByteArray(m5));
		System.out.println(Message.fromByteArray(Message.toByteArray(m5)));
		
   }
} 