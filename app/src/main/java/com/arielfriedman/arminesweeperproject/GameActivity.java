package com.arielfriedman.arminesweeperproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.arielfriedman.arminesweeperproject.baseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemFactory;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;
import com.arielfriedman.arminesweeperproject.model.Tile;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    //Constants
    final static int ROWS = 20;
    final static int COLS = 10;
    final static int WINPOINTS = ROWS*COLS;
    private static final String FORMAT = "%02d:%02d";

    //Game variables
    int mineCount, flagCount;
    int secondsCountDown = 150;
    int endOfRoundMoney = 30;
    boolean lost = false;
    boolean trueFirstClick = true;
    private int round;
    int totalPoints = 0;  //player will not be able to see points
    RunState runstate;

    //Setting up tile array
    Tile[][] tilesArr = new Tile[ROWS][COLS];
    Button[][] tileBtnArr = new Button[ROWS][COLS];

    //UI + Other variables set up for later
    TextView flagCountText;
    TextView timerCountText;
    TextView pointsCountText;
    TextView roundCountText;
    TextView moneyCountText;
    TextView healthCountText;
    CountDownTimer downTimer;
    private SharedPreferences prefs;
    GridLayout mineGridLayout;
    Intent intent;

    //Listener variable
    private RunState.StateListener runStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        runstate = RunState.getInstance();
        InitViews();

        runStateListener = new RunState.StateListener() {
            @Override
            public void onMoneyChanged(int money) {
                moneyCountText.setText("כסף: " + money);
            }

            @Override
            public void onHealthChanged(int health) {
                healthCountText.setText("לבבות: " + health);
            }
        };

        setGameDiff();
        mineGridLayout.setColumnCount(COLS);
        mineGridLayout.setRowCount(ROWS);

        //runstate.addItem(ItemFactory.createGoldMissile()); //TEST AN ITEM
        //runstate.addItem(ItemFactory.createMineMissile()); //TEST AN ITEM

        buildBoard(mineGridLayout);
        flagCountText.setText(flagCount + " ⚑");
        roundCountText.setText("סיבוב: " + round);
        placeMines(mineCount);
        calculateAllMineCounts();
        timerHandler();
        moneyCountText.setText("כסף: " + runstate.getMoney());
        healthCountText.setText("לבבות: " + runstate.getHealth());
    }

    @Override
    protected void onStart() {
        super.onStart();
        runstate.addListener(runStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        runstate.removeListener(runStateListener);
    }

    public void InitViews() {
        flagCountText = findViewById(R.id.flagText);
        timerCountText = findViewById(R.id.timerText);
        pointsCountText = findViewById(R.id.pointsText);
        roundCountText = findViewById(R.id.roundText);
        moneyCountText = findViewById(R.id.moneyText);
        healthCountText = findViewById(R.id.healthText);
        mineGridLayout = findViewById(R.id.gridLayout);
    }

    public void setGameDiff() {
        round = runstate.getRound();
        int startingMines = runstate.getMineCount();
        runstate.setMineCount(startingMines + 5*(round-1));
        secondsCountDown += 10*(round-1);
    }

    public void buildBoard(GridLayout mineGridLayout) {
        runstate.triggerEvent(GameEventType.NEWROUND);
        mineCount = runstate.getMineCount();
        flagCount = mineCount;
        int tileSize = dpToPx(33);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tile tile = new Tile(row, col);
                tilesArr[row][col] = tile;

                Button btn = new Button(this);
                tileBtnArr[row][col] = btn;

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = tileSize;
                params.height = tileSize;
                btn.setLayoutParams(params);

                btn.setBackgroundColor(getColor(R.color.gray));
                btn.setText("");
                btn.setPadding(0, 0, 0, 0);
                btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                btn.setGravity(android.view.Gravity.CENTER);
                btn.setTextSize(16);

                // Store coordinates in the tag
                btn.setTag(new int[]{row, col});

                // Use this as the click listener
                btn.setOnClickListener(this);
                btn.setOnLongClickListener(this);

                mineGridLayout.addView(btn);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int[] pos = (int[]) v.getTag();
        int row = pos[0];
        int col = pos[1];
        Log.d("GameActivity", "Clicked: " + "row-"+ row + " col-" + col);
        onTileClicked(row, col);
    }

    @Override
    public boolean onLongClick(View v) {
        int[] pos = (int[]) v.getTag();
        int row = pos[0];
        int col = pos[1];
        Log.d("GameActivity", "Long clicked: " + "row-"+ row + " col-" + col);
        return onTileLongPressed(row, col);
    }

    public void onTileClicked(int row, int col) {
        Tile tile = tilesArr[row][col];
        Button btn = tileBtnArr[row][col];

        if (tile.isFlagged() || tile.getWasRevealed()) return;

        tile.setWasRevealed(true);
        btn.setEnabled(false);


        if (runstate.getFirstClicks() > 0) { //add an if and a bool if its true first click
            runstate.changeFirstClicks(-1);
            btn.setText("");
            btn.setBackgroundColor(getColor(R.color.light_gray));
            Log.d("GameActivity", "First click activated");
            if (trueFirstClick) {
                trueFirstClick = false;
                clearMinesAroundAndMove(tile);
                //try to make the board beatable but not break the game over it (so only 3 times)
                for (int i = 1; i <=3; i++){
                    clearMineBricks();
                }
            }
            else {
                clearMinesAroundAndDelete(tile);
            }
        }
        else if (tile.getIsMine()) {
            btn.setText("X");
            changeFlagCount(-1);
            btn.setBackgroundColor(getColor(R.color.red));
            mineClicked();
        }
        else {
            if (tile.getMinesAround() == 0) {
                btn.setText("");
                Log.d("GameActivity", "Tile is empty (clear around)");
                clearMinesAroundAndDelete(tile);
            }
            else {
                btn.setText(String.valueOf(tile.getMinesAround()));
            }
            btn.setBackgroundColor(getColor(R.color.light_gray));
        }
        addPoints(1);
        tileClicked(); // the trigger happens before click takes effect
    }

    public boolean onTileLongPressed(int row, int col) {
        Tile tile = tilesArr[row][col];
        Button btn = tileBtnArr[row][col];

        if (tile.getWasRevealed()) return true;

        tile.toggleFlagged();
        if (tile.isFlagged()) {
            if (tile.getIsMine()) {
                addPoints(1);
            }
            btn.setText("⚑");
            changeFlagCount(-1);
        }
        else {
            if (tile.getIsMine()) {
                addPoints(-1);
            }
            btn.setText("");
            changeFlagCount(1);
        }
        return true;
    }

    public void clearMinesAround(Tile tile, boolean move) {
        int row = tile.getRow();
        int col = tile.getCol();
        int countMines = 0;
        for (int iRow = -1; iRow <= 1; iRow++) {  //Remove mine status from tiles around and calc how many mines there were
            for (int iCol = -1; iCol <= 1; iCol++) {
                int tileRow = row + iRow;
                int tileCol = col + iCol;
                if (tileRow >= 0 && tileRow < ROWS && tileCol >= 0 && tileCol < COLS && tilesArr[tileRow][tileCol].getIsMine()) {
                    if (!tilesArr[tileRow][tileCol].getWasRevealed()) { //If revealed we dont need to count (move/remove flag)
                        countMines++;
                    }
                    tilesArr[tileRow][tileCol].setMine(false);
                }
            }
        }
        Log.d("GameActivity", countMines + " mines detected around click");
        calculateAllMineCounts();
        row = tile.getRow();
        col = tile.getCol();
        for (int iRow = -1; iRow <= 1; iRow++) {  //Click the tiles around
            for (int iCol = -1; iCol <= 1; iCol++) {
                int tileRow = row + iRow;
                int tileCol = col + iCol;
                if (tileRow >= 0 && tileRow < ROWS && tileCol >= 0 && tileCol < COLS){
                    onTileClicked(tileRow, tileCol);
                }
            }
        }
        if (move) {
            placeMines(countMines);
            Log.d("GameActivity", countMines + " detected mines moved (if there was room)");
            calculateAllMineCounts();
        }
        else {
            changeFlagCount(-countMines);
        }
        calculateAllRevealedMineCounts();
    }

    public void clearMineBricks() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (!tilesArr[row][col].getWasRevealed()) continue;
                int count = calculateTileMineCount(tilesArr[row][col]);
                if (row == 0 || row == ROWS-1) {
                    count += 3;
                }
                if (col == 0 || col == COLS-1) {
                    count += 3;
                }
                if (count >= 8) {
                    clearMinesAroundAndMove(tilesArr[row][col]);
                    // another mine brick may be created
                }
            }
        }
    }

    public void placeMines(int mineCount) {
        int tilesLeft = calculateClearTiles();
        Random random = new Random();
        int placed = 0;
        while (placed < mineCount) {
            if (tilesLeft <= 0) {   //if no room, escape the loop
                changeFlagCount(-(mineCount-placed));
                Log.d("GameActivity", "No room to move more mines");//change flagCount to match the deleted mines
                return;
            }
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLS);

            if (!tilesArr[row][col].getIsMine() && !tilesArr[row][col].getWasRevealed()) {
                tilesArr[row][col].setMine(true);
                placed++;
                tilesLeft--;
            }
        }
    }

    public int calculateClearTiles() {
        int clearTiles = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (!tilesArr[row][col].getIsMine() && !tilesArr[row][col].getWasRevealed()) clearTiles++;
            }
        }
        return clearTiles;
    }

    public void calculateAllMineCounts() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (tilesArr[row][col].getIsMine()) continue;
                calculateTileMineCount(tilesArr[row][col]);
            }
        }
    }

    // could prob combine with the function above
    public void calculateAllRevealedMineCounts(){
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (!tilesArr[row][col].getWasRevealed() || tilesArr[row][col].getIsMine()) continue;

                int count = calculateTileMineCount(tilesArr[row][col]);
                Button btn = tileBtnArr[row][col];
                btn.setBackgroundColor(getColor(R.color.light_gray));
                if (count == 0) {
                    btn.setText("");
                }
                else {
                    btn.setText(String.valueOf(count));
                }
            }
        }
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    public int calculateTileMineCount(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();

        int count = 0;

        for (int iRow = -1; iRow <= 1; iRow++) {
            for (int iCol = -1; iCol <= 1; iCol++) {
                int tileRow = row + iRow;
                int tileCol = col + iCol;
                if (tileRow >= 0 && tileRow < ROWS && tileCol >= 0 && tileCol < COLS && tilesArr[tileRow][tileCol].getIsMine()) {
                    count++;
                }
            }
        }
        tile.setMinesAround(count);
        return count;
    }

    public void timerHandler() {
        int millis = secondsCountDown * 1000; // convert seconds into milliseconds
        downTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerCountText.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                timerCountText.setText("Time Over!");
                gameLost();
            }
        }.start();
    }

    public void addPoints(int i) {
        totalPoints += i;
        pointsCountText.setText("נקודות: " + totalPoints);
        if (totalPoints >= WINPOINTS && !lost) {
            gameWin();
        }
    }

    public void mineClicked() {
        runstate.triggerEvent(GameEventType.MINECLICK);
        runstate.changeHealth(-1);
    }

    public void changeFlagCount(int i) {
        flagCount += i;
        flagCountText.setText(flagCount + " ⚑");
    }

    public void tileClicked() {
        runstate.triggerEvent(GameEventType.TILECLICK);
    }

    public void gameWin() {
        runstate.increaseRound();
        runstate.changeMoney(endOfRoundMoney);
        intent = new Intent(GameActivity.this, ShopActivity.class);
        startActivity(intent);
    }

    public void gameLost() {
        lost = true;
    }

    public void clearMinesAroundAndMove(Tile tile) {
        clearMinesAround(tile, true);
    }

    public void clearMinesAroundAndDelete(Tile tile) {
        clearMinesAround(tile, false);
    }
}
