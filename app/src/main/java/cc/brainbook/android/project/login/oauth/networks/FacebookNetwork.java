package cc.brainbook.android.project.login.oauth.networks;

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

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;

///https://github.com/maksim88/EasyLogin
public class FacebookNetwork extends SocialNetwork {

    private static final String EMAIL_PERMISSION_FIELD = "email";
    private static final String NAME_FIELD = "name";

    private CallbackManager callbackManager;

    private List<String> permissions;

    private FacebookCallback<LoginResult> loginCallback = new FacebookCallback<LoginResult>() {

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
                    .userId(userId)
                    .photoUrl("https://graph.facebook.com/" + userId+ "/picture?type=large")   ///[EasyLogin#photoUrl]
                    .build();
            listener.onLoginSuccess(getNetwork());
        }

        @Override
        public void onCancel() {
            listener.onError(getNetwork(), null);
        }

        @Override
        public void onError(FacebookException error) {
            listener.onError(getNetwork(), error.getMessage());
        }
    };

    public FacebookNetwork(Activity activity, View button, OnLoginCompleteListener onLoginCompleteListener, List<String> permissions) {
        this.activity = new WeakReference<>(activity);
        this.button = new WeakReference<>(button);
        this.listener = onLoginCompleteListener;

        callbackManager = CallbackManager.Factory.create();
        final String applicationID = Utility.getMetadataApplicationId(this.activity.get());
        this.permissions = permissions;
        if (applicationID == null) {
            throw new IllegalStateException("applicationID can't be null\n" +
                    "Please check https://developers.facebook.com/docs/android/getting-started/");
        }

        ((LoginButton)(this.button.get())).setReadPermissions(permissions);
        ((LoginButton)(this.button.get())).registerCallback(callbackManager, loginCallback);

        ((LoginButton)(this.button.get())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
                LoginManager.getInstance().registerCallback(callbackManager, loginCallback);
            }
        });
    }

    @Override
    public AccessToken getAccessToken() {
        if (com.facebook.AccessToken.getCurrentAccessToken() != null && accessToken == null) {
            final com.facebook.AccessToken facebookToken = com.facebook.AccessToken.getCurrentAccessToken();
            accessToken = new AccessToken.Builder(facebookToken.getToken()).userId(facebookToken.getUserId()).build();
        }
        return accessToken;
    }

    @Override
    public Network getNetwork() {
        return Network.EL_FACEBOOK;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
    }

    @Override
    public boolean isConnected() {
        return com.facebook.AccessToken.getCurrentAccessToken() != null;
    }

    private void setButtonEnabled(boolean enabled) {
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
                            Log.d("FacebookNetwork", "Error occurred while fetching Facebook email");
                            accessToken = new AccessToken.Builder(token)
                                    .userId(userId)
                                    .photoUrl("https://graph.facebook.com/" + userId+ "/picture?type=large")   ///[EasyLogin#photoUrl]
                                    .build();
                            listener.onLoginSuccess(getNetwork());
                        } else {
                            final String email = me.optString(EMAIL_PERMISSION_FIELD);
                            final String name = me.optString(NAME_FIELD);
                            if (TextUtils.isEmpty(email)) {
                                Log.d("FacebookNetwork", "Email could not be fetched. The user might not have an email or have unchecked the checkbox while connecting.");
                            }
                            accessToken = new AccessToken.Builder(token)
                                    .userId(userId)
                                    .email(email)
                                    .userName(name)
                                    .photoUrl("https://graph.facebook.com/" + userId+ "/picture?type=large")   ///[EasyLogin#photoUrl]
                                    .build();
                            listener.onLoginSuccess(getNetwork());
                        }
                    }
                });

        final Bundle parameters = new Bundle();
        parameters.putString("fields", NAME_FIELD + "," + EMAIL_PERMISSION_FIELD);
        meRequest.setParameters(parameters);
        meRequest.executeAsync();
    }
}
