package com.arielfriedman.arminesweeperproject;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.arielfriedman.arminesweeperproject.BaseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.model.Tile;
import com.google.firebase.events.EventHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    final static int ROWS = 20;
    final static int COLS = 10;
    int mineCount = ROWS * COLS /5;
    int minutes = 1;
    int flagCount = mineCount;
    TextView flagCountText;
    TextView timerCountText;
    boolean firstClick = true;
    CountDownTimer downTimer;
    private static final String FORMAT = "%02d:%02d";
    Tile[][] tilesArr = new Tile[ROWS][COLS];
    Button[][] tileBtnArr = new Button[ROWS][COLS];


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
        flagCountText = findViewById(R.id.flagText);
        timerCountText = findViewById(R.id.timerText);
        flagCountText.setText(flagCount + " ⚑");
        GridLayout mineGridLayout = findViewById(R.id.gridLayout);
        mineGridLayout.setColumnCount(COLS);
        mineGridLayout.setRowCount(ROWS);

        buildBoard(mineGridLayout);
        placeMines(mineCount);
        calculateAllMineCounts();
        timerHandler();
    }
    public void buildBoard(GridLayout mineGridLayout) {
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
        Log.d("GameActivity", "clicked: " + "row-"+ row + " col-" + col);
        onTileClicked(row, col);
    }

    @Override
    public boolean onLongClick(View v) {
        int[] pos = (int[]) v.getTag();
        int row = pos[0];
        int col = pos[1];
        Log.d("GameActivity", "long clicked: " + "row-"+ row + " col-" + col);
        return onTileLongPressed(row, col);
    }

    public void onTileClicked(int row, int col) {
        Tile tile = tilesArr[row][col];
        Button btn = tileBtnArr[row][col];

        if (tile.isFlagged() || tile.getWasRevealed()) return;

        tile.setWasRevealed(true);
        btn.setEnabled(false);

        if (tile.getIsMine() && !firstClick) {
            btn.setText("X");
            flagCount--;
            flagCountText.setText(flagCount + " ⚑");
            btn.setBackgroundColor(getColor(R.color.red));
        } else {
            tile.setMine(false);
            if (tile.getMinesAround() == 0) {
                btn.setText("");
                Log.d("GameActivity", "tile is empty (clear around)");
                clearMinesAround(tile);
            }
            else {
                btn.setText(String.valueOf(tile.getMinesAround()));
            }
            btn.setBackgroundColor(getColor(R.color.light_gray));
        }
        if (firstClick) {
            Log.d("GameActivity", "first click activated");
            firstClick = false;
            btn.setText("");
            clearMinesAround(tile);
            //try to make the board beatable but not break the game over it (so only 3 times)
            for (int i = 1; i <=3; i++){
                clearMineBricks();
            }
        }
    }

    public boolean onTileLongPressed(int row, int col) {
        Tile tile = tilesArr[row][col];
        Button btn = tileBtnArr[row][col];

        if (tile.getWasRevealed()) return true;

        tile.toggleFlagged();
        if (tile.isFlagged()) {
            btn.setText("⚑");
            flagCount--;
            flagCountText.setText(flagCount + " ⚑");
        }
        else {
            btn.setText("");
            flagCount++;
            flagCountText.setText(flagCount + " ⚑");
        }
        return true;
    }

    public void clearMinesAround(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        int countMines = 0;
        for (int iRow = -1; iRow <= 1; iRow++) {
            for (int iCol = -1; iCol <= 1; iCol++) {
                int tileRow = row + iRow;
                int tileCol = col + iCol;
                if (tileRow >= 0 && tileRow < ROWS && tileCol >= 0 && tileCol < COLS && tilesArr[tileRow][tileCol].getIsMine()) {
                    countMines++;
                    tilesArr[tileRow][tileCol].setMine(false);
                }
            }
        }
        calculateAllMineCounts();
        row = tile.getRow();
        col = tile.getCol();
        for (int iRow = -1; iRow <= 1; iRow++) {
            for (int iCol = -1; iCol <= 1; iCol++) {
                int tileRow = row + iRow;
                int tileCol = col + iCol;
                if (tileRow >= 0 && tileRow < ROWS && tileCol >= 0 && tileCol < COLS){
                    onTileClicked(tileRow, tileCol);
                }
            }
        }
        placeMines(countMines);
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
                    clearMinesAround(tilesArr[row][col]);
                    // another mine brick may be created
                }
            }
        }
    }

    public void placeMines(int mineCount) {
        Random random = new Random();
        int placed = 0;
        while (placed < mineCount) {
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLS);

            if (!tilesArr[row][col].getIsMine() && !tilesArr[row][col].getWasRevealed()) {
                tilesArr[row][col].setMine(true);
                placed++;
            }
        }
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
        minutes *= 60000;
        downTimer = new CountDownTimer(minutes, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerCountText.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() { timerCountText.setText("Time Over!"); }
        }.start();
    }
}






