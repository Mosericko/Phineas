package com.mdevs.phineas.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mdevs.phineas.R;
import com.mdevs.phineas.classes.GasDetails;
import com.mdevs.phineas.helperclasses.RequestHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsVH> {
    Context context;
    ArrayList<GasDetails> gasDetails;

    public ProductsAdapter(Context context, ArrayList<GasDetails> gasDetails) {
        this.context = context;
        this.gasDetails = gasDetails;
    }

    @NonNull
    @NotNull
    @Override
    public ProductsVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product_card, parent, false);
        return new ProductsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductsAdapter.ProductsVH holder, int position) {
        GasDetails gasD = gasDetails.get(position);
        holder.name.setText(gasD.getName());
        holder.tag.setText(gasD.getTag());
        holder.price.setText(gasD.getPrice());

        Glide.with(context)
                .load(gasD.getImage())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return gasDetails.size();
    }

    public class ProductsVH extends RecyclerView.ViewHolder {
        TextView name, tag, price;
        ImageView image;

        public ProductsVH(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            tag = itemView.findViewById(R.id.tag);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(v -> {
                openDialog();
            });
        }

        private void openDialog() {
            AlertDialog.Builder archiveDialog = new AlertDialog.Builder(itemView.getContext());
            archiveDialog.setTitle("Alert")
                    .setMessage("Proceed with Archive Action?")
                    .setPositiveButton("YES", ((dialog, which) -> {
                        archiveId();
                    }))
                    .setNegativeButton("NO", (dialog, which) -> {
                        Toast.makeText(context, "Not Archived", Toast.LENGTH_SHORT).show();
                    });
            archiveDialog.show();
        }

        private void archiveId() {
            int i;
            String id;
            i = getAdapterPosition();
            GasDetails gasDetails1 = gasDetails.get(i);
            id = gasDetails1.getId();

            //execute async task
            ArchiveAsync archiveAsync = new ArchiveAsync(id);
            archiveAsync.execute();
            gasDetails.remove(i);
            notifyItemRemoved(i);

        }

        class ArchiveAsync extends AsyncTask<Void, Void, String> {
            String id;

            public ArchiveAsync(String id) {
                this.id = id;
            }


            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> param = new HashMap<>();
                param.put("id", id);

                return requestHandler.sendPostRequest("http://android.officialm-devs.com/api-call/Operations.php?apicall=archive", param);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {

                    JSONObject obj = new JSONObject(s);

                    Toast.makeText(itemView.getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
