package one.com.pesosense.model;

/**
 * Created by mobile1 on 7/5/15.
 */
public class RemittanceItem {
    int remId;
    String remDate;
    String remType;
    String remContent;

    public RemittanceItem(int remId, String remDate, String remType, String remContent) {
        this.remId = remId;
        this.remDate = remDate;
        this.remType = remType;
        this.remContent = remContent;
    }

    public int getId() {
        return remId;
    }

    public String getDate() {
        return remDate;
    }

    public String getType() {
        return remType;
    }

    public String getContent() {
        return remContent;
    }

    public void setDate(String remId) {
        this.remDate = remDate;
    }

    public void setType(String remType) {
        this.remType = remType;
    }

    public void setContent(String remContent) {
        this.remContent = remContent;
    }
}
