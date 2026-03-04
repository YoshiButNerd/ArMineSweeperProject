package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class MinePrecentRemover implements ItemEffect {

    private int minesPrecentToChange;
    private final RunState runstate = RunState.getInstance();

    public MinePrecentRemover(int minesPrecentToChange) {
        this.minesPrecentToChange = minesPrecentToChange;
    }

    @Override
    public void onEvent(GameEventType type, RunState runState) {
        if (type == GameEventType.NEWROUND) {
            double minesChanged = Math.ceil((double)runstate.getMineCount()/minesPrecentToChange);
            runState.changeMines(-((int)minesChanged));
        }
    }
}
