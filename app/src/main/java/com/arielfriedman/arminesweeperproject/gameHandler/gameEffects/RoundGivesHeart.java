package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class RoundGivesHeart implements ItemEffect {
    private int heartsToGain;

    public RoundGivesHeart(int heartsToGain) {
        this.heartsToGain = heartsToGain;
    }

    @Override
    public void onEvent(GameEventType type, RunState runstate) {
        if (type == GameEventType.NEWROUND) {
            runstate.changeHealth(heartsToGain);
        }
    }
}
