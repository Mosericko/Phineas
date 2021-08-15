package com.mdevs.phineas.customer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mdevs.phineas.R;
import com.mdevs.phineas.databases.DatabaseHandler;
import com.mdevs.phineas.helperclasses.RequestHandler;
import com.mdevs.phineas.helperclasses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CheckOut extends AppCompatActivity {

    String jsonIn, idIn, orderNumIn, totalPriceIn;
    EditText mpesaCode;
    Button placeOrder;
    DatabaseHandler myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        mpesaCode = findViewById(R.id.mpesaCode);
        placeOrder = findViewById(R.id.placeOrder);

        myDb = new DatabaseHandler(this);

        placeOrder.setOnClickListener(v -> {
            sendOrderToDb();
        });
    }

    private void sendOrderToDb() {

        Intent intent = getIntent();
        idIn = intent.getStringExtra("ID");
        orderNumIn = intent.getStringExtra("ORDER_NUM");
        totalPriceIn = intent.getStringExtra("Price");
        jsonIn = intent.getStringExtra("JSON");
        Log.d("TAG", "onCreate: " + jsonIn);
        Log.d("TAG", "onCreate: " + orderNumIn);
        Log.d("TAG", "onCreate: " + totalPriceIn);
        Log.d("TAG", "onCreate: " + idIn);
        final String mpesa_code = mpesaCode.getText().toString().trim();
        Log.d("TAG", "onCreate: " + mpesa_code);

        //Validations
        if (mpesa_code.isEmpty()) {
            mpesaCode.setError("Cannot be Blank!");
            return;
        }
        if (mpesa_code.length() < 10) {
            mpesaCode.setError("Cannot be less than 10");
            return;
        }

        OrderAsync orderAsync = new OrderAsync(jsonIn, idIn, orderNumIn, mpesa_code, totalPriceIn);
        orderAsync.execute();
    }

    public class OrderAsync extends AsyncTask<Void, Void, String> {
        String json;
        String user_id;
        String orderNumber;
        String mpesa_code;
        String totalPrice;

        public OrderAsync(String json, String user_id, String orderNumber, String mpesa_code, String totalPrice) {
            this.json = json;
            this.user_id = user_id;
            this.orderNumber = orderNumber;
            this.mpesa_code = mpesa_code;
            this.totalPrice = totalPrice;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(CheckOut.this, "Please wait...", Toast.LENGTH_SHORT).show();
            placeOrder.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("json", json);
            params.put("user_id", user_id);
            params.put("order_num", orderNumber);
            params.put("mpesa_code", mpesa_code);
            params.put("total_price", totalPrice);

            return requestHandler.sendPostRequest(URLs.URL_PLACE_ORDER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            placeOrder.setVisibility(View.VISIBLE);

            try {
                JSONObject jsonObject = new JSONObject(s);

                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(CheckOut.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    myDb.deleteCartItems();

                    startActivity(new Intent(CheckOut.this, CustomerActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}