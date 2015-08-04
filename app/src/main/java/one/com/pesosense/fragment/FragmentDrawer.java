
package one.com.pesosense.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.adapter.NavigationDrawerAdapter;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.NavDrawerItem;
import one.com.pesosense.model.UserItem;

/**
 * Created by mykelneds on 6/19/15.
 */
public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private static TypedArray icons = null;
    private static TypedArray iconsSeletected = null;
    private FragmentDrawerListener drawerListener;

    private TextView lblUsername;
    private TextView lblUseraddress;
    CircularImageView civ;
    LinearLayout navbarbgtop;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            navItem.setIcon(icons.getResourceId(i, -1));
            navItem.setSelectedIcon(iconsSeletected.getResourceId(i, -1));
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateList();
        // drawer labels

    }

    private void populateList() {
        if (UtilsApp.APP_LOGIN == 0) {
            titles = getActivity().getResources().getStringArray(
                    R.array.nav_item_thin);
            icons = getActivity().getResources().obtainTypedArray(
                    R.array.nav_icons_thin);

        } else {
            titles = getActivity().getResources().getStringArray(
                    R.array.nav_item_rich);
            icons = getActivity().getResources().obtainTypedArray(
                    R.array.nav_icons_rich);
            iconsSeletected = getActivity().getResources().obtainTypedArray(R.array.nav_icons_rich_selected);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer,
                container, false);

        UtilsApp.initSharedPreferences(getActivity());

        navbarbgtop = (LinearLayout) layout.findViewById(R.id.nav_header_container);
        lblUsername = (TextView) layout.findViewById(R.id.lblUsername);
        lblUsername.setTypeface(UtilsApp.opensansBold());

        lblUseraddress = (TextView) layout.findViewById(R.id.lblUserAddress);
        lblUseraddress.setTypeface(UtilsApp.opensansNormal());

        civ = (CircularImageView) layout.findViewById(R.id.imgUser);
        civ.setImageDrawable(getResources().getDrawable(R.drawable.ic_unsync));

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        setupUser();

        return layout;


    }

    public void setupUser() {

        String url = "http://search.onesupershop.com/api/photos/";
        UserItem item = readDB();

        String name = item.getfName() + " " + item.getlName();
        String address = item.getAddress();
        String profilePic = item.getImgPath();

        if (!name.trim().equalsIgnoreCase("")) {
            lblUsername.setText(name);
        } else {
            lblUsername.setText("PESO SENSE");
        }

        if (!address.equalsIgnoreCase("")) {
            lblUseraddress.setText(address);
        } else
            lblUseraddress.setVisibility(View.INVISIBLE);

        if (!profilePic.equalsIgnoreCase("")) {
            Picasso.with(getActivity()).load(url + profilePic).into(civ);
            Log.d("picture", url + profilePic);
        } else {
            civ.setImageDrawable(getResources().getDrawable(R.drawable.pesosense_sqaure));
        }

    }

    public UserItem readDB() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String email = preferences.getString("email", null);
        int userID = UtilsApp.getInt("user_id");
        UserItem item = null;

        String imgPath = "";
        String lName = "";
        String fName = "";
        String mName = "";
        String gender = "";
        String birthday = "";
        String address = "";


        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM tbl_user_info WHERE user_id = " + userID;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                imgPath = cursor.getString(cursor.getColumnIndex("photo"));
                lName = cursor.getString(cursor.getColumnIndex("last_name"));
                fName = cursor.getString(cursor.getColumnIndex("first_name"));
                mName = cursor.getString(cursor.getColumnIndex("middle_name"));
                gender = cursor.getString(cursor.getColumnIndex("gender"));
                birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                address = cursor.getString(cursor.getColumnIndex("address"));
            }
            item = new UserItem(imgPath, lName, fName, mName, gender, birthday, address);
        }

        return item;

    }

    public void setUp(int fragmentId, final DrawerLayout drawerLayout,
                      final Toolbar toolbar, final LinearLayout container) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
                container.setTranslationX(slideOffset * drawerView.getWidth());
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
                //
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements
            RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context,
                                     final RecyclerView recyclerView,
                                     final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            View child = recyclerView.findChildViewUnder(
                                    e.getX(), e.getY());
                            if (child != null && clickListener != null) {
                                clickListener.onLongClick(child,
                                        recyclerView.getChildPosition(child));
                            }
                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null
                    && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean arg0) {
            // TODO Auto-generated method stub

        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
