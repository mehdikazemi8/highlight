package ir.ugstudio.highlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

    public TennisSession getTennisSession(Uri videoURI) {
        String json = sharedPreferences.getString(videoURI.toString(), null);

        Log.d("TAG", "abcd " + json);

        // todo remove
        if (json == null || true) {
            return new TennisSession(videoURI);
        }

        Gson gson = new Gson();
        return gson.fromJson(json, TennisSession.class);
    }

    public void putTennisSession(TennisSession session) {
        String json = new Gson().toJson(session);
        Log.d("TAG", json);
        editor.putString(session.getVideoURI().toString(), json);
        editor.apply();
    }
}
