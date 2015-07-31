package one.com.pesosense.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.activity.ShopItemView;
import one.com.pesosense.model.ShopItem;

/**
 * Created by mykelneds on 7/25/15.
 */
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {

    private final String TAG = "SHOP";

    List<ShopItem> data = Collections.emptyList();
    LayoutInflater inflater;
    Context context;

    public ShopAdapter(Context context, List<ShopItem> data){
        this.context = context;
        inflater = LayoutInflater.from(context);

        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.shop_item_grid, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ShopItem current = data.get(position);

        Picasso.with(context).load(current.getImage()).into(holder.imgProduct);

        holder.lblProductName.setText(current.getName());
        holder.lblProductPrice.setText(current.getPrice());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardItem;
        ImageView imgProduct;

        TextView lblProductName;
        TextView lblProductPrice;

        ShopItem item;

        Intent intent = null;
        Bundle extras;

        public MyViewHolder(View v) {
            super(v);

            cardItem = (CardView) v.findViewById(R.id.cardItem);
            cardItem.setOnClickListener(this);

            imgProduct = (ImageView) v.findViewById(R.id.imgProduct);

            lblProductName = (TextView) v.findViewById(R.id.lblProductName);
            lblProductName.setTypeface(UtilsApp.opensansNormal());

            lblProductPrice = (TextView) v.findViewById(R.id.lblProductPrice);
            lblProductPrice.setTypeface(UtilsApp.opensansNormal());


        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cardItem) {
                Log.d(TAG, "Fetch item information");
                item = data.get(getPosition());
                itemInformation(item);
            }
        }

        private void itemInformation(ShopItem item) {

            int id = item.getId();
            int price = item.getPrice();
            int rating = item.getRating();
            String name = item.getName();
            String description = item.getDescription();
            String image = item.getImage();

            extras = new Bundle();
            extras.putInt("id", id);
            extras.putInt("price", price);
            extras.putInt("rating", rating);
            extras.putString("name", name);
            extras.putString("description", description);
            extras.putString("image", image);

            intent = new Intent(context, ShopItemView.class);
            intent.putExtras(extras);

            context.startActivity(intent);


        }
    }
}
