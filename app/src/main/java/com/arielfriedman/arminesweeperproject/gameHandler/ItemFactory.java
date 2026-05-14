package com.arielfriedman.arminesweeperproject.gameHandler;

import com.arielfriedman.arminesweeperproject.Items.Item;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.ChargeBombClick;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.ChargeMineHealth;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MineGivesMoney;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MinePrecentRemover;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MineRemovesTiles;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MoneyGivesMoney;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.MoneyRemovesMine;
import com.arielfriedman.arminesweeperproject.gameHandler.gameEffects.RoundGivesHeart;

import java.util.List;

public class ItemFactory {
    public static Item createGoldShower() { //Trigger
        return new Item(
                "gold_shower", "Gold Shower", "בכל פעם שמורווח זהב, קבל +1 זהב",
                8, 0, List.of(new MoneyGivesMoney(1))
        );
    }
    public static Item createGoldenBombs() { //Trigger
        return new Item(
                "gold_bombs", "Golden Bombs", "כאשר מוקש מופעל הרווח +3 זהב",
                5, 3, List.of(new MineGivesMoney(3))
        );
    }
    public static Item createMineMissile() { //New Round
        return new Item(
                "mine_missile", "Mine Missile", "מסיר 10% מהמוקשים (מעוגל למעלה)",
                10, 100, List.of(new MinePrecentRemover(10))
        );
    }
    public static Item createGoldMissile() { //New Round
        return new Item(
                "mine_briber", "Mine Bribe", "לכל 10 זהב שאתה מחזיק בתחילת סיבוב מוסר מוקש",
                12, 5, List.of(new MoneyRemovesMine(1, 10))
        );
    }
    public static Item createHeartIncome() { //New Round
        return new Item(
                "heart_income", "Heart Factory", "בכל סיבוב חדש קבל לב",
                15, 50, List.of(new RoundGivesHeart(1))
        );
    }
    public static Item createChargeBombClick() { //Charge
        return new Item(
                "bomb_click", "Power of the Fist", "כל 80 משבצות שנלחצו, הלחיצה הבאה שלך תנקה את כל האריחים סביבך, ותשמיד מוקשים",
                15, 10, List.of(new ChargeBombClick(80, 1))
        );
    }
    public static Item createChargeMineHealth() { //Charge
        return new Item(
                "health_mines", "Mines of Medicine", "כל 3 מוקשים שנלחצו קבל לב",
                12, 40, List.of(new ChargeMineHealth(3, 1))
        );
    }
    public static Item createMineBombs() { //On Pickup
        return new Item(
                "mine_bombs", "Friendly Fire", "לחיצה על מוקשים תנקה את כל המשבצות סביבם ותשמיד מוקשים אחרים (הרס מוקשים לא יגרום לכך)",
                10, 100, List.of(new MineRemovesTiles())
        );
    }
}
