# About

This is an application that solves mazes. The maze to solve is given in a text file. The structure of the maze is contained within the text file. These maze text files can be found in the this location `resources/mazes`.

# How To use

## We need to get the application running first. Run the commands below. Make sure you are in `src` folder before running the commands

To compile, run this command:
`javac --module-path ./lib/ --add-modules=javafx.controls MazeApplication.java`

To execute the application, run this command:
`java --module-path ./lib/ --add-modules=javafx.controls MazeApplication`

## Now that we have got the application running, you will see a user interface. You will see various buttons. We break it down below.

**Load Maze Map**: Enter the full path of the maze text file. e.g `../resources/mazes/maze2.txt` and click the button. This loads a maze onto your screen from the maze text file.

**Save Route**: This saves the current progress of the maze solver (essentially the route the maze solver has taken so far). Enter the name of the file you want to save it to. e.g `route1` or `route2`

**Load Route**: This loads a Route object previously saved using Save Route button. This route object holds the current route in the Maze solver. The user interface will update to reflect this route status. Enter the name of the file you want to load from. e.g `route1` This file must exist or there will be an error.

**step**: This button should be clicked repeatedly to initiate the route solver. On your screen you will see the current status of the route to be indicated with stars (`*`). Dead-ends are indicated as (`-`). As you repeatedly click this button, the maze will be solved step by step.

## Running tests

This project needed to meet certain requirements. We checked what requirements we met by running tests.

Run the tests using this command. Make sure you are in the root directory of the project before you run this command.

`./run_tests.sh`

# Class information

To find out about the structure of all the classes used in this project, go to the folder `html-javadocs`.


# Final message

Happy Maze Solving!
