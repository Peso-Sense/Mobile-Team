package one.com.pesosense;

/**
 * Created by mobile1 on 7/28/15.
 */
public class C {

    // URL end points
    public static final String URL_API = "http://search.onesupershop.com/api";
    public static final String URL_USER = URL_API + "/users";
    public static final String URL_USER_INFO = URL_API + "/me";
    public static final String URL_USER_PHOTO = URL_USER_INFO + "/photo";

    // Media
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int GALLERY_IMAGE_REQUEST_CODE = 150;
    public static final int IMAGE_CROP = 110;
    public static final String APP_DIR_NAME = ".Peso Sense";

    // Specifics
    public static final int MIN_AGE = 13;

}


