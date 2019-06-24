package cc.brainbook.android.project.login.oauth.networks;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;

///https://github.com/maksim88/EasyLogin
///https://developers.google.com/identity/sign-in/android/start
public class GoogleNetwork extends SocialNetwork {

    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    public GoogleNetwork(Activity activity, View button, OnLoginCompleteListener onLoginCompleteListener) {
        this.activity = new WeakReference<>(activity);
        this.button = new WeakReference<>(button);
        this.listener = onLoginCompleteListener;

        ((SignInButton)(this.button.get())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected()) {
                    setButtonEnabled(false);
                    final Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    activity.startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        ///https://developers.google.com/identity/sign-in/android/sign-in
        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    @Nullable
    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public Network getNetwork() {
        return Network.GOOGLE;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                // The Task returned from this call is always completed, no need to attach a listener.
                final Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("TAG", "Authorization failed, request was canceled.");
                callLoginFailure(CommonStatusCodes.getStatusCodeString(CommonStatusCodes.CANCELED));
            }
        }
    }

    @Override
    public void logout() {
        if (isConnected()) {
            ///https://developers.google.com/identity/sign-in/android/disconnect
            mGoogleSignInClient.signOut();
            mGoogleSignInClient.revokeAccess();
        }
        setButtonEnabled(true);
    }

    @Override
    public boolean isConnected() {
        // Check for existing Google Sign In account, if the user is already signed in the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.activity.get());
        return account != null;
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        if (button != null && button.get() != null) {
            ((SignInButton)(this.button.get())).setEnabled(enabled);
        }
    }

    private void callLoginSuccess() {
        setButtonEnabled(false);
        listener.onLoginSuccess(getNetwork(), accessToken);
    }

    private void callLoginFailure(final String errorMessage) {
        setButtonEnabled(true);
        listener.onError(getNetwork(), errorMessage);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                accessToken = new AccessToken.Builder(acct.getIdToken())
                        .userId(acct.getId())
                        .userName(acct.getDisplayName())
                        .email(acct.getEmail())
                        .photoUrl((acct.getPhotoUrl() == null) ? null : acct.getPhotoUrl().toString())   ///[EasyLogin#photoUrl]
                        .build();
                callLoginSuccess();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("TAG", "GoogleSignInResult:failed code=" + e.getStatusCode());
            callLoginFailure(CommonStatusCodes.getStatusCodeString(e.getStatusCode()));
        }
    }

}
