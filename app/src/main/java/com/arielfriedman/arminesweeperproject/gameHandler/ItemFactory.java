package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MineHandler;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MoneyHandler;

import java.util.List;

public class ItemFactory {
    public static Item createMoneyMult() {
        return new Item(
                "money_mult", "Gold Shower", "1.25 times mult to money earned at end of round", 10,
                List.of(new MoneyHandler(10)) // change to mult money at end of round
        );
    }
    public static Item createMineCleaner() {
        return new Item(
                "mine_cleaner", "Mine Missile", "removes 10% of mines", 8,
                List.of(new MineHandler(10)) // change to 10 precent of mines
        );
    }
}
