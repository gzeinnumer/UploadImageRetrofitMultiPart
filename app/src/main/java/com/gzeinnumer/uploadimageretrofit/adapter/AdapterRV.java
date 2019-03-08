package com.gzeinnumer.uploadimageretrofit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzeinnumer.uploadimageretrofit.Constants;
import com.gzeinnumer.uploadimageretrofit.R;
import com.gzeinnumer.uploadimageretrofit.model.ImagesItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//todo 28.
public class AdapterRV extends RecyclerView.Adapter<AdapterRV.MyHolder> {

    private Context mContex;
    private List<ImagesItem> mList = new ArrayList();

    public AdapterRV(Context mContex, List<ImagesItem> mList) {
        this.mContex = mContex;
        this.mList = mList;
    }

    //todo 32.
    private onItemClick click;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //todo 29.
        View view = LayoutInflater.from(mContex).inflate(R.layout.item, viewGroup, false);
        //todo 31.
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        myHolder.imgNama.setText(mList.get(i).getName());
        Picasso.get().load(Constants.BASE_IMAGE_URL+mList.get(i).getUrl()).into(myHolder.imgItem);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView imgNama;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            imgNama = itemView.findViewById(R.id.imgNama);
        }
    }

    //todo 33.
    public interface onItemClick {
        void onItemClick(int position);
    }

    //todo 34.
    public void setOnClickListener2(onItemClick onClick) {
        click = onClick;
    }
}
