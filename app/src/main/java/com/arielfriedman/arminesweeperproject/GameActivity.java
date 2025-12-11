package com.arielfriedman.arminesweeperproject;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import com.arielfriedman.arminesweeperproject.model.Tile;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    final static int ROWS = 20;
    final static int COLS = 10;
    int mineCount = ROWS * COLS /4;
    boolean firstClick = true;

    Tile[][] tilesArr = new Tile[ROWS][COLS];
    Button[][] tileBtnArr = new Button[ROWS][COLS];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        GridLayout mineGridLayout = findViewById(R.id.gridLayout);
        mineGridLayout.setColumnCount(COLS);
        mineGridLayout.setRowCount(ROWS);

        buildBoard(mineGridLayout);
        placeMines();
        calculateAllMineCounts();
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
        onTileClicked(row, col);
    }

    @Override
    public boolean onLongClick(View v) {
        int[] pos = (int[]) v.getTag();
        int row = pos[0];
        int col = pos[1];
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
            btn.setBackgroundColor(getColor(R.color.red));
        } else {
            tile.setMine(false);
            //calculateTileMineCount(tile);
            int count = tile.getMinesAround();
            btn.setText(count == 0 ? "" : String.valueOf(count));
            btn.setBackgroundColor(getColor(R.color.light_gray));
        }
        if (firstClick) {
            firstClick = false;
            TileFirstClick(tile, btn);
        }
    }

    public void TileFirstClick(Tile tile, Button btn) {
        int row = tile.getRow();
        int col = tile.getCol();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && tilesArr[nr][nc].getIsMine()) {
                    tilesArr[nr][nc].setMine(false);
                }
            }
        }
        calculateAllMineCounts();
        row = tile.getRow();
        col = tile.getCol();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS){
                    onTileClicked(nr, nc);
                }
            }
        }
    }

    public boolean onTileLongPressed(int row, int col) {
        Tile tile = tilesArr[row][col];
        Button btn = tileBtnArr[row][col];

        if (tile.getWasRevealed()) return true;

        tile.toggleFlagged();
        btn.setText(tile.isFlagged() ? "⚑" : "");
        return true;
    }

    public void placeMines() {
        Random random = new Random();
        int placed = 0;
        while (placed < mineCount) {
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLS);

            if (!tilesArr[row][col].getIsMine()) {
                tilesArr[row][col].setMine(true);
                placed++;
            }
        }
    }

    public void calculateTileMineCount(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();

        int count = 0;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && tilesArr[nr][nc].getIsMine()) {
                    count++;
                }
            }
        }
        tile.setMinesAround(count);
    }
    public void calculateAllMineCounts() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                tilesArr[row][col].setMinesAround(0);
            }
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (tilesArr[row][col].getIsMine()) continue;

                int count = 0;

                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = row + dr;
                        int nc = col + dc;
                        if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && tilesArr[nr][nc].getIsMine()) {
                            count++;
                        }
                    }
                }

                tilesArr[row][col].setMinesAround(count);
            }
        }
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}