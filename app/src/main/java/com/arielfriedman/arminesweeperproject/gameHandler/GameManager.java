package com.arielfriedman.arminesweeperproject.gameHandler;

public class GameManager {

    private static GameManager instance;
    private RunState runState;

    private GameManager() {
        runState = RunState.getInstance();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public RunState getRunState() {
        return runState;
    }
}
