package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;

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

    public void setNewRun() {
        this.money = 0;
        this.health = 3;
        this.mineCount = 40;
        this.round = 1;
        this.firstClicks = 1;
        this.items.clear();
        notifyMoneyChanged();
        notifyHealthChanged();
    }

    private RunState() {
        this.money = 0;
        this.health = 3;
        this.mineCount = 40;
        this.round = 1;
        this.firstClicks = 1;
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

    public void changeHealth(int amount){
        this.health += amount;
        triggerEvent(GameEventType.HEALTHCHANGE);
        notifyHealthChanged();
    }

    public void changeFirstClicks(int amount) {
        this.firstClicks += amount;
        triggerEvent(GameEventType.FIRSTCLICK);
    }

    public void changeMines(int amount){
        this.mineCount += amount;
    }

    public void addItem(Item item) {
        items.add(item);
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
        for (Item item : items) {
            item.trigger(event, this);
        }
    }

    @Override
    public String toString() {
        return "RunState{" +
                "money=" + money +
                ", health=" + health +
                ", mineCount=" + mineCount +
                ", round=" + round +
                ", firstClicks=" + firstClicks +
                ", items=" + items +
                '}';
    }

    //Functions for items
    public void moneyItemGain(int amount) {
        this.money += amount;
        notifyMoneyChanged();
    }

}
