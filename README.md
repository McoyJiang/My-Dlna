My-Dlna
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
