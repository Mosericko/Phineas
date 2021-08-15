package com.mdevs.phineas.financemanager;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingOrders extends AppCompatActivity {
    RecyclerView ordersHistoryRV;
    OrdersAdapter orderHistoryAdapter;
    ArrayList<Orders> myOrders = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        ordersHistoryRV = findViewById(R.id.ordersRecycler);

    }


}