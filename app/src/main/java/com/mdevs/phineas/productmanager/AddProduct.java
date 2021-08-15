package com.mdevs.phineas.productmanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mdevs.phineas.R;
import com.mdevs.phineas.helperclasses.RequestHandler;
import com.mdevs.phineas.helperclasses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddProduct extends AppCompatActivity {
    EditText name, price;
    AutoCompleteTextView tag, quantity, gasType;
    ImageView selectPhoto;
    TextView save;
    Bitmap bitmap;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String imageString;
    private final int galleryReqCode = 1;
    private final int cameraReqCode = 2;
    private Uri fileTrace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        tag = findViewById(R.id.tag);
        quantity = findViewById(R.id.quantity);
        gasType = findViewById(R.id.gasType);
        selectPhoto = findViewById(R.id.choose_photo);
        save = findViewById(R.id.save);

        byteArrayOutputStream = new ByteArrayOutputStream();

        selectPhoto.setOnClickListener(v -> showOptionsDialog());


        ArrayList<String> tagList = new ArrayList<>();
        tagList.add("Full Set");
        tagList.add("Component");
        tagList.add("Refill");

        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(this, R.layout.feedback_menu_design, tagList);
        tag.setAdapter(tagAdapter);

        //quantity
        ArrayList<String> quanta = new ArrayList<>();
        quanta.add("6 kg");
        quanta.add("13 kg");

        ArrayAdapter<String> qAdapter = new ArrayAdapter<>(this, R.layout.feedback_menu_design, quanta);
        quantity.setAdapter(qAdapter);

        //gas categories
        ArrayList<String> gasT = new ArrayList<>();
        gasT.add("Pro Gas");
        gasT.add("K - Gas");
        gasT.add("Hashi Gas");
        gasT.add("OilLibya Gas");
        gasT.add("Total Gas");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.feedback_menu_design, gasT);
        gasType.setAdapter(categoryAdapter);

        save.setOnClickListener(v -> {
            sendDetailsToDb();
        });
    }

    private void showOptionsDialog() {

        AlertDialog.Builder optionsDialog = new AlertDialog.Builder(this);
        String[] options = {"Capture Image", "Choose Existing Image"};

        optionsDialog.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    takePhoto();
                    break;

                case 1:
                    choosePhoto();
                    break;
            }
        });

        optionsDialog.show();
    }

    private void choosePhoto() {
        Intent gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(gallery, galleryReqCode);
    }

    private void takePhoto() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, cameraReqCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryReqCode && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            fileTrace = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);

                Glide.with(this)
                        .load(bitmap)
                        .into(selectPhoto);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == cameraReqCode && resultCode == RESULT_OK && data != null && data.getData() != null) {

            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");

            Glide.with(this)
                    .load(bitmap)
                    .into(selectPhoto);


        }
    }

    private void sendDetailsToDb() {
        String prodName, prodTag, prodQuantity, prodPrice, prodType;

        prodName = name.getText().toString().trim();
        prodTag = tag.getText().toString().trim();
        prodQuantity = quantity.getText().toString().trim();
        prodPrice = price.getText().toString().trim();
        prodType = gasType.getText().toString().trim();

        if (fileTrace == null) {
            Toast.makeText(this, "Choose a Photo!", Toast.LENGTH_SHORT).show();
            return;
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d("TAG", "sendDetailsToDb: " + imageString);

        //validations
        if (TextUtils.isEmpty(prodName)) {
            name.setError("Please Enter Product name!");
            name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(prodPrice)) {
            price.setError("Please Enter Product Price!");
            price.requestFocus();
            return;
        }

        SendDetailsAsync sendDetails = new SendDetailsAsync(prodTag, prodType, prodName, imageString, prodQuantity, prodPrice);
        sendDetails.execute();

    }

    public class SendDetailsAsync extends AsyncTask<Void, Void, String> {

        String tag, type, name, image, quantity, price;

        public SendDetailsAsync(String tag, String type, String name, String image, String quantity, String price) {
            this.tag = tag;
            this.type = type;
            this.name = name;
            this.image = image;
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            HashMap<String, String> params = new HashMap<>();
            params.put("gas_tag", tag);
            params.put("category", type);
            params.put("image", image);
            params.put("productName", name);
            params.put("quantity", quantity);
            params.put("price", price);

            return requestHandler.sendPostRequest(URLs.URL_ADD_PRODUCTS, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject obj = new JSONObject(s);

                Toast.makeText(AddProduct.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddProduct.this, ProductActivity.class));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}