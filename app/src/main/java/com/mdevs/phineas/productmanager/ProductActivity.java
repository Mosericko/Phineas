package com.mdevs.phineas.productmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdevs.phineas.R;
import com.mdevs.phineas.adapters.ProductsAdapter;
import com.mdevs.phineas.classes.GasDetails;
import com.mdevs.phineas.helperclasses.URLs;
import com.mdevs.phineas.loginregistration.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    RelativeLayout profile;
    FloatingActionButton fab, archiveFab;
    RecyclerView productList;
    ArrayList<GasDetails> gasDetails = new ArrayList<>();
    ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        profile = findViewById(R.id.profile);
        fab = findViewById(R.id.addFab);
        archiveFab = findViewById(R.id.archiveFab);
        productList = findViewById(R.id.productList);


        profile.setOnClickListener(v -> startActivity(new Intent(this, UserProfile.class)));
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddProduct.class)));
        archiveFab.setOnClickListener(v -> startActivity(new Intent(this, ArchivedProducts.class)));

        listAllProducts();
    }

    private void listAllProducts() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        productList.setLayoutManager(gridLayoutManager);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest getJsonArray = new JsonArrayRequest(Request.Method.GET, URLs.URL_ADD_PRODUCTS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    //loop through the event details array
                    for (int i = 0; i < response.length(); i++) {

                        //get the current Json Object
                        JSONObject productDetails = response.getJSONObject(i);

                        String id = productDetails.getString("id");
                        String gas_tag = productDetails.getString("gas_tag");
                        String category = productDetails.getString("category");
                        String name = productDetails.getString("productName");
                        String image = productDetails.getString("image");
                        String quantity = productDetails.getString("quantity");
                        String price = productDetails.getString("price");


                        gasDetails.add(new GasDetails(id, gas_tag, category, name, image, quantity, price));
                    }

                    productsAdapter = new ProductsAdapter(ProductActivity.this, gasDetails);
                    productList.setAdapter(productsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Throwable::printStackTrace);

        requestQueue.add(getJsonArray);
    }
}
