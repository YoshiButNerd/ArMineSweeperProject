package com.arielfriedman.arminesweeperproject.Items;

import com.arielfriedman.arminesweeperproject.gameHandler.GameEventType;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemEffect;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String id;
    private String name;
    private String desc;
    private int price;
    private int priority;
    private List<ItemEffect> effects;

    public Item(String id, String name, String desc, int price, int priority, List<ItemEffect> effects) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.priority = priority;
        this.effects = new ArrayList<>(effects);
    }

    public void trigger(GameEventType type, RunState runState) {
        for (ItemEffect effect : effects) {
            effect.onEvent(type, runState);
        }
    }

    public Item() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {return name; }

    public void setName(String name) {this.name = name; }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriority() { return priority;}

    public void setPriority(int priority) { this.priority = priority;}

    public List<ItemEffect> getEffects() {return effects; }

    public void setEffects(List<ItemEffect> effects) {this.effects = effects; }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                ", priority=" + priority +
                ", effects=" + effects +
                '}';
    }
}
