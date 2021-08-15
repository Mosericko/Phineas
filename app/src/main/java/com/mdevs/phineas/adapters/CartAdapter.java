package com.mdevs.phineas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mdevs.phineas.R;
import com.mdevs.phineas.classes.CartDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartVH> {
    Context context;
    ArrayList<CartDetails> cartList;

    public CartAdapter(Context context, ArrayList<CartDetails> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @NotNull
    @Override
    public CartVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product_design, parent, false);
        return new CartVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.CartVH holder, int position) {
        CartDetails cartDetails = cartList.get(position);
        holder.name.setText(cartDetails.getName());
        holder.tag.setText(cartDetails.getTag());
        holder.price.setText(cartDetails.getPrice());
        holder.quantity.setText(cartDetails.getQuantity());

        Glide.with(context)
                .load(cartDetails.getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class CartVH extends RecyclerView.ViewHolder {
        TextView name, tag, price, quantity;
        ImageView image;

        public CartVH(@NonNull @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            tag = itemView.findViewById(R.id.tag);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}






















