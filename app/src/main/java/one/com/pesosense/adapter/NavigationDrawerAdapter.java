package one.com.pesosense.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.model.NavDrawerItem;

/**
 * Created by mykelneds on 6/26/15.
 */
public class NavigationDrawerAdapter extends
        RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    List<MyViewHolder> holders = new ArrayList<MyViewHolder>();
    // List<MyViewHolder> holders = new ArrayList<MyViewHolder>();
    private LayoutInflater inflater;
    private Context context;
    private RelativeLayout root;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.title.setTypeface(UtilsApp.opensansNormal());
        holder.icon.setImageResource(current.getIcon());


        data.get(0).setShowNotify(true);
        holders.get(0).root.setBackgroundColor(context.getResources().getColor(
                R.color.colorSelected));
        holders.get(0).title.setTextColor(context.getResources().getColor(R.color.textColorBlack));
        holders.get(0).icon.setImageResource(data.get(0).getSelectedIcon());

        holder.root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                holders.get(position).root.setBackgroundColor(context.getResources().getColor(R.color.colorSelected));
                holders.get(position).title.setTextColor(context.getResources().getColor(R.color.textColorBlack));
                holders.get(position).icon.setImageResource(current.getSelectedIcon());
                for (int i = 0; i < data.size(); i++) {
                    if (i != position) {
                        holders.get(i).root.setBackgroundColor(context
                                .getResources().getColor(R.color.colorPrimary));
                        holders.get(i).title.setTextColor(context.getResources().getColor(R.color.textColorPrimary));
                        holders.get(i).icon.setImageResource(data.get(i).getIcon());

                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout root;
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            root = (RelativeLayout) itemView.findViewById(R.id.root);
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.icon);


            // root.setOnClickListener(this);
        }

        public void onClick(View v) {
            Toast.makeText(context, "Position: " + title.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}