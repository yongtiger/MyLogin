package cc.brainbook.android.project.user.oauth.networks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.Utility;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import cc.brainbook.android.project.user.oauth.AccessToken;
import cc.brainbook.android.project.user.oauth.config.Config;
import cc.brainbook.android.project.user.oauth.listener.OnOauthCompleteListener;

///https://github.com/maksim88/EasyLogin
public class FacebookNetwork extends SocialNetwork {

    private static final String EMAIL_PERMISSION_FIELD = "email";
    private static final String NAME_FIELD = "name";

    private CallbackManager callbackManager;

    private List<String> permissions;

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {

            final com.facebook.AccessToken fbAccessToken = loginResult.getAccessToken();

            if (permissions.contains(EMAIL_PERMISSION_FIELD)) {
                addEmailToToken(fbAccessToken);
                return;
            }

            final String token = fbAccessToken.getToken();
            final String userId = fbAccessToken.getUserId();
            accessToken = new AccessToken.Builder(token)
                    .network(getNetwork().toString())
                    .openId(userId)
                    .avatar("https://graph.facebook.com/" + userId+ "/picture?type=large")   ///[EasyLogin#avatar]
                    .build();
            callOauthSuccess();
        }

        @Override
        public void onCancel() {
            callOauthFailure("Authorization failed, request was canceled.");
        }

        @Override
        public void onError(FacebookException error) {
            callOauthFailure(error.getMessage());
        }
    };

    public FacebookNetwork(Activity activity, View button, OnOauthCompleteListener onOauthCompleteListener, List<String> permissions) {
        this.activity = new WeakReference<>(activity);
        this.button = new WeakReference<>(button);
        this.listener = onOauthCompleteListener;

        callbackManager = CallbackManager.Factory.create();
        final String applicationID = Utility.getMetadataApplicationId(this.activity.get());
        this.permissions = permissions;
        if (applicationID == null) {
            throw new IllegalStateException("applicationID can't be null\n" +
                    "Please check https://developers.facebook.com/docs/android/getting-started/");
        }

        ((LoginButton)(this.button.get())).setPermissions(permissions);
        ((LoginButton)(this.button.get())).registerCallback(callbackManager, facebookCallback);

        ///[FIX BUG]下面代码执行后，点击会重复出现Facebook认证对话框！所以注释掉
        ///https://developers.facebook.com/docs/facebook-login/android
//        ((LoginButton)(this.button.get())).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
//                LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);
//            }
//        });
    }

    @Override
    public AccessToken getAccessToken() {
        if (com.facebook.AccessToken.getCurrentAccessToken() != null && accessToken == null) {
            final com.facebook.AccessToken facebookToken = com.facebook.AccessToken.getCurrentAccessToken();
            accessToken = new AccessToken.Builder(facebookToken.getToken()).openId(facebookToken.getUserId()).build();
        }
        return accessToken;
    }

    @Override
    public Config.Network getNetwork() {
        return Config.Network.FACEBOOK;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null && requestCode == ((LoginButton)(this.button.get())).getRequestCode()) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
        setButtonEnabled(true);
    }

    @Override
    public boolean isConnected() {
        ///https://developers.facebook.com/docs/facebook-login/android
        return com.facebook.AccessToken.getCurrentAccessToken() != null
                && com.facebook.AccessToken.getCurrentAccessToken().isExpired();
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        if (button != null && button.get() != null) {
            ((LoginButton)(this.button.get())).setEnabled(enabled);
        }
    }

    private void addEmailToToken(final com.facebook.AccessToken fbAccessToken) {
        final GraphRequest meRequest = GraphRequest.newMeRequest(
                fbAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        final String token = fbAccessToken.getToken();
                        final String userId = fbAccessToken.getUserId();
                        if (response.getError() != null) {
                            Log.e("FacebookNetwork", "Error occurred while fetching Facebook email");
                            accessToken = new AccessToken.Builder(token)
                                    .openId(userId)
                                    .avatar("https://graph.facebook.com/" + userId+ "/picture?type=large")   ///[EasyLogin#avatar]
                                    .build();
                            callOauthSuccess();
                        } else {
                            final String email = me.optString(EMAIL_PERMISSION_FIELD);
                            final String name = me.optString(NAME_FIELD);
                            if (TextUtils.isEmpty(email)) {
                                Log.w("FacebookNetwork", "Email could not be fetched. The user might not have an email or have unchecked the checkbox while connecting.");
                            }
                            accessToken = new AccessToken.Builder(token)
                                    .openId(userId)
                                    .email(email)
                                    .username(name)
                                    .avatar("https://graph.facebook.com/" + userId+ "/picture?type=large")   ///[EasyLogin#avatar]
                                    .build();
                            callOauthSuccess();
                        }
                    }
                });

        final Bundle parameters = new Bundle();
        parameters.putString("fields", NAME_FIELD + "," + EMAIL_PERMISSION_FIELD);
        meRequest.setParameters(parameters);
        meRequest.executeAsync();
    }
}
