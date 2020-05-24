call g++ -o "Infinity Jump IDE_temp.exe" -Wl,-subsystem,windows launch.cpp
call "C:\Program Files (x86)\Resource Hacker\ResourceHacker.exe" -open "Infinity Jump IDE_temp.exe" -save "..\bin\Infinity Jump IDE.exe" -resource "icon.ico" -action addskip -mask ICONGROUP, MAINICON,
del "Infinity Jump IDE_temp.exe"

robocopy ../../lib/assets ../lib/assets /mir

call java -jar "Installer Generator.jar" "Infinity Jump IDE-installer.nsi" ..\ 2.1.0 build build\version

call "C:\Program Files (x86)\NSIS\makensis.exe" "Infinity Jump IDE-installer.nsi"

move *.exe versions