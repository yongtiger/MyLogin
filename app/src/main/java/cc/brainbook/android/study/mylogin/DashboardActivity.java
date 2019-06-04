package cc.brainbook.android.study.mylogin;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

///https://github.com/androidigniter/Login-and-Registration-in-Android-with-PHP-and-MySQL
public class DashboardActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView welcomeText = findViewById(R.id.welcomeText);



        Button logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new SessionHandler(getApplicationContext());
//                User user = session.getUserDetails();
                session.logoutUser();
                Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

                doHttpRequest("http://192.168.1.108/_study/_login/MyLogin/member/logout.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=192.168.1.108",
                        "5bkrnm37no99nubdm89merlkb7",
                        new HttpRequestCallback() {
                            @Override
                            public void doCallback(String message) {
                                Toast.makeText(getApplicationContext(), "Logout " + message, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        doHttpRequest("http://192.168.1.108/_study/_login/MyLogin/member/dash.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=192.168.1.108",
                "5bkrnm37no99nubdm89merlkb7",
                new HttpRequestCallback() {
                    @Override
                    public void doCallback(String message) {
                        welcomeText.setText("Welcome " + message);
                    }
                });
    }

    interface HttpRequestCallback {
        void doCallback(String message);
    }

    private void doHttpRequest(String url, String token, HttpRequestCallback httpRequestCallback) {
        // 创建okHttpClient对象
        final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build();

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .header("token", token)
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

                // OkHttp3在子线程更新UI线程
                // https://www.jianshu.com/p/5c1cddf2c46b
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(jsonStr);

                            //Check if user got logged in successfully
                            if (jsonObject.getInt(KEY_STATUS) == 0) {

                                httpRequestCallback.doCallback(jsonObject.getString("message"));

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
        });
    }
}