package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MoneyHandler;

import java.util.List;

public class ItemFactory {
    public static Item createMoneyMult() {
        return new Item(
                "money_mult", "Gold Shower", "1.25 times mult to money earned at end of round", 10,
                List.of(new MoneyHandler(10)) // change to mult money at end of round
        );
    }
}
