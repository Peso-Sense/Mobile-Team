package one.com.pesosense.activity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import one.com.pesosense.R;
import one.com.pesosense.adapter.RecyclerAdapterOnlineShop;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.Item_shop;

/**
 * Created by Marc Lim gwaps on 7/14/15.
 */
public class OnlineShopItemOnclick extends FragmentActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Context context;

    RecyclerAdapterOnlineShop adapter;
    ArrayList<Item_shop> itemshop;


    String name,price,product_image,description,brand,quantity;
    Bundle b;
    ImageView itemimage,itemimage2;
    TextView lblbrand, lblname, lblprice, lblquantity, lbldesc;
    Button inquire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        b = getIntent().getExtras();
        name = b.getString("name");
        price = b.getString("price");
        brand = b.getString("brand");
        quantity = b.getString("quantity");
        product_image = b.getString("image");
        description = b.getString("description");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_onlineshop_onclick);

        lblname = (TextView) findViewById(R.id.txtname);
        lblprice = (TextView) findViewById(R.id.txtprice);
        lblbrand = (TextView) findViewById(R.id.txtbrand);
        lblquantity = (TextView) findViewById(R.id.txtquantity);
        itemimage = (ImageView) findViewById(R.id.itemimage);
        inquire = (Button) findViewById(R.id.btninquire);
        lbldesc = (TextView) findViewById(R.id.txtdesc);
        itemimage2 = (ImageView) findViewById(R.id.productimage);
        inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OnlineShopItemOnclick.this, InquireMessage.class));
            }
        });

        passValue();

    }


    public void passValue() {
        lblname.setText(name);
        lblprice.setText(price);
        lblbrand.setText(brand);
        lblquantity.setText(quantity);
        lbldesc.setText(description);
        Picasso.with(context).load("http://search.onesupershop.com/api/photos/"+product_image).into(itemimage);
        Picasso.with(context).load("http://search.onesupershop.com/api/photos/"+product_image).into(itemimage2);

    }






}
