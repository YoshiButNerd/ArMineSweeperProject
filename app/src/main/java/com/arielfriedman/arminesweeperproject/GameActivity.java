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
    int mineCount = ROWS * COLS /2;
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
        calculateMineCounts();
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

        if (tile.getIsMine()) {
            btn.setText("X");
            btn.setBackgroundColor(getColor(R.color.red));
        } else {
            int count = tile.getMinesAround();
            btn.setText(count == 0 ? "" : String.valueOf(count));
            btn.setBackgroundColor(getColor(R.color.light_gray));
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
            int r = random.nextInt(ROWS);
            int c = random.nextInt(COLS);

            if (!tilesArr[r][c].getIsMine()) {
                tilesArr[r][c].setMine(true);
                placed++;
            }
        }
    }

    public void calculateMineCounts() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (tilesArr[r][c].getIsMine()) continue;

                int count = 0;

                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr;
                        int nc = c + dc;
                        if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && tilesArr[nr][nc].getIsMine()) {
                            count++;
                        }
                    }
                }

                tilesArr[r][c].setMinesAround(count);
            }
        }
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}