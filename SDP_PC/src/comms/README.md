Establishing Connection
-----------------------

To create a new connection first initialise a new Bluetooth object:

    private static Bluetooth myConnection;
    
then create the new Bluetooth object with the robot name being either HERCULES or TEAM_TRINITY.

    myConnection = new Bluetooth(ROBOT_NAME);
    
Sending Commands
----------------

After a connection has been established between PC and robot you can send commands to the robot using the sendCommand(int[]) method. This method requires an array of four integers with the first being the command to be executed by the robot. A list of available commands are located in the Bluetooth class. The remainig three integers are optional variables that can used within Commands. For example the command

    int [] testSPEED = new int [] {Bluetooth.SPEED,10,0,0};
    sendCommand(testSPEED);
    
sets the speed of the robot to 10. If the optional variables are not needed then the values should be left as 0.

When sending the commands to the robot each integer is converted to a byte therefor the largest decimal value of any of the four integers is 127.
