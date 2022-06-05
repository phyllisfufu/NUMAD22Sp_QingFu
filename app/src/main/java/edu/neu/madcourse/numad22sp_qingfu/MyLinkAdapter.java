package edu.neu.madcourse.numad22sp_qingfu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyLinkAdapter extends RecyclerView.Adapter<MyLinkViewHolder> {

    private List<Item> items;
    private ItemClickListener listener;


    public MyLinkAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    public void setOnLinkClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyLinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new MyLinkViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MyLinkViewHolder holder, int position) {
        Item item = items.get(position);

        holder.name.setText(item.getName());
        holder.url.setText(item.getURL());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
