#Google Minesweeper
To run this code, open Google minesweeper, and choose a difficulty level.

At the top of the PlayGame class, change the variabes a and b to match the size of the board given the difficulty level you've selected.

Run the code (main method is in the PlayGame class), then switch to the tab with minesweeper open -- note there is a main method in the Gaussean class for testing only.

You have 3 seconds to make sure the board is completely visible on the screen.

Then, the code will run.

Make note of where it first clicks, if it's not at the complete center of the board (which is a vertex shared by four squared), the code will not work.

To fix this, at the top of the GenerateBoard class, there is an int[] array called pixelFix.

Increase or decrease the values in pixelFix by 1 until the first click is at the exact center of the board.

The first index changes the width and the second the height; adding 1 will make the mouse click more right or more low (corresponding to width and height respectively).
Then let the code run through its entirety.

If it still can't solve it, then I have no clue how to fix it, maybe change the color values and sensitivity? No clue.

Sample run: https://www.speedrun.com/gm/run/ylr504xy (faster than any human time on speedrun.com)
