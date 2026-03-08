package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.ChargeBombClick;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MineGivesMoney;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MinePrecentRemover;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MoneyGivesMoney;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MoneyRemovesMine;

import java.util.List;

public class ItemFactory {
    public static Item createGoldShower() { //Trigger
        return new Item(
                "gold_shower", "Gold Shower", "Every time gold is gained gain +1 gold", 8, 0,
                List.of(new MoneyGivesMoney(1))
        );
    }
    public static Item createGoldenBombs() { //Trigger
        return new Item(
                "gold_bombs", "Golden Bombs", "When a mine is triggered gain +3 gold", 5, 3,
                List.of(new MineGivesMoney(3))
        );
    }
    public static Item createMineMissile() { //New Round
        return new Item(
                "mine_missile", "Mine Missile", "Removes 10% of mines (rounded down)", 10, 100,
                List.of(new MinePrecentRemover(10))
        );
    }
    public static Item createGoldMissile() { //New Round
        return new Item(
                "mine_briber", "Mine Bribe", "For every 10 gold you hold at start of round one more mine is removed", 12, 5,
                List.of(new MoneyRemovesMine(1, 10))
        );
    }
    public static Item createChargeBombClick() { //Charge
        return new Item(
                "bomb_click", "Power of the Fist", "Every 80 tiles clicked, your next click will clear all tiles around your click, destroying mines",
                15, 10, List.of(new ChargeBombClick(80, 1))
        );
    }
}
