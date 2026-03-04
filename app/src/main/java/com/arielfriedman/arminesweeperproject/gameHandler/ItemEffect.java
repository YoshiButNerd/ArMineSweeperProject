package com.arielfriedman.arminesweeperproject.gameHandler;

public interface ItemEffect {
    void onEvent(GameEventType type, RunState runState);
}
