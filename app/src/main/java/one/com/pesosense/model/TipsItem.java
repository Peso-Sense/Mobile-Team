package one.com.pesosense.model;

/**
 * Created by mykelneds on 7/7/15.
 */
public class TipsItem {

    int id;
    int type;
    String tipsEnglish;
    String tipsTagalog;

    public TipsItem(int id, int type, String tipsEnglish, String tipsTagalog) {
        this.id = id;
        this.type = type;
        this.tipsEnglish = tipsEnglish;
        this.tipsTagalog = tipsTagalog;
    }

    public int getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public String getEnglish() {
        return this.tipsEnglish;
    }

    public String getTagalog() {
        return this.tipsTagalog;
    }

}
