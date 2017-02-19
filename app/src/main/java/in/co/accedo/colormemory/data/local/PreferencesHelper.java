package in.co.accedo.colormemory.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import in.co.accedo.colormemory.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_boilerplate_pref_file";
    public static final String SCORE = "high_score";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }


    public void setHighScore(Integer score) {
        mPref.edit().putInt(SCORE, score).apply();
    }

    public Integer getHighScore() {
        return mPref.getInt(SCORE, 0);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

}
