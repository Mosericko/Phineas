package com.mdevs.phineas.loginregistration;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.mdevs.phineas.R;
import com.mdevs.phineas.classes.User;
import com.mdevs.phineas.customer.CustomerActivity;
import com.mdevs.phineas.databases.DatabaseHandler;
import com.mdevs.phineas.databases.PrefManager;
import com.mdevs.phineas.distributionmanager.DistributionActivity;
import com.mdevs.phineas.driver.DriverActivity;
import com.mdevs.phineas.financemanager.FinanceActivity;
import com.mdevs.phineas.helperclasses.RequestHandler;
import com.mdevs.phineas.helperclasses.URLs;
import com.mdevs.phineas.productmanager.ProductActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    EditText email, password;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginPass);
        login = findViewById(R.id.login);

        login.setOnClickListener(v -> userLogin());
    }


    private void userLogin() {
        final String loginEmail, loginPass;
        loginEmail = email.getText().toString().trim();
        loginPass = password.getText().toString().trim();

        String email_pattern = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "gmail.com$";

        TextInputLayout eAddress = findViewById(R.id.mailTil);
        TextInputLayout passLayout = findViewById(R.id.passTil);


        if (TextUtils.isEmpty(loginEmail)) {
            eAddress.setError("Please enter your email address!");
            email.requestFocus();
            return;
        } else {
            eAddress.setError(null);
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
            eAddress.setError("Enter a valid email address!");
            email.requestFocus();
            return;
        } else {
            eAddress.setError(null);
        }

        if (!email.getText().toString().matches(email_pattern)) {
            eAddress.setError("Enter a valid email address!");
            email.requestFocus();
            return;
        } else {
            eAddress.setError(null);
        }

        if (TextUtils.isEmpty(loginPass)) {
            passLayout.setError("Enter your password!");
            password.requestFocus();
            return;
        } else {
            passLayout.setError(null);
        }

        UserSignIn user_login = new UserSignIn(loginEmail, loginPass);
        user_login.execute();


    }


    class UserSignIn extends AsyncTask<Void, Void, String> {

        String email, password;

        UserSignIn(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(Login.this, "Loading...Please Wait", Toast.LENGTH_SHORT).show();
            login.setVisibility(View.INVISIBLE);
        }


        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);
            //returning the response
            return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            login.setVisibility(View.VISIBLE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");

                    String typeUser = userJson.getString("userType");

                    //creating a new user object
                    User user = new User(
                            userJson.getString("id"),
                            userJson.getString("firstName"),
                            userJson.getString("lastName"),
                            userJson.getString("gender"),
                            userJson.getString("phone"),
                            userJson.getString("email"),
                            userJson.getString("userType")

                    );

                    //storing the user in shared preferences
                    PrefManager.getInstance(getApplicationContext()).setUserLogin(user);
                    finish();

                    DatabaseHandler myDb = new DatabaseHandler(Login.this);

                    //storing User to SQLite DataBase
                    myDb.addUser(user);

                    switch (typeUser) {
                        case "1":
                            startActivity(new Intent(Login.this, CustomerActivity.class));
                            finish();
                            break;
                        case "2":
                            startActivity(new Intent(Login.this, ProductActivity.class));
                            finish();
                            break;
                        case "3":
                            startActivity(new Intent(Login.this, FinanceActivity.class));
                            finish();
                            break;
                        case "4":
                            startActivity(new Intent(Login.this, DistributionActivity.class));
                            finish();
                            break;
                        case "5":
                            startActivity(new Intent(Login.this, DriverActivity.class));
                            finish();
                            break;
                    }


                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}
