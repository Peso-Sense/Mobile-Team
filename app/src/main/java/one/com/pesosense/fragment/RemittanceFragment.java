package one.com.pesosense.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
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
    public TextView txtDate;
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

    // Date pickerEditText
    DatePickerDialog dpd;


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

        txtDate = (TextView) dialog.findViewById(R.id.txt_date);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
                //dialog.dismiss();
            }
        });

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

    public void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                txtDate.setText(formatMonth(month) + " " + day + ", " + year);
            }
        }, year, month, day);

//        Build.VERSION_CODES
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        //    dpd.getDatePicker().setMaxDate(maxDate());
        dpd.getDatePicker().setMinDate(minDate());
        dpd.show();
    }

    private String formatMonth(int m) {
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        return months[m];
    }

    private Long minDate() {

        try {

            String time = "1900-01-01";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = df.parse(time);
            Long minDate = dt.getTime();

            return minDate;

        } catch (ParseException e) {

            UtilsApp.toast(e.getLocalizedMessage());
            return 0L;

        }

    }

    private Long maxDate() {

        Calendar now = Calendar.getInstance();

        String currentYear = String.valueOf(now.get(Calendar.YEAR));
        String currentMonth = String.valueOf(now.get(Calendar.MONTH) + 1);
        String currentDay = String.valueOf(now.get(Calendar.DAY_OF_MONTH));

        Log.d("Peso Sense", "Current year: " + currentYear); // track

        try {

            String time = currentYear + "-" + currentMonth + "-" + currentDay;
            Log.d("Peso Sense", time);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = df.parse(time);
            Long maxDate = dt.getTime();

            Log.d("Peso Sense", "This is the long min date: " + String.valueOf(maxDate)); // track
            return maxDate;

        } catch (ParseException e) {

            UtilsApp.toast(e.getLocalizedMessage());
            return 0L;

        }
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
