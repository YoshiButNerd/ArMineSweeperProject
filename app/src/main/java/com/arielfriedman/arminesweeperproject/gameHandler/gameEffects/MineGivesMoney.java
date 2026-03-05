package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class MineGivesMoney implements ItemEffect {
    private int moneyAmount;

    public MineGivesMoney(int moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public void onEvent(GameEventType type, RunState runstate) {
        if (type == GameEventType.MINECLICK) {
            runstate.changeMoney(moneyAmount);
        }
    }
}
