# TP-Link_HS100-Controller
Drive the Smart Switch locally from any app in your network.

Prerequsite: 
* Java needs to be installed and on the path.
  * If you know where *javac* or *javac.exe* is you can use the full path to it to compile the class.
* You know the IP address of the switch.
  * Query your router's web page for attached devices HS100
  * You can allocate a fixed ip address so things dont change over time.
* I have included a compiled file (.class) so you dont really need to compile it unless you change the code (.java).
  * You will need the Java runtime to be installed.
  * just type 'java' on the command line to find out if it installed.

# Clone this project:
CD to a project directory (any path will do):
On the command line type
```
git clone https://github.com/stuartdd/TP-Link_HS100-Controller.git
```
The following files and directories are created:
```
├── external
│   ├── HS100Controller.class
│   └── HS100Controller.java
└── README.md
```
* HS100Controller.java is the source code. You will need to compile if it is changed.
* HS100Controller.class is the java executable produced when you compile the source file.
  * The existing file will run on any computer that has Java Rruntime installed.
* README.md The file you are reading now!

# Compile (IF REQUIRED)
You will need to have the Java Development Kit (JDK) installed to compile the code.

From the root directory. The one with the README.md file in it, type:
```
javac external/HS100Controller.java
```
This will create a new HS100Controller.class file.

# Test the program 
Assuming your device has an ip address like this '192.168.1.13' you can execute the program as follows:
```
java external/HS100Controller 192.168.1.13
```
One of two things will happen:

1. A long String will be printed to the log. 
   1. If this happens your are good to go.
   1. This string contains status data from your switch. (More on this below)
1. The program hangs (freezes) for about 15 seconds
   1. This means the ip address is probably wrong.

Be patient. It often takes a couple of seconds to wake up!
   
# Read the help
```
java external/HS100Controller
```
This will uptput the usage instructions (I wont include them here as you can see them for yourself).

# Turn the switch ON
```
java external/HS100Controller 192.168.1.13 on
```
The following text should be output:
```
{"system":{"set_relay_state":{"err_code":0}}}
```
# Turn the switch OFF
```
java external/HS100Controller 192.168.1.13 off
```
The following text should be output:
```
{"system":{"set_relay_state":{"err_code":0}}}
```
# Fetch the device status
```
java external/HS100Controller 192.168.1.13 status
```
OR:
```
java external/HS100Controller 192.168.1.13
```
Note this is the default behaviour if no action is defined

The following text should be output (note. I have changed the id's to ? to hide my device):
```
{"syste":{"get_sysinfo":{"err_code":0,"sw_ver":"1.2.5 Build 171213 Rel.101014","hw_ver":"1.0","type":"IOT.SMARTPLUGSWITCH","model":"HS100(UK)","mac":"??:??:??:??:??:??","deviceId":"?????????????????????","hwId":"?????????????????????","fwId":"00000000000000000000000000000000","oemId":"?????????????????????","alias":"plug 1","dev_name":"Wi-Fi Smart Plug","icon_hash":"","relay_state":1,"on_time":7194,"active_mode":"schedule","feature":"TIM","updating":0,"rssi":-65,"led_off":0,"latitude":99.99,"longitude":99.99}}}
```
A clearer version of that data follows:

To see this go to the following site http://json-validator.com/ and paste the text in to the empty box. Then press the *Validate* button. The following will be displayed:
```
{
   "syste":{
      "get_sysinfo":{
         "err_code":0,
         "sw_ver":"1.2.5 Build 171213 Rel.101014",
         "hw_ver":"1.0",
         "type":"IOT.SMARTPLUGSWITCH",
         "model":"HS100(UK)",
         "mac":"??:??:??:??:??:??",
         "deviceId":"?????????????????????",
         "hwId":"?????????????????????",
         "fwId":"00000000000000000000000000000000",
         "oemId":"?????????????????????",
         "alias":"plug 1",
         "dev_name":"Wi-Fi Smart Plug",
         "icon_hash":"",
         "relay_state":1,            <--- This is the important line --->
         "on_time":7194,
         "active_mode":"schedule",
         "feature":"TIM",
         "updating":0,
         "rssi":-65,
         "led_off":0,
         "latitude":99.99,
         "longitude":99.99
      }
   }
}
```
If the switch is ON:
```
"relay_state":1,
```
If the switch is OFF:
```
"relay_state":0,
```
# THANKS:
Thanks to the following articles who's authors inspired this work!

*Visit their sites to get more information on the TP-LINK switches.*
```
https://machinekoder.com/controlling-tp-link-hs100110-smart-plugs-with-machinekit/
https://blog.georgovassilis.com/2016/05/07/controlling-the-tp-link-hs100-wi-fi-smart-plug/
```
And Mkyong for the REGX ip validation.
```
https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
```

