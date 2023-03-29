# How it works

The algorithm works in two phases:

1. It finds numbers that have the same amount of unknowns and sets all nearby to mines, decreasing the numbers near mines; then it clears all squares next to numbers that have been decreased to zero.

2. If the first phase turns nothing new, it moves to a longer method of solving. It finds all uncleared squares next to numbers and assigns each a variable name; equations using these variables are generated where solutions of 0 mean no mine while 1 means mine; a Gaussean elimination determines solutions to these equations.

Sample run: https://www.speedrun.com/gm/run/ylr504xy (faster than any human time on speedrun.com)

# How to use
* To run this code, open Google minesweeper, and choose a difficulty level.
* At the top of the PlayGame class, change the variabes a and b to match the size of the board given the difficulty level you've selected.
* Run the code (main method is in the PlayGame class), then switch to the tab with minesweeper open -- note there is a main method in the Gaussean class for testing only.
* You have 3 seconds to make sure the board is completely visible on the screen.
* Then, the code will run.
* Make note of where it first clicks, if it's not at the complete center of the board (which is a vertex shared by four squared), the code will not work.
* To fix this, at the top of the GenerateBoard class, there is an int[] array called pixelFix.
* Increase or decrease the values in pixelFix by 1 until the first click is at the exact center of the board.
* The first index changes the width and the second the height; adding 1 will make the mouse click more right or more low (corresponding to width and height respectively).
* Then let the code run through its entirety.

### Challenges
* The algorithm only clicks around every second because every time a tile is uncovered, an animation is played that covers up the numbers, so the program cannot read it in.
* The colors of the numbers are a bit different color on the edges compared to the middle, so colors are read in with a ~15 RGB value sensitivity for each color.
