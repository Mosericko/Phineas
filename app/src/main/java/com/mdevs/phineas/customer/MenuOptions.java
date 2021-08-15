package com.mdevs.phineas.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.mdevs.phineas.R;

public class MenuOptions extends AppCompatActivity {
    CardView orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options);

        orders = findViewById(R.id.orders);

        orders.setOnClickListener(v -> {
            startActivity(new Intent(this, MyOrders.class));
        });
    }
}