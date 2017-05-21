import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UIDrone {
   private JFrame mainFrame;
   private JTextArea statusLabel;
   private JPanel controlPanel;
   private StringBuffer log = new StringBuffer();
   private int LogStringCount = 0;
   private JScrollPane sp;

   public UIDrone(){
      prepareGUI();
   }
   public static void main(String[] args){
	  UIDrone swingControlDemo = new UIDrone();  
      swingControlDemo.showEventDemo();       
   }
   private void prepareGUI(){
      mainFrame = new JFrame("RCDP drone");
      mainFrame.setSize(1000,600);
      
      
      mainFrame.setLayout(new GridLayout(1, 2));

      statusLabel = new JTextArea();        
      sp = new JScrollPane(statusLabel); 
      
      
      
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      controlPanel = new JPanel();
      controlPanel.setLayout(null);

      mainFrame.add(controlPanel);
      mainFrame.getContentPane().add(sp);
      mainFrame.setVisible(true); 
      
       
   }
   private void showEventDemo(){
	   
	  JLabel headerLable = new JLabel("Remote control Drone Protocol: Drone", JLabel.CENTER);
	  JLabel errorLable = new JLabel("Error simulate", JLabel.CENTER);
      JButton turnOnButton = new JButton("TurnOn");
      JButton turnOffButton = new JButton("TurnOff");


      

      
      turnOnButton.setActionCommand("TurnOn");
      turnOffButton.setActionCommand("TurnOff");
      
      
      
      JButton E0000Button = new JButton("Can not find drone");
      E0000Button.setActionCommand("E0000");
      
      JButton E0001Button = new JButton("Connection error");
      E0001Button.setActionCommand("E0001");
      
      JButton E0010Button = new JButton("Authentication error");
      E0010Button.setActionCommand("E0010");
      
      JButton E0101Button = new JButton("Drone low battery");
      E0101Button.setActionCommand("E0101");
      
      JButton E0111Button = new JButton("Weak signal");
      E0111Button.setActionCommand("E0111");
      
      JButton E1000Button = new JButton("Lost signal");
      E1000Button.setActionCommand("E1000");

      
      turnOnButton.addActionListener(new ButtonClickListener()); 
      turnOffButton.addActionListener(new ButtonClickListener()); 
      E0000Button.addActionListener(new ButtonClickListener()); 
      E0001Button.addActionListener(new ButtonClickListener()); 
      E0010Button.addActionListener(new ButtonClickListener()); 
      E0101Button.addActionListener(new ButtonClickListener()); 
      E0111Button.addActionListener(new ButtonClickListener()); 
      E1000Button.addActionListener(new ButtonClickListener()); 

      
      GridBagLayout gbl = new GridBagLayout();  
      GridBagConstraints gbs = new GridBagConstraints();  
      controlPanel.setLayout(gbl);   
      
      controlPanel.add(headerLable);
      controlPanel.add(turnOnButton);
      controlPanel.add(turnOffButton);
      controlPanel.add(errorLable);
      controlPanel.add(E0000Button);
      controlPanel.add(E0001Button);
      controlPanel.add(E0010Button);
      controlPanel.add(E0101Button);
      controlPanel.add(E0111Button);
      controlPanel.add(E1000Button);
      
      gbs.fill=GridBagConstraints.HORIZONTAL;gbs.gridwidth=2;gbs.gridheight=1;  
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
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=2;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=2; gbs.ipadx=200;gbs.ipady=20;
      gbl.setConstraints(errorLable, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=2;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=3; gbs.ipadx=400;gbs.ipady=20;
      gbl.setConstraints(E0000Button, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=2;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=4; gbs.ipadx=400;gbs.ipady=20;
      gbl.setConstraints(E0001Button, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=2;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=5; gbs.ipadx=400;gbs.ipady=20;
      gbl.setConstraints(E0010Button, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=2;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=6; gbs.ipadx=400;gbs.ipady=20;
      gbl.setConstraints(E0101Button, gbs); 
    
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=2;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=7; gbs.ipadx=400;gbs.ipady=20;
      gbl.setConstraints(E0111Button, gbs); 
      
      gbs.fill=GridBagConstraints.NONE;gbs.gridwidth=2;gbs.gridheight=1;  
      gbs.insets=new Insets(1, 0, 1, 0);gbs.weightx=1;gbs.weighty=1;  
      gbs.gridx=0;gbs.gridy=8; gbs.ipadx=400;gbs.ipady=20;
      gbl.setConstraints(E1000Button, gbs); 
      
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
         String TurnOn = "TurnOn.";
         String TurnOff = "TurnOff";
         String E0000 = "Simulate Error 0000: Can not find drone";
         String E0001 = "Simulate Error 0001: Connection error";
         String E0010 = "Simulate Error 0010: Authentication error";
         String E0101 = "Simulate Error 0101: Drone low battery";
         String E0111 = "Simulate Error 0111: Weak signal";
         String E1000 = "Simulate Error 1000: Lost signal";
         

         
         
         if( command.equals( "TurnOn" ))  {
        	 Logout(TurnOn);
         } else if( command.equals( "TurnOff" ) )  {
        	 Logout(TurnOff);
         }  else if( command.equals( "E0000" ) )  {
        	 Logout(E0000);
         } else if( command.equals( "E0001" ) )  {
        	 Logout(E0001);
         } else if( command.equals( "E0010" ) )  {
        	 Logout(E0010);
         } else if( command.equals( "E0101" ) )  {
        	 Logout(E0101);
         } else if( command.equals( "E0111" ) )  {
        	 Logout(E0111);
         } else if( command.equals( "E1000" ) )  {
        	 Logout(E1000);
         }
      }		
   }
}