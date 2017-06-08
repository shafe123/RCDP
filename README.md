# RCDP
**Remote Control Drone Protocol**

Executables: <br/>
------------------

How to run: <br/>
//TODO

Files: <br/>
//TODO

Robustness: <br/>
------------------
- Correctness: <br/>
 -We tested all the functionality of the receiver and it's peripheral devices. 

- Protocol correctness: <br/>
 -We checked the protocol by performing a sanity check on the protocol components <br/>
 under properly configured settings. 

- Robustness: <br/>
 -Can the states handel invalid Messages, valid Messages for invalid states, <br/>
valid Messages on current state but with invalid operation (i.e. maintain location while changing direction)  

- Concurrency: <br/>
 -We need to see if the drone can handel multiple connections. <br/>
In this case, adding an extra slave receiver, or additional devices. <br/>
 -We need to consider about what would happen is several Messages commands<br/>
 are sent at once. 

- Environment: <br/>
Should be tested on local machines as well as TUX.


Extra-Credit: <br/>
------------------
Extra credit was not implemented in this assignment.


