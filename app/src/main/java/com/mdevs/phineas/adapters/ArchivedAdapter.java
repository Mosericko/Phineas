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
import com.mdevs.phineas.classes.GasDetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ArchivedAdapter extends RecyclerView.Adapter<ArchivedAdapter.ArchivedVH> {
    Context context;
    ArrayList<GasDetails> gasDetails;

    public ArchivedAdapter(Context context, ArrayList<GasDetails> gasDetails) {
        this.context = context;
        this.gasDetails = gasDetails;
    }

    @NonNull
    @NotNull
    @Override
    public ArchivedVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.product_card, parent, false);
        return new ArchivedVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ArchivedAdapter.ArchivedVH holder, int position) {
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


    public class ArchivedVH extends RecyclerView.ViewHolder {
        TextView name, tag, price;
        ImageView image;

        public ArchivedVH(@NonNull @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            tag = itemView.findViewById(R.id.tag);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);

        }
    }
}
