package maze.routing;
import maze.Maze;
import maze.Tile;
import maze.Maze.Direction;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import maze.routing.NoRouteFoundException;

/**  Class to create RouteFinder objects
*    @author Ashreen Kaur
*/
public class RouteFinder implements Serializable{

  private Maze maze;
  private Stack<Tile> route;
  private boolean finished;

  /**  Contructor to create RouteFinder objects
  *    @param mazeObj: the maze the RouteFinder object will try to solve
  */
  public RouteFinder(Maze mazeObj) {
    maze = mazeObj;
    route = new Stack<Tile>();
    route.push(maze.getEntrance());
    maze.getEntrance().setVisited(true);
  }

  /**  Gets Maze object
  *    @return Returns Maze object we are trying to find a route for
  */
  public Maze getMaze() {
    return maze;
  }


  /**  Gets ArrayList object
  *    @return Returns ArrayListe object containing the current route (unifinished or finished)
  */
  public List<Tile> getRoute() {
    int sizeOfStack = route.size();
    List<Tile> routeList = new ArrayList<Tile>();

    for (int i=0; i<sizeOfStack; i++) {
      routeList.add(route.get(i));
    }
    return routeList;

  }

  /**  Returns a boolean value which tells us if the route is complete
  *    @return Returns a boolean value, true if the route is complete and false if it is not.
  */
  public boolean isFinished() {
    return finished;
  }

  /**  Reads an object file to retrive  RouteFinder object
  *    @param mazeFile: name of the object file we are reading from
  *    @return Returns a RouteFinder object read from the file
  */
  public static RouteFinder load(String mazeFile) {
    RouteFinder rfObject = null;
    try (ObjectInputStream objectInStream = new ObjectInputStream(new FileInputStream(mazeFile))) {
      rfObject = (RouteFinder) objectInStream.readObject();
    } catch (FileNotFoundException e) {
        System.out.println("Error: Could not read " + mazeFile);
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error: problem when reading "+ mazeFile);
    }
    return rfObject;
  }

  /**  Serializes current RouteFinder object into a object file
  *    @param outputFilename The name of the file the object is being serialized to.
  */
  public void save(String outputFilename) {
    try (ObjectOutputStream objectOutStream = new ObjectOutputStream(new FileOutputStream(outputFilename))) {
      objectOutStream.writeObject(this);
    } catch (FileNotFoundException e) {
      System.out.println("No file found");
    } catch (IOException e) {
      System.out.println("There was a problem reading the file");
    }
  }

  /**  Solves the maze one step at a time
  *    @throws maze.routing.NoRouteFoundException if maze cannot be solved
  *    @return Returns true if the route is complete and false if is not
  */
  public boolean step() {

    if (finished == true) {
      return finished;
    }

    Direction[] directions = new Direction[]{Maze.Direction.NORTH, Maze.Direction.EAST, Maze.Direction.SOUTH, Maze.Direction.WEST};
    int yValue = maze.getTileLocation(route.peek()).getX();
    int xValue = maze.getTileLocation(route.peek()).getY();
    xValue = (maze.getTiles().size() - 1) - xValue;

    int maximumX = maze.getTiles().size()-1;
    int maximumY = maze.getTiles().get(0).size()-1;


    if (yValue == 0 || yValue == maximumY || xValue == 0 || xValue == maximumX) {

      return stepEdge(xValue, yValue, maximumX, maximumY);
    } else {

      for (Direction dir : directions) {
        Tile adjTile = maze.getAdjacentTile(route.peek(), dir);

        if (adjTile.getType() == Tile.Type.EXIT) {
          route.push(adjTile);
          finished = true;
          return true;
        }
        if (adjTile.isNavigable() == true && adjTile.alreadyVisited() == false) {
          route.push(adjTile);
          adjTile.setVisited(true);
          return false;
        }
      }

      for (Direction dir : directions) {
        Tile adjDeadTile = maze.getAdjacentTile(route.peek(), dir);
        if (adjDeadTile.isDeadEnd() == false && adjDeadTile.isNavigable() == true) {
          Tile deadEndTile = route.pop();
          deadEndTile.setDeadEnd(true);
          return false;
        }
      }

      throw new NoRouteFoundException();
    }
  }

  /**  Visulaises current route solving state
  *    @return a String of the current route solving state
  */
  public String toString() {
    String mazeString = "";
    int gridNumber = 0;
    String bottomAxis = "";

    for (int i=0; i < maze.getTiles().size(); i++) {
      String line = "";
      List<Tile> innerList = maze.getTiles().get(i);

      for (int j=0; j<innerList.size(); j++) {
        if (j == 0) {
          if (innerList.get(j).isDeadEnd() == true && innerList.get(j).alreadyVisited() == true) {
            line =  String.valueOf(gridNumber) + "  " + "-";
          } else if (innerList.get(j).isDeadEnd() == false && innerList.get(j).alreadyVisited() == true) {
            line =  String.valueOf(gridNumber) + "  " + "*";
          } else {
            line =  String.valueOf(gridNumber) + "  " + (innerList.get(j).toString());
          }
        } else {

          if (innerList.get(j).isDeadEnd() == true && innerList.get(j).alreadyVisited() == true) {
            line = line + " " + "-";
          } else if (innerList.get(j).isDeadEnd() == false && innerList.get(j).alreadyVisited() == true) {
            line = line + " " + "*";
          } else {
            line = line + " " + (innerList.get(j).toString());
          }
        }
      }

      gridNumber = gridNumber + 1;
      line = line + "\n";
      mazeString = mazeString + line;
    }

    for (int i=0; i<maze.getTiles().get(0).size(); i++){
      bottomAxis = bottomAxis + String.valueOf(i) + " ";
    }

    mazeString = mazeString + "\n" + "   " + bottomAxis + "\n";
    return mazeString;

  }


  /**  Helper method for step. Solves the maze when current tile is on any of the maze edges
  *    @param x indicates a specific row of the maze
  *    @param y indicates specific column of the maze
  *    @param maxX indicates the length of the maze
  *    @param maxY indicates the width of the maze
  *    @throws maze.routing.NoRouteFoundException if maze cannot be solved
  *    @return Returns true if the route is complete and false if is not
  */
  public boolean stepEdge(int x, int y, int maxX, int maxY) {

   if (x==0 && y==0) {
     Direction[] topLeft = new Direction[]{Maze.Direction.EAST, Maze.Direction.SOUTH};

     for (Direction dir : topLeft) {
       Tile adjTile = maze.getAdjacentTile(route.peek(), dir);

       if (adjTile.getType() == Tile.Type.EXIT) {

         route.push(adjTile);
         finished = true;
         return true;
       }
       if (adjTile.isNavigable() == true && adjTile.alreadyVisited() == false) {
         route.push(adjTile);
         adjTile.setVisited(true);
         return false;
       }
     }

     for (Direction dir : topLeft) {
       Tile adjDeadTile = maze.getAdjacentTile(route.peek(), dir);
       if (adjDeadTile.isDeadEnd() == false && adjDeadTile.isNavigable() == true) {
         Tile deadEndTile = route.pop();
         deadEndTile.setDeadEnd(true);
         return false;
       }
     }

     throw new NoRouteFoundException();
   }

   if (x==maxX && y==0) {
     Direction[] bottomLeft = new Direction[]{Maze.Direction.NORTH, Maze.Direction.EAST};

     for (Direction dir : bottomLeft) {
       Tile adjTile = maze.getAdjacentTile(route.peek(), dir);

       if (adjTile.getType() == Tile.Type.EXIT) {

         route.push(adjTile);
         finished = true;
         return true;
       }
       if (adjTile.isNavigable() == true && adjTile.alreadyVisited() == false) {
         route.push(adjTile);
         adjTile.setVisited(true);
         return false;
       }
     }

     for (Direction dir : bottomLeft) {
       Tile adjDeadTile = maze.getAdjacentTile(route.peek(), dir);
       if (adjDeadTile.isDeadEnd() == false && adjDeadTile.isNavigable() == true) {
         Tile deadEndTile = route.pop();
         deadEndTile.setDeadEnd(true);
         return false;
       }
     }
     throw new NoRouteFoundException();
   }

   if (x==0 && y==maxY) {
     Direction[] topRight = new Direction[]{Maze.Direction.SOUTH, Maze.Direction.WEST};

     for (Direction dir : topRight) {
       Tile adjTile = maze.getAdjacentTile(route.peek(), dir);

       if (adjTile.getType() == Tile.Type.EXIT) {

         route.push(adjTile);
         finished = true;
         return true;
       }
       if (adjTile.isNavigable() == true && adjTile.alreadyVisited() == false) {
         route.push(adjTile);
         adjTile.setVisited(true);
         return false;
       }
     }

     for (Direction dir : topRight) {
       Tile adjDeadTile = maze.getAdjacentTile(route.peek(), dir);
       if (adjDeadTile.isDeadEnd() == false && adjDeadTile.isNavigable() == true) {
         Tile deadEndTile = route.pop();
         deadEndTile.setDeadEnd(true);
         return false;
       }
     }
     throw new NoRouteFoundException();
   }

   if (x==maxX && y==maxY) {
     Direction[] bottomRight = new Direction[]{Maze.Direction.WEST, Maze.Direction.NORTH};

     for (Direction dir : bottomRight) {
       Tile adjTile = maze.getAdjacentTile(route.peek(), dir);

       if (adjTile.getType() == Tile.Type.EXIT) {

         route.push(adjTile);
         finished = true;
         return true;
       }
       if (adjTile.isNavigable() == true && adjTile.alreadyVisited() == false) {
         route.push(adjTile);
         adjTile.setVisited(true);
         return false;
       }
     }

     for (Direction dir : bottomRight) {
       Tile adjDeadTile = maze.getAdjacentTile(route.peek(), dir);
       if (adjDeadTile.isDeadEnd() == false && adjDeadTile.isNavigable() == true) {
         Tile deadEndTile = route.pop();
         deadEndTile.setDeadEnd(true);
         return false;
       }
     }
     throw new NoRouteFoundException();
   }


   if (y == 0) {
     Direction[] noWestDirections = new Direction[]{Maze.Direction.NORTH, Maze.Direction.EAST, Maze.Direction.SOUTH};

     for (Direction dir : noWestDirections) {
       Tile adjTile = maze.getAdjacentTile(route.peek(), dir);

       if (adjTile.getType() == Tile.Type.EXIT) {

         route.push(adjTile);
         finished = true;
         return true;
       }
       if (adjTile.isNavigable() == true && adjTile.alreadyVisited() == false) {
         route.push(adjTile);
         adjTile.setVisited(true);
         return false;
       }
     }

     for (Direction dir : noWestDirections) {
       Tile adjDeadTile = maze.getAdjacentTile(route.peek(), dir);
       if (adjDeadTile.isDeadEnd() == false && adjDeadTile.isNavigable() == true) {
         Tile deadEndTile = route.pop();
         deadEndTile.setDeadEnd(true);
         return false;
       }
     }
     throw new NoRouteFoundException();

   }
   if (y == maxY) {
     Direction[] noEastDirections = new Direction[]{Maze.Direction.NORTH, Maze.Direction.SOUTH, Maze.Direction.WEST};

     for (Direction dir : noEastDirections) {
       Tile adjTile = maze.getAdjacentTile(route.peek(), dir);

       if (adjTile.getType() == Tile.Type.EXIT) {

         route.push(adjTile);
         finished = true;

         return true;
       }
       if (adjTile.isNavigable() == true && adjTile.alreadyVisited() == false) {
         route.push(adjTile);
         adjTile.setVisited(true);
         return false;
       }
     }

     for (Direction dir : noEastDirections) {
       Tile adjDeadTile = maze.getAdjacentTile(route.peek(), dir);
       if (adjDeadTile.isDeadEnd() == false && adjDeadTile.isNavigable() == true) {
         Tile deadEndTile = route.pop();
         deadEndTile.setDeadEnd(true);
         return false;
       }
     }
     throw new NoRouteFoundException();
   }

   if (x == 0) {
     Direction[] noNorthDirections = new Direction[]{Maze.Direction.EAST, Maze.Direction.SOUTH, Maze.Direction.WEST};

     for (Direction dir : noNorthDirections) {
       Tile adjTile = maze.getAdjacentTile(route.peek(), dir);

       if (adjTile.getType() == Tile.Type.EXIT) {

         route.push(adjTile);
         finished = true;
         return true;
       }
       if (adjTile.isNavigable() == true && adjTile.alreadyVisited() == false) {
         route.push(adjTile);
         adjTile.setVisited(true);
         return false;
       }
     }

     for (Direction dir : noNorthDirections) {
       Tile adjDeadTile = maze.getAdjacentTile(route.peek(), dir);
       if (adjDeadTile.isDeadEnd() == false && adjDeadTile.isNavigable() == true) {
         Tile deadEndTile = route.pop();
         deadEndTile.setDeadEnd(true);
         return false;
       }
     }
     throw new NoRouteFoundException();
   }
   if (x == maxX) {
     Direction[] noSouthDirections = new Direction[]{Maze.Direction.NORTH, Maze.Direction.EAST, Maze.Direction.WEST};

     for (Direction dir : noSouthDirections) {
       Tile adjTile = maze.getAdjacentTile(route.peek(), dir);

       if (adjTile.getType() == Tile.Type.EXIT) {

         route.push(adjTile);
         finished = true;
         return true;
       }
       if (adjTile.isNavigable() == true && adjTile.alreadyVisited() == false) {
         route.push(adjTile);
         adjTile.setVisited(true);
         return false;
       }
     }

     for (Direction dir : noSouthDirections) {
       Tile adjDeadTile = maze.getAdjacentTile(route.peek(), dir);
       if (adjDeadTile.isDeadEnd() == false && adjDeadTile.isNavigable() == true) {
         Tile deadEndTile = route.pop();
         deadEndTile.setDeadEnd(true);
         return false;
       }
     }
     throw new NoRouteFoundException();
    }
    return false;
  }

  /**  Gets the Tile object at the top of Stack attribute 'route'
  *    @return Returns Tile object at the top of the Stack
  */
  public Tile getTopOfStack() {
    return route.peek();
  }

}
