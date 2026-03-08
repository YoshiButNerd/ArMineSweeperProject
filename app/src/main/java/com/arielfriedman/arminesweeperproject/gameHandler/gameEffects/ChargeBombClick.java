package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class ChargeBombClick implements  ItemEffect{
        private int currentClicks = 0;
        private int firstClickAdd = 0;
        private final int threshold;

        public ChargeBombClick(int threshold, int firstClickAdd) {
            this.threshold = threshold;
            this.firstClickAdd = firstClickAdd;
        }

        @Override
        public void onEvent(GameEventType type, RunState runstate) {
            if (type == GameEventType.TILECLICK) {
                currentClicks++;
                if (currentClicks >= threshold) {
                    runstate.changeFirstClicks(firstClickAdd);
                    currentClicks = 0;
                }
            }
        }
}

