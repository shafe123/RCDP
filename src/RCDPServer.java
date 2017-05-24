import java.net.*;
import java.io.*;

public class RCDPServer {
	
	private String portNumber;
	private PrintWriter out;
	private BufferedReader in;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	

	public RCDPServer(String pNumber) throws NumberFormatException, IOException{
		portNumber = pNumber;
		serverSocket = new ServerSocket(Integer.parseInt(portNumber));
//		clientSocket = serverSocket.accept();
//		out = new PrintWriter(clientSocket.getOutputStream(), true);
//		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

    public void run() throws IOException {
    
    	
		clientSocket = serverSocket.accept();
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			out.println(inputLine);
      }

    }
}