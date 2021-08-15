package com.mdevs.phineas.loginregistration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.phineas.MainActivity;
import com.mdevs.phineas.R;
import com.mdevs.phineas.classes.User;
import com.mdevs.phineas.databases.DatabaseHandler;
import com.mdevs.phineas.databases.PrefManager;

public class UserProfile extends AppCompatActivity {
    String id;
    TextView firstName, lastName, email, phone, gender, usertype;
    DatabaseHandler myDb;
    TextView signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.profile_email);
        phone = findViewById(R.id.profile_phone);
        gender = findViewById(R.id.profile_gender);
        usertype = findViewById(R.id.userType);
        signOut = findViewById(R.id.signOut);

        signOut.setOnClickListener(v -> {
            PrefManager.getInstance(this).logout();
            //myDb.deleteCartItems();
            Intent intent = new Intent(UserProfile.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        displayUserInformation();
    }

    private void displayUserInformation() {
        id = PrefManager.getInstance(this).UserID();
        myDb = new DatabaseHandler(this);
        User user = myDb.getUser(id);
        String userCategory = user.getUserType();

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());

        Log.d("TAG", "displayUserInformation: " + user);
        phone.setText(user.getPhone());
        gender.setText(user.getGender());

        switch (userCategory) {
            case "1":
                usertype.setText(R.string.usertype1);
                break;
            case "2":
                usertype.setText(R.string.usertype2);
                break;
            case "3":
                usertype.setText(R.string.usertype3);
                break;
            case "4":
                usertype.setText(R.string.usertype4);
                break;
            case "5":
                usertype.setText(R.string.usertype5);
                break;
        }


    }
}