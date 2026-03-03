package com.arielfriedman.arminesweeperproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arielfriedman.arminesweeperproject.Items.Item;
import com.arielfriedman.arminesweeperproject.baseActivity.BaseActivity;
import com.arielfriedman.arminesweeperproject.gameHandler.ItemPool;
import com.arielfriedman.arminesweeperproject.gameHandler.RunState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopActivity extends BaseActivity implements View.OnClickListener {

    Button btnItem1, btnItem2, btnItem3;
    List<Item> itemList;
    List<Item> ownedItemsList;

    Button btnGoNext;

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
        List<Item> shopItems = setItems(3);
        Button[] itemBtns = { btnItem1, btnItem2, btnItem3 };
        int i = 0;
        for (Item item : shopItems) {
            Button btn = itemBtns[i];
            btn.setText(item.getName());
            btn.setOnClickListener(v -> {
                RunState.getInstance().addItem(item);
                // optionally deduct money etc.
                btn.setEnabled(false);
            });
            i++;
        }
    }

    public void InitViews() {
        btnItem1 = findViewById(R.id.item1Btn);
        btnItem2 = findViewById(R.id.item2Btn);
        btnItem3 = findViewById(R.id.item3Btn);
        btnGoNext = findViewById(R.id.goNextBtn);
        btnGoNext.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(ShopActivity.this, GameActivity.class);
        startActivity(intent);
    }
}