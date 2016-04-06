package  com.example.mardiak.marek.rpdemoapp.hellojni;

/**
 * Created by mm on 3/21/2016.
 */
public class InfoServiceJni {

    private String value = "FUBAR in private sense";
    private static String staticValue="FUBAR in private static sense";

    public String phoneInfo() {
        return "DEVICE: " + android.os.Build.DEVICE +
        ", MODEL: " + android.os.Build.MODEL +
        ", PRODUCT: " + android.os.Build.PRODUCT;
    }



}
