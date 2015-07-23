package one.com.pesosense.activity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import one.com.pesosense.R;
import one.com.pesosense.adapter.RecyclerAdapter_Otop2;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.Item;

/**
 * Created by Marc Lim gwaps on 7/14/15.
 */
public class OtopItemOnclick extends FragmentActivity {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    RecyclerView rv2;
    LinearLayoutManager llm;
    Context context;

    RecyclerAdapter_Otop2 adapter;
    ArrayList<Item> item;

    int id,ratings;
    String name,province, rating, price,product_image,description,brand,inventory;
    Bundle b;
    ImageView itemimage;
    TextView lblbrand, lblname, lblprice, lblquantity, lblprovince, lblrating, lbldesc;
    Button inquire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        b = getIntent().getExtras();
        name = b.getString("name");
        province = b.getString("province");
        price = b.getString("price");
        rating = b.getString("rating");
        brand = b.getString("brand");
        inventory = b.getString("inventory");
        product_image = b.getString("image");
        description = b.getString("description");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_otop_onclick);

        lblname = (TextView) findViewById(R.id.txtname);
        lblprovince = (TextView) findViewById(R.id.txtprovince);
        lblprice = (TextView) findViewById(R.id.txtprice);
        lblbrand = (TextView) findViewById(R.id.txtbrand);
        lblquantity = (TextView) findViewById(R.id.txtinventory);
        lblrating = (TextView) findViewById(R.id.txtrating);
        itemimage = (ImageView) findViewById(R.id.itemimage);
        inquire = (Button) findViewById(R.id.btninquire);
        lbldesc = (TextView) findViewById(R.id.txtdesc);

        inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OtopItemOnclick.this, InquireMessage.class));
            }
        });

        passValue();
        initValues();
    }


    public void passValue() {
        lblname.setText(name);
        lblprovince.setText(province);
        lblprice.setText(price);
        lblbrand.setText(brand);
        lblquantity.setText(inventory);
        lblrating.setText(rating);
        lbldesc.setText(description);
        Picasso.with(context).load(product_image).into(itemimage);


    }


    public void initValues() {

        dbHelper = DatabaseHelper.getInstance(getApplicationContext());

        item = new ArrayList<Item>();

        adapter = new RecyclerAdapter_Otop2(OtopItemOnclick.this, item);


        rv2 = (RecyclerView) findViewById(R.id.rv2);
        rv2.setAdapter(adapter);


        llm = new LinearLayoutManager(getApplicationContext());
        llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//      llm = new GridLayoutManager(this, 2);


        rv2.setLayoutManager(llm);


        readDB();


    }

    public void readDB() {


        db = dbHelper.getReadableDatabase();
        cursor = db.query("tbl_otop", null, null, null, null, null, null);

        Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                id = cursor.getInt(0);
                province = cursor.getString(1);
                name = cursor.getString(2);
                price = cursor.getString(3);
                product_image = cursor.getString(4);
                description = cursor.getString(5);
                ratings = cursor.getInt(6);
                brand = cursor.getString(7);
                inventory = cursor.getString(8);

                item.add(new Item(id, province, name, price, product_image, description, ratings, brand, inventory));


            }
        }
        adapter.notifyDataSetChanged();
    }


}
