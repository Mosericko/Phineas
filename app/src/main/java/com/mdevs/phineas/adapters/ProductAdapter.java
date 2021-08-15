package com.mdevs.phineas.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mdevs.phineas.R;
import com.mdevs.phineas.classes.CartDetails;
import com.mdevs.phineas.classes.GasDetails;
import com.mdevs.phineas.databases.DatabaseHandler;
import com.mdevs.phineas.interfaces.OnClickInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH> {
    Context context;
    ArrayList<GasDetails> gasList;
    OnClickInterface onClickInterface;

    public ProductAdapter(Context context, ArrayList<GasDetails> gasList) {
        this.context = context;
        this.gasList = gasList;
    }

    private void setOnItemClickListener(OnClickInterface onItemClickListener) {
        this.onClickInterface = onItemClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product_design, parent, false);
        return new ProductVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductAdapter.ProductVH holder, int position) {
        GasDetails gasD = gasList.get(position);
        holder.name.setText(gasD.getName());
        holder.tag.setText(gasD.getTag());
        holder.price.setText(gasD.getPrice());
        holder.quantity.setText(gasD.getQuantity());

        Glide.with(context)
                .load(gasD.getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return gasList.size();
    }

    public class ProductVH extends RecyclerView.ViewHolder {
        TextView name, tag, price, quantity;
        ImageView image;

        public ProductVH(@NonNull @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            tag = itemView.findViewById(R.id.tag);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            quantity = itemView.findViewById(R.id.quantity);

            itemView.setOnClickListener(v -> {
                showOptionsDialog();
            });
        }

        private void showOptionsDialog() {
            DatabaseHandler myDb = new DatabaseHandler(itemView.getContext());
            int position = getAdapterPosition();
            LinearLayout yes, no;
            final GasDetails cart = gasList.get(position);


            //get the values
            String id, name, price, tag, image, quantity;
            id = cart.getId();
            name = cart.getName();
            price = cart.getPrice();
            tag = cart.getTag();
            image = cart.getImage();
            quantity = cart.getQuantity();

            AlertDialog.Builder cartDialog = new AlertDialog.Builder(itemView.getContext());
            LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
            View view = layoutInflater.inflate(R.layout.cart_card, null);
            cartDialog.setView(view);


            yes = view.findViewById(R.id.yes);
            no = view.findViewById(R.id.no);

            final AlertDialog alertDialog = cartDialog.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            yes.setOnClickListener(v -> {
                CartDetails cartDetails = new CartDetails(id, tag, name, image, quantity, price);
                if (myDb.checkIfRowExists(cartDetails)) {
                    Toast.makeText(context, "Item already Added to Cart!", Toast.LENGTH_SHORT).show();
                } else {

                    myDb.addToCart(cartDetails);
                    Toast.makeText(context, "Successfully Added  to the Cart", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            no.setOnClickListener(v -> {
                alertDialog.dismiss();
            });


        }
    }
}
