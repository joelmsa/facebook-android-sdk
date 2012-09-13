package com.facebook.samples.switchuser;

import android.content.Context;
import android.os.Bundle;
import com.facebook.GraphUser;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SharedPreferencesTokenCache;

public class Slot {

    private static final String CACHE_NAME_FORMAT = "TokenCache%d";
    private static final String CACHE_USER_ID_KEY = "SwitchUserSampleUserId";
    private static final String CACHE_USER_NAME_KEY = "SwitchUserSampleUserName";

    private String userName;
    private String userId;
    private SharedPreferencesTokenCache tokenCache;
    private SessionLoginBehavior loginBehavior;

    public Slot(Context context, int slotNumber, SessionLoginBehavior loginBehavior) {
        this.loginBehavior = loginBehavior;
        this.tokenCache = new SharedPreferencesTokenCache(
                context,
                String.format(CACHE_NAME_FORMAT, slotNumber));

        restore();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public SessionLoginBehavior getLoginBehavior() {
        return loginBehavior;
    }

    public Session createSession(Context context) {
        return new Session(context, null, null, tokenCache);
    }

    public void update(GraphUser user) {
        if (user == null) {
            return;
        }

        userId = user.getId();
        userName = user.getName();

        Bundle userInfo = tokenCache.load();
        userInfo.putString(CACHE_USER_ID_KEY, userId);
        userInfo.putString(CACHE_USER_NAME_KEY, userName);

        tokenCache.save(userInfo);
    }

    public void clear() {
        tokenCache.clear();
        restore();
    }

    private void restore() {
        Bundle userInfo = tokenCache.load();
        userId = userInfo.getString(CACHE_USER_ID_KEY);
        userName = userInfo.getString(CACHE_USER_NAME_KEY);
    }
}
