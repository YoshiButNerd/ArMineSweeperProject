package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class RunState {
    private int money;
    private int health;
    private List<Item> items;

    public RunState() {
        this.money = 0;
        this.health = 3;
        items = new ArrayList<>();
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

    public void changeMoney(int amount) {
        this.money += amount;
    }

    public void changeHealth(int amount){
        this.health += amount;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void triggerEvent(GameEvent event) {
        for (Item item : items) {
            item.trigger(event, this);
        }
    }

    @Override
    public String toString() {
        return "RunState{" +
                "money=" + money +
                ", health=" + health +
                ", items=" + items +
                '}';
    }
}
