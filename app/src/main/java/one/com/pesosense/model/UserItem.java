package one.com.pesosense.model;

/**
 * Created by mykelneds on 7/17/15.
 */
public class UserItem {

    String imgPath;
    String lName;
    String fName;
    String mName;
    String gender;
    String birthday;
    String address;


    public UserItem(String imgPath, String lName, String fName, String mName, String gender, String birthday, String address) {

        this.imgPath = imgPath;
        this.lName = lName;
        this.fName = fName;
        this.mName = mName;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;

    }



    public String getImgPath() {
        return imgPath;
    }

    public String getlName() {
        return lName;
    }

    public String getfName() {
        return fName;
    }

    public String getmName() {
        return mName;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }
}
