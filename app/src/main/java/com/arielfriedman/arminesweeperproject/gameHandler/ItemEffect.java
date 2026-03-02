package com.arielfriedman.arminesweeperproject.gameHandler;

public interface ItemEffect {
    void onEvent(GameEvent event, RunState runState);
}
