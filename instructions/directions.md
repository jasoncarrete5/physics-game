# Wavefront -- *by Jason Carrete*
Wavefront is a single-player puzzle game that requires the player to get passed a series of puzzles in order to be victorious.
Play time ranges from 10-30 minutes depending on how quickly you can solve each puzzle. Each puzzle requires knowledge of reflection and refraction of waves.

### Understanding the tools
 * Reflectors will reflect incoming waves.
 * Refractors will refract incoming waves based on their refraction index.
 * Walls will stop incoming waves. Avoid these.
 * Receivers will receive incoming waves. These are the end points of each level.
 * Transmitters will transmit incoming waves. These are the starting points of each level.

 
![Diagram](http://45.33.68.145/files/diagram.png)


## Gameplay
The player is able to place and rotate tools in order to redirect conpression waves so that they reach the receiver. In order to complete a level, all waves must reach a receiver, otherwise, the level will reset and the player will have to try again. It should also be noted that when any wave hits a wall or goes out of bounds, the level will automatically reset.

### Observer
The observer is used to view the properties of the currently selected tool. The observer can tell you if the currently selected tool is rotatable or movable which is necessary to complete most levels.

### Toolbox
The toolbox is where all of your movable tools start out in a level. Some levels will require you to use tools in your toolbox to successfully complete a level.

#### Controls
  * The 'A' and 'left-arrow' keys will rotate the selected tool, counter-clockwise
  * The 'D' and 'right-arrow' keys will rotate the selected tool, clockwise
  * The 'T' key will toggle the toolbox
  * The 'O' key will toggle the observer window
  * The 'R' key will reset the current level
  * The 'Space' key will tell all transmitters to fire a compression wave
    * *This can only be done once per level or multiple times if the level is reset*
  * Use the mouse to move tool by clicking and dragging
    * *it should be noted that not all tools can be moved and rotated. To check the properties of a selected tool, look at the 'observer window'*


### Final Tips
 * Experiment with the mechanics of the game. some levels will seem impossible unless you try something out of the box.
 * Refractors can only refract waves in one direction. They will reflect waves if the come in from the wrong direction.
