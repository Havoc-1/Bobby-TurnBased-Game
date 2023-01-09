
# Bobby's Turn-based Strategy Game

A simple 2-dimensional turn based strategy game utilizing JFrame and Java to showcase object-oriented programming for course work; May 2021

The game offers both a single and two-player experience.
To activate the single player mode simply modify the value of 

```Java
static boolean isAIEnabled = true;
```



## How to play the game

To navigate and interact with the game, the user must make use of their mouse and simply left click.

After a unit is moved, a black outline will appear representing that the unit has been moved and no further action can be done.

The APC can be loaded with two (2) units, the player must simply choose the desired unit to be loaded and have it’s endpoint be the position of the APC. 

To unload the units from the APC, the player must double click on it. Once unloaded, the once stored units cannot be moved until the next turn.

Every round the player will be given a certain amount of gold depending on the cities or villages owned, this gold is used to create units on the factory or headquarters

The goal of the player is to either
-	Eliminate all hostile forces or
-	Capture the enemy HQ

The player is not required to exhaust the turn of all his / her units, the player may prematurely end their turn by simply pressing the button on the lower right corner of the window.


![Button to end turn; Color will determine who's turn it is](/images/sc2.jpg)




## Starting Gameplay

At the beginning of each match the player will have available to him the following:
- Headquarters
- 2 factories
- 2 rifleman units
- 1 light tank unit
- 500 gold
The units listed above will be pre-deployed (surrounding the headquarters tile)

## Gameplay Mechanics

**Movement Mechanics:**

Unit movement is by 8 directions through the mouse
 
Unit movement is determined by how many blocks it has moved from its original position, limited by its **MR** (Movement Range). A unit’s movement can also be affected by the terrain, either increasing or decreasing how many blocks it can move. The unit may choose to use all of its movement, some of it, or none at all.

A block can only be occupied by one unit at a time. 


The system looks at the base stats of the unit (in this case-infantry) which has a move range of 5 tiles, it will then check what tile the unit is on and apply the **MB** (Move Bonus) or **MP** (Move Penalty) then it will project the new movement range of the unit for that turn. The player will select the end point within the range and end their turn. At the beginning of the new turn the cycle will repeat.

Unused movement tiles will not stack towards the next turn and it is not dynamic, only considers the tile the unit is currently on and the base movement stats. You cannot occupy the same tile as an enemy unit.

**Move Bonus and Move Penalty Mechanics:**

Depending on the current position of the unit (the terrain tile they are on) and the stats of the unit (MR), it will dictate the movement range based on the penalties or bonuses given by the tile and project the possible tiles it can move to.

**Attack Mechanics:**

The unit has three stats that affect attacking: their **Attack (A)**, their **Attack Type (AT)**, and their **Health Points (HP)**. A unit is functional as long as it has HP. When its HP reaches 0, it is considered dead/destroyed. A ranged unit can attack from any number of blocks within their Attack Range(found in AT) unless they are directly beside the unit while a direct unit has to be one (1) block close to the unit they are attacking.

**Unit Production System:**

Depending on the amount of resources present-any factory and headquarter can produce 1 unit per turn. The player will select the factory and choose which unit to produce from the context menu.  There must be no unit on top of the factory tile to produce a unit. Each unit will require a certain amount of **Gold (G)** to be produced. The unit will be produced instantly but cannot be moved until the next turn.
Only Factories and the Headquarters can produce units. They cannot be cancelled.

**Resource System:**

The City, Headquarter, and Village tiles respectively produce a certain amount of Gold (G) per turn (if they are captured). Those of which will be deposited to the player total gold balance / account at the beginning of the round.

**Capture System:**

Every building tile (city, factory, and village) produce Gold, at the beginning of the round these tiles will be neutral and not owned by either player. To capture a neutral or enemy building tile a friendly unit must occupy (be on the tile) for two rounds. Every turn the unit is on the tile, it will capture the tile by 33% (or in 3 turns) regardless of unit HP and when it was captured. The amount of rounds it takes to capture a tile doesn’t reset when 
If the occupying force is killed, the enemy unit must recapture the tile in 3 turns again to capture the tile.

Building tiles cannot be destroyed, only captured.


**Damage / Combat Mechanic:**

Each unit outputs a specific number of damage. Damage is applied if the unit is (beside, behind, or in front of the unit) – or if the enemy is within range of a **ranged unit** and the player chooses to attack then the value cited in the **Units** table will be applied to the enemy.

**Defense System:**

As seen in the Terrain Tiles, each tile has a unique set of values. Depending on the D value, it determines the reduction of incoming damage from an enemy unit.

**Turn-based System:**

The player goes against an AI or another player and take turns in controlling their units. A turn consists in moving units and attacking with them. All units can be moved and can attack in a turn. The player can choose to only move some units and end their turn early. Once they have done their action (moving and/or attacking), they cannot perform another action until the player’s next turn. The player can choose If the player goes against the AI, the AI will have a simplistic ruleset which is made up of random decisions.

**Win / Game Over Mechanic:**

The game is considered over once one player no longer has units to control or the headquarters has been captured. 

## Screenshots

![Screenshot 1](/images/sc1.jpg)
![Screenshot 2](/images/sc3.jpg)


## Authors

- [@Meepster212](https://github.com/Meepster212)
- [@Havoc-1](https://github.com/Havoc-1)
## Acknowledgements

Sprite & Art are made by Nintendo:

Game: Advance Wars 2: Black-Hole Rising

Subject: Overworld Buildings & Tilesets

Copyright: Intelligent Systems, Nintendo

Sprite Ripper: Zack

Hosting Permissions: The Spriters Resource


