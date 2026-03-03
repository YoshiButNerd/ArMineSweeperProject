package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEvent;
import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class MineHandler implements ItemEffect {
    private int minesToChange;

    public MineHandler(int moneyToAdd) {
        this.minesToChange = moneyToAdd;
    }

    @Override
    public void onEvent(GameEvent event, RunState runState) {
        if (event.getType() == GameEventType.MINECHANGE) {
            runState.changeMoney(minesToChange);
        }
    }
}
