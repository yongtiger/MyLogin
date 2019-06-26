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

import java.util.Arrays;
import java.util.List;

import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.EasyLogin;
import cc.brainbook.android.project.login.oauth.listener.OnOauthCompleteListener;
import cc.brainbook.android.project.login.oauth.networks.FacebookNetwork;
import cc.brainbook.android.project.login.oauth.networks.GoogleNetwork;
import cc.brainbook.android.project.login.oauth.networks.MobFacebookNetwork;
import cc.brainbook.android.project.login.oauth.networks.MobLinkedInNetwork;
import cc.brainbook.android.project.login.oauth.networks.MobQQNetwork;
import cc.brainbook.android.project.login.oauth.networks.MobSinaWeiboNetwork;
import cc.brainbook.android.project.login.oauth.networks.MobTwitterNetwork;
import cc.brainbook.android.project.login.oauth.networks.MobWechatNetwork;
import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;
import cc.brainbook.android.project.login.oauth.networks.TwitterNetwork;
import cc.brainbook.android.project.login.resetpassword.ui.ResetPasswordActivity;
import cc.brainbook.android.project.login.result.Result;
import cc.brainbook.android.project.login.useraccount.authentication.ui.register.RegisterActivity;
import cc.brainbook.android.project.login.util.PrefsUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnOauthCompleteListener {
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
    private GoogleNetwork googlePlusNetwork;
    private FacebookNetwork facebookNetwork;
    private TwitterNetwork twitterNetwork;

    ///[oAuth#EasyLogin#Mob]
    private MobFacebookNetwork mobFacebookNetwork;
    private MobTwitterNetwork mobTwitterNetwork;
    private MobLinkedInNetwork mobLinkedInNetwork;
    private MobQQNetwork mobQQNetwork;
    private MobWechatNetwork mobWechatNetwork;
    private MobSinaWeiboNetwork mobSinaWeiboNetwork;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        Toast.makeText(getApplicationContext(), result.getSuccess(), Toast.LENGTH_LONG).show();

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
    }

    private void initListener() {
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

    private void actionLogin() {
        ///[FIX#IME_ACTION_DONE没检验form表单状态]
        if (loginViewModel.getLoginFormState().getValue() != null
                && loginViewModel.getLoginFormState().getValue().isDataValid()) {
            loginViewModel.login(etUsername.getText().toString(),
                    etPassword.getText().toString());
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    /* --------------------- ///[oAuth] --------------------- */
    private void initEasyLogin() {
        tvConnectedStatus = (TextView) findViewById(R.id.connected_status);

        easyLogin = EasyLogin.getInstance();

        ///[oAuth#EasyLogin#Google Sign In]
        final SignInButton sibGoogleSignIn = (SignInButton) findViewById(R.id.sib_google_sign_in);
        googlePlusNetwork = new GoogleNetwork(this, sibGoogleSignIn, this);
        easyLogin.addSocialNetwork(googlePlusNetwork);
        ///注意：因为会根据状态而改变文字或背景颜色，所以不建议修改！
//        sibGoogleSignIn.setSize(SIZE_ICON_ONLY);///https://developers.google.com/android/reference/com/google/android/gms/common/SignInButton.ButtonSize
//        sibGoogleSignIn.setColorScheme(COLOR_DARK);///https://developers.google.com/android/reference/com/google/android/gms/common/SignInButton.ColorScheme

        ///[oAuth#EasyLogin#Facebook]
        final List<String> fbScope = Arrays.asList("public_profile", "email");
        final LoginButton loginButton = (LoginButton) findViewById(R.id.lb_facebook_login);
        facebookNetwork = new FacebookNetwork(this, loginButton, this, fbScope);
        easyLogin.addSocialNetwork(facebookNetwork);
        ///注意：因为会根据状态而改变文字或背景颜色，所以不建议修改！
//        loginButton.setLoginText("Facebook");

        ///[oAuth#EasyLogin#Twitter]
        final TwitterLoginButton twitterLoginButton = (TwitterLoginButton) findViewById(R.id.tlb_twitter_login);
        twitterNetwork = new TwitterNetwork(this, twitterLoginButton, this);
        easyLogin.addSocialNetwork(twitterNetwork);
        ///注意：因为会根据状态而改变文字或背景颜色，所以不建议修改！
//        twitterLoginButton.setText("Twitter");

        ///[oAuth#EasyLogin#MobQQ]
        final Button btnQQLogin = findViewById(R.id.btn_qq_login);
        mobQQNetwork = new MobQQNetwork(this, btnQQLogin, this);
        easyLogin.addSocialNetwork(mobQQNetwork);

        ///[oAuth#EasyLogin#MobWechat]
        final Button btnWechatLogin = findViewById(R.id.btn_wechat_login);
        mobWechatNetwork = new MobWechatNetwork(this, btnWechatLogin, this);
        easyLogin.addSocialNetwork(mobWechatNetwork);

        ///[oAuth#EasyLogin#MobSinaWeibo]
        final Button btnSinaWeiboLogin = findViewById(R.id.btn_sinaweibo_login);
        mobSinaWeiboNetwork = new MobSinaWeiboNetwork(this, btnSinaWeiboLogin, this);
        easyLogin.addSocialNetwork(mobSinaWeiboNetwork);

        ///[oAuth#EasyLogin#MobFacebook]
        final Button btnFacebookLogin = findViewById(R.id.btn_facebook_login);
        mobFacebookNetwork = new MobFacebookNetwork(this, btnFacebookLogin, this);
        easyLogin.addSocialNetwork(mobFacebookNetwork);

        ///[oAuth#EasyLogin#MobTwitter]
        final Button btnTwitterLogin = findViewById(R.id.btn_twitter_login);
        mobTwitterNetwork = new MobTwitterNetwork(this, btnTwitterLogin, this);
        easyLogin.addSocialNetwork(mobTwitterNetwork);

        ///[oAuth#EasyLogin#MobLinkedIn]
        final Button btnLinkedInLogin = findViewById(R.id.btn_linkedin_login);
        mobLinkedInNetwork = new MobLinkedInNetwork(this, btnLinkedInLogin, this);
        easyLogin.addSocialNetwork(mobLinkedInNetwork);

    }

    private void actionOAuthLogin(String network, String openId) {
        loginViewModel.oAuthLogin(network, openId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ///updateUI
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onOauthSuccess(SocialNetwork.Network network, AccessToken accessToken) {
        Log.d("TAG", network + " Oauth successful: " + accessToken.getToken()
                + "|||" + accessToken.getUserId()
                + "|||" + accessToken.getUserName()
                + "|||" + accessToken.getEmail()
                + "|||" + accessToken.getPhotoUrl());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ///updateUI
                updateUI();

                Toast.makeText(getApplicationContext(), network + " Oauth successful", Toast.LENGTH_SHORT).show();
            }
        });

        ///todo ... oAuthLogin()

    }

    @Override
    public void onOauthError(SocialNetwork.Network socialNetwork, String errorMessage) {
        Log.e("TAG", "ERROR!" + socialNetwork + "|||" + errorMessage);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ///updateUI
                updateUI();

                Toast.makeText(getApplicationContext(), socialNetwork.name() + ": " + errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        final StringBuilder content = new StringBuilder();
        for (SocialNetwork socialNetwork : easyLogin.getInitializedSocialNetworks()) {
            content.append(socialNetwork.getNetwork())
                    .append(": ")
                    .append(socialNetwork.isConnected())
                    .append("\n");

            socialNetwork.setButtonEnabled(!socialNetwork.isConnected());

            if (socialNetwork.isConnected()) {
                Log.d("TAG", "updateUI(): " + socialNetwork.getNetwork());
            }

        }

        tvConnectedStatus.setText(content.toString());
    }

}
