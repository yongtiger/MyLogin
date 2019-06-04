package cc.brainbook.android.study.mylogin;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    private String login_url = "http://192.168.1.108/_study/_login/MyLogin/member/login.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=192.168.1.108";
//    private String login_url = "http://192.168.1.108/_study/_login/MyLogin/member/login.php";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        if(session.isLoggedIn()){
            loadDashboard();
        }
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        Button register = findViewById(R.id.login);
        Button login = findViewById(R.id.login);

        //Launch Registration screen when Register Button is clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                username = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }

    /**
     * Launch Dashboard Activity on Successful Login
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
        finish();

    }

    /**
     * Display Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();

        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        JSONObject jsonObject = new JSONObject();
        try {
            //Populate the request parameters
            jsonObject.put(KEY_USERNAME, username);
            jsonObject.put(KEY_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //构建post的RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建okHttpClient对象
        final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build();


        // 创建一个Request
        final Request request = new Request.Builder()
                .url(login_url)
                .post(requestBody)
                .build();
        // new call
        final Call call = mOkHttpClient.newCall(request);
        // 请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // OkHttp3在子线程更新UI线程
                // https://www.jianshu.com/p/5c1cddf2c46b
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error: HTTP请求失败！onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonStr =  response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pDialog.dismiss();
                try {
                    //Check if user got logged in successfully
                    if (jsonObject.getInt(KEY_STATUS) == 0) {
                        session.loginUser(username,jsonObject.getString(KEY_FULL_NAME));
                        loadDashboard();

                    }else{
                        Toast.makeText(getApplicationContext(),
                                jsonObject.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if(KEY_EMPTY.equals(username)){
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
}