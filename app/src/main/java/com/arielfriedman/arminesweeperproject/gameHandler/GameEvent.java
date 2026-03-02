package com.arielfriedman.arminesweeperproject.gameHandler;

public class GameEvent {

    private GameEventType type;
    private Object data;

    public GameEvent(GameEventType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public GameEventType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
