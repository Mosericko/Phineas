package com.mdevs.phineas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdevs.phineas.R;
import com.mdevs.phineas.classes.Address;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressVH> {
    Context context;
    ArrayList<Address> addressArrayList;
    CardClickListener cardClickListener;

    public AddressAdapter(Context context, ArrayList<Address> addressArrayList) {
        this.context = context;
        this.addressArrayList = addressArrayList;
    }

    public interface CardClickListener {
        void onCardClick(int cardPosition);
    }

    public void setCardListener(CardClickListener cardClickListener) {
        this.cardClickListener = cardClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public AddressVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.address_card, parent, false);
        return new AddressVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddressAdapter.AddressVH holder, int position) {

        Address address = addressArrayList.get(position);
        holder.streetName.setText(address.getStreetName());
        holder.building.setText(address.getBuilding());
        holder.phone.setText(address.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }


    public class AddressVH extends RecyclerView.ViewHolder {
        TextView streetName, building, phone, edit;


        public AddressVH(@NonNull @NotNull View itemView) {
            super(itemView);

            streetName = itemView.findViewById(R.id.street);
            building = itemView.findViewById(R.id.landmark);
            phone = itemView.findViewById(R.id.userPhone);


        }
    }
}
