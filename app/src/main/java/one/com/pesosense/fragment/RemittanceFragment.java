package one.com.pesosense.fragment;


import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import one.com.pesosense.R;
import one.com.pesosense.adapter.RemittanceAdapter;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.RemittanceItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemittanceFragment extends Fragment {

    public RemittanceFragment() {
        // Required empty public constructor
    }


    // floating action button
    FloatingActionButton btnFab;
    public Dialog dialog;
    public EditText txtDate;
    public EditText txtTitle;
    public EditText txtMessage;

    // For the recycler view
    private RecyclerView recyclerView;
    private RemittanceAdapter adapter;
    ArrayList<RemittanceItem> item;

    int rId;
    String rDate;
    String rType;
    String rMessage;

    //For database
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_remittance, container, false);
        populateRecycler(layout);

        btnFab = (FloatingActionButton) layout.findViewById(R.id.btn_add_rem);
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddRemittanceDialog();
            }
        });

        return layout;
    }

    public void openAddRemittanceDialog() {
        // function for calling the add new dialog
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // for lower version
        dialog.setContentView(R.layout.dialog_add_remittance);
        dialog.show();

        txtDate = (EditText) dialog.findViewById(R.id.txt_date);
        txtTitle = (EditText) dialog.findViewById(R.id.txt_title);
        txtMessage = (EditText) dialog.findViewById(R.id.txt_message);
        Button btnAdd = (Button) dialog.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Your item shall be saved", Toast.LENGTH_LONG).show();
                addNewRemittance();
                dialog.dismiss();
            }
        });
    }

    public void populateRecycler(View layout) {
        item = new ArrayList<>();
        adapter = new RemittanceAdapter(getActivity(), item);

        recyclerView = (RecyclerView) layout.findViewById(R.id.mainRemittances);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        readDb();
    }

    public void readDb() {
        dbHelper = DatabaseHelper.getInstance(getActivity());
        db = dbHelper.getReadableDatabase();

        cursor = db.query("tbl_remittances", null, null, null, null, null, null);
        //populate here using while loop
        if (cursor != null) {
            while (cursor.moveToNext()) {
                rId = cursor.getInt(0);
                rDate = cursor.getString(1);
                rType = cursor.getString(2);
                rMessage = cursor.getString(3);
                item.add(0, new RemittanceItem(rId, rDate, rType, rMessage));
            }
        }

        adapter.notifyDataSetChanged();
    }

    /* ADD
     * EDIT
     * DELETE
     */

    public void addNewRemittance() {
        db = dbHelper.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();

        values.put("rem_date", txtDate.getText().toString());
        values.put("rem_title", txtTitle.getText().toString());
        values.put("rem_message", txtMessage.getText().toString());

        db.insert("tbl_remittances", null, values);
        db.close();
        item.clear();
        readDb();
    }
}
