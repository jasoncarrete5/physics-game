# Wavefront -- *by Jason Carrete*

### General Description
See [directions.md](https://github.com/jasoncarrete5/physics-game/tree/master/instructions/directions.md).
Can somebody cheat? Yes.

### Design
I used Java, an object-oriented language, to program my game. The programming went smoothly at first but 4,000+ lines of code later, 
one can imagine how unmanagable that is for 1 person.

### Requirements
##### Software
Because the game is written in Java 1.8, the only real requirement is a system with Java 1.8 installed.
##### Hardware
The game will only run on desktop.

### Implementation
##### Language
I used Java 1.8 to program my game and I chose this language because it is the language I am most familiar with.

Pros | Cons
-----|------
High familiarity with the language | Garbage collections slows down games
Java 1.8 is one of the easier languages to use and has a variety of available framewokrs |

##### Frameworks
This game was built on top of [LibGDX](https://github.com/libgdx/libgdx) which is built on top of lwjgl which is the java port of OpenGL.
I chose to use this framework because I have used it before and it is more intuitive than the primitive Java2D or fx libraries.

##### Algorithms
No common algorithms were used to make this game. I used built in LibGDX collision algorithms to handle polygon collisions.

### Strategy
##### Strategies
The is no real strategy for this game. The player is forced to think and always has the option to use math from physics to calculate
the way the compression waves will reflect and refract.

##### Artificial Intelligence?
None.

### Testing
There are no unit tests for this project, however I did ensure that the mechanics of each tool worked before creating the multiple levels.
As for play-testing, I have personally completed every level in the game. The only thing I changed based on the feedback from others was
the instructions.

### Memorable Bug
There were two bugs found after some of the levels were completed. I decided to leave them in and use them as a gameplay mechanic. The
first bug allowed the user to rotate tools in the middle of the simulation and the second bug allowed the user to move tools in the
middle of the simulation.

### Future
If I were to remake this game, I would use LibGDX's ashley entity system library in order to simplify game logic and to follow th MVC
model. By completing this physics project, I have demonstrated to myself that I can complete a program from start to finish by myself.
