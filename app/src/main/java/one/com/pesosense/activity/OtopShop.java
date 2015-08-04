package one.com.pesosense.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.adapter.RecyclerAdapterOnlineShop;
import one.com.pesosense.adapter.RecyclerAdapter_bodl_listview;
import one.com.pesosense.download.APIHandler;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.Item_shop;

/**
 * Created by mobile2 on 8/3/15.
 */
public class OtopShop extends Activity {


    Cursor cursor;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<Item_shop> itemshop;
    RecyclerAdapterOnlineShop adapter;
    RecyclerAdapter_bodl_listview adapter2;
    RecyclerView rv;
    LinearLayoutManager llm, llm2;

    int prod_id, user_id;
    String prod_name, prod_desc, prod_image, prod_price, prod_brand, prod_quantity;

    APIHandler apiHandler;
    private String url = "http://search.onesupershop.com/api/items";


    String token ;
    Map<String, String> params;
    ImageButton list, grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_otop);
        UtilsApp.initSharedPreferences(getApplicationContext());
        token = UtilsApp.getString("access_token");
        apiHandler = new APIHandler();
        params = new HashMap<String, String>();
        list = (ImageButton) findViewById(R.id.btnlist);
        grid = (ImageButton) findViewById(R.id.btngrid);

        initValues();
    }

    public void initValues() {

        dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        rv = (RecyclerView) findViewById(R.id.rv);
        itemshop = new ArrayList<Item_shop>();

        adapter = new RecyclerAdapterOnlineShop(OtopShop.this, itemshop);
        adapter2 = new RecyclerAdapter_bodl_listview(OtopShop.this, itemshop);

        llm = new LinearLayoutManager(getApplicationContext());
        llm = new GridLayoutManager(this, 2);

        llm2 = new LinearLayoutManager(getApplicationContext());

        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rv.setLayoutManager(llm2);
                rv.setAdapter(adapter2);
                grid.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);

            }
        });


        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rv.setLayoutManager(llm);
                rv.setAdapter(adapter);
                grid.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        });


        new LoadData().execute();

    }

    public boolean isDataExist(String tableName, int id) {

        boolean exists = false;
        int tempID;

        db = dbHelper.getReadableDatabase();
        cursor = db.query(tableName, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                tempID = cursor.getInt(0);

                if (id == tempID) {
                    Log.d("DATABASE", "EXISTS " + id);
                    exists = true;
                    break;
                }
            }
        }
        return exists;


    }

    public void readDB() {


        db = dbHelper.getReadableDatabase();
        cursor = db.query("tbl_prod", null, null, null, null, null, null);
        Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();


        if (cursor != null) {
            while (cursor.moveToNext()) {
                prod_id = cursor.getInt(0);
                user_id = cursor.getInt(1);
                prod_name = cursor.getString(2);
                prod_desc = cursor.getString(3);
                prod_price = cursor.getString(4);
                prod_brand = cursor.getString(5);
                prod_quantity = cursor.getString(6);
                prod_image = cursor.getString(7);


                itemshop.add(new Item_shop(prod_id, user_id, prod_name, prod_desc, prod_price, prod_brand, prod_quantity, prod_image));



            }
        }
        adapter.notifyDataSetChanged();
    }



    public void insertProduct(int prod_id, int user_id, String prod_name, String prod_desc, String prod_price, String prod_brand, String prod_quantity, String prod_image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("prod_id", prod_id);
        values.put("user_id", user_id);
        values.put("prod_name", prod_name);
        values.put("prod_desc", prod_desc);
        values.put("prod_price", prod_price);
        values.put("prod_brand", prod_brand);
        values.put("prod_quantity", prod_quantity);
        values.put("prod_image", prod_image);
        if (!isDataExist("tbl_prod", prod_id)) {
            db.insert("tbl_prod", null, values);

            db.close();
        }
    }

    public class LoadData extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(OtopShop.this);
            pDialog.setMessage("Loading Data Please Wait...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voidsparams) {
            String response = apiHandler.httpMakeRequest(url, params, "get", token);
            Log.d("zzzzzzz", "response" + response);

            try {

                if (response != null) {
                    JSONObject allData = new JSONObject(response);
                    JSONArray datas = allData.getJSONArray("data");


                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject data_object = datas.getJSONObject(i);

                        String prod_id = data_object.getString("id");
                        String user_id = data_object.getString("user_id");
                        String prod_name = data_object.getString("name");
                        String prod_desc = data_object.getString("description");
                        String prod_price = data_object.getString("price");
                        String prod_brand = data_object.getString("brand");
                        String prod_quantity = data_object.getString("quantity");

                        if(!data_object.isNull("photos")) {

                            JSONArray array_object = data_object.getJSONArray("photos");
                            String prod_image = array_object.getString(0);


                            insertProduct(Integer.parseInt(prod_id), Integer.parseInt(user_id), prod_name, prod_desc, prod_price, prod_brand, prod_quantity, prod_image);

                        }

                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }



        protected void onPostExecute(Void result) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            readDB();

        }


    }

}
