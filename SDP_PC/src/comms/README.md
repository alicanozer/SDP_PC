Bluetooth Communication
=======================

This readme gives a quick runthrough of how to establish a connection, send commands to the robot and lists the commands available.

Establishing Connection
-----------------------

To create a new connection first initialise new Bluetooth Robot objects:


    static BluetoothRobotTwin attackRobot;
    static BluetoothRobotTwin defenceRobot;
    
then create a new bluetooth object:

    Bluetooth myConnection = new Bluetooth(connectionType);
    
where connection type can be: *"attack"*, *"defence"* or *"both"*. If you would like to connect to only one robot then pass the relevant string connection type or if you would like to connect to both robots then pass *"both"*.

Next create the new BluetoothRobot objects:

    attackRobot = new BluetoothRobot(RobotType.AttackUs, myConnection);
    defenceRobot = new BluetoothRobot(RobotType.DefendUs, myConnection);
    
You should now be able to send commands to the robots you connected to.
    
Sending Commands
----------------

After a connection has been established between PC and robot you can send commands to the robot using the sendCommand(int[]) method. This method requires an array of four integers with the first being the command to be executed by the robot. A list of available commands are located in the Bluetooth class. The remainig three integers are optional parameters that can used within commands. For example the command

    int [] testSPEED = new int [] {Bluetooth.SPEED,10,0,0};
    sendCommand(testSPEED);
    
sets the speed of the robot to 10. If the optional parameters are not needed then the values should be left as 0.

When sending the commands to the robot each integer is converted to a byte therefor the largest decimal value of any of the four integers is 127.

Available Commands
------------------

* Command Code 0 - NOTHING

* Command Code 1 - FORWARDS

* Command Code 2 - BACKWARDS

* Command Code 3 - STOP

* Command Code 4 - KICK

* Command Code 5 - SPEED
    * Optional parameter 1 should be the desired motor speed

* Command Code 6 - ROTATELEFT

* Command Code 7 - ROTATERIGHT

* Command Code 9 - QUIT


Additional commands can easily be added. 

