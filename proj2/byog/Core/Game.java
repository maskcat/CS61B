package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.*;

public class Game implements Serializable {
    /* Feel free to change the width and height. */
    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;
    private Random RANDOM = null;
    private final int ROOM_CHANCE = 40;
    private long SEED;
    TERenderer ter = new TERenderer();
    private int currentFeature = 0;
    private Player player;
    private TETile[][] world = null;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        while (true) {
            char option = menu();
            if ('N' == option) {
                SEED = getSeed();
                RANDOM = new Random(SEED);
                world = genWorld(ROOM_CHANCE);
                ter.initialize(WIDTH, HEIGHT);
                ter.renderFrame(world);
                world = operator(world);
            } else if ('L' == option) {
                if (world == null) {
                    int result;
                    File file;
                    String path;
                    JFileChooser fileChooser = new JFileChooser();
                    FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
                    System.out.println(fsv.getDefaultDirectory());                //得到桌面路径
                    fileChooser.setCurrentDirectory(fsv.getDefaultDirectory());
                    fileChooser.setDialogTitle("请选择要上传的文件...");
                    fileChooser.setApproveButtonText("确定");
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    result = fileChooser.showOpenDialog(null);
                    if (JFileChooser.APPROVE_OPTION == result) {
                        path = fileChooser.getSelectedFile().getPath();
                        System.out.println("path: " + path);
                        file = new File(path);
                        try {
                            ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(file.toPath()));
                            Game game = (Game) inputStream.readObject();
                            this.SEED = game.SEED;
                            this.player = game.player;
                            this.RANDOM = game.RANDOM;
                            world = game.world;
                            ter.initialize(WIDTH, HEIGHT);
                            ter.renderFrame(world);
                            world = operator(game.world);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if ('C' == option) {
                if (world != null) {
                    ter.initialize(WIDTH, HEIGHT);
                    ter.renderFrame(world);
                    world = operator(world);
                }
            } else if ('S' == option) {
                if (world != null) {
                    File file = new File(System.currentTimeMillis() + "-" + SEED + ".txt");
                    if (!file.exists()) {
                        try {
                            System.out.println(file.toPath());
                            if (file.createNewFile()) {
                                ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
                                outputStream.writeObject(this);
                                outputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.exit(0);
            }
        }
    }

    public char menu() {
        List<Character> options = new ArrayList<>();
        options.add('N');
        options.add('S');
        options.add('L');
        options.add('C');
        options.add('Q');
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 50));
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2d, HEIGHT - 10, "这是一个游戏");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.text(WIDTH / 2d, HEIGHT - 18, "新游戏[N]");
        StdDraw.text(WIDTH / 2d, HEIGHT - 21, "加载[L]");
        StdDraw.text(WIDTH / 2d, HEIGHT - 24, "继续[C]");
        StdDraw.text(WIDTH / 2d, HEIGHT - 27, "退出[Q]");
        StdDraw.show();
        char option = '=';
        while (!options.contains(option)) {
            if (StdDraw.hasNextKeyTyped()) {
                option = Character.toUpperCase(StdDraw.nextKeyTyped());
            }
        }
        return option;
    }

    public long getSeed() {
        StringBuilder str;
        boolean currentIsNotS = true;
        while (true) {
            str = new StringBuilder();
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2d, HEIGHT - 10, "请输入随机整数:");
            StdDraw.show();
            char input;
            while (currentIsNotS) {
                if (StdDraw.hasNextKeyTyped()) {
                    input = Character.toUpperCase(StdDraw.nextKeyTyped());
                    if (input == 'S' || input == '\n') {
                        currentIsNotS = false;
                        continue;
                    }
                    str.append(input);
                    drawFrame(str.toString());
                }
            }
            String numberStr = str.toString();
            if (!numberStr.isEmpty() && numberStr.matches("^[0-9]*$")) {
                break;
            }
            currentIsNotS = true;
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
            StdDraw.setPenColor(Color.green);
            StdDraw.text(WIDTH / 2d, HEIGHT - 10, "请保证输入的为纯数字");
            StdDraw.show();
            StdDraw.pause(1000);

        }
        return Long.parseLong(str.toString());
    }

    private TETile[][] operator(TETile[][] world) {
        Map<Character, Direction> map = new HashMap<>();
        map.put('W', Direction.North);
        map.put('S', Direction.South);
        map.put('A', Direction.West);
        map.put('D', Direction.East);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char op = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (map.containsKey(op)) {
                    world = move(world, map.get(op));
                    ter.renderFrame(world);
                } else if (op == '\u001B') {
                    return world;
                }
            }
        }
    }

    private TETile[][] move(TETile[][] world, Direction direction) {
        switch (direction) {
            case East:
                if (world[player.x + 1][player.y].equals(Tileset.FLOOR)) {
                    world[player.x][player.y] = Tileset.FLOOR;
                    world[player.x + 1][player.y] = Tileset.PLAYER;
                    player.x = player.x + 1;
                    return world;
                }
                break;
            case West:
                if (world[player.x - 1][player.y].equals(Tileset.FLOOR)) {
                    world[player.x][player.y] = Tileset.FLOOR;
                    world[player.x - 1][player.y] = Tileset.PLAYER;
                    player.x = player.x - 1;
                    return world;
                }
                break;
            case North:
                if (world[player.x][player.y + 1].equals(Tileset.FLOOR)) {
                    world[player.x][player.y] = Tileset.FLOOR;
                    world[player.x][player.y + 1] = Tileset.PLAYER;
                    player.y = player.y + 1;
                    return world;
                }
                break;
            case South:
                if (world[player.x][player.y - 1].equals(Tileset.FLOOR)) {
                    world[player.x][player.y] = Tileset.FLOOR;
                    world[player.x][player.y - 1] = Tileset.PLAYER;
                    player.y = player.y - 1;
                    return world;
                }
                break;
            default:
                return world;
        }
        return world;
    }

    public void drawFrame(String s) {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;
        StdDraw.clear();
        StdDraw.clear(Color.black);
        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, s);
        StdDraw.show();
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
        //      Fill out this method to run the game using the input passed in,
        //      and return a 2D tile representation of the world that would have been
        //      drawn if the same inputs had been given to playWithKeyboard().
        SEED = Long.parseLong(input.substring(1, input.length() - 1));
        RANDOM = new Random(SEED);
        TETile[][] finalWorldFrame = null;
        switch (Character.toUpperCase(input.charAt(0))) {
            case 'N':
                finalWorldFrame = genWorld(ROOM_CHANCE);
                break;
            case 'S':
                break;
            default:
                throw new RuntimeException("未定义的操作！" + input);
        }
        return finalWorldFrame;
    }

    private TETile[][] genWorld(int roomChance) {
        StdDraw.clear();
        StdDraw.clear(StdDraw.BLACK);
        // 地图算法
        // 1.地图中填满土
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        // 2.地图中间挖一个房间
        world = createRoom(world, (WIDTH - 1) / 2, (HEIGHT - 1) / 2, 5, 5, Direction.North);
        currentFeature++;
        while (currentFeature < 50) {
            WallPosition wp = getRandomWall(world);
            if (wp == null) {
                continue;
            }
            int feature = RandomUtils.uniform(RANDOM, 0, 100);
            TETile[][] temp;
            if (feature <= roomChance) {
                int width = RandomUtils.uniform(RANDOM, 6, 10);
                int height = RandomUtils.uniform(RANDOM, 6, 10);
                temp = createRoom(world, wp.nX, wp.nY, width, height, wp.direction);
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
        world = fillWall(world);
        return genPlayer(world);
    }

    private WallPosition getRandomWall(TETile[][] world) {
        // 随机找到一个点
        WallPosition wallPosition = new WallPosition();
        int i = 0;
        while (i < (WIDTH - 2) * (HEIGHT - 2)) {
            int rX = RandomUtils.uniform(RANDOM, 1, WIDTH - 2);
            int rY = RandomUtils.uniform(RANDOM, 1, HEIGHT - 2);
            if (world[rX][rY].equals(Tileset.NOTHING)) {
                if (!world[rX + 1][rY].equals(Tileset.NOTHING) /*|| world[rX + 1][rY].equals(Tileset.GRASS)*/) {
                    wallPosition.nX = rX - 1;
                    wallPosition.nY = rY;
                    wallPosition.direction = Direction.West;
                } else if (!world[rX][rY - 1].equals(Tileset.NOTHING) /*|| world[rX][rY - 1].equals(Tileset.GRASS)*/) {
                    wallPosition.nX = rX;
                    wallPosition.nY = rY + 1;
                    wallPosition.direction = Direction.North;
                } else if (!world[rX - 1][rY].equals(Tileset.NOTHING) /*|| world[rX - 1][rY].equals(Tileset.GRASS)*/) {
                    wallPosition.nX = rX + 1;
                    wallPosition.nY = rY;
                    wallPosition.direction = Direction.East;
                } else if (!world[rX][rY + 1].equals(Tileset.NOTHING) /*|| world[rX][rY + 1].equals(Tileset.GRASS)*/) {
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
        if (!hasEnoughRoomSpace(world, x, y, width, height, direction)) {
            return null;
        }
        switch (direction) {
            case East:
                for (int i = y - height / 2; i < y + (height + 1) / 2; i++) {
                    for (int j = x; j < x + width; j++) {
                        world[j][i] = Tileset.FLOOR;
                    }
                }
                break;
            case South:
                for (int i = y; i > y - height; i--) {
                    for (int j = x - width / 2; j < x + (width + 1) / 2; j++) {
                        world[j][i] = Tileset.FLOOR;
                    }
                }
                world[RandomUtils.uniform(RANDOM, x - width / 2, x + (width + 1) / 2)][RandomUtils.uniform(RANDOM, y - height + 1, y + 1)] = Tileset.FLOWER;
                break;
            case West:
                for (int i = y - height / 2; i < y + (height + 1) / 2; i++) {
                    for (int j = x; j > x - width; j--) {
                        world[j][i] = Tileset.FLOOR;
                    }
                }
                break;
            case North:
                for (int i = y; i < y + height; i++) {
                    for (int j = x - width / 2; j < x + (width + 1) / 2; j++) {
                        world[j][i] = Tileset.FLOOR;
                    }
                }
                break;
            default:
                return null;
        }
        return world;
    }

    private TETile[][] createHallWay(TETile[][] world, int x, int y, int len, Direction direction) {
        if (!hasEnoughHallWaySpace(world, x, y, len, direction)) {
            return null;
        }
        switch (direction) {
            case East:
                for (int i = x; i < x + (len + 1); i++) {
                    world[i][y] = Tileset.FLOOR;
                }
                break;
            case South:
                for (int i = y; i > y - (len + 1); i--) {
                    world[x][i] = Tileset.FLOOR;

                }
                break;
            case West:
                for (int i = x; i > x - (len + 1); i--) {
                    world[i][y] = Tileset.FLOOR;
                }
                break;
            case North:
                for (int i = y; i < y + (len + 1); i++) {
                    world[x][i] = Tileset.FLOOR;
                }
                break;
            default:
                return null;
        }
        return world;
    }

    private boolean hasEnoughRoomSpace(TETile[][] world, int x, int y, int width, int height, Direction direction) {
        if (direction.equals(Direction.East)) {
            for (int i = y - height / 2; i < y + (height + 1) / 2; i++) {
                if (i < 1 || i > HEIGHT - 2) {
                    return false;
                }
                for (int j = x; j < x + width; j++) {
                    if (j < 1 || j > WIDTH - 2) {
                        return false;
                    }
                    if (!world[j][i].equals(Tileset.NOTHING)) {
                        return false;
                    }
                }
            }
        } else if (direction.equals(Direction.South)) {
            for (int i = y; i > y - height; i--) {
                if (i < 1 || i > HEIGHT - 2) {
                    return false;
                }
                for (int j = x - width / 2; j < x + (width + 1) / 2; j++) {
                    if (j < 1 || j > WIDTH - 2) {
                        return false;
                    }
                    if (!world[j][i].equals(Tileset.NOTHING)) {
                        return false;
                    }
                }
            }
        } else if (direction.equals(Direction.West)) {
            for (int i = y - height / 2; i < y + (height + 1) / 2; i++) {
                if (i < 1 || i > HEIGHT - 2) {
                    return false;
                }
                for (int j = x; j > x - width; j--) {
                    if (j < 1 || j > WIDTH - 2) {
                        return false;
                    }
                    if (!world[j][i].equals(Tileset.NOTHING)) {
                        return false;
                    }
                }
            }
        } else if (direction.equals(Direction.North)) {
            for (int i = y; i < y + height; i++) {
                if (i < 1 || i > HEIGHT - 2) {
                    return false;
                }
                for (int j = x - width / 2; j < x + (width + 1) / 2; j++) {
                    if (j < 1 || j > WIDTH - 2) {
                        return false;
                    }
                    if (!world[j][i].equals(Tileset.NOTHING)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean hasEnoughHallWaySpace(TETile[][] world, int x, int y, int len, Direction direction) {
        if (direction.equals(Direction.East)) {
            for (int i = x; i < x + (len + 1); i++) {
                if (i > WIDTH - 2) {
                    return false;
                } else if (y + 1 > HEIGHT - 1 || y - 1 < 1) {
                    return false;
                } else if (!world[i][y].equals(Tileset.NOTHING)) {
                    return false;
                } else if (!world[i][y - 1].equals(Tileset.NOTHING) && !world[i][y - 1].equals(Tileset.WALL)) {
                    return false;
                } else if (!world[i][y + 1].equals(Tileset.NOTHING) && !world[i][y + 1].equals(Tileset.WALL)) {
                    return false;
                }
            }
        } else if (direction.equals(Direction.South)) {
            for (int i = y; i > y - (len + 1); i--) {
                if (i < 1 || i > HEIGHT - 2) {
                    return false;
                } else if (x + 1 > WIDTH - 1 || x - 1 < 1) {
                    return false;
                } else if (!world[x][i].equals(Tileset.NOTHING)) {
                    return false;
                } else if (!world[x - 1][i].equals(Tileset.NOTHING) && !world[x - 1][i].equals(Tileset.WALL)) {
                    return false;
                } else if (!world[x + 1][i].equals(Tileset.NOTHING) && !world[x + 1][i].equals(Tileset.WALL)) {
                    return false;
                }
            }
        } else if (direction.equals(Direction.West)) {
            for (int i = x; i > x - (len + 1); i--) {
                if (i > WIDTH - 1 || i < 1) {
                    return false;
                } else if (y + 1 > HEIGHT - 1 || y - 1 < 1) {
                    return false;
                } else if (!world[i][y].equals(Tileset.NOTHING)) {
                    return false;
                } else if (!world[i][y - 1].equals(Tileset.NOTHING) && !world[i][y - 1].equals(Tileset.WALL)) {
                    return false;
                } else if (!world[i][y + 1].equals(Tileset.NOTHING) && !world[i][y + 1].equals(Tileset.WALL)) {
                    return false;
                }
            }
        } else if (direction.equals(Direction.North)) {
            for (int i = y; i < y + (len + 1); i++) {
                if (i > HEIGHT - 2) {
                    return false;
                } else if (x + 1 > WIDTH - 1 || x - 1 < 1) {
                    return false;
                } else if (!world[x][i].equals(Tileset.NOTHING)) {
                    return false;
                } else if (!world[x - 1][i].equals(Tileset.NOTHING) && !world[x - 1][i].equals(Tileset.WALL)) {
                    return false;
                } else if (!world[x + 1][i].equals(Tileset.NOTHING) && !world[x + 1][i].equals(Tileset.WALL)) {
                    return false;
                }
            }
        }
        return true;
    }

    private TETile[][] fillWall(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j].equals(Tileset.NOTHING)) {
                    if (i + 1 < WIDTH - 1 && !world[i + 1][j].equals(Tileset.NOTHING) && !world[i + 1][j].equals(Tileset.WALL)) {
                        world[i][j] = Tileset.WALL;
                    } else if (i + 1 < WIDTH - 1 && j + 1 < HEIGHT - 1 && !world[i + 1][j + 1].equals(Tileset.NOTHING) && !world[i + 1][j + 1].equals(Tileset.WALL)) {
                        world[i][j] = Tileset.WALL;
                    } else if (i + 1 < WIDTH - 1 && j - 1 > 0 && !world[i + 1][j - 1].equals(Tileset.NOTHING) && !world[i + 1][j - 1].equals(Tileset.WALL)) {
                        world[i][j] = Tileset.WALL;
                    } else if (i - 1 > 0 && !world[i - 1][j].equals(Tileset.NOTHING) && !world[i - 1][j].equals(Tileset.WALL)) {
                        world[i][j] = Tileset.WALL;
                    } else if (i - 1 > 0 && j + 1 < HEIGHT - 1 && !world[i - 1][j + 1].equals(Tileset.NOTHING) && !world[i - 1][j + 1].equals(Tileset.WALL)) {
                        world[i][j] = Tileset.WALL;
                    } else if (i - 1 > 0 && j - 1 > 0 && !world[i - 1][j - 1].equals(Tileset.NOTHING) && !world[i - 1][j - 1].equals(Tileset.WALL)) {
                        world[i][j] = Tileset.WALL;
                    } else if (j + 1 < HEIGHT - 1 && !world[i][j + 1].equals(Tileset.NOTHING) && !world[i][j + 1].equals(Tileset.WALL)) {
                        world[i][j] = Tileset.WALL;
                    } else if (j - 1 > 0 && !world[i][j - 1].equals(Tileset.NOTHING) && !world[i][j - 1].equals(Tileset.WALL)) {
                        world[i][j] = Tileset.WALL;
                    }
                }
            }
        }
        return world;
    }

    private TETile[][] genPlayer(TETile[][] world) {
        while (true) {
            int rX = RandomUtils.uniform(RANDOM, 1, WIDTH - 2);
            int rY = RandomUtils.uniform(RANDOM, 1, HEIGHT - 2);
            if (world[rX][rY].equals(Tileset.FLOOR)) {
                world[rX][rY] = Tileset.PLAYER;
                player = new Player(rX, rY);
                return world;
            }
        }
    }

    enum Direction {
        East, South, West, North
    }

    static class WallPosition implements Serializable {
        private static final long serialVersionUID = 1L;
        Direction direction;
        int oX;
        int oY;
        int nX;
        int nY;
    }

    static class Player implements Serializable {
        private static final long serialVersionUID = 1L;

        public Player(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x;
        int y;
    }
}
