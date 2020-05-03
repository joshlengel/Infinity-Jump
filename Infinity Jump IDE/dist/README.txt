------------------------------------------------------------------------------------
--------------------- Infinity Jump Integrated Development Environment -------------
------------------------------------------------------------------------------------

Version:     1.0.0
Author:      Josh Lengel
Description: An integrated development environment to create levels and scripts used in
             the Infinity Jump game.

---------------- Version 1.0.0 ------------------

Features:
	- Import/Export option. Script files are generated, but empty
	- Property panel for each object, but interface also allows dragging and simple
	   manipulation
	- Test feature allows game to be launched with the current level (Playable)

Warning:
	- Script files unsupported until Infinity Jump creates compiler and runtime environment
	- Minor bugs. Please contact me (Email: jlengel12@gmail.com) to report bugs

--------------------------------------------------

--------------- How to use -----------------------

Use tabs at top of the screen in menu bar to find import/export and other options.
Hotkey combinations are also listed.

Click on objects (Player: black, Target: light-violet, Quads and boundary: brown) to access property panel
and adjust numbers.

Click and drag on the edges of objects (highlighted on hover) to adjust easily. When dragging, the cursor can be
dragged over another edge with the same orientation (left-right, bottom-top) to snap edge to other edge.

Drag over screen to scroll.

--------------- Hotkeys ---------------------------

Import:               Ctrl+I
Export:               Ctrl+E
Add Quad:             Alt+Q

Adjust Zoom:          Slider, no hotkey

Launch current level: Ctrl+L

-------------- How to launch IDE ------------------

To launch the Infinity Jump IDE, double-click the "Infinity Jump IDE.exe" file located in bin folder.
You can also create a desktop shortcut.
DO NOT MOVE THIS FILE!

-------------- For Developers ---------------------

If you wish to compile the c++ launcher with your own compiler make sure you have
the MinGW G++ compiler installed and make sure that the location of G++ is set in
the PATH system environment variable

---------------------------------------------------