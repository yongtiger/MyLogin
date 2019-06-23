package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.lang.ref.WeakReference;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;
import retrofit2.Call;

///https://github.com/maksim88/EasyLogin
public class TwitterNetwork extends SocialNetwork {

    private Callback<TwitterSession> buttonCallback = new Callback<TwitterSession>() {

        @Override
        public void success(Result<TwitterSession> result) {
            final TwitterSession session = result.data;
            final TwitterAuthToken authToken = session.getAuthToken();
            final String token = authToken.token;
            final String secret = authToken.secret;
            final AccessToken tempToken = new AccessToken.Builder(token)
                    .secret(secret)
                    .userName(session.getUserName())
                    .userId(String.valueOf(session.getUserId()))
                    .build();
            requestUser(session, tempToken);
        }

        @Override
        public void failure(TwitterException e) {
            callLoginFailure(e.getMessage());
        }
    };

    public TwitterNetwork(Activity activity, View button, OnLoginCompleteListener onLoginCompleteListener) {
        this.activity = new WeakReference<>(activity);
        this.button = new WeakReference<>(button);
        this.listener = onLoginCompleteListener;

        ((TwitterLoginButton)(this.button.get())).setCallback(buttonCallback);
    }

    @Override
    public Network getNetwork() {
        return Network.EL_TWITTER;
    }

    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (button != null && button.get() != null) {
            ((TwitterLoginButton)button.get()).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void logout() {
        final TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {
            TwitterCore.getInstance().getSessionManager().clearActiveSession();
            setButtonEnabled(true);
        }
    }

    @Override
    public boolean isConnected() {
        return TwitterCore.getInstance().getSessionManager().getActiveSession() != null;
    }

    private void setButtonEnabled(boolean enabled) {
        if (button != null && button.get() != null) {
            ((TwitterLoginButton)(this.button.get())).setEnabled(enabled);
        }
    }

    private void callLoginSuccess() {
        setButtonEnabled(false);
        listener.onLoginSuccess(getNetwork());
    }

    private void callLoginFailure(final String errorMessage) {
        setButtonEnabled(true);
        listener.onError(getNetwork(), errorMessage);
    }

    ///[EasyLogin#Twitter]获取头像和Email等User数据
    ///com.twitter.sdk.android.core.identity.TwitterAuthClient#requestUser(TwitterSession session, final Callback<String> callback)
    public void requestUser(TwitterSession session, final AccessToken tempToken) {
        final Call<User> verifyRequest = TwitterCore.getInstance().getApiClient(session).getAccountService()
                .verifyCredentials(false, false, true);

        verifyRequest.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                final String email = result.data.email;
                if (TextUtils.isEmpty(email)) {
                    logout();
                    callLoginFailure("Before fetching an email, ensure that 'Request email addresses from users' is checked for your Twitter app.");
                    return;
                }
                accessToken = new AccessToken.Builder(tempToken)
                        .email(email)
                        .photoUrl(result.data.profileImageUrl)
                        .build();
                callLoginSuccess();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e("TwitterNetwork", "Before fetching an email, ensure that 'Request email addresses from users' is checked for your Twitter app.");
                callLoginFailure(exception.getMessage());
            }
        });
    }

}
