package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemPool {
    private static final List<Item> allItems = new ArrayList<>();

    static {
        //Trigger
        allItems.add(ItemFactory.createGoldShower());
        allItems.add(ItemFactory.createGoldenBombs());
        //New Round
        allItems.add(ItemFactory.createMineMissile());
        allItems.add(ItemFactory.createGoldMissile());
        allItems.add(ItemFactory.createHeartIncome());
        //Charge
        allItems.add(ItemFactory.createChargeBombClick());
        allItems.add(ItemFactory.createChargeMineHealth());
        //On Pickup
        allItems.add(ItemFactory.createMineBombs());
    }

    public static List<Item> getAllItems() {
        return allItems;
    }
}
