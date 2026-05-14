package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class HeartGivesHeart implements ItemEffect {
    private int heartsToGain;

    public HeartGivesHeart(int heartsToGain) {
        this.heartsToGain = heartsToGain;
    }

    @Override
    public void onEvent(GameEventType type, RunState runstate) {
        if (type == GameEventType.HEALTHGAIN) {
            runstate.healthItemGain(heartsToGain);
        }
    }
}
