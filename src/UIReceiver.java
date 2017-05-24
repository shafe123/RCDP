import java.awt.*;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.ssl.HostnameVerifier;
import javax.swing.*;

import java.net.*;

public class UIReceiver {
   private JFrame mainFrame;
   private JTextArea statusLabel;
   private JPanel controlPanel;
   private StringBuffer log = new StringBuffer();
   private int LogStringCount = 0;
   private JScrollPane sp;
   
   public int portNumber = 8080;
   public String hostName = "127.0.0.1";
   
   private Socket echoSocket;
   public PrintWriter out;
   public BufferedReader in;

   public UIReceiver(){
      prepareGUI();
   }
   public static void main(String[] args){
	  UIReceiver swingControlDemo = new UIReceiver();  
      swingControlDemo.showEventDemo();       
   }
   private void prepareGUI(){
      mainFrame = new JFrame("RCDP receiver");
      mainFrame.setSize(600,600);
      
      
      mainFrame.setLayout(new GridLayout(2, 1));

      statusLabel = new JTextArea();        
//      statusLabel.setSize(350,100);
      sp = new JScrollPane(statusLabel); 
      
      
      
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      controlPanel = new JPanel();
//      controlPanel.setLayout(new FlowLayout());
      controlPanel.setLayout(null);

      mainFrame.add(controlPanel);
      mainFrame.getContentPane().add(sp);
      mainFrame.setVisible(true); 
      
       
   }
   private void showEventDemo(){
	   
	  JLabel headerLable = new JLabel("Remote control Drone Protocol: Receiver", JLabel.CENTER);
      JButton turnOnButton = new JButton("TurnOn");
      JButton turnOffButton = new JButton("TurnOff");
      JButton upButton = new JButton("Up");
      JButton downButton = new JButton("Down");
      JButton leftButton = new JButton("<");
      JButton rightButton = new JButton(">");
      JButton forwardButton = new JButton("^");
      JButton backwardButton = new JButton("v");
      JButton landButton = new JButton("Land");
      JButton autoButton = new JButton("Auto");
      JButton propellerButton = new JButton("Propeller");
      JButton beaconButton = new JButton("Beacon");
      

      
      turnOnButton.setActionCommand("TurnOn");
      turnOffButton.setActionCommand("TurnOff");
      upButton.setActionCommand("Up");
      downButton.setActionCommand("Down");
      leftButton.setActionCommand("Left");
      rightButton.setActionCommand("Right");
      forwardButton.setActionCommand("Forward");
      backwardButton.setActionCommand("Backward");
      landButton.setActionCommand("Land");
      autoButton.setActionCommand("Auto");
      propellerButton.setActionCommand("Propeller");
      beaconButton.setActionCommand("Beacon");
      
      turnOnButton.addActionListener(new ButtonClickListener()); 
      turnOffButton.addActionListener(new ButtonClickListener()); 
      upButton.addActionListener(new ButtonClickListener()); 
      downButton.addActionListener(new ButtonClickListener()); 
      leftButton.addActionListener(new ButtonClickListener()); 
      rightButton.addActionListener(new ButtonClickListener()); 
      forwardButton.addActionListener(new ButtonClickListener()); 
      backwardButton.addActionListener(new ButtonClickListener()); 
      landButton.addActionListener(new ButtonClickListener()); 
      autoButton.addActionListener(new ButtonClickListener()); 
      propellerButton.addActionListener(new ButtonClickListener()); 
      beaconButton.addActionListener(new ButtonClickListener()); 
      
      GridBagLayout gbl = new GridBagLayout();  
      GridBagConstraints gbs = new GridBagConstraints();  
      controlPanel.setLayout(gbl);   
      
      controlPanel.add(headerLable);
      controlPanel.add(turnOnButton);
      controlPanel.add(turnOffButton);
      controlPanel.add(upButton);
      controlPanel.add(downButton);
      controlPanel.add(leftButton);
      controlPanel.add(rightButton);
      controlPanel.add(forwardButton);
      controlPanel.add(backwardButton);
      controlPanel.add(landButton);
      controlPanel.add(autoButton);
      controlPanel.add(propellerButton);
      controlPanel.add(beaconButton);
      
      gbs.fill=GridBagConstraints.HORIZONTAL;gbs.gridwidth=5;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=0;
      gbl.setConstraints(headerLable, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=1;gbs.ipadx=30;gbs.ipady=20;
      gbl.setConstraints(turnOnButton, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=1;gbs.gridy=1; gbs.ipadx=30;gbs.ipady=20;
      gbl.setConstraints(turnOffButton, gbs); 

      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=1;gbs.gridy=2; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(forwardButton, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=3;gbs.gridy=2; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(propellerButton, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=4;gbs.gridy=2; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(beaconButton, gbs);
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=5;gbs.gridy=2; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(upButton, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=3; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(leftButton, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=2;gbs.gridy=3; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(rightButton, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=3;gbs.gridy=3; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(landButton, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=4;gbs.gridy=3; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(autoButton, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=1;gbs.gridy=4; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(backwardButton, gbs); 

      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=1;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=5;gbs.gridy=4; gbs.ipadx=50;gbs.ipady=20;
      gbl.setConstraints(downButton, gbs); 
      
      mainFrame.setVisible(true);  
   }
   
   
   
   private void Logout(String logstring){
	   LogStringCount++;
	   log.append(LogStringCount+":"+logstring + "\n");
       statusLabel.setText(log.toString());
   }
   
   
   
   private class ButtonClickListener implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         String command = e.getActionCommand();  
         String TurnOn = "TurnOn, port number: " + portNumber;
         String TurnOff = "TurnOff";
         String Up = "Up commend sent";
         String Down = "Down commend sent";
         String Left = "< commend sent";
         String Right = "> commend sent";
         String Forward = "^ commend sent";
         String Backward = "v commend sent";
         String Land = "Land commend sent";
         String Auto = "Auto commend sent";
         String Propeller = "Propeller commend sent";
         String Beacon = "Beacon commend sent";
         
         
         if( command.equals( "TurnOn" ))  {
        	 Logout(TurnOn);
        	 try {
        		 echoSocket = new Socket(hostName, portNumber);
 				out = new PrintWriter(echoSocket.getOutputStream(), true);
 				in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
 				String inputLine;
 				// Send Receiver Hello
 				out.println("ReceiverHello");
 				Logout("ReceiverHello sent");
 				inputLine = in.readLine();
 				
 				// After sent Receiver hello, if DroneHello is received, send Received DroneHello
 				if (inputLine.equals("DroneHello")){
 				Logout("Message from server : "+ inputLine);
 				out.println("Received DroneHello");
 				Logout("Received DroneHello sent");
 				}
 				
 				
			} catch (Exception e2) {
				// TODO: handle exception
				Logout(e2.getMessage());
			}
        	 
         } else if( command.equals( "TurnOff" ) )  {
				Logout(TurnOff);
				System.exit(0);
         } else if( command.equals( "Up" ) )  {
        	 out.println("Up");
        	 Logout(Up);
        	 try {
				Logout("Receive ACK: " + in.readLine());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Logout(e1.getMessage());
			}
         } else if( command.equals( "Down" ) )  {
        	 Logout(Down); 
         } else if( command.equals( "Left" ) )  {
        	 Logout(Left); 
         } else if( command.equals( "Right" ) )  {
        	 Logout(Right);
          } else if( command.equals( "Forward" ) )  {
        	  Logout(Forward); 
          } else if( command.equals( "Backward" ) )  {
        	  Logout(Backward); 
          } else if( command.equals( "Land" ) )  {
        	  Logout(Land); 
          } else if( command.equals( "Auto" ) )  {
        	  Logout(Auto); 
          }else if( command.equals( "Propeller" ) )  {
        	  Logout(Propeller); 
          }else if( command.equals( "Beacon" ) )  {
        	  Logout(Beacon); 
          }
      }		
   }
}