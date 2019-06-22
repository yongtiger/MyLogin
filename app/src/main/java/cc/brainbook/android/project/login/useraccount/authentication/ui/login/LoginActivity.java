package cc.brainbook.android.project.login.useraccount.authentication.ui.login;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
//import com.facebook.login.widget.LoginButton;///改用Mob！
//import com.twitter.sdk.android.core.identity.TwitterLoginButton;///改用Mob！

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.EasyLogin;
import cc.brainbook.android.project.login.oauth.listener.OnLoginCompleteListener;
//import cc.brainbook.android.project.login.oauth.networks.FacebookNetwork;///改用Mob！
import cc.brainbook.android.project.login.oauth.networks.FacebookNetwork;
import cc.brainbook.android.project.login.oauth.networks.GooglePlusNetwork;
import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;
import cc.brainbook.android.project.login.oauth.networks.TwitterNetwork;
import cc.brainbook.android.project.login.resetpassword.ui.ResetPasswordActivity;
import cc.brainbook.android.project.login.result.Result;
import cc.brainbook.android.project.login.useraccount.authentication.ui.register.RegisterActivity;
import cc.brainbook.android.project.login.util.PrefsUtil;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.twitter.Twitter;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnLoginCompleteListener, PlatformActionListener {
    private static final String KEY_REMEMBER_USERNAME = "remember_username";
    private static final String KEY_REMEMBER_PASSWORD = "remember_password";

    private LoginViewModel loginViewModel;

    private EditText etUsername;
    private ImageView ivClearUsername;
    private EditText etPassword;
    private ImageView ivClearPassword;
    private ImageView ivPasswordVisibility;
    private Button btnResetPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private Button btnRegister;
    private ProgressBar pbLoading;

    ///[oAuth#EasyLogin]
    private EasyLogin easyLogin;
    private TextView tvConnectedStatus;
    private Button btnLogoutAllNetworks;
    private GooglePlusNetwork googlePlusNetwork;
    private SignInButton sibGoogleSignIn;

    private Button btnOauthLogin;/////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ///[oAuth#EasyLogin]
        initEasyLoginBeforeSetContentView();

        setContentView(R.layout.activity_login);

        initView();
        initListener();

        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        // Note: A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
        ///https://developer.android.com/topic/libraries/architecture/viewmodel
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(false))  ///[EditText显示/隐藏Password]初始化
                .get(LoginViewModel.class);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                btnLogin.setEnabled(loginFormState.isDataValid());

                ///[EditText错误提示]
                if (loginFormState.getUsernameError() == null) {
                    etUsername.setError(null);
                } else {
                    etUsername.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() == null) {
                    etPassword.setError(null);
                } else {
                    etPassword.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getResult().observe(this, new Observer<Result>() {
            @Override
            public void onChanged(@Nullable Result result) {
                if (result == null) {
                    return;
                }
                pbLoading.setVisibility(View.GONE);
                if (result.getError() != null) {
                    ///[Request focus#根据返回错误来请求表单焦点]
                    switch (result.getError()) {
                        case R.string.error_network_error:
                            break;
                        case R.string.error_invalid_parameters:
                            break;
                        case R.string.login_error_invalid_username:
                            etUsername.requestFocus();
                            break;
                        case R.string.login_error_invalid_password:
                            etPassword.requestFocus();
                            break;
                        default:    ///R.string.error_unknown
                    }
                    ///Display login failed
                    showLoginFailed(result.getError());
                } else {
                    if (result.getSuccess() != null)
                        updateUI(result.getSuccess());

                    ///[RememberMe]登陆成功后如果勾选了RememberMe，则保存SharedPreferences为用户名/密码，否则保存为null
                    saveRememberMe(true);

                    setResult(Activity.RESULT_OK);

                    //Complete and destroy login activity once successful
                    finish();
                }
            }
        });

        ///[EditText显示/隐藏Password]
        loginViewModel.getPasswordVisibility().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == null) {
                    return;
                }
                if(aBoolean){
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivPasswordVisibility.setImageResource(R.drawable.ic_visibility);
                }else{
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivPasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
                }
                final String pwd = etPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    etPassword.setSelection(pwd.length());
            }
        });

        ///[RememberMe#记住用户名密码自动登陆]
        initRememberMe();

        ///[oAuth#EasyLogin]
        initEasyLogin();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_username:
                ///[EditText清除输入框]
                etUsername.setText("");
                break;
            case R.id.iv_clear_password:
                ///[EditText清除输入框]
                etPassword.setText("");
                break;
            case R.id.iv_password_visibility:
                ///[EditText显示/隐藏Password]
                ///注意：因为初始化了，所以不会产生NullPointerException
                loginViewModel.setPasswordVisibility(!loginViewModel.getPasswordVisibility().getValue());
                break;
            case R.id.btn_reset:
                ///[ResetPassword]
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.cb_remember_me:
                ///[RememberMe]如果RememberMe未勾选，则保存SharedPreferences的用户名/密码为null
                saveRememberMe(false);
                break;
            case R.id.btn_login:
                pbLoading.setVisibility(View.VISIBLE);
                actionLogin();
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            ///[oAuth#MobService]//////////////////////////
            case R.id.btn_oauth_login:
                pbLoading.setVisibility(View.VISIBLE);

                ///[oAuth#MobService]
                ///http://wiki.mob.com/sdk-share-android-3-0-0/#map-5
//                Platform plat = ShareSDK.getPlatform(QQ.NAME);
//                Platform plat = ShareSDK.getPlatform(Wechat.NAME);
//                Platform plat = ShareSDK.getPlatform(SinaWeibo.NAME);
//                Platform plat = ShareSDK.getPlatform(Facebook.NAME);  ///不建议用Mob！
                Platform plat = ShareSDK.getPlatform(Twitter.NAME);  ///不建议用Mob！
//                Platform plat = ShareSDK.getPlatform(LinkedIn.NAME);
                plat.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
                plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
                plat.setPlatformActionListener(this);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
                if(plat.isClientValid()){
                    Log.d("TAG", "onClick: ");
                    //判断是否存在授权凭条的客户端，true是有客户端，false是无
                }
                if(plat.isAuthValid()){
                    //判断是否已经存在授权状态，可以根据自己的登录逻辑设置
                    Toast.makeText(this, "已经授权过了", Toast.LENGTH_SHORT).show();
                    return;
                }
                ShareSDK.setActivity(this);//抖音登录适配安卓9.0
                plat.showUser(null);    //要数据不要功能，主要体现在不会重复出现授权界面

//                actionOAuthLogin("1","1");
                break;
        }
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        ivClearUsername = findViewById(R.id.iv_clear_username);
        etPassword = findViewById(R.id.et_password);
        ivClearPassword = findViewById(R.id.iv_clear_password);
        ivPasswordVisibility = findViewById(R.id.iv_password_visibility);

        btnResetPassword = findViewById(R.id.btn_reset);
        cbRememberMe = findViewById(R.id.cb_remember_me);

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        pbLoading = findViewById(R.id.pb_loading);

        btnOauthLogin = findViewById(R.id.btn_oauth_login);///////////////////////
    }

    private void initListener() {
        btnOauthLogin.setOnClickListener(this);//////////////////


        ivClearUsername.setOnClickListener(this);
        ivClearPassword.setOnClickListener(this);
        ivPasswordVisibility.setOnClickListener(this);

        btnResetPassword.setOnClickListener(this);
        cbRememberMe.setOnClickListener(this);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                loginViewModel.loginDataChanged(etUsername.getText().toString(),
                        etPassword.getText().toString());

                ///[EditText清除输入框]
                if (!TextUtils.isEmpty(s) && ivClearUsername.getVisibility() == View.GONE) {
                    ivClearUsername.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearUsername.setVisibility(View.GONE);
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                loginViewModel.loginDataChanged(etUsername.getText().toString(),
                        etPassword.getText().toString());

                ///[EditText清除输入框]
                if (!TextUtils.isEmpty(s) && ivClearPassword.getVisibility() == View.GONE) {
                    ivClearPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearPassword.setVisibility(View.GONE);
                }
            }
        });

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    actionLogin();
                }
                return false;
            }
        });
    }

    private void actionLogin() {
        ///[FIX#IME_ACTION_DONE没检验form表单状态]
        if (loginViewModel.getLoginFormState().getValue() != null
                && loginViewModel.getLoginFormState().getValue().isDataValid()) {
            loginViewModel.login(etUsername.getText().toString(),
                    etPassword.getText().toString());
        }
    }

    /**
     * 根据SharedPreferences是否保存用户名/密码来设置RememberMe的初始选择状态
     *
     * 注意：CheckBox跟EditText一样可以在屏幕翻转时记住状态，所以可不必LiveDate！
     * 况且PrefsUtil需要context，最好不用UserRepository
     */
    private void initRememberMe() {
        final String rememberUsername = PrefsUtil.getString(getApplicationContext(), KEY_REMEMBER_USERNAME, null);
        final String rememberPassword = PrefsUtil.getString(getApplicationContext(), KEY_REMEMBER_PASSWORD, null);
        if (!TextUtils.isEmpty(rememberUsername) && !TextUtils.isEmpty(rememberPassword)) {
            etUsername.setText(rememberUsername);
            etPassword.setText(rememberPassword);
            cbRememberMe.setChecked(true);
        }
    }

    /**
     * 保存RememberMe到SharedPreferences
     *
     * @param isSaveNow 如果true，则不立即保存，比如登陆时点击RememberMe后，不立即保存用户名/密码，而是登陆成功后再保存
     */
    private void saveRememberMe(boolean isSaveNow) {
        if (!cbRememberMe.isChecked()) {
            PrefsUtil.putString(getApplicationContext(), KEY_REMEMBER_USERNAME, null);
            PrefsUtil.putString(getApplicationContext(), KEY_REMEMBER_PASSWORD, null);
        } else if (isSaveNow) {
            PrefsUtil.putString(getApplicationContext(), KEY_REMEMBER_USERNAME, etUsername.getText().toString());
            PrefsUtil.putString(getApplicationContext(), KEY_REMEMBER_PASSWORD, etPassword.getText().toString());
        }
    }

    private void updateUI(@StringRes Integer successString) {
        Toast.makeText(getApplicationContext(), successString, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    ///[oAuth#EasyLogin]
    ///Twitter initialization needs to happen before setContentView() if using the LoginButton!
    private void initEasyLoginBeforeSetContentView() {
        easyLogin = EasyLogin.getInstance();

        ///[oAuth#EasyLogin#Twitter]///改用Mob！
        String twitterKey = getString(R.string.twitter_consumer_key);
        String twitterSecret = getString(R.string.twitter_consumer_secret);
        easyLogin.addSocialNetwork(new TwitterNetwork(this, twitterKey, twitterSecret));
    }

    private void initEasyLogin() {
        tvConnectedStatus = (TextView) findViewById(R.id.connected_status);

        ///[oAuth#EasyLogin#Google Sign In]
        easyLogin.addSocialNetwork(new GooglePlusNetwork(this));
        googlePlusNetwork = (GooglePlusNetwork) easyLogin.getSocialNetwork(SocialNetwork.Network.GOOGLE_PLUS);
        googlePlusNetwork.setListener(this);
        sibGoogleSignIn = (SignInButton) findViewById(R.id.sib_google_sign_in);
        googlePlusNetwork.setSignInButton(sibGoogleSignIn);

//        ///[oAuth#EasyLogin#Facebook]///改用Mob！
        List<String> fbScope = Arrays.asList("public_profile", "email");
        easyLogin.addSocialNetwork(new FacebookNetwork(this, fbScope));
        FacebookNetwork facebook = (FacebookNetwork) easyLogin.getSocialNetwork(SocialNetwork.Network.FACEBOOK);
        LoginButton loginButton = (LoginButton) findViewById(R.id.lb_facebook_login);
        facebook.requestLogin(loginButton, this);
//
        ///[oAuth#EasyLogin#Twitter]///改用Mob！
        TwitterNetwork twitter = (TwitterNetwork) easyLogin.getSocialNetwork(SocialNetwork.Network.TWITTER);
        twitter.setAdditionalEmailRequest(true);
        TwitterLoginButton twitterLoginButton = (TwitterLoginButton) findViewById(R.id.tlb_twitter_login);
        twitter.requestLogin(twitterLoginButton, this);

        ///[oAuth#EasyLogin#修改按钮样式]
        ///https://stackoverflow.com/questions/27267809/using-custom-login-button-with-twitter-fabric
        twitterLoginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        twitterLoginButton.setBackgroundResource(R.mipmap.ic_launcher_round);
        twitterLoginButton.setCompoundDrawablePadding(0);
        twitterLoginButton.setPadding(0, 0, 0, 0);
        twitterLoginButton.setText("Login with Twitter");
        twitterLoginButton.setTextSize(18);
    }

    private void actionOAuthLogin(String network, String openId) {
        loginViewModel.oAuthLogin(network, openId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!googlePlusNetwork.isConnected()) {
            googlePlusNetwork.silentSignIn();
        } else {
            sibGoogleSignIn.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatuses();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess(SocialNetwork.Network network) {
        if (network == SocialNetwork.Network.GOOGLE_PLUS) {
            AccessToken token = easyLogin.getSocialNetwork(SocialNetwork.Network.GOOGLE_PLUS).getAccessToken();
            Log.d("TAG", "G+ Login successful: " + token.getToken() + "|||" + token.getEmail());
            sibGoogleSignIn.setEnabled(false);
        } else if (network == SocialNetwork.Network.FACEBOOK) {
            AccessToken token = easyLogin.getSocialNetwork(SocialNetwork.Network.FACEBOOK).getAccessToken();
            Log.d("TAG", "FACEBOOK Login successful: " + token.getToken() + "|||" + token.getEmail());
        } else if (network == SocialNetwork.Network.TWITTER) {
            AccessToken token = easyLogin.getSocialNetwork(SocialNetwork.Network.TWITTER).getAccessToken();
            Log.d("TAG", "TWITTER Login successful: " + token.getToken() + "|||" + token.getEmail());
        }
        updateStatuses();
    }

    @Override
    public void onError(SocialNetwork.Network socialNetwork, String errorMessage) {
        Log.e("TAG", "ERROR!" + socialNetwork + "|||" + errorMessage);
        Toast.makeText(getApplicationContext(), socialNetwork.name() + ": " + errorMessage,
                Toast.LENGTH_SHORT).show();
    }

    private void updateStatuses() {
        StringBuilder content = new StringBuilder();
        for (SocialNetwork socialNetwork : easyLogin.getInitializedSocialNetworks()) {
            content.append(socialNetwork.getNetwork())
                    .append(": ")
                    .append(socialNetwork.isConnected())
                    .append("\n");
        }
        tvConnectedStatus.setText(content.toString());
    }

    public void logoutAllNetworks(View view) {
        for (SocialNetwork socialNetwork : easyLogin.getInitializedSocialNetworks()) {
            socialNetwork.logout();
        }
        updateStatuses();
    }

    ///[oAuth#MobService]
    ///http://wiki.mob.com/sdk-share-android-3-0-0/#map-5
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.d("TAG", "onComplete: ");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.d("TAG", "onError: ");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.d("TAG", "onCancel: ");
    }
}
