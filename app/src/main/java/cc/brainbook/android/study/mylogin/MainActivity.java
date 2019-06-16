package cc.brainbook.android.study.mylogin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.allen.library.SuperTextView;

import cc.brainbook.android.study.mylogin.useraccount.modify.ModifyActivity;
import cc.brainbook.android.study.mylogin.useraccount.data.UserRepository;
import cc.brainbook.android.study.mylogin.useraccount.authentication.exception.LogoutException;
import cc.brainbook.android.study.mylogin.useraccount.authentication.interfaces.LogoutCallback;
import cc.brainbook.android.study.mylogin.useraccount.authentication.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOGIN = 1;

    private LinearLayout llLoggedOut;
    private LinearLayout llLoggedIn;
    private SuperTextView stvUserAccount;
    private SuperTextView stvUserProfile;

    private Button btnLogin;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llLoggedOut = findViewById(R.id.ll_logged_out);
        llLoggedIn = findViewById(R.id.ll_logged_in);
        stvUserAccount = findViewById(R.id.stv_user_account);
        stvUserProfile = findViewById(R.id.stv_user_profile);

        btnLogin = findViewById(R.id.btn_login);
        btnLogout = findViewById(R.id.btn_logout);

        stvUserAccount.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                ///
                startActivity(new Intent(MainActivity.this, ModifyActivity.class));
            }
        });
        stvUserProfile.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                ///
//                startActivity(new Intent(MainActivity.this, UserAccountProfile.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_CODE_LOGIN);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserRepository userRepository = UserRepository.getInstance();
                userRepository.logout(new LogoutCallback() {
                    @Override
                    public void onSuccess() {
                        ///[返回结果及错误处理]返回结果
                        ///注意：要先获得user！否则会被随后执行的UserRepository#setLoggedInUser(null)清空
                        final String username = userRepository.getLoggedInUser().getUsername();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Logout! " + username, Toast.LENGTH_SHORT).show();

                                ///Update UI
                                updateUi();
                            }
                        });
                    }

                    @Override
                    public void onError(LogoutException e) {
                        ///[返回结果及错误处理]错误处理
                        int error;
                        switch (e.getCode()) {
                            case -4:
                                error = R.string.error_token_is_invalid_or_expired;
                                break;
                            case -3:
                                error = R.string.error_network_error;
                                break;
                            case -2:
                                error = R.string.error_unknown;
                                break;
                            case -1:
                                error = R.string.error_invalid_parameters;
                                break;
                            default:
                                error = R.string.error_unknown;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getString(error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        ///Update UI
        updateUi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                final UserRepository loginRepository = UserRepository.getInstance();
                if (loginRepository.isLoggedIn()) {
                    Toast.makeText(getApplicationContext(), "Login! " + loginRepository.getLoggedInUser().getUsername(), Toast.LENGTH_SHORT).show();

                    ///Update UI
                    updateUi();
                }
            }
        }
    }

    private void updateUi() {
        final UserRepository loginRepository = UserRepository.getInstance();
        if (loginRepository.isLoggedIn()) {
            llLoggedIn.setVisibility(View.VISIBLE);
            llLoggedOut.setVisibility(View.GONE);
        } else {
            llLoggedIn.setVisibility(View.GONE);
            llLoggedOut.setVisibility(View.VISIBLE);
        }
    }

}
