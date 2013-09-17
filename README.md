My-Dlna: MediaServer and MediaRenderer
=======
in DLNA group, there are three main functions included:
MediaServer,MediaRenderer,MediaController

MediaServer is mainly about a device which stores a lot of meida source, and 
it will expose these sources to web source, so we can browse these sources using 
the action "browse()" in client port.

MediaRenderer is a device which can receive Uri from other devices,then it can show
the content of the Uri,it contains many methods,like play(), next(),stop() etc.

MediaController is a device which can control the progress of playing. It contains 
a few methods like setMute(),setVolume() etc.


Here we implements the MediaServer and MediaRenderer (MediaController will be implemented in a few days)

Implement details!!!
I implements the DLNA function using the Cling source which is 
a UPnP-compatible software stack for Java environments. 
The project's goals are strict specification compliance, complete, 
clean and extensive APIs, as well as rich SPIs for easy customization.
Here i implements the file MyContentDirectoryService.java is used to create
LocalService whose type is "ContentDirectory"

the files MyAVTransportService.java and MyRendererControlService.java are used
to create LocalService whose type is AVTransPort and RedereringControl in respect.

How to use this project!!
Import this project into your eclipse or other IDEs,
build this project,the there will be a My DLNA.apk in 
the folder bin/. Install this apk into your phone or pad
the the phone/pad will be signed as a MediaServer and MediaRenderer
at the same time. Other devices in the same environment of wireless
connection will find this device as MediaServer and MediaRenderer.
