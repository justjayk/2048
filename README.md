# 2048 in java #
a simple implement of [2048] using java.
credits to Alwayswithme for the template:
https://github.com/Alwayswithme/2048.java

### Development ###

some ideas behind the game.

* Tiles are drawn by java.awt.Graphics.
* Single dimensional arrays to simulate coordinate system.
* Generalizing movement to apply to all four directions, merging and then placing tiles.
* This project focused heavily on designing a games algorithim from the beginning, then its implementation.
* Used object oriented programming to design the games logic and system.


### Requirements ###

* OpenJDK-7 or newer to compile and run

```
$ >  cd 2048.java; mkdir bin
$ >  javac -d bin src/phx/*       # compile
$ >  java -cp bin phx.GUI2048     # enjoy
```


### HOWTO play ###

1. use arrow keys to move the tiles.
2. a vi-like keybindind also OK :)
3. press 'r' to start new game.

### TO-DO ###

* Could implement a scoring system.
