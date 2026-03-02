package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEvent;
import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class MoneyHandler implements ItemEffect {
    private int moneyToAdd;

    public MoneyHandler(int moneyToAdd) {
        this.moneyToAdd = moneyToAdd;
    }

    @Override
    public void onEvent(GameEvent event, RunState runState) {
        if (event.getType() == GameEventType.MONEYCHANGE) {
            runState.changeMoney(moneyToAdd);
        }
    }
}
