# RCDP
**Remote Control Drone Protocol**

Executables: <br/>
------------------

How to run: <br/>
You will find a folder called "rcdp" that contains the jar files in order to run the 
protocol application. You will find two more folders "LinuxEnv" and "WinEnv".
	
	WinEnv:
	To run the application in a Windows environment first open the "WinEnv" folder
	and simply double click on the the jar files to execute. That is, "UIDrone.jar"
	and "UIReceiver.jar". Follow the steps below on how to properly use the 
	application	under the Initial Use section.    

	NOTE:	It is advisable to first try to run the applications in a Windows 
			environment as it has been show to be the most stable. Also, ensure 
			you have the latest version of java JDK running on your system. 

	LinuxEnv:
	To run the application in a Linux or Unix environment like tux you can execute 
	the script in the "LinuxEnv" folder by changing the permission to execute and enter 
	command "./RCDP_Script" in the terminal. It will open two windows one for the drone
	and one for the receiver. Follow the steps below on how to properly use the 
	application	under the Initial Use section. 

	NOTE:	It is advisable to run this as a backup option as the application
			was not throughly test in this environment. If you plan to do so
			ensure that you have X11 or Xmig windows manager running.

Initial Use: <br/>
Once you have both the "UIReceiver.jar" and "UIDrone.jar" running follow these  
initial steps to establish a clear connection. 

	1.	First run the "UIDrone.jar". 
	2.	Second run the "UIReceiver.jar". 
	3.	Enter the password and drone id on the UIDrone and select "on".
	4.	Enter the password you entered for the drone into the UIReceiver field.
		and select "on". 
	5. 	You have now established a connection with the drone and can start
		sending messages using the receiver's interface. 

Files and Folders: <br/>
	rcdp:			
		Contains all the jar files in order to run the application. 
	
	rcdp-src:		
		Contains all the source files and their associated packages
	
	rcdp-demo.mp4:	
		Is the video file that demonstrates the basic functionality
		of the application. The file can also be viewed on youtube 
		at https://www.youtube.com/watch?v=6fJyiBPO2hU		 
	
	Requierments_Team4.pdf:
		A document describing which files satisfy each of the 
		specified requirements.

	steps-to-run-the-rcdp-project.docx: 
		Provides additional steps on how to compile the program 
		using either in either a Linux or Windows environment.

	Update_Design_Team4.pdf:
		The updated complete proposal document including any changes
		or updates that were suggested and a section describing any 
		differences in the second version. 

Additional Instructions: <br/>
Please refer to "steps-to-run-the-rcdp-project.docx" to compile project if needed.

Robustness: <br/>
------------------
- Correctness: <br/>
 -We tested all the functionality of the receiver and it's communications with the drone
 by running it in two different environments and observing the operations.

- Protocol correctness: <br/>
 -We checked the protocol by performing a sanity check on the protocol components 
 under properly configured settings in both a Windows and Linux environment. 

- Robustness: <br/>
 -The states handle invalid Messages, valid Messages for invalid states, 
valid Messages on current state but with invalid operation 
(i.e. maintain location while changing direction).   

- Concurrency: <br/>
 -The drone can handle multiple connections, but only asynchronously with one receiver
 at a time. In this case, adding an extra slave receiver, the first receiver will 
 have to disconnect while the second re-establishes a new connection with the drone. This
 was it was implemented in this manner in order to ensure that only one receiver has
 control of the drone at any time as having two receivers sending message would
 conflict with the operations. 

- Environment: <br/>
We ran both the application in both a Windows and Linux environment but only 
throughly tested the robustness and functionality on a Windows system.


Extra-Credit: <br/>
------------------
Extra credit was not implemented in this assignment.


