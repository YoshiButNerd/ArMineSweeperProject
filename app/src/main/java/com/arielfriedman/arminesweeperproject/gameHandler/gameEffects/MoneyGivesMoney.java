package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class MoneyGivesMoney implements ItemEffect {
    private int moneyToGain;

    public MoneyGivesMoney(int moneyToGain) {
        this.moneyToGain = moneyToGain;
    }

    @Override
    public void onEvent(GameEventType type, RunState runState) {
        if (type == GameEventType.MONEYGAIN) {
            runState.moneyItemGain(moneyToGain);
        }
    }
}
