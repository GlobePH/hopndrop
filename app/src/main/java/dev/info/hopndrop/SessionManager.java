package dev.info.hopndrop;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;
    private static String api_id="";
    private static String origin="";
    private static String destination="";
    private static String time="";
    private static String filter="";

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static String TOKEN ="";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setToken(String tokenValue){
        TOKEN = tokenValue;
    }

    public String getToken(){
        return TOKEN;
    }


    public String getApi_id(){
        return api_id;
    }

    public void setApi_id(String api_idValue){
        api_id = api_idValue;
    }



    public String getOrigin(){
        return origin;
    }
    public void setOrigin(String originValue){
        origin = originValue;
    }
    public String getDestination(){
        return destination;
    }
    public void setDestination(String destinationValue){
        destination = destinationValue;
    } public String getTime(){
        return time;
    }
    public void setTime(String timeValue){
        time = timeValue;
    }

    public String getFilter(){
        return filter;
    }
    public void setFilter(String filterValue){
        filter = filterValue;
    }
}