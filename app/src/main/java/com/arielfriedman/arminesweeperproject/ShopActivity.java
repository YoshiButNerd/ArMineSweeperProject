package com.arielfriedman.arminesweeperproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arielfriedman.arminesweeperproject.Items.Item;
import com.arielfriedman.arminesweeperproject.baseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemPool;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.MusicManager;
import com.arielfriedman.arminesweeperproject.specialClasses.MusicHandler.SfxManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopActivity extends BaseActivity implements View.OnClickListener {

    Button btnItem1, btnItem2, btnItem3, btnHeartUp, btnGoNext;
    TextView moneyCountTxt, healthCountTxt;
    List<Item> itemList;
    List<Item> ownedItemsList;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentLayout(R.layout.activity_shop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        InitViews();
        manageItemBtns();
        manageHidingNavigationBar();
        manageBackPress();
    }

    public void InitViews() {
        btnItem1 = findViewById(R.id.item1Btn);
        btnItem2 = findViewById(R.id.item2Btn);
        btnItem3 = findViewById(R.id.item3Btn);
        btnHeartUp = findViewById(R.id.heartUpBtn);
        btnGoNext = findViewById(R.id.goNextBtn);
        btnItem1.setSoundEffectsEnabled(false);
        btnItem2.setSoundEffectsEnabled(false);
        btnItem3.setSoundEffectsEnabled(false);
        btnHeartUp.setSoundEffectsEnabled(false);
        btnGoNext.setSoundEffectsEnabled(false);
        moneyCountTxt = findViewById(R.id.moneyTxt);
        healthCountTxt = findViewById(R.id.healthTxt);
        moneyCountTxt.setText("כסף: " + RunState.getInstance().getMoney());
        healthCountTxt.setText("לבבות: " + RunState.getInstance().getHealth());
        btnGoNext.setOnClickListener(this);
        btnHeartUp.setOnClickListener(this);
        itemList = new ArrayList<>(ItemPool.getAllItems());
        ownedItemsList = new ArrayList<>(RunState.getInstance().getItems());
    }

    public List<Item> setItems(int amount) {
        List<Item> availableItems = new ArrayList<>();

        for (Item item : itemList) {
            boolean alreadyOwned = false;
            for (Item owned : ownedItemsList) {
                if (item.getId().equals(owned.getId())) {
                    alreadyOwned = true;
                    break;
                }
            }
            if (!alreadyOwned) {
                availableItems.add(item);
            }
        }
        Collections.shuffle(availableItems);
        return new ArrayList<>(availableItems.subList(0, Math.min(amount, availableItems.size())));
    }

    public void manageItemBtns() {
        List<Item> shopItems = setItems(3);
        Button[] itemBtns = { btnItem1, btnItem2, btnItem3 };
        int i = 0;
        for (Item item : shopItems) {
            Button btn = itemBtns[i];
            btn.setText(
                    item.getName() + "\n\n" + "מחיר: " +
                            item.getPrice() + "\n\n" + "יכולת: " + "\n" +
                            item.getDesc()
            );
            btn.setOnClickListener(v -> {
                if (RunState.getInstance().getMoney() >= item.getPrice()){
                    SfxManager.play(this, R.raw.sfx_clickbtn);
                    RunState.getInstance().addItem(item);
                    moneyChange(-(item.getPrice()));
                    btn.setBackgroundColor(getColor(R.color.light_gray));
                    btn.setEnabled(false);
                }
                else {
                    Toast.makeText(ShopActivity.this, "אין לך מספיק כסף" , Toast.LENGTH_SHORT).show();
                }
            });
            i++;
        }
    }

    public void moneyChange(int i) {
        RunState.getInstance().changeMoney(i);
        moneyCountTxt.setText("כסף: " + RunState.getInstance().getMoney());
    }

    public void healthChange(int i) {
        RunState.getInstance().changeHealth(i);
        healthCountTxt.setText("לבבות: " + RunState.getInstance().getHealth());
    }

    public void manageBackPress() {
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        new AlertDialog.Builder(ShopActivity.this)
                                .setTitle("לצאת מהמשחק?")
                                .setMessage("המשחק יסתיים")
                                .setPositiveButton("כן", (dialog, which) -> {
                                    dialog.dismiss();
                                    MusicManager.getInstance().stopMusic();
                                    finishAffinity();
                                })
                                .setNegativeButton("לא", (dialog, which) -> dialog.dismiss())
                                .show();
                    }
                });
    }

    private void manageHidingNavigationBar() { //Hides navigation bar until user swipes from top
        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
            controller.hide(WindowInsets.Type.systemBars());
            controller.setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        }
    }

    @Override
    public void onClick(View view) {
        SfxManager.play(this, R.raw.sfx_clickbtn);
        if (view == btnGoNext) {
            intent = new Intent(ShopActivity.this, GameActivity.class);
            startActivity(intent);
        }
        else if (view == btnHeartUp) {
            if (RunState.getInstance().getMoney() >= 10){
                moneyChange(-10);
                healthChange(1);
            }
            else {
                Toast.makeText(ShopActivity.this, "אין לך מספיק כסף" , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
