package one.com.pesosense.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import one.com.pesosense.R;
import one.com.pesosense.activity.OnlineShopItemOnclick;
import one.com.pesosense.model.Item_shop;


/**
 * Created by Marc Lim on 7/15/15.
 */

public class RecyclerAdapter_bodl_listview extends RecyclerView.Adapter<RecyclerAdapter_bodl_listview.MyViewHolder> {

    List<Item_shop> data = Collections.emptyList();


    private LayoutInflater inflater;
    private Context context;


    public RecyclerAdapter_bodl_listview(Context context, List<Item_shop> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.custom_cardview_bodl_listview, parent, false);
        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item_shop current = data.get(position);

        holder.lblname.setText(current.getProd_name());
        holder.lblprice.setText(current.getProd_price());
        holder.lblquantity.setText(current.getProd_quantity());
        holder.lblbrand.setText(current.getProd_brand());

        Picasso.with(context).load("http://search.onesupershop.com/api/photos/"+current.getProd_image()).into(holder.itemimage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{



        ImageView itemimage;
        TextView lblbrand, lblname, lblprice, lblquantity, lbldescription;
        CardView card;

        String name,price,description,quantity,image,brand;

        Item_shop item;


        public MyViewHolder(View v) {
            super(v);



            lblname = (TextView) v.findViewById(R.id.txtname);
            lblprice = (TextView) v.findViewById(R.id.txtprice);
            lblbrand = (TextView) v.findViewById(R.id.txtbrand);
            lblquantity = (TextView) v.findViewById(R.id.txtinventory);
            itemimage = (ImageView) v.findViewById(R.id.itemimage);

            card = (CardView) v.findViewById(R.id.card_view);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    item = data.get(getPosition());
                    name = item.getProd_name();
                    price = item.getProd_price();
                    brand = item.getProd_brand();
                    quantity = item.getProd_quantity();
                    image = item.getProd_image();
                    description = item.getProd_desc();
                    Bundle b =  new Bundle();

                    b.putString("name",name);
                    b.putString("price",price);
                    b.putString("brand",brand);
                    b.putString("quantity",quantity);
                    b.putString("image",image);
                    b.putString("description",description);

                    Intent i = new Intent(context,OnlineShopItemOnclick.class );
                    i.putExtras(b);
                    context.startActivity(i);


                }
            });
        }



    }

}