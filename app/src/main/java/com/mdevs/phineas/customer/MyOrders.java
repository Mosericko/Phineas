package com.mdevs.phineas.customer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.phineas.R;
import com.mdevs.phineas.adapters.OrdersAdapter;
import com.mdevs.phineas.classes.Orders;
import com.mdevs.phineas.databases.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrders extends AppCompatActivity {
    RecyclerView ordersHistoryRV;
    OrdersAdapter orderHistoryAdapter;
    ArrayList<Orders> myOrders = new ArrayList<>();
    String id = PrefManager.getInstance(this).UserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        ordersHistoryRV = findViewById(R.id.ordersRecycler);
        getAllOrders();
    }

    private void getAllOrders() {
        ordersHistoryRV.setHasFixedSize(true);
        ordersHistoryRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://android.officialm-devs.com/api-call/Operations.php?apicall=orders", response -> {
            try {
                JSONArray j = new JSONArray(response);
                for (int i = 0; i < j.length(); i++) {
                    try {
                        JSONObject orders = j.getJSONObject(i);

                        String orderNum = orders.getString("order_num");
                        String mpesa_code = orders.getString("mpesa_code");
                        String orderDate = orders.getString("order_date");
                        String amountPaid = orders.getString("total_price");
                        String orderStatus = orders.getString("status");

                        myOrders.add(new Orders(orderNum, mpesa_code, orderDate, amountPaid, orderStatus));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    orderHistoryAdapter = new OrdersAdapter(MyOrders.this, myOrders);
                    ordersHistoryRV.setAdapter(orderHistoryAdapter);
                    //orderHistoryAdapter.setOnItemClickListener(OrderHistory.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this, "Error500", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}