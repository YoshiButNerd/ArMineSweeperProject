package com.arielfriedman.arminesweeperproject.gameHandler.gameEffects;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

public class MoneyRemovesMine implements ItemEffect {
    private int minesToRemovePerXGold;
    private int XGold;

    public MoneyRemovesMine(int minesToRemovePerXGold, int XGold) {
        this.minesToRemovePerXGold = minesToRemovePerXGold;
        this.XGold = XGold;
    }

    @Override
    public void onEvent(GameEventType type, RunState runstate) {
        if (type == GameEventType.NEWROUND) {
            runstate.changeMines(-((runstate.getMoney()/XGold)*minesToRemovePerXGold));
        }
    }
}
