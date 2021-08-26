import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

import maze.Maze;
import maze.Tile;
import maze.InvalidMazeException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.NoEntranceException;
import maze.NoExitException;
import maze.RaggedMazeException;
import maze.routing.NoRouteFoundException;
import java.lang.NullPointerException;


import maze.routing.RouteFinder;
import maze.visualisation.WallTile;
import maze.visualisation.CorridorTile;

import java.util.List;
import java.util.ArrayList;

/**  Class for the Maze application for users to solve a maze
*    @author Ashreen Kaur
*/
public class MazeApplication extends Application {

  private CorridorTile visitedTile;
  private Maze maze;
  private RouteFinder routeFinder;

  /**  Launches the application */
  public static void main(String[] args) {
    launch(args);
  }


  /**  Creates Stage for application and controls functionality of the application
  *    @param stage: the stage setup for the javafx application
  *    @throws java.lang.NullPointerException if user doesnt input anything in the interface
  */
  @Override
  public void start(Stage stage){
    maze = null;
    GridPane grid = new GridPane();

    VBox pane = new VBox(20);
    pane.setAlignment(Pos.CENTER);
    VBox userInputPane = new VBox(10);
    userInputPane.setAlignment(Pos.CENTER_LEFT);

    HBox loadMapBox = new HBox(10);
    Button loadMapBtn = new Button("Load Maze Map");
    TextField enterPathToMapFile = new TextField();
    loadMapBox.getChildren().addAll(enterPathToMapFile, loadMapBtn);
    loadMapBtn.setOnAction(l -> {
      try {
        // maze = Maze.fromTxt("../resources/mazes/maze2.txt");
        grid.getChildren().clear();
        maze = Maze.fromTxt(enterPathToMapFile.getText());
        routeFinder = new RouteFinder(maze);


        List<List<Tile>> tilesList = maze.getTiles();
        grid.setAlignment(Pos.CENTER);
        for (int i=0; i<tilesList.size(); i++) {
          for (int j=0; j<tilesList.get(i).size(); j++) {
            if (tilesList.get(i).get(j).getType() == Tile.Type.WALL) {
              WallTile wall = new WallTile();
              grid.add(wall.getWall(), j, i);
            } else if (tilesList.get(i).get(j).getType() == Tile.Type.ENTRANCE) {
              CorridorTile entrance = new CorridorTile("E");
              grid.add(entrance.getCorridor(), j, i);
            } else if (tilesList.get(i).get(j).getType() == Tile.Type.EXIT) {
              CorridorTile entrance = new CorridorTile("X");
              grid.add(entrance.getCorridor(), j, i);
            } else {
              CorridorTile corridor = new CorridorTile("");
              grid.add(corridor.getCorridor(), j, i);
            }
          }
        }

      } catch (RaggedMazeException e) {
        System.out.println("Maze file contianed ragged maze. Ragged maze not allowed");
      } catch (MultipleEntranceException e) {
        System.out.println("Multiple entrances were found in the maze file");
      } catch (MultipleExitException e) {
        System.out.println("Multiple exits were found in the maze file");
      } catch (NoEntranceException e) {
        System.out.println("No entrance was found in the maze file");
      } catch (NoExitException e) {
        System.out.println("No exit was found in the maze file");
      } catch (InvalidMazeException e) {
        System.out.println("Invalid maze");
      } catch (NullPointerException e) {
        System.out.println("Could not load Map");
      }


    });

    HBox saveBox = new HBox(10);
    Button saveRouteBtn = new Button("Save Route");
    TextField enterFilenameToCreate = new TextField();
    saveBox.getChildren().addAll(enterFilenameToCreate, saveRouteBtn);
    saveRouteBtn.setOnAction(s -> {
      try {
        routeFinder.save(enterFilenameToCreate.getText());
      } catch (NullPointerException n) {
        System.out.println("Cannot enter empty filename");
      }

    });

    HBox loadBox = new HBox(10);
    Button loadRouteBtn = new Button("Load Route");
    TextField enterFilnameToReadFrom = new TextField();
    loadBox.getChildren().addAll(enterFilnameToReadFrom, loadRouteBtn);
    loadRouteBtn.setOnAction(lo -> {

      try {
        routeFinder = routeFinder.load(enterFilnameToReadFrom.getText());
        maze = routeFinder.getMaze();

        grid.getChildren().clear();
        List<List<Tile>> tilesList = routeFinder.getMaze().getTiles();
        grid.setAlignment(Pos.CENTER);
        for (int i=0; i<tilesList.size(); i++) {
          for (int j=0; j<tilesList.get(i).size(); j++) {
            if (tilesList.get(i).get(j).getType() == Tile.Type.WALL) {
              WallTile wall = new WallTile();
              grid.add(wall.getWall(), j, i);
            }  else if (tilesList.get(i).get(j).getType() == Tile.Type.ENTRANCE) {
              CorridorTile entrance = new CorridorTile("E");
              grid.add(entrance.getCorridor(), j, i);
            } else if (tilesList.get(i).get(j).getType() == Tile.Type.EXIT) {
              CorridorTile entrance = new CorridorTile("X");
              grid.add(entrance.getCorridor(), j, i);
            } else if (tilesList.get(i).get(j).alreadyVisited() == true && tilesList.get(i).get(j).isDeadEnd() == false) {
              CorridorTile entrance = new CorridorTile("*");
              grid.add(entrance.getCorridor(), j, i);
            } else if (tilesList.get(i).get(j).isDeadEnd() == true) {
              CorridorTile entrance = new CorridorTile("-");
              grid.add(entrance.getCorridor(), j, i);
            } else {
              CorridorTile corridor = new CorridorTile("");
              grid.add(corridor.getCorridor(), j, i);
            }
          }
        }
      } catch (NullPointerException nu) {
        System.out.println("Could not load route");
      }
    });


    Button stepBtn = new Button("step");
    pane.getChildren().addAll(loadMapBox, saveBox, loadBox, grid, stepBtn);
    stepBtn.setOnAction(e -> {
      try {
        routeFinder.step();

        int x = maze.getTileLocation(routeFinder.getTopOfStack()).getX();
        int y = maze.getTileLocation(routeFinder.getTopOfStack()).getY();
        y = (maze.getTiles().size() - 1) - y;

        for (int i=0; i<maze.getTiles().size(); i++) {
          for (int j=0; j<maze.getTiles().get(i).size(); j++) {
            Tile tile = maze.getTiles().get(i).get(j);
            if (tile.isDeadEnd() == true) {
              visitedTile = new CorridorTile("-");
              grid.add(visitedTile.getCorridor(), j, i);
            }
          }
        }

        if (routeFinder.getTopOfStack().isDeadEnd() == false && routeFinder.getTopOfStack().getType() == Tile.Type.CORRIDOR) {
          visitedTile = new CorridorTile("*");
          grid.add(visitedTile.getCorridor(), x, y);
        }
        System.out.println(routeFinder.toString());
      } catch (NoRouteFoundException nr) {
        System.out.println("Cannot solve maze");
      }
    });

    stage.setScene(new Scene(pane, 700,700));
    stage.setTitle("Maze solver");
    stage.show();

  }
}
