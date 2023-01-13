PROJECT TITLE: Modified Pacman (Fishman)

VERSION or DATE: 2022 Jan

AUTHORS: Jiaqi Tang, Katelyn Lam, Helen Lee


 
Note: This is a Greenfoot Project

Fishman is a game based off of Pacman. The player moves around the maze and eats 
“crumbs” to gain points within a certain amount of time. They are chased by enemy blobs. If
they collide with an “active” enemy blob or the time runs out, the player will lose. There are 
7 levels and it will grow progressively harder. Each level has a different amount of time that 
requires the level to be completed in. The first level is unlocked by default, then the player
has to keep playing to unlock the rest of the levels. 

Fishman moves using arrow key controls. It has two looks: one is its resting position 
(moving, mouth closed) and one is its eating position.

The enemy blobs move about somewhat randomly for the first 5 levels. They change image 
when bigger crumbs are eaten. It has three looks: one is the regular look, one is the edible 
look (depicted with a glitch effect), and one is after it has been eaten (starry eyes). When the 
enemies are eaten, they return to the home base and then regenerates to the original form.

The maze is represented by a 2D array of <code>Square</code> of varying sizes. 
Please see the images in the Group 3 folder for reference. Each <code>Square</code>
is represented by either “road”, “ice”, “wall”, or “home.”

Player moves 2x speed on ice and cannot move past the wall. Player cannot enter
the home square, but blobs can enter the home square to regenerate. Both the maze and 
the crumbs are rendered by reading from a text file with characters. The squares contain 
crumbs in the center which the player eats to gain points.

There are 4 types of crumbs. When the Fishman eats different crumbs, there will be a 
different amount of points for each type and some different effects as well. Regular crumbs 
are worth 2 points. Bigger green-looking crumbs are worth 4 points and make blobs edible 
for 10 seconds. Cherries are worth 100 points and appear randomly every 50pts.
Cakes are worth 20 points, appear randomly half to ¾ way in the game, and freezes the 
ghost in place.

The player’s level and high score is saved in a text file.

All graphics have been created by either Helen Lee or Katelyn Lam. Helen’s graphics consist  
of the player, enemy blobs, cherries, cakes, and the other interface related graphics (buttons 
and instructions page). Katelyn’s graphics consist of the maze graphics (squares: wall, road, 
ice, and home) and the other crumb graphics (regular and bigger crumbs). 

The background music was taken from SoundCloud at   
https://soundcloud.com/jamie-berry/jamie-berry-super-mario-bros. It is named “Jamie Berry - 
Super Marios Bros (Electro-Swing Remix) **FREE DOWNLOAD**” by JamieBerry.


Katelyn Lam, Helen Lee, Jiaqi Tang

@version 2022 Jan

