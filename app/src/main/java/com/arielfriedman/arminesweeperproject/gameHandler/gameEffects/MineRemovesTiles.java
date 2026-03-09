package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class MineRemovesTiles implements ItemEffect {
    @Override
    public void onEvent(GameEventType type, RunState runstate) {
        if (type == GameEventType.ONOBTAIN) {
            runstate.setMineBombs(true);
        }
    }
}
