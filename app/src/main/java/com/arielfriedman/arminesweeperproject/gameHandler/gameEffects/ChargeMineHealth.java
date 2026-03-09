package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class ChargeMineHealth implements ItemEffect {
    private int currentMineClicks = 0;
    private int healthAdd;
    private final int threshold;

    public ChargeMineHealth(int threshold, int healthAdd) {
        this.threshold = threshold;
        this.healthAdd = healthAdd;
    }

    @Override
    public void onEvent(GameEventType type, RunState runstate) {
        if (type == GameEventType.MINECLICK) {
            currentMineClicks++;
            if (currentMineClicks >= threshold) {
                runstate.changeHealth(healthAdd);
                currentMineClicks = 0;
            }
        }
    }
}
