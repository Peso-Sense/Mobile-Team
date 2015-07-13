package one.com.pesosense.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.adapter.FeedsAdapter;
import one.com.pesosense.download.GetFbFeeds;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.FbImageItem;
import one.com.pesosense.model.FbVideoItem;
import one.com.pesosense.model.TipsItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsFragment extends Fragment {

    RecyclerView rv;
    LinearLayoutManager llm;

    FeedsAdapter adapter;

    ArrayList<Object> fi;
    ArrayList<String> excludeID;

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor, cursor2, cursor3;

    String nextUrl = "";

    String tipsType[] = {"Entrepreneurs", "Homemakers", "Professionals", "Seniors and retiree", "Yound adults"};
    int icons[] = {R.drawable.tips1, R.drawable.tips2, R.drawable.tips3, R.drawable.tips4, R.drawable.tips5};

    int colors[] = {R.color.tips1, R.color.tips2, R.color.tips3, R.color.tips4, R.color.tips5};

    public FeedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feeds, container, false);
        initValues(v);
        return v;
    }

    public void initValues(View v) {

        dbHelper = DatabaseHelper.getInstance(getActivity());

        fi = new ArrayList<>();
        excludeID = new ArrayList<>();

        adapter = new FeedsAdapter(this.getActivity(), fi);

        llm = new LinearLayoutManager(getActivity());
        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);

//        readFeeds();
//        if (checkDB() != 0)
//            readDB();
//        else {
        new FeedsTask().execute();
//         }
    }


    public int checkDB() {
        int count = 0;

        db = dbHelper.getReadableDatabase();
        cursor = db.query("tbl_fb_image", null, null, null, null, null, null);

        count = cursor.getCount();
        return count;

    }

    public void readFeeds() {

        String id;
        int type;

        db = dbHelper.getReadableDatabase();
        cursor = db.query("tbl_fb_feeds", null, null, null, null, null, null);
        Log.d("DATABASE", "Count: " + String.valueOf(cursor.getCount()));
        while (cursor.moveToNext()) {
            id = cursor.getString(0);
            type = cursor.getInt(1);

            excludeID.add(id);
//
            if (!isExist(id)) {
                if (type == 0) {
                    fi.add(getFBImage(id));
                } else if (type == 1) {
                    fi.add(getFBVideo(id));
                }
            }
            Log.d("All feeds", id + " type: " + String.valueOf(type));
        }
        db.close();
        adapter.notifyDataSetChanged();
    }

    public boolean isExist(String id) {

        boolean exist = false;
        String tempId = "";
        for (int i = 0; i < fi.size(); i++) {

            if (fi.get(i) instanceof FbImageItem) {

                FbImageItem fii = (FbImageItem) (fi.get(i));
                tempId = fii.getId();
            } else if (fi.get(i) instanceof FbVideoItem) {
                FbVideoItem fvi = (FbVideoItem) (fi.get(i));
                tempId = fvi.getId();
            }

            if (id.equalsIgnoreCase(tempId)) {
                exist = true;
                break;
            }
        }


        return exist;
    }


    public FbImageItem getFBImage(String id) {
        FbImageItem item = null;

        String profilePic = "";
        String message = "";
        String link = "";
        int likes = 0;
        int comment = 0;

        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM tbl_fb_image WHERE id = '" + id + "'";
        cursor2 = db.rawQuery(query, null);
        if (cursor2 != null) {
            cursor2.moveToNext();
            profilePic = cursor2.getString(1);
            message = cursor2.getString(2);
            link = cursor2.getString(3);
            likes = cursor2.getInt(4);
            comment = cursor2.getInt(5);

            item = new FbImageItem(id, profilePic, message, link, likes, comment);
        }

        return item;
    }

    public FbVideoItem getFBVideo(String id) {
        FbVideoItem item = null;

        String profilePic = "";
        String message = "";
        String link = "";
        int likes = 0;
        int comment = 0;

        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM tbl_fb_video WHERE id = '" + id + "'";
        cursor3 = db.rawQuery(query, null);
        if (cursor3 != null) {
            cursor3.moveToNext();
            profilePic = cursor3.getString(1);
            message = cursor3.getString(2);
            link = cursor3.getString(3);
            likes = cursor3.getInt(4);
            comment = cursor3.getInt(5);

            item = new FbVideoItem(id, profilePic, message, link, likes, comment);
        }

        return item;
    }

//    public void readDB() {
//
//        String id;
//        String profilePic;
//        String message;
//        String link;
//        int likes;
//        int comment;
//
//        db = dbHelper.getReadableDatabase();
//
//        cursor = db.query("tbl_fb_image", null, null, null, null, null, null);
//
//        while (cursor.moveToNext()) {
//            id = cursor.getString(0);
//            profilePic = cursor.getString(1);
//            message = cursor.getString(2);
//            link = cursor.getString(3);
//            likes = cursor.getInt(4);
//            comment = cursor.getInt(5);
//
//            fi.add(new FbImageItem(id, profilePic, message, link, likes, comment));
//        }
//
//        adapter.notifyDataSetChanged();
//        displayTips();
//
//
//    }
//
//    public void readVideo() {
//
//        String id;
//        String profilePic;
//        String message;
//        String link;
//        int likes;
//        int comment;
//
//        db = dbHelper.getReadableDatabase();
//
//        cursor = db.query("tbl_fb_video", null, null, null, null, null, null);
//
//        while (cursor.moveToNext()) {
//            id = cursor.getString(0);
//            profilePic = cursor.getString(1);
//            message = cursor.getString(2);
//            link = cursor.getString(3);
//            likes = cursor.getInt(4);
//            comment = cursor.getInt(5);
//
//            fi.add(new FbVideoItem(id, profilePic, message, link, likes, comment));
//        }
//
//        adapter.notifyDataSetChanged();
////        displayTips();
//    }

    public class FeedsTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Pansamantala...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            nextUrl = new GetFbFeeds(getActivity()).getData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();

            displayTips();
            readFeeds();
            new NextFeedTask().execute();
            // readVideo();
        }
    }

    public class NextFeedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "LOADING ulit...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            nextUrl = new GetFbFeeds(getActivity()).getNext(nextUrl);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            readFeeds();
        }
    }

    public void displayTips() {

        int id, type;
        String tipsEnglish, tipsTagalog;

        //     UtilsApp.putInt("display_tips", 1);

        Random rand = new Random();
        id = rand.nextInt(55) + 1;

        TipsItem ti = getTips(id);

        type = ti.getType();
        tipsEnglish = ti.getEnglish();
        tipsTagalog = ti.getTagalog();

        TextView lblTips;
        TextView lblTipsEnglish;
        TextView lblTipsTagalog;

        ImageView imgIcon;

        Button btnClose;

        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tips);

        lblTips = (TextView) dialog.findViewById(R.id.lblTips);
        lblTips.setTypeface(UtilsApp.opensansNormal());

        lblTipsEnglish = (TextView) dialog.findViewById(R.id.lblTipsEnglish);
        lblTipsEnglish.setTypeface(UtilsApp.opensansNormal());

        lblTipsTagalog = (TextView) dialog.findViewById(R.id.lblTipsTagalog);
        lblTipsTagalog.setTypeface(UtilsApp.opensansNormal());

        lblTips.setText("Tips for " + tipsType[type - 1]);
        lblTipsEnglish.setText(tipsEnglish);
        lblTipsTagalog.setText(tipsTagalog);

        imgIcon = (ImageView) dialog.findViewById(R.id.imgIcon);
        imgIcon.setImageDrawable(getResources().getDrawable(icons[type - 1]));

        btnClose = (Button) dialog.findViewById(R.id.btnClose);
        btnClose.setBackgroundColor(getResources().getColor(colors[type - 1]));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public TipsItem getTips(int id) {
        TipsItem ti = null;

        DatabaseHelper dbHelper;
        SQLiteDatabase db;
        Cursor cursor;

        dbHelper = DatabaseHelper.getInstance(getActivity());
        db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM tbl_financial_tips WHERE id = " + id;
        cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                ti = new TipsItem(id, cursor.getInt(1), cursor.getString(2), cursor.getString(3));
            }
        }

//        ti = new TipsItem(0, 1, "", "");

        return ti;
    }


}
