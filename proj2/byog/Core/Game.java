package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;
    private static Random RANDOM = null;
    private int currentFeature = 0;

    enum Direction {
        East, South, West, North;
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        //      and return a 2D tile representation of the world that would have been
        //      drawn if the same inputs had been given to playWithKeyboard().
        long seed = Long.parseLong(input.substring(1, input.length() - 1));
        RANDOM = new Random(seed);
        TETile[][] finalWorldFrame = null;
        switch (input.charAt(0)) {
            case 'N':
                ter.initialize(WIDTH, HEIGHT);
                finalWorldFrame = genWorld(60);
                ter.renderFrame(finalWorldFrame);
                break;
            case 'S':
                break;
            default:
                throw new RuntimeException("未定义的操作！");
        }
        return finalWorldFrame;
    }

    private TETile[][] genWorld(int roomChance) {
        // 地图算法
        // 1.地图中填满土
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        // 2.地图中间挖一个房间
        world = createRoom(world, (WIDTH - 1) / 2, (HEIGHT - 1) / 2, 10, 10, Direction.North);
        currentFeature++;
        while (currentFeature < 60) {
            WallPosition wp = getRandomWall(world);
            if (wp == null) {
                continue;
            }
            int feature = RandomUtils.uniform(RANDOM, 0, 100);
            TETile[][] temp;
            if (feature <= roomChance) {
                temp = createRoom(world, wp.nX, wp.nY, RandomUtils.uniform(RANDOM, 6, 10), RandomUtils.uniform(RANDOM, 6, 10), wp.direction);
                if (temp != null) {
                    world = temp;
                    world[wp.oX][wp.oY] = Tileset.FLOOR;
                    temp[wp.nX][wp.nY] = Tileset.FLOOR;
                    currentFeature++;
                }
            } else {
                temp = createHallWay(world, wp.nX, wp.nY, RandomUtils.uniform(RANDOM, 3, 5), wp.direction);
                if (temp != null) {
                    world = temp;
                    world[wp.oX][wp.oY] = Tileset.FLOOR;
                    currentFeature++;
                }
            }
        }
        return world;
    }

    private WallPosition getRandomWall(TETile[][] world) {
        // 随机找到一个点
        WallPosition wallPosition = new WallPosition();
        int i = 0;
        while (i < (WIDTH - 2) * (HEIGHT - 2)) {
            int rX = RandomUtils.uniform(RANDOM, 1, WIDTH - 2);
            int rY = RandomUtils.uniform(RANDOM, 1, HEIGHT - 2);
            if (world[rX][rY].equals(Tileset.WALL)) {
                if (world[rX + 1][rY].equals(Tileset.FLOOR) || world[rX + 1][rY].equals(Tileset.GRASS)) {
                    wallPosition.nX = rX - 1;
                    wallPosition.nY = rY;
                    wallPosition.direction = Direction.West;
                } else if (world[rX][rY - 1].equals(Tileset.FLOOR) || world[rX][rY - 1].equals(Tileset.GRASS)) {
                    wallPosition.nX = rX;
                    wallPosition.nY = rY + 1;
                    wallPosition.direction = Direction.North;
                } else if (world[rX - 1][rY].equals(Tileset.FLOOR) || world[rX - 1][rY].equals(Tileset.GRASS)) {
                    wallPosition.nX = rX + 1;
                    wallPosition.nY = rY;
                    wallPosition.direction = Direction.East;
                } else if (world[rX][rY + 1].equals(Tileset.FLOOR) || world[rX][rY + 1].equals(Tileset.GRASS)) {
                    wallPosition.nX = rX;
                    wallPosition.nY = rY - 1;
                    wallPosition.direction = Direction.South;
                }
                if (wallPosition.direction != null) {
                    wallPosition.oX = rX;
                    wallPosition.oY = rY;
                    return wallPosition;
                }
            }
            i++;
        }
        return null;
    }

    private TETile[][] createRoom(TETile[][] world, int x, int y, int width, int height, Direction direction) {
        switch (direction) {
            case East:
                for (int i = y - height / 2; i < y + (height + 1) / 2; i++) {
                    if (i < 0 || i > HEIGHT - 1) {
                        return null;
                    }
                    for (int j = x; j < x + width; j++) {
                        if (j < 0 || j > WIDTH - 1) {
                            return null;
                        }
                        if (!world[j][i].equals(Tileset.NOTHING)) {
                            return null;
                        }
                    }
                }
                for (int i = y - height / 2; i < y + (height + 1) / 2; i++) {
                    for (int j = x; j < x + width; j++) {
                        if (i == y - height / 2 || i == y + (height - 1) / 2) {
                            world[j][i] = Tileset.WALL;
                        } else if (j == x || j == x + width - 1) {
                            world[j][i] = Tileset.WALL;
                        } else {
                            world[j][i] = Tileset.FLOOR;
                        }
                    }
                }
                break;
            case South:
                for (int i = y; i > y - height; i--) {
                    if (i < 0 || i > HEIGHT - 1) {
                        return null;
                    }
                    for (int j = x - width / 2; j < x + (width + 1) / 2; j++) {
                        if (j < 0 || j > WIDTH - 1) {
                            return null;
                        }
                        if (!world[j][i].equals(Tileset.NOTHING)) {
                            return null;
                        }
                    }
                }
                for (int i = y; i > y - height; i--) {
                    for (int j = x - width / 2; j < x + (width + 1) / 2; j++) {
                        if (i == y || i == y - height + 1) {
                            world[j][i] = Tileset.WALL;
                        } else if (j == x - width / 2 || j == x + (width - 1) / 2) {
                            world[j][i] = Tileset.WALL;
                        } else {
                            world[j][i] = Tileset.FLOOR;
                        }
                    }
                }
                break;
            case West:
                for (int i = y - height / 2; i < y + (height + 1) / 2; i++) {
                    if (i < 0 || i > HEIGHT - 1) {
                        return null;
                    }
                    for (int j = x; j > x - width; j--) {
                        if (j < 0 || j > WIDTH - 1) {
                            return null;
                        }
                        if (!world[j][i].equals(Tileset.NOTHING)) {
                            return null;
                        }
                    }
                }
                for (int i = y - height / 2; i < y + (height + 1) / 2; i++) {
                    for (int j = x; j > x - width; j--) {
                        if (i == y - height / 2 || i == y + (height - 1) / 2) {
                            world[j][i] = Tileset.WALL;
                        } else if (j == x || j == x - width + 1) {
                            world[j][i] = Tileset.WALL;
                        } else {
                            world[j][i] = Tileset.FLOOR;
                        }
                    }
                }
                break;
            case North:
                for (int i = y; i < y + height; i++) {
                    if (i < 0 || i > HEIGHT - 1) {
                        return null;
                    }
                    for (int j = x - width / 2; j < x + (width + 1) / 2; j++) {
                        if (j < 0 || j > WIDTH - 1) {
                            return null;
                        }
                        if (!world[j][i].equals(Tileset.NOTHING)) {
                            return null;
                        }
                    }
                }
                for (int i = y; i < y + height; i++) {
                    for (int j = x - width / 2; j < x + (width + 1) / 2; j++) {
                        if (i == y || i == y + height - 1) {
                            world[j][i] = Tileset.WALL;
                        } else if (j == x - width / 2 || j == x + (width - 1) / 2) {
                            world[j][i] = Tileset.WALL;
                        } else {
                            world[j][i] = Tileset.FLOOR;
                        }
                    }
                }
                break;
            default:
                return null;
        }
        return world;
    }

    private TETile[][] createHallWay(TETile[][] world, int x, int y, int len, Direction direction) {
        if (x < 0 || x > WIDTH - 1 || y < 0 || y > HEIGHT - 1) {
            return null;
        }
        switch (direction) {
            case East:
                for (int i = x; i < x + (len + 1); i++) {
                    if (i > WIDTH - 1) {
                        return null;
                    } else if (y + 1 > HEIGHT - 1 || y - 1 < 0) {
                        return null;
                    } else if (!world[i][y].equals(Tileset.NOTHING)) {
                        return null;
                    } else if (!world[i][y - 1].equals(Tileset.NOTHING) && !world[i][y - 1].equals(Tileset.WALL)) {
                        return null;
                    } else if (!world[i][y + 1].equals(Tileset.NOTHING) && !world[i][y + 1].equals(Tileset.WALL)) {
                        return null;
                    }
                }
                for (int i = x; i < x + (len + 1); i++) {
                    world[i][y - 1] = Tileset.WALL;
                    if (i == x + len) {
                        world[i][y] = Tileset.WALL;
                    } else {
                        world[i][y] = Tileset.GRASS;
                    }
                    world[i][y + 1] = Tileset.WALL;
                }
                break;
            case South:
                for (int i = y; i > y - (len + 1); i--) {
                    if (i < 0 || i > HEIGHT - 1) {
                        return null;
                    } else if (x + 1 > WIDTH - 1 || x - 1 < 0) {
                        return null;
                    } else if (!world[x][i].equals(Tileset.NOTHING)) {
                        return null;
                    } else if (!world[x - 1][i].equals(Tileset.NOTHING) && !world[x - 1][i].equals(Tileset.WALL)) {
                        return null;
                    } else if (!world[x + 1][i].equals(Tileset.NOTHING) && !world[x + 1][i].equals(Tileset.WALL)) {
                        return null;
                    }
                }
                for (int i = y; i > y - (len + 1); i--) {
                    world[x - 1][i] = Tileset.WALL;
                    if (i == y - len) {
                        world[x][i] = Tileset.WALL;
                    } else {
                        world[x][i] = Tileset.GRASS;
                    }
                    world[x + 1][i] = Tileset.WALL;
                }
                break;
            case West:
                for (int i = x; i > x - (len + 1); i--) {
                    if (i > WIDTH - 1 || i < 0) {
                        return null;
                    } else if (y + 1 > HEIGHT - 1 || y - 1 < 0) {
                        return null;
                    } else if (!world[i][y].equals(Tileset.NOTHING)) {
                        return null;
                    } else if (!world[i][y - 1].equals(Tileset.NOTHING) && !world[i][y - 1].equals(Tileset.WALL)) {
                        return null;
                    } else if (!world[i][y + 1].equals(Tileset.NOTHING) && !world[i][y + 1].equals(Tileset.WALL)) {
                        return null;
                    }
                }
                for (int i = x; i > x - (len + 1); i--) {
                    world[i][y - 1] = Tileset.WALL;
                    if (i == x - len) {
                        world[i][y] = Tileset.WALL;
                    } else {
                        world[i][y] = Tileset.GRASS;
                    }
                    world[i][y + 1] = Tileset.WALL;
                }
                break;
            case North:
                for (int i = y; i < y + (len + 1); i++) {
                    if (i > HEIGHT - 1) {
                        return null;
                    } else if (x + 1 > WIDTH - 1 || x - 1 < 0) {
                        return null;
                    } else if (!world[x][i].equals(Tileset.NOTHING) || !world[x + 1][i].equals(Tileset.NOTHING) || !world[x - 1][i].equals(Tileset.NOTHING)) {
                        return null;
                    }
                }
                for (int i = y; i < y + (len + 1); i++) {
                    world[x - 1][i] = Tileset.WALL;
                    if (i == y + len) {
                        world[x][i] = Tileset.WALL;
                    } else {
                        world[x][i] = Tileset.GRASS;
                    }
                    world[x + 1][i] = Tileset.WALL;
                }
                break;
            default:
                return null;
        }
        return world;
    }

    static class WallPosition {
        Direction direction;
        int oX;
        int oY;
        int nX;
        int nY;
    }
}
