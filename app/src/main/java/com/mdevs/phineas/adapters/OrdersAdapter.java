package com.mdevs.phineas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdevs.phineas.R;
import com.mdevs.phineas.classes.Orders;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersVH> {
    Context context;
    ArrayList<Orders> ordersArrayList;

    public OrdersAdapter(Context context, ArrayList<Orders> ordersArrayList) {
        this.context = context;
        this.ordersArrayList = ordersArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public OrdersVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.orders_card_design, parent, false);
        return new OrdersVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrdersAdapter.OrdersVH holder, int position) {
        Orders orders = ordersArrayList.get(position);

        holder.orderNo.setText(orders.getOrderNo());
        holder.mpesaCode.setText(orders.getMpesaCode());
        holder.orderDate.setText(orders.getDateTime());
        holder.amountPaid.setText(orders.getAmountPaid());
        if (orders.getOrderStatus().equals("0")) {
            holder.orderStatus.setText("pending");
        } else {
            holder.orderStatus.setText("Approved");
        }

    }

    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }


    public class OrdersVH extends RecyclerView.ViewHolder {
        TextView orderNo, mpesaCode, orderDate, amountPaid, orderStatus;

        public OrdersVH(@NonNull @NotNull View itemView) {
            super(itemView);

            orderNo = itemView.findViewById(R.id.orderNo);
            mpesaCode = itemView.findViewById(R.id.mpesaCode);
            orderDate = itemView.findViewById(R.id.orderDate);
            amountPaid = itemView.findViewById(R.id.amountPaid);
            orderStatus = itemView.findViewById(R.id.orderStatus);
        }
    }
}
