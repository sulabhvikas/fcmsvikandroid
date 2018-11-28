package fcm.svikas.com.sv_fcm_notify_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MySharedPrefManager {

    private static final String SHARED_PREF_NAME = "fcmsharedprefex";
    private static final String KEY_ACCESS_TOKEN = "fcmtoken";

    private static Context mCtx;
    private static MySharedPrefManager mInstance;

    private static final String TAG = "MySharedPrefManager";

    private MySharedPrefManager (Context context) {
        mCtx = context;
    }

    public static synchronized MySharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean storeToken (String token) {
        Log.d(TAG, "storeToken: " + token);
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        Log.d(TAG, "storeToken::sharedPreferences: " + sharedPreferences.toString());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        Log.d(TAG, "storeToken(" + KEY_ACCESS_TOKEN + ") = " + token);
        editor.apply();
        return true;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Log.d(TAG, "getToken::sharedPreferences: " + sharedPreferences.toString());
        Log.d(TAG, "getToken(" + KEY_ACCESS_TOKEN + ")" + sharedPreferences.getString(KEY_ACCESS_TOKEN, null));

        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

}
