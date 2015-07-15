package one.com.pesosense.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mykelneds on 7/13/15.
 */
public class FbImageItem implements Comparable<FbImageItem> {

    String id;
    String profilePic;
    String message;
    String link;
    int likes;
    int comment;

    Date timeStamp;
    SimpleDateFormat sdf, output;

    public FbImageItem(String id, String profilePic, String message, String link, int likes, int comment, String timestamp) {
        this.id = id;
        this.profilePic = profilePic;
        this.message = message;
        this.link = link;
        this.likes = likes;
        this.comment = comment;
        // this.timestamp = timestamp;

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.timeStamp = output.parse(output.format(sdf.parse(timestamp)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getMessage() {
        return message;
    }

    public String getLink() {
        return link;
    }

    public int getLikes() {
        return likes;

    }

    public int getComment() {
        return comment;
    }

    public Date getTimestamp() {
        return this.timeStamp;
    }

    @Override
    public int compareTo(FbImageItem fbImageItem) {

        if (getTimestamp() == null || fbImageItem.getTimestamp() == null)
            return 0;
        else
            return getTimestamp().compareTo(fbImageItem.getTimestamp());
    }
}
