package one.com.pesosense.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import one.com.pesosense.R;
import one.com.pesosense.activity.OtopItemOnclick;
import one.com.pesosense.model.Item;

/**
 * Created by Marc Lim on 7/15/15.
 */

public class RecyclerAdapter_Otop1 extends RecyclerView.Adapter<RecyclerAdapter_Otop1.MyViewHolder> {

    List<Item> data = Collections.emptyList();


    private LayoutInflater inflater;
    private Context context;


    public RecyclerAdapter_Otop1(Context context, List<Item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.custom_cardview_otop_listview, parent, false);
        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item current = data.get(position);

        holder.lblname.setText(current.getName());
        holder.lblprovince.setText(current.getProvince());
        holder.lblprice.setText(current.getPrice());
        holder.lblquantity.setText(String.valueOf(current.getInventory()));
        holder.lblbrand.setText(current.getBrand());
        holder.lblrating.setText(String.valueOf(current.getRatings()));

        Picasso.with(context).load(current.getProduct_image()).into(holder.itemimage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{



        ImageView itemimage;
        TextView lblbrand, lblname, lblprice, lblquantity, lblprovince, lblrating;
        Button inquire;
        CardView card;

        String name,province,price,description,rating,image,brand,inventory;

        Item item;


        public MyViewHolder(View v) {
            super(v);



            lblname = (TextView) v.findViewById(R.id.txtname);
            lblprovince = (TextView) v.findViewById(R.id.txtprovince);
            lblprice = (TextView) v.findViewById(R.id.txtprice);
            lblbrand = (TextView) v.findViewById(R.id.txtbrand);
            lblquantity = (TextView) v.findViewById(R.id.txtinventory);
            lblrating = (TextView) v.findViewById(R.id.txtinventory);
            itemimage = (ImageView) v.findViewById(R.id.itemimage);
            inquire = (Button) v.findViewById(R.id.btninquire);
            card = (CardView) v.findViewById(R.id.card_view);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    item = data.get(getPosition());
                    name = item.getName();
                    province = item.getProvince();
                    price = item.getPrice();
                    brand = item.getBrand();
                    inventory = item.getInventory();
                    rating = String.valueOf(item.getRatings());
                    image = item.getProduct_image();
                    description = item.getDescription();
                    Bundle b =  new Bundle();

                    b.putString("name",name);
                    b.putString("province", province);
                    b.putString("price",price);
                    b.putString("brand",brand);
                    b.putString("inventory",inventory);
                    b.putString("rating",rating);
                    b.putString("image",image);
                    b.putString("description",description);

                    Intent i = new Intent(context,OtopItemOnclick.class );
                    i.putExtras(b);
                    context.startActivity(i);


                }
            });
        }



    }

}