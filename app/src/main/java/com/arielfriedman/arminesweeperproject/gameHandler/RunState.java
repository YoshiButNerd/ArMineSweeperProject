package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.model.Item;

import java.util.ArrayList;
import java.util.List;

public class RunState {
    private int money;
    private int health;
    private int mineCount;
    private int round;
    private int firstClicks;
    private List<Item> items;
    private static RunState instance;
    //Booleans for special items
    private boolean mineBombs;

    //
    public interface StateListener {
        void onMoneyChanged(int money);
        void onHealthChanged(int health);
    }

    private List<StateListener> listeners = new ArrayList<>();

    public void addListener(StateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(StateListener listener) {
        listeners.remove(listener);
    }

    private void notifyMoneyChanged() {
        for (StateListener listener : new ArrayList<>(listeners)) {
            listener.onMoneyChanged(money);
        }
    }

    private void notifyHealthChanged() {
        for (StateListener listener : new ArrayList<>(listeners)) {
            listener.onHealthChanged(health);
        }
    }
    //

    private void setNewRunValues() {
        this.money = 0;
        this.health = 3;
        this.mineCount = 30;
        this.round = 1;
        this.firstClicks = 1;

        //booleans for special items
        this.mineBombs = false;
    }

    public void setNewRun() {
        setNewRunValues();
        this.items.clear();
        notifyMoneyChanged();
        notifyHealthChanged();
    }

    private RunState() {
        setNewRunValues();
        items = new ArrayList<>();
    }

    public static RunState getInstance() {
        if (instance == null) {
            instance = new RunState();
        }
        return instance;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getFirstClicks() { return firstClicks;}

    public void setFirstClicks(int firstClicks) { this.firstClicks = firstClicks;}

    public void changeMoney(int amount) {
        this.money += amount;
        if (amount > 0) {
            triggerEvent(GameEventType.MONEYGAIN);
        }
        notifyMoneyChanged();
    }

    public void changeHealth(int amount) {
        this.health += amount;
        triggerEvent(GameEventType.HEALTHCHANGE);
        if (amount > 0)
            triggerEvent(GameEventType.HEALTHGAIN);
        notifyHealthChanged();
    }

    public void changeFirstClicks(int amount) {
        this.firstClicks += amount;
        if (amount < 0)
            triggerEvent(GameEventType.FIRSTCLICK);
    }

    public void changeMines(int amount){
        this.mineCount += amount;
    }

    public void addItem(Item item) {
        items.add(item);
        item.trigger(GameEventType.ONOBTAIN, this);
    }

    public int getMineCount() {
        return mineCount;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void increaseRound() {
        this.round++;
        changeFirstClicks(1);
        triggerEvent(GameEventType.ENDROUND);
    }

    public void triggerEvent(GameEventType event) {
        items.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority())); // Sorts items from high priority to low

        for (Item item : new ArrayList<>(items)) {
            item.trigger(event, this);
        }
    }

    @Override
    public String toString() {
        return "RunState{" +
                " mineBombs=" + mineBombs +
                ", items=" + items +
                ", firstClicks=" + firstClicks +
                ", round=" + round +
                ", mineCount=" + mineCount +
                ", health=" + health +
                ", money=" + money +
                '}';
    }

    //Functions for items
    public void moneyItemGain(int amount) {
        this.money += amount;
        notifyMoneyChanged();
    }

    public void healthItemGain(int amount) {
        this.health += amount;
        notifyHealthChanged();
    }

    public boolean getMineBombs() {
        return mineBombs;
    }

    public void setMineBombs(boolean mineBombs) {
        this.mineBombs = mineBombs;
    }

}
