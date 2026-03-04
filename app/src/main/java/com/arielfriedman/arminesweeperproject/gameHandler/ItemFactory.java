package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MinePrecentRemover;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MoneyGivesMoney;

import java.util.List;

public class ItemFactory {
    public static Item createMoneyMult() {
        return new Item(
                "money_mult", "Gold Shower", "every time money is gained gain +1 money", 8,
                List.of(new MoneyGivesMoney(1))
        );
    }
    public static Item createMineCleaner() {
        return new Item(
                "mine_cleaner", "Mine Missile", "removes 10% of mines (rounded down)", 10,
                List.of(new MinePrecentRemover(10))
        );
    }
}
