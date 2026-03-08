package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemPool {
    private static final List<Item> allItems = new ArrayList<>();

    static {
        allItems.add(ItemFactory.createGoldShower());
        allItems.add(ItemFactory.createGoldenBombs());
        allItems.add(ItemFactory.createMineMissile());
        allItems.add(ItemFactory.createChargeBombClick());
    }

    public static List<Item> getAllItems() {
        return allItems;
    }
}
