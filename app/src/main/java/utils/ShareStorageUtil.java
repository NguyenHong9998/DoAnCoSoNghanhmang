package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareStorageUtil {
   Context context;
   SharedPreferences sharedPreferences;

    public ShareStorageUtil(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("localStorage", Context.MODE_PRIVATE);
    }

    public void applyValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getValue(String key) {
        return this.sharedPreferences.getString(key, "");
    }
}
