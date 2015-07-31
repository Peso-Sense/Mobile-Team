package one.com.pesosense.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import one.com.pesosense.R;
import one.com.pesosense.model.Item;

/**
 * Created by Marc Lim gwaps on 7/15/15.
 */
public class RecyclerAdapter_Otop2 extends RecyclerView.Adapter<RecyclerAdapter_Otop2.MyViewHolder> {

    List<Item> data = Collections.emptyList();


    private LayoutInflater inflater;
    private Context context;


    public RecyclerAdapter_Otop2(Context context, List<Item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.custom_cardview_otop_images, parent, false);
        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item current = data.get(position);


        Picasso.with(context).load(current.getProduct_image()).into(holder.itemimage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{



        ImageView itemimage;


        public MyViewHolder(View v) {
            super(v);



            itemimage = (ImageView) v.findViewById(R.id.itemimage);




        }



    }

}