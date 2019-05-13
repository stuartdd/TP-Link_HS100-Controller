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
* HS100Controller.java is the source code. Needs to be compiled if it is changed.
* HS100Controller.class is the java executable produced when you compile the source file.
  * The existing file will run on any computer that has Java installed.
* README.md The file you are reading now!

# Compile (IF REQUIRED)
From the root directory. The one with the README.md file in it, type:
```
javac external/HS100Controller.java
```
This will create a new HS100Controller.class file.

# Test the program 
Assuming an ip address like this 192.168.1.13 you can execute the program as follows:
```
java external/HS100Controller 192.168.1.13
```
One of two things will happen:
1. A long String will be printed to the log. 
   1. If this happens your are good to go.
   1. This string contains status data from your switch. (More on this below)
1. The program hangs (freezes) for about 15 seconds
   1. This means the ip address is probably wrong.
   
# Read the help
```
java external/HS100Controller
```
This will uptput the usage instructions.

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
# Fetch the status
```
java external/HS100Controller 192.168.1.13 status
```
Note this is the default behaviour is no action is defined

The following text should be output (note I have changes the id's to ? to hide my device):
```
{"syste":{"get_sysinfo":{"err_code":0,"sw_ver":"1.2.5 Build 171213 Rel.101014","hw_ver":"1.0","type":"IOT.SMARTPLUGSWITCH","model":"HS100(UK)","mac":"??:??:??:??:??:??","deviceId":"???????","hwId":"?????????","fwId":"00000000000000000000000000000000","oemId":"?????????","alias":"plug 1","dev_name":"Wi-Fi Smart Plug","icon_hash":"","relay_state":1,"on_time":7194,"active_mode":"schedule","feature":"TIM","updating":0,"rssi":-65,"led_off":0,"latitude":????,"longitude":????}}}
```
A clearer version of that data follows:
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
         "deviceId":"???????",
         "hwId":"?????????",
         "fwId":"00000000000000000000000000000000",
         "oemId":"?????????",
         "alias":"plug 1",
         "dev_name":"Wi-Fi Smart Plug",
         "icon_hash":"",
         "relay_state":1,
         "on_time":7194,
         "active_mode":"schedule",
         "feature":"TIM",
         "updating":0,
         "rssi":-65,
         "led_off":0,
         "latitude":99.99,
         "longitude":99.9
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


