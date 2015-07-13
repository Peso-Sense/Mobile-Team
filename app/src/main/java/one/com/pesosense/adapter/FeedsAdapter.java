package one.com.pesosense.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.model.FbImageItem;
import one.com.pesosense.model.FbVideoItem;

/**
 * Created by mykelneds on 7/13/15.
 */
public class FeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Object> data;
    LayoutInflater inflater;
    Context context;

    final int FB_IMAGE = 0;
    final int FB_VIDEO = 1;
    final int TWITTER = 2;

    public FeedsAdapter(Context context, ArrayList<Object> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = null;
        switch (viewType) {
            case FB_IMAGE:
                v = inflater.inflate(R.layout.feeds_fbimage, parent, false);
                viewHolder = new FbImageHolder(v);
                break;
            case FB_VIDEO:
                v = inflater.inflate(R.layout.feeds_fbvideo, parent, false);
                viewHolder = new FbVideoHolder(v);
                break;

        }

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case FB_IMAGE:
                FbImageHolder imageHolder = (FbImageHolder) holder;
                fbImageHolder(imageHolder, position);
                break;
            case FB_VIDEO:
                FbVideoHolder videoHolder = (FbVideoHolder) holder;
                fbVideoHolder(videoHolder, position);
                break;
        }

    }


    public void fbImageHolder(FbImageHolder holder, int position) {
        FbImageItem current = (FbImageItem) data.get(position);

        holder.fbMessage.setText(current.getMessage());
        holder.lblNoLikes.setText(String.valueOf(current.getLikes()));
        holder.lblNoComments.setText(String.valueOf(current.getComment()));

        Picasso.with(context).load(current.getProfilePic()).into(holder.fbProfilePicture);
        Picasso.with(context).load(current.getLink()).into(holder.fbImage);
    }

    public class FbImageHolder extends RecyclerView.ViewHolder {

        TextView lblPesoSense;
        TextView fbMessage;

        ImageView fbProfilePicture;
        ImageView fbImage;

        TextView lblNoComments;
        TextView lblComments;
        TextView lblNoLikes;
        TextView lblLikes;

        public FbImageHolder(View v) {
            super(v);

            lblPesoSense = (TextView) v.findViewById(R.id.lblPesoSense);
            lblPesoSense.setTypeface(UtilsApp.opensansBold());

            fbProfilePicture = (ImageView) v.findViewById(R.id.fbProfilePicture);

            fbImage = (ImageView) v.findViewById(R.id.fbImage);

            fbMessage = (TextView) v.findViewById(R.id.fbMessage);
            fbMessage.setTypeface(UtilsApp.opensansNormal());

            lblNoComments = (TextView) v.findViewById(R.id.lblNoComments);
            lblNoComments.setTypeface(UtilsApp.opensansNormal());

            lblComments = (TextView) v.findViewById(R.id.lblComments);
            lblComments.setTypeface(UtilsApp.opensansNormal());

            lblNoLikes = (TextView) v.findViewById(R.id.lblNoLikes);
            lblNoLikes.setTypeface(UtilsApp.opensansNormal());

            lblLikes = (TextView) v.findViewById(R.id.lblLikes);
            lblLikes.setTypeface(UtilsApp.opensansNormal());
        }
    }

    public void fbVideoHolder(FbVideoHolder holder, int position) {

        FbVideoItem current = (FbVideoItem) data.get(position);

        holder.fbMessage.setText(current.getMessage());
        holder.lblNoLikes.setText(String.valueOf(current.getLikes()));
        holder.lblNoComments.setText(String.valueOf(current.getComment()));

        Picasso.with(context).load(current.getProfilePic()).into(holder.fbProfilePicture);

        //URL new_url = new URL(current.getLink());
        MediaController mediacontroller = new MediaController(context);
        mediacontroller.setAnchorView(holder.fbVideo);

        Uri video = Uri.parse(current.getLink());
        holder.fbVideo.setMediaController(mediacontroller);
        holder.fbVideo.setVideoURI(video);
        holder.fbVideo.start();


    }

    public class FbVideoHolder extends RecyclerView.ViewHolder {

        TextView lblPesoSense;
        TextView fbMessage;

        ImageView fbProfilePicture;
        VideoView fbVideo;

        TextView lblNoComments;
        TextView lblComments;
        TextView lblNoLikes;
        TextView lblLikes;

        public FbVideoHolder(View v) {
            super(v);

            lblPesoSense = (TextView) v.findViewById(R.id.lblPesoSense);
            lblPesoSense.setTypeface(UtilsApp.opensansBold());

            fbProfilePicture = (ImageView) v.findViewById(R.id.fbProfilePicture);

            fbVideo = (VideoView) v.findViewById(R.id.fbVideo);

            fbMessage = (TextView) v.findViewById(R.id.fbMessage);
            fbMessage.setTypeface(UtilsApp.opensansNormal());

            lblNoComments = (TextView) v.findViewById(R.id.lblNoComments);
            lblNoComments.setTypeface(UtilsApp.opensansNormal());

            lblComments = (TextView) v.findViewById(R.id.lblComments);
            lblComments.setTypeface(UtilsApp.opensansNormal());

            lblNoLikes = (TextView) v.findViewById(R.id.lblNoLikes);
            lblNoLikes.setTypeface(UtilsApp.opensansNormal());

            lblLikes = (TextView) v.findViewById(R.id.lblLikes);
            lblLikes.setTypeface(UtilsApp.opensansNormal());

        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        int type = 0;

        if (data.get(position) instanceof FbImageItem)
            return FB_IMAGE;
        if (data.get(position) instanceof FbVideoItem)
            return FB_VIDEO;
        return type;

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

}
