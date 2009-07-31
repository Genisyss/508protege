@echo off
title rs2hd compiler
echo Preparing...
mkdir bin
cd src
copy log4j.xml ..
cd ..
move log4j.xml bin/
echo Compiling core...
javac -d bin -cp deps/jython.jar;deps/log4j-1.2.15.jar;deps/mina-core-1.1.7.jar;deps/slf4j-api-1.5.3.jar;deps/slf4j-log4j12-1.5.3.jar;xpp3-1.1.4c.jar;deps/xstream-1.3.1-20081003.103259-2.jar;deps/xpp3-1.1.4c.jar -sourcepath src src/com/rs2hd/Main.java
echo Compiling packet handlers...
javac -d bin -cp deps/jython.jar;deps/log4j-1.2.15.jar;deps/mina-core-1.1.7.jar;deps/slf4j-api-1.5.3.jar;deps/slf4j-log4j12-1.5.3.jar;xpp3-1.1.4c.jar;deps/xstream-1.3.1-20081003.103259-2.jar;deps/xpp3-1.1.4c.jar -sourcepath src src/com/rs2hd/packethandler/*.java
echo Compiling tools...
javac -d bin -cp deps/jython.jar;deps/log4j-1.2.15.jar;deps/mina-core-1.1.7.jar;deps/slf4j-api-1.5.3.jar;deps/slf4j-log4j12-1.5.3.jar;xpp3-1.1.4c.jar;deps/xstream-1.3.1-20081003.103259-2.jar;deps/xpp3-1.1.4c.jar -sourcepath src src/com/rs2hd/tools/*.java
echo Compiling logging system...
javac -d bin -cp deps/jython.jar;deps/log4j-1.2.15.jar;deps/mina-core-1.1.7.jar;deps/slf4j-api-1.5.3.jar;deps/slf4j-log4j12-1.5.3.jar;xpp3-1.1.4c.jar;deps/xstream-1.3.1-20081003.103259-2.jar;deps/xpp3-1.1.4c.jar -sourcepath src src/com/rs2hd/util/log/SLF4JAppender.java
echo Compiling classes referenced by scripts...
javac -d bin -cp deps/jython.jar;deps/log4j-1.2.15.jar;deps/mina-core-1.1.7.jar;deps/slf4j-api-1.5.3.jar;deps/slf4j-log4j12-1.5.3.jar;xpp3-1.1.4c.jar;deps/xstream-1.3.1-20081003.103259-2.jar;deps/xpp3-1.1.4c.jar -sourcepath src src/com/rs2hd/util/HelpManager.java
echo Complete.
pause

