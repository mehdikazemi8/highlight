package ir.ugstudio.highlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

public class MyPreferenceManager {

    private static MyPreferenceManager instance = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static MyPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new MyPreferenceManager(context);
        }

        return instance;
    }

    public MyPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences("abcd", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public TennisSession getTennisSession(String videoURI) {
        String json = sharedPreferences.getString(videoURI, null);

        Log.d("TAG", "abcd " + json);

        if (json == null) {
            return new TennisSession(videoURI);
        }

        Gson gson = new Gson();
        return gson.fromJson(json, TennisSession.class);
    }

    public void putTennisSession(TennisSession session) {
        String json = new Gson().toJson(session);
        Log.d("TAG", json);
        editor.putString(session.getVideoURI(), json);
        editor.apply();
    }
}
