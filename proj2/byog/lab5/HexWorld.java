package byog.lab5;

import org.junit.Test;

import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        drawAllHexes(world, 28, 50, 3, 7);
        // draws the world to the screen
        ter.renderFrame(world);
    }

    public static void drawAllHexes(TETile[][] world, int x, int y, int size, int count) {
        drawRandomVerticalHexes(world, x, y, size, count);
        drawLeftHexes(world, x, y, size, count);
        drawRightHexes(world, x, y, size, count);

    }

    public static void drawLeftHexes(TETile[][] world, int x, int y, int size, int count) {
        for (int i = 1; i < count / 2 + 1; i++) {
            drawRandomVerticalHexes(world, x - i * 2 * size + i, y - i * size, size, count - i);
        }
    }

    public static void drawRightHexes(TETile[][] world, int x, int y, int size, int count) {
        for (int i = 1; i < count / 2 + 1; i++) {
            drawRandomVerticalHexes(world, x + i * 2 * size - i, y - i * size, size, count - i);
        }
    }

    public static void drawRandomVerticalHexes(TETile[][] world, int x, int y, int size, int count) {
        for (int i = 0; i < count; i++) {
            addHexagon(world, x, y - i * 2 * size, size, randomTile());
        }
    }

    public static void addHexagon(TETile[][] world, int x, int y, int size, TETile t) {
        if (size < 2) {
            throw new IllegalArgumentException("size must >= 2");
        }
        TETile colorTile = TETile.colorVariant(t, RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM);
        addTop(world, x, y, size, colorTile);
        addBottom(world, x, y - (2 * size - 1), size, colorTile);
    }

    public static void addLine(TETile[][] world, int x, int y, int count, TETile t) {
        for (int i = 0; i < count; i++) {
            if (x + i >= WIDTH || y >= HEIGHT || y < 0 || x < 0) {
                continue;
            }
            world[x + i][y] = t;
        }
    }

    public static void addTop(TETile[][] world, int x, int y, int size, TETile t) {
        for (int i = 0; i < size; i++) {
            addLine(world, x - i, y - i, size + 2 * i, t);
        }
    }

    public static void addBottom(TETile[][] world, int x, int y, int size, TETile t) {
        for (int i = 0; i < size; i++) {
            addLine(world, x - i, y + i, size + 2 * i, t);
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0:
                return Tileset.TREE;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.MOUNTAIN;
            case 3:
                return Tileset.FLOOR;
            case 4:
                return Tileset.GRASS;
            default:
                return Tileset.WATER;
        }
    }
}
