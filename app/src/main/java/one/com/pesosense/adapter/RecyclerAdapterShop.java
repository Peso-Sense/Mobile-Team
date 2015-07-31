package one.com.pesosense.adapter;


import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import one.com.pesosense.R;
import one.com.pesosense.activity.OtopItemOnclick;
import one.com.pesosense.model.Item;

public class RecyclerAdapterShop extends RecyclerView.Adapter<RecyclerAdapterShop.MyViewHolder> {

    List<Item> data = Collections.emptyList();


    private LayoutInflater inflater;
    private Context context;
    public Dialog dialog;

    public RecyclerAdapterShop(Context context, List<Item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.custom_cardview_otop_gridview, parent, false);
        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item current = data.get(position);

        holder.lblname.setText(current.getName());
        holder.lblprovince.setText(current.getProvince());
        holder.lbldescription.setText(current.getDescription());
        holder.lblprice.setText(current.getPrice());
        holder.lblrating.setText("Rating: " + String.valueOf(current.getRatings()));

        Picasso.with(context).load(current.getProduct_image()).into(holder.itemimage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

//        Button btnUpdate;
//        Button btnDelete;

        ImageView itemimage;
        TextView lblprovince, lblname, lblprice, lblrating, lbldescription;
        CardView card;

        // insert hjere
        String name,province,price,description,rating,image,brand,inventory;

        Item item;

        public MyViewHolder(View v) {
            super(v);

//            btnUpdate = (Button) v.findViewById(R.id.btnUpdate);
//            btnDelete = (Button) v.findViewById(R.id.btnDelete);

            lblname = (TextView) v.findViewById(R.id.txtname);
            lblprovince = (TextView) v.findViewById(R.id.txtprovince);
            lblprice = (TextView) v.findViewById(R.id.txtprice);
            lbldescription = (TextView) v.findViewById(R.id.txtdescription);
            lblrating = (TextView) v.findViewById(R.id.txtrating);
            itemimage = (ImageView) v.findViewById(R.id.itemimage);
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




//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.btnDelete) {
//                removeView(getPosition(), data.get(getPosition()).getId());
//            }
//
//            if (v.getId() == R.id.btnUpdate) {
//                updateView(getPosition(), data.get(getPosition()).getId());
//            }
//        }
//    }


//    public void removeView(int position, int id) {
//        deleteDB(id);
//        data.remove(position);
//        notifyItemRemoved(position);
//
//
//    }
//
//    public void deleteDB(int id) {
//        DatabaseHelper dbHelper;
//        SQLiteDatabase db;
//
//        dbHelper = DatabaseHelper.getInstance(context);
//        db = dbHelper.getWritableDatabase();
//
//        db.delete("tbl_sample", "id = " + id, null);
//        db.close();
//
//    }

//    public void updateView(final int position, final int id) {
//
//
//        final EditText txtNewMessage;
//        Button btnNewUpdate;
//
//        dialog = new Dialog(context);
//
//        dialog.setContentView(R.layout.custom_text);
//        dialog.show();
//
//        txtNewMessage = (EditText) dialog.findViewById(R.id.txtNewMessage);
//
//        btnNewUpdate = (Button) dialog.findViewById(R.id.btnNewUpdate);
//        btnNewUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateDB(position, id, txtNewMessage.getText().toString());
//                dialog.dismiss();
//            }
//        });
//
//    }

//    public void updateDB(int position, int id, String message) {
//        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues values;
//
//        db = dbHelper.getWritableDatabase();
//
//        values = new ContentValues();
//        values.put("text", message);
//
//        db.update("tbl_sample", values, "id = " + id, null);
//        db.close();
//
//        data.get(position).setName(message);
//        notifyItemChanged(position);
//    }


    }

}