package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class MinePrecentRemover implements ItemEffect {

    private int minesPrecentToChange;

    public MinePrecentRemover(int minesPrecentToChange) {
        this.minesPrecentToChange = minesPrecentToChange;
    }

    @Override
    public void onEvent(GameEventType type, RunState runstate) {
        if (type == GameEventType.NEWROUND) {
            double minesChanged = Math.ceil((double)runstate.getMineCount()/minesPrecentToChange);
            runstate.changeMines(-((int)minesChanged));
        }
    }
}
