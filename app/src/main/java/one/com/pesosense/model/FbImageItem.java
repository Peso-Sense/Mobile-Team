package one.com.pesosense.model;

/**
 * Created by mykelneds on 7/13/15.
 */
public class FbImageItem {

    String id;
    String profilePic;
    String message;
    String link;
    int likes;
    int comment;

    public FbImageItem(String id, String profilePic, String message, String link, int likes, int comment) {
        this.id = id;
        this.profilePic = profilePic;
        this.message = message;
        this.link = link;
        this.likes = likes;
        this.comment = comment;
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
}
