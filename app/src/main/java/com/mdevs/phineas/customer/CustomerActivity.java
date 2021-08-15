package com.mdevs.phineas.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdevs.phineas.R;
import com.mdevs.phineas.adapters.CategoryAdapter;
import com.mdevs.phineas.adapters.ProductAdapter;
import com.mdevs.phineas.classes.CategoryDetails;
import com.mdevs.phineas.classes.GasDetails;
import com.mdevs.phineas.helperclasses.URLs;
import com.mdevs.phineas.loginregistration.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity implements CategoryAdapter.ItemClickListener {
    RelativeLayout profile;
    RecyclerView categoryRV, products;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryDetails> categList = new ArrayList<>();
    RecyclerView productList;
    ArrayList<GasDetails> gasDetails = new ArrayList<>();
    ProductAdapter productsAdapter;
    FloatingActionButton cart, menu;
    public static final String EXTRA_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        profile = findViewById(R.id.profile);
        categoryRV = findViewById(R.id.categoryRV);
        productList = findViewById(R.id.productsList);
        cart = findViewById(R.id.cartFab);
        menu = findViewById(R.id.menuOptions);

        profile.setOnClickListener(v -> startActivity(new Intent(this, UserProfile.class)));
        cart.setOnClickListener(v -> {
            startActivity(new Intent(this, Cart.class));
        });

        menu.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuOptions.class));
        });
        listCategories();
        listAllProducts();
    }

    private void listCategories() {

        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // add the static files into the arrayList
        categList.add(new CategoryDetails(1, "Pro Gas", R.drawable.ic_p));
        categList.add(new CategoryDetails(2, "K - Gas", R.drawable.ic_k));
        categList.add(new CategoryDetails(3, "Hashi Gas", R.drawable.ic_h));
        categList.add(new CategoryDetails(4, "OilLibya Gas", R.drawable.ic_o));
        categList.add(new CategoryDetails(5, "Total Gas", R.drawable.ic_t));


        categoryAdapter = new CategoryAdapter(this, categList);
        categoryRV.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListener(CustomerActivity.this);
    }

    private void listAllProducts() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        productList.setLayoutManager(gridLayoutManager);
        productList.invalidateItemDecorations();

        //volley library
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

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

                    productsAdapter = new ProductAdapter(CustomerActivity.this, gasDetails);
                    productList.setAdapter(productsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Throwable::printStackTrace);

        requestQueue.add(getJsonArray);
    }

    @Override
    public void onItemCLick(int position) {
        Intent intent = new Intent(this, CategoryItems.class);
        CategoryDetails clickedItem = categList.get(position);

        intent.putExtra(EXTRA_CATEGORY, clickedItem.getName());
        startActivity(intent);
    }
}