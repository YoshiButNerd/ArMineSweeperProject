package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MineGivesMoney;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MinePrecentRemover;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MoneyGivesMoney;

import java.util.List;

public class ItemFactory {
    public static Item createGoldShower() {
        return new Item(
                "gold_shower", "Gold Shower", "Every time money is gained gain +1 money", 8,
                List.of(new MoneyGivesMoney(1))
        );
    }
    public static Item createGoldenBombs() {
        return new Item(
                "gold_bombs", "Golden Bombs", "When a mine is triggered gain +3 money", 5,
                List.of(new MineGivesMoney(3))
        );
    }
    public static Item createMineMissile() {
        return new Item(
                "mine_missile", "Mine Missile", "Removes 10% of mines (rounded down)", 10,
                List.of(new MinePrecentRemover(10))
        );
    }
}
