package ao.play.freekick.Models;

public class Common {

    public static final String REGEX_NAME = "^[a-zA-Zء-ي]?/?\\s?+(([a-zA-Zء-ي]{3,10})(?:\\s|$)){1,6}$";
    public static final String REGEX_ERROR = "[A-Za-zء-ى0-9]+$|^(.*?)$";

    // String constants
    public static final String ARABIC_NAME = "arabic_name";
    public static final String CODE = "code";
    public static final String CONTROLLERS = "Controllers";
    public static final String DATABASE_NAME = "Database";
    public static final String USER_DATA = "user_data";
    public static final String FIREBASE_USERS = "Users";
    public static final String DAY = "day";
    public static final String DAY_VALUE = "dayValue";
    public static final String DEBT = "Debt";
    public static final String DEVICE = "Device";
    public static final String DEVICE_DETAILS = "deviceDetails";
    public static final String DEVICE_NUMBER = "deviceNumber";
    public static final String DURATION = "duration";
    public static final String ENGLISH_NAME = "english_name";
    public static final String LETTER = "letter";
    public static final String NAME = "name";
    public static final String FOR_YOU = "forYou";
    public static final String FOR_HIM = "forHim";
    public static final String HISTORY = "HISTORY_DATA_NUMBER_";
    public static final String MONTH = "month";
    public static final String MONTH_VALUE = "monthValue";
    public static final String NUMBER = "number";
    public static final String NOTIFICATION_CONTENT = "content";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String PHONE_NUMBER = "phone";
    public static final String PRICE = "price";
    public static final String REVENUE = "DEVICE_REVENUE_NUMBER_";
    public static final String SHARED_PREFERENCE_NAME = "9bcbKJMXOoMZss1GLo+oew==";
    public static final String SHOP_ID = "shop_id";
    public static final String ITEM_POSITION = "thread_position";
    public static final String TITLE = "title";
    public static final String IMAGE = "image";
    public static final String USER_PASSWORD = "user_password";
    public static final String YEAR = "year";
    public static final String YEAR_VALUE = "yearValue";
    public static final String REMEMBER_ME = "remember me";
    // Integer constants
    public static final int DEVICES_NUMBER = 4;
    public static final int HOUR = 60;
    public static final int HOUR_PRICE = 10;
    public static final int TIME_INTERVAL = 500;
    public static final String CUSTOMER_MANE = "customer name";
    public static final String CUSTOMER_FOR_YOU = "customer for you";
    public static final String CUSTOMER_FOR_ME = "customer_for_me";
    public static final String ID = "controller id";
    private static String ROOT;

    public static String getROOT() {
        return ROOT;
    }

    public static void setROOT(String ROOT) {
        Common.ROOT = ROOT;
    }
}