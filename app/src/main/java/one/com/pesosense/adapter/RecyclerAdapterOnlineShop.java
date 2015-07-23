package one.com.pesosense.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import one.com.pesosense.R;
import one.com.pesosense.model.Item_shop;

public class RecyclerAdapterOnlineShop extends RecyclerView.Adapter<RecyclerAdapterOnlineShop.MyViewHolder> {

    List<Item_shop> data = Collections.emptyList();


    private LayoutInflater inflater;
    private Context context;


    public RecyclerAdapterOnlineShop(Context context, List<Item_shop> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.custom_cardview_bodl, parent, false);
        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item_shop current = data.get(position);

        holder.lblname.setText(current.getProduct_name());
        holder.lbldesc.setText(current.getProduct_desc());
        holder.lblquantity.setText("Quantity: " + String.valueOf(current.getProduct_quantity()));
        holder.lblprice.setText(String.valueOf(current.getProduct_price()));

        Picasso.with(context).load(current.getProduct_image()).into(holder.itemimage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{



        ImageView itemimage;
        TextView lbldesc, lblname, lblprice, lblquantity;


        public MyViewHolder(View v) {
            super(v);



            lblname = (TextView) v.findViewById(R.id.txtname);
            lbldesc = (TextView) v.findViewById(R.id.txtdescription);
            lblprice = (TextView) v.findViewById(R.id.txtprice);
            lblquantity = (TextView) v.findViewById(R.id.txtquantity);
            itemimage = (ImageView) v.findViewById(R.id.itemimage);




        }



    }

}