SDP_PC
======

The code that will be run on a DICE machine, separate from the NXT code that will run on the bricks.


V4l4j and BoofCV
==================

I have included the jar files (and the compiled executables for v4l4j) inside the repository and, in theory, it _should_ be possible to just checkout the repository and the vision would work straight away on DICE i.e. the idea **is that we shouldn't include the jar files locally on each of ours' laptops/DICE accounts but rather they should be inside the repository itself**  . At the moment of writing 30.01.2014, this is working on my own machine i.e. I can run all the classes in src.vision . 

**Update:**
 
The vision won't run because of the same linking problem with v4l4j. The fix is to right click on SimpleViewer > Run As > Run Configuration > Environments > 
and then add a new environment variable called LD_LIBRARY_PATH with the value of /group/teaching/sdp/sdp4/v4l4j-0.9.1
Note this is for DICE machines, if you want to run this on your own machine you need to have installed v4l4j on your own (using sudo commands for Linux).
