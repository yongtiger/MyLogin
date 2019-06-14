package cc.brainbook.android.study.mylogin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cc.brainbook.android.study.mylogin.userauthentication.data.UserRepository;
import cc.brainbook.android.study.mylogin.userauthentication.exception.LogoutException;
import cc.brainbook.android.study.mylogin.userauthentication.interfaces.LogoutCallback;
import cc.brainbook.android.study.mylogin.userauthentication.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOGIN = 1;

    private TextView tvUsername;

    private Button btnLogin;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvUsername = findViewById(R.id.tv_username);

        btnLogin = findViewById(R.id.btn_login);
        btnLogout = findViewById(R.id.btn_logout);

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
                            case -3:
                                error = R.string.error_network_error;
                                break;
                            case -2:
                                error = R.string.error_unknown;
                                break;
                            case -1:
                                error = R.string.logout_error_token_is_invalid_or_expired;
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
            tvUsername.setText(loginRepository.getLoggedInUser().getUsername());
            tvUsername.setVisibility(View.VISIBLE);

            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            tvUsername.setText("");
            tvUsername.setVisibility(View.GONE);

            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

}
