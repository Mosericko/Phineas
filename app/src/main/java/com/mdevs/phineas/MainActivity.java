package com.mdevs.phineas;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.phineas.customer.CustomerActivity;
import com.mdevs.phineas.databases.PrefManager;
import com.mdevs.phineas.distributionmanager.DistributionActivity;
import com.mdevs.phineas.driver.DriverActivity;
import com.mdevs.phineas.financemanager.FinanceActivity;
import com.mdevs.phineas.loginregistration.Login;
import com.mdevs.phineas.loginregistration.Register;
import com.mdevs.phineas.productmanager.ProductActivity;

public class MainActivity extends AppCompatActivity {
    Animation animation;
    LinearLayout phineas;
    TextView wel, login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phineas = findViewById(R.id.phineas);
        wel = findViewById(R.id.wel);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        animation = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        phineas.setAnimation(animation);
        wel.setAnimation(animation);

        login.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
        });

        register.setOnClickListener(v -> {
            startActivity(new Intent(this, Register.class));
        });


        if (PrefManager.getInstance(this).isLoggedIn()) {
            this.finish();

            switch (PrefManager.getInstance(this).UserType()) {
                case "1":
                    startActivity(new Intent(MainActivity.this, CustomerActivity.class));
                    finish();
                    break;
                case "2":
                    startActivity(new Intent(MainActivity.this, ProductActivity.class));
                    finish();
                    break;
                case "3":
                    startActivity(new Intent(MainActivity.this, FinanceActivity.class));
                    finish();
                    break;
                case "4":
                    startActivity(new Intent(MainActivity.this, DistributionActivity.class));
                    finish();
                    break;
                case "5":
                    startActivity(new Intent(MainActivity.this, DriverActivity.class));
                    finish();
                    break;
            }
        }
    }
}