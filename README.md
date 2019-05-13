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
* HS100Controller.java is the java executable produced when you compile the source file.
  * The existing file will run on any computer that has Java installed.

# Compile (IF REQUIRED)
```
javac external/HS100Controller.java
```
