package com.mdevs.phineas.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.phineas.R;
import com.mdevs.phineas.adapters.ProductAdapter;
import com.mdevs.phineas.classes.GasDetails;
import com.mdevs.phineas.helperclasses.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mdevs.phineas.customer.CustomerActivity.EXTRA_CATEGORY;

public class CategoryItems extends AppCompatActivity {

    RecyclerView catProducts;
    TextView name;
    String itemType;
    ProductAdapter catItemsAdapter;
    ArrayList<GasDetails> details = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);

        name = findViewById(R.id.itemCategory);
        catProducts = findViewById(R.id.cat);

        Intent intent = getIntent();
        itemType = intent.getStringExtra(EXTRA_CATEGORY);
        name.setText(itemType);

        fetchItems();
    }

    public void fetchItems() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        catProducts.setLayoutManager(gridLayoutManager);

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ROOT_URL, response -> {
            try {
                JSONArray j = new JSONArray(response);
                for (int i = 0; i < j.length(); i++) {
                    try {
                        JSONObject productDetails = j.getJSONObject(i);

                        String id = productDetails.getString("id");
                        String gas_tag = productDetails.getString("gas_tag");
                        String category = productDetails.getString("category");
                        String name = productDetails.getString("productName");
                        String image = productDetails.getString("image");
                        String quantity = productDetails.getString("quantity");
                        String price = productDetails.getString("price");


                        details.add(new GasDetails(id, gas_tag, category, name, image, quantity, price));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    catItemsAdapter = new ProductAdapter(CategoryItems.this, details);
                    catProducts.setAdapter(catItemsAdapter);
                    //catItemsAdapter.setItemClickListener(CategoryItems.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(CategoryItems.this, "Error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_category", itemType);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}