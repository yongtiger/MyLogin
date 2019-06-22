package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.lang.ref.WeakReference;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;

///https://github.com/maksim88/EasyLogin
public class GoogleNetwork extends SocialNetwork implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String GOOGLE_CONNECTED = "google_connected";

    private static final int GOOGLE_REQUEST_CODE = 1337;

    private SharedPreferences sharedPrefs;

    private GoogleApiClient googleApiClient;

    public GoogleNetwork(Activity activity, View button, OnLoginCompleteListener onLoginCompleteListener) {
        this.activity = new WeakReference<>(activity);
        this.button = new WeakReference<>(button);
        this.listener = onLoginCompleteListener;

        ((SignInButton)(this.button.get())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected()) {
                    final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    activity.startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE);
                } else {
                    silentSignIn();
                }
            }
        });

        sharedPrefs = activity.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        final GoogleApiClient.Builder googleApiBuilder = new GoogleApiClient.Builder(activity);
        if (activity instanceof FragmentActivity) {
            googleApiBuilder.enableAutoManage((FragmentActivity) activity, this);
        } else {
            googleApiBuilder
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this);
        }
        googleApiClient = googleApiBuilder
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Nullable
    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public Network getNetwork() {
        return Network.EL_GOOGLE;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_REQUEST_CODE) {
            final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public boolean isConnected() {
        // GoogleApiClient behaves weirdly with isConnected(), so let's save our own state
        return sharedPrefs.getBoolean(GOOGLE_CONNECTED, false);
    }

    @Override
    public void logout() {
        if (isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient);
            googleApiClient.disconnect();
            setSignInButtonEnabled(true);
            sharedPrefs.edit().putBoolean(GOOGLE_CONNECTED, false).apply();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        sharedPrefs.edit().putBoolean(GOOGLE_CONNECTED, true).apply();
        setSignInButtonEnabled(false);
        listener.onLoginSuccess(getNetwork());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        sharedPrefs.edit().putBoolean(GOOGLE_CONNECTED, false).apply();
        setSignInButtonEnabled(true);
        listener.onError(getNetwork(), getStatusCodeString(cause));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        sharedPrefs.edit().putBoolean(GOOGLE_CONNECTED, false).apply();
        setSignInButtonEnabled(true);
        listener.onError(getNetwork(), getStatusCodeString(connectionResult.getErrorCode()));
    }

    private void handleSignInResult(GoogleSignInResult result) {
        parseGoogleSignInResult(result);
    }

    private void parseGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            sharedPrefs.edit().putBoolean(GOOGLE_CONNECTED, true).apply();
            setSignInButtonEnabled(false);
            final GoogleSignInAccount acct = result.getSignInAccount();

            if (acct != null) {
                accessToken = new AccessToken.Builder(acct.getId())
                        .email(acct.getEmail())
                        .userName(acct.getDisplayName())
                        .userId(acct.getId())
                        .photoUrl((acct.getPhotoUrl() == null) ? null : acct.getPhotoUrl().toString())   ///[EasyLogin#photoUrl]
                        .build();
                listener.onLoginSuccess(getNetwork());
            }
        } else {
            if (result.getStatus().getStatusCode() == CommonStatusCodes.SIGN_IN_REQUIRED) {
                final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                activity.get().startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE);
                return;
            }
            sharedPrefs.edit().putBoolean(GOOGLE_CONNECTED, false).apply();
            setSignInButtonEnabled(true);
            listener.onError(getNetwork(), getStatusCodeString(result.getStatus().getStatusCode()));
        }
    }

    private String getStatusCodeString(int statusCode) {
        return CommonStatusCodes.getStatusCodeString(statusCode);
    }

    private void setSignInButtonEnabled(boolean enabled) {
        if (button != null && button.get() != null) {
            button.get().setEnabled(enabled);
        }
    }


    public void silentSignIn() {
        OptionalPendingResult<GoogleSignInResult> pendingResult =
                Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (pendingResult.isDone()) {
            // There's immediate result available.
            parseGoogleSignInResult(pendingResult.get());
        } else {
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    parseGoogleSignInResult(result);
                }
            });
        }
    }

    /**
     * Use this method if you cannot use autoManage from GoogleApiClient to connect in onStart().
     * The easier approach is to pass a FragmentActivity in the Network and let GoogleApiClient manage itself.
     */
    public void connectGoogleApiClient() {
        googleApiClient.connect();
    }

    /**
     * Use this method if you cannot use autoManage from GoogleApiClient to disconnect in onStop().
     * The easier approach is to pass a FragmentActivity in the Network and let GoogleApiClient manage itself.
     */
    public void disconnectGoogleApiClient() {
        googleApiClient.disconnect();
    }

}
