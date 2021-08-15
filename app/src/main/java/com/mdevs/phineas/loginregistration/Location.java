package com.mdevs.phineas.loginregistration;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.mdevs.phineas.R;
import com.mdevs.phineas.helperclasses.RequestHandler;
import com.mdevs.phineas.helperclasses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Location extends AppCompatActivity {
    String firstName, lastName, email, password, phoneNumber, gender, userType;
    EditText street, building, phone;
    TextView saveLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        street = findViewById(R.id.streetName);
        building = findViewById(R.id.building);
        phone = findViewById(R.id.phoneNumb);
        saveLocation = findViewById(R.id.submit);

        Intent receiveIntentData = getIntent();
        firstName = receiveIntentData.getStringExtra("firstName");
        lastName = receiveIntentData.getStringExtra("lastName");
        gender = receiveIntentData.getStringExtra("gender");
        email = receiveIntentData.getStringExtra("email");
        password = receiveIntentData.getStringExtra("password");
        phoneNumber = receiveIntentData.getStringExtra("phoneNumber");
        userType = receiveIntentData.getStringExtra("userType");

        phone.setText(phoneNumber);

        saveLocation.setOnClickListener(v -> {
            validateDetails();
        });
    }

    private void validateDetails() {
        String streetName = street.getText().toString().trim();
        String build = building.getText().toString().trim();

        TextInputLayout sName = findViewById(R.id.streetTil);
        TextInputLayout bName = findViewById(R.id.buildingTil);


        if (TextUtils.isEmpty(streetName)) {
            sName.setError("Please enter Street name!");
            street.requestFocus();
            return;
        } else {
            sName.setError(null);
        }

        if (TextUtils.isEmpty(build)) {
            bName.setError("Please enter Building name!");
            building.requestFocus();
            return;
        } else {
            bName.setError(null);
        }

        LocationAsync locationAsync = new LocationAsync(firstName, lastName, gender, phoneNumber, email, userType, password, streetName, build,phoneNumber);
        locationAsync.execute();
    }

    public class LocationAsync extends AsyncTask<Void, Void, String> {
        String firstName;
        String lastName;
        String gender;
        String phoneNumber;
        String email;
        String typeUser;
        String password;
        String streetName, building,phoneN;

        public LocationAsync(String firstName, String lastName, String gender, String phoneNumber, String email, String typeUser, String password, String streetName, String building,String phoneN) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.typeUser = typeUser;
            this.password = password;
            this.streetName = streetName;
            this.building = building;
            this.phoneN = phoneN;

        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("firstName", firstName);
            params.put("lastName", lastName);
            params.put("gender", gender);
            params.put("phone", phoneNumber);
            params.put("email", email);
            params.put("userType", typeUser);
            params.put("password", password);
            params.put("street", streetName);
            params.put("building", building);
            params.put("phone", phoneN);


            return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(Location.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(Location.this, Login.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}