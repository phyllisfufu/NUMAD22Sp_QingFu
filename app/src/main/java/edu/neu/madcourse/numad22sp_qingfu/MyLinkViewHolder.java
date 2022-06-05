package edu.neu.madcourse.numad22sp_qingfu;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyLinkViewHolder extends RecyclerView.ViewHolder{
    public TextView name;
    public TextView url;


    public MyLinkViewHolder(View itemView, final ItemClickListener listener) {
        super(itemView);
        name = itemView.findViewById(R.id.name_text_view);
        url = itemView.findViewById(R.id.url_text_view);

        itemView.setOnClickListener( v -> {
            int position = getLayoutPosition();
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position);
            }
        });
    }
}
