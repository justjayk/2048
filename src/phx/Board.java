package phx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public class Board extends JPanel {
    private static final long serialVersionUID = -1790261785521495991L;
    /* this array use for convenience iterate */
    public static final int[] _0123 = { 0, 1, 2, 3 };
    public static final int ROW = 4;

    final GUI2048 host;


    public static Value GOAL = Value._2048;

    public Value highestTile;

    private Tile[] tiles;

    public Board(GUI2048 f) {
        host = f;
        highestTile = Tile.ZERO.value();
        setFocusable(true);
        initTiles();
    }

    /**
     * initialize the game, also use to start a new game
     */
    public void initTiles() {
        tiles = new Tile[ROW*ROW];
        for (int i = 0; i < ROW*ROW; i++){
            tiles[i] = Tile.ZERO;
        }
        addTile();
        addTile();
        host.statusBar.setText("");
    }

    /**
     * move all the tiles to the left side.
     */
    public void left() {
        for (int row = 0; row < 16; row += 4){
            Tile[] args = Arrays.copyOfRange(tiles, row, row+4);
            Tile[] shift = shift(args);
            for (int i = 0; i < 4; i++){
                tiles[row+i] = shift[i];
            }
        }
        addTile();

    }

    /**
     * move tiles to the right side.
     */
    public void right() {
        for (int row = 0; row < 16; row += 4){
            Tile[] args = Arrays.copyOfRange(tiles, row, row+4);
            List<Tile> argsList = Arrays.asList(args);
            Collections.reverse(argsList);
            argsList.toArray(args);
            Tile[] shift = shift(args);
            int j = 3;
            for (int i = 0; i < 4; i++){
                tiles[row+j] = shift[i];
                j--;
            }
        }
        addTile();

    }

    /**
     * move tiles up.
     */
    public void up() {
        for (int col = 0; col < 4; col++){
            Tile[] args = {tiles[col], tiles[col + 4], tiles[col + 8], tiles[col + 12]};
            Tile[] shift = shift(args);
            for (int i = 0; i < 4; i++){
                tiles[col + i*4] = shift[i];
            }
        }
        addTile();

    }

    /**
     * move tiles down.
     */
    public void down() {
        for (int col = 0; col < 4; col++){
            Tile[] args = {tiles[col], tiles[col + 4], tiles[col + 8], tiles[col + 12]};
            List<Tile> argsList = Arrays.asList(args);
            Collections.reverse(argsList);
            argsList.toArray(args);
            Tile[] shift = shift(args);
            int j = 3;
            for (int i = 0; i < 4; i++){
                tiles[col + j*4] = shift[i];
                j--;
            }
        }
        addTile();

    }

    /**
     * returns a list of what new board should look like, shifted to the left
     */
    public Tile[] shift(Tile[] a) {
        Tile[] combined = new Tile[ROW];
        Arrays.fill(combined, Tile.ZERO);
        int j = 0;
        for (int i = 0; i < 4; i++) {
            if (a[i] != Tile.ZERO) {
                int k = i+1;
                while (k < 3 && a[k] == Tile.ZERO){
                    k++;
                }
                if (i != 3 && a[i] == a[k]) {
                    a[i] = a[i].getDouble();
                    a[k] = Tile.ZERO;
                }
                combined[j] = a[i];
                if (highestTile.score() < combined[j].value().score()){
                    highestTile = combined[j].value();
                }
                j++;
            }
        }
        return combined;
    }

//    /**
//     *
//     * DON'T NEED?
//     *
//     * returns t/f if a and b are same
//     */
//    public boolean canCombine(Tile a, Tile b){
//        return true;
//    }
//
//    /**
//     *
//     * DON'T NEED?
//     *
//     * get the Tile which at tiles[x + y * ROW ]
//     */
//    private Tile tileAt(int x, int y) {
//        return null; // FIXME
//    }

    /**
     * Generate a new Tile in the availableSpace.
     */
    private void addTile() {
        List<Tile> tilesList = Arrays.asList(tiles);
        if (tilesList.contains(Tile.ZERO)){
            int currentTile = ((int) (Math.random() * ROW * ROW)) % (ROW*ROW);
            while (!tiles[currentTile].equals(Tile.ZERO)) {
                currentTile = (currentTile + 3) % (ROW*ROW);
            }
            tiles[currentTile] = Tile.randomTile();
        }


        // FIXME exit while loop after board is full?
    }

    /**
     * return true if didn't lose
     */
    boolean canMove() {
        for (int i = 0; i < 4; i++) {
            int row = i * 4;
            for (int col = 0; col < 4; col++) {
                int index = row+col;
                if ((tiles[index] == Tile.ZERO)
                        || (col < 3 && tiles[index] == tiles[index+1])
                        || (i < 3 && tiles[index] == tiles[index+4])){
                    return true;
                }
            }
        }
        return false;

    }

//    private int index() {
//
//        }

    /* Background color */
    private static final Color BG_COLOR = new Color(0xbbada0);

    /* Font */
    private static final Font STR_FONT = new Font(Font.SANS_SERIF,
                                                    Font.BOLD, 17);

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BG_COLOR);
        g.setFont(STR_FONT);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        for (int y : _0123) {
            for (int x : _0123) {
                drawTile(g, tiles[x + y * ROW], x, y);
            }
        }
    }

    /* Side of the tile square */
    private static final int SIDE = 64;

    /* Margin between tiles */
    private static final int MARGIN = 16;

    /**
     * Draw a tile use specific number and color in (x, y) coords, x and y need
     * offset a bit.
     */
    private void drawTile(Graphics g, Tile tile, int x, int y) {
        Value val = tile.value();
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);
        g.setColor(val.color());
        g.fillRect(xOffset, yOffset, SIDE, SIDE);
        g.setColor(val.fontColor());
        if (val.score() != 0)
            g.drawString(tile.toString(), xOffset
                    + (SIDE >> 1) - MARGIN, yOffset + (SIDE >> 1));
    }

    private static int offsetCoors(int arg) {
        return arg * (MARGIN + SIDE) + MARGIN;
    }

}
