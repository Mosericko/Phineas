package com.mdevs.phineas.productmanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mdevs.phineas.R;
import com.mdevs.phineas.adapters.ArchivedAdapter;
import com.mdevs.phineas.classes.GasDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArchivedProducts extends AppCompatActivity {
    RecyclerView productList;
    ArrayList<GasDetails> gasDetails = new ArrayList<>();
    ArchivedAdapter archivedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_products);

        productList = findViewById(R.id.productList);
        listAllProducts();
    }

    private void listAllProducts() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        productList.setLayoutManager(gridLayoutManager);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest getJsonArray = new JsonArrayRequest(Request.Method.GET, "http://android.officialm-devs.com/api-call/Operations.php?apicall=fetchArchivedProducts", null, new Response.Listener<JSONArray>() {
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

                    archivedProducts = new ArchivedAdapter(ArchivedProducts.this, gasDetails);
                    productList.setAdapter(archivedProducts);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Throwable::printStackTrace);

        requestQueue.add(getJsonArray);
    }
}