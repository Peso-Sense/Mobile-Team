package one.com.pesosense.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import one.com.pesosense.R;

/**
 * Created by mykelneds on 8/1/15.
 */
public class CardSample extends Activity implements View.OnClickListener {

    ImageView imgLike;
    ImageView imgLiked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_fbimage3);

        imgLike = (ImageView) findViewById(R.id.imgLike);
        imgLike.setOnClickListener(this);

        imgLiked = (ImageView) findViewById(R.id.imgLiked);
        imgLiked.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgLike) {
            imgLike.setVisibility(View.INVISIBLE);
            imgLiked.setVisibility(View.VISIBLE);
        }

        if (v.getId() == R.id.imgLiked) {
            imgLiked.setVisibility(View.INVISIBLE);
            imgLike.setVisibility(View.VISIBLE);
        }


    }
}
