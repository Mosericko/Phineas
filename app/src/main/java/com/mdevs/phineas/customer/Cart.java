package com.mdevs.phineas.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mdevs.phineas.R;
import com.mdevs.phineas.adapters.AddressAdapter;
import com.mdevs.phineas.adapters.CartAdapter;
import com.mdevs.phineas.classes.Address;
import com.mdevs.phineas.classes.CartDetails;
import com.mdevs.phineas.classes.User;
import com.mdevs.phineas.databases.DatabaseHandler;
import com.mdevs.phineas.databases.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView, address;
    RelativeLayout hiddenLay, hiddenLay2;
    DatabaseHandler myDb;
    TextView totalPrice;
    TextView checkOut;
    TextView clearAll;
    CartAdapter cartAdapter;
    ArrayList<CartDetails> cartArray = new ArrayList<>();
    String totalIn, orderNumber, json;
    Gson gson;
    //address
    ArrayList<Address> details = new ArrayList<>();
    AddressAdapter addressAdapter;
    String id = PrefManager.getInstance(this).UserID();
    ArrayList<CartDetails> totalCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        myDb = new DatabaseHandler(this);
        hiddenLay = findViewById(R.id.hiddenLayout);
        hiddenLay2 = findViewById(R.id.hiddenLayout2);
        recyclerView = findViewById(R.id.cartRecycler);
        totalPrice = findViewById(R.id.totalPrice);
        checkOut = findViewById(R.id.checkOut);
        clearAll = findViewById(R.id.clearItems);
        address = findViewById(R.id.locationDetails);

        clearAll.setOnClickListener(v -> {
            myDb.deleteCartItems();
            clearAllItems();
            Toast.makeText(Cart.this, "Items Cleared Successfully!", Toast.LENGTH_SHORT).show();
        });




        loadCart();
        grandTotal();
        listAddress();
    }

    public void loadCart() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        cartArray.addAll(myDb.getCartDetails());

        cartAdapter = new CartAdapter(this, cartArray);
        recyclerView.setAdapter(cartAdapter);

        if (cartArray.isEmpty()) {
            hiddenLay.setVisibility(View.GONE);
            hiddenLay2.setVisibility(View.GONE);
        } else {
            hiddenLay.setVisibility(View.VISIBLE);
            hiddenLay2.setVisibility(View.VISIBLE);
        }


    }

    private void grandTotal() {
        int position;
        int priceTotal = 0;
        CartDetails cartDetails;


        for (position = 0; position < cartArray.size(); position++) {
            cartDetails = cartArray.get(position);
            priceTotal = priceTotal + (Integer.parseInt(cartDetails.getPrice()));

            totalPrice.setText(String.valueOf(priceTotal));

            int finalPriceTotal = priceTotal;

            checkOut.setOnClickListener(v -> {
                gson = new Gson();
                totalCheckOut = myDb.getCartDetails();

                json = gson.toJson(totalCheckOut);
                Log.d("TAG", "sendOrderToDb: " + json);
                orderNumber = setOrderNumber();
                Log.d("TAG", "oNumber: " + orderNumber);

                Intent orders = new Intent(Cart.this, CheckOut.class);
                orders.putExtra("Price", String.valueOf(finalPriceTotal));
                orders.putExtra("ID", id);
                orders.putExtra("ORDER_NUM", orderNumber);
                orders.putExtra("TOTAL_PRICE", totalIn);
                orders.putExtra("JSON", json);

                startActivity(orders);
            });

        }
    }

    private void clearAllItems() {

        int size = cartArray.size();
        if (size > 0) {
            cartArray.subList(0, size).clear();

            cartAdapter.notifyItemRangeRemoved(0, size);
            hiddenLay.setVisibility(View.GONE);
            hiddenLay2.setVisibility(View.GONE);


        }
    }

    private void listAddress() {
        User user = myDb.getUser(id);

        address.setHasFixedSize(true);
        address.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/phineasgas/android/location?phone=" + user.getPhone(), null, response -> {

            try {
                for (int i = 0; i < response.length(); i++) {

                    JSONObject locationDetails = response.getJSONObject(i);

                    String streetName = locationDetails.getString("street");
                    String buildingName = locationDetails.getString("building");
                    String phone = locationDetails.getString("phone");

                    details.add(new Address(streetName, buildingName, phone));
                }

                addressAdapter = new AddressAdapter(Cart.this, details);
                address.setAdapter(addressAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);

    }

    private String setOrderNumber() {
        char[] myChars = "1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            char c1 = myChars[random.nextInt(myChars.length)];
            stringBuilder.append(c1);
        }

        String randomString = stringBuilder.toString();

        return ("PurNo." + randomString);
    }
}