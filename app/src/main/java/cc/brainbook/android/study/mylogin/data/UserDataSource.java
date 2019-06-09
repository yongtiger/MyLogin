package cc.brainbook.android.study.mylogin.data;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cc.brainbook.android.study.mylogin.exception.LoginException;
import cc.brainbook.android.study.mylogin.exception.LogoutException;
import cc.brainbook.android.study.mylogin.exception.RegisterException;
import cc.brainbook.android.study.mylogin.interfaces.RegisterCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cc.brainbook.android.study.mylogin.interfaces.LoginCallback;
import cc.brainbook.android.study.mylogin.interfaces.LogoutCallback;
import cc.brainbook.android.study.mylogin.data.model.LoggedInUser;

import static cc.brainbook.android.study.mylogin.config.Config.CONNECT_TIMEOUT;
import static cc.brainbook.android.study.mylogin.config.Config.LOGIN_URL;
import static cc.brainbook.android.study.mylogin.config.Config.LOGOUT_URL;
import static cc.brainbook.android.study.mylogin.config.Config.REGISTER_URL;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class UserDataSource {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USER = "user";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    public void register(String username, String password, RegisterCallback registerCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the request parameters
            jsonObject.put(KEY_USERNAME, username);
            jsonObject.put(KEY_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(requestBody)
                .build();

        // 创建okHttpClient对象
        final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        // 请求加入调度
        mOkHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ///[返回结果及错误处理]错误处理
                        registerCallback.onError(new RegisterException(RegisterException.EXCEPTION_IO_EXCEPTION, e.getCause()));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = null;
                            if (response.body() != null) {
                                jsonObject = new JSONObject(response.body().string());
                            }

                            //Check if user got logged in successfully
                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -1:
                                        registerCallback.onError(new RegisterException(RegisterException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        registerCallback.onSuccess();
                                        break;
                                    case 1:
                                        registerCallback.onError(new RegisterException(RegisterException.EXCEPTION_USER_EXISTS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        registerCallback.onError(new RegisterException(RegisterException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void login(String username, String password, LoginCallback loginCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the request parameters
            jsonObject.put(KEY_USERNAME, username);
            jsonObject.put(KEY_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(requestBody)
                .build();

        // 创建okHttpClient对象
        final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        // 请求加入调度
        mOkHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ///[返回结果及错误处理]错误处理
                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_IO_EXCEPTION, e.getCause()));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = null;
                            if (response.body() != null) {
                                jsonObject = new JSONObject(response.body().string());
                            }

                            //Check if user got logged in successfully
                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -1:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        final LoggedInUser loggedInUser =  new Gson().fromJson(jsonObject.getString(KEY_USER), LoggedInUser.class);
                                        loginCallback.onSuccess(loggedInUser);
                                        break;
                                    case 1:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_INVALID_USERNAME, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 2:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_INVALID_PASSWORD, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void logout(LoggedInUser loggedInUser, LogoutCallback logoutCallback) {
        // 创建一个Request
        final Request request = new Request.Builder()
                .url(LOGOUT_URL)
                .header(KEY_TOKEN, loggedInUser.getToken())
                .build();

        // 创建okHttpClient对象
        final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        // 请求加入调度
        mOkHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ///[返回结果及错误处理]错误处理
                        logoutCallback.onError(new LogoutException(LogoutException.EXCEPTION_IO_EXCEPTION, e.getCause()));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = null;
                            if (response.body() != null) {
                                jsonObject = new JSONObject(response.body().string());
                            }

                            //Check if user got logged out successfully
                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -1:
                                        ///注意：如果没有登录，也返回正常状态0
//                                        logoutCallback.onError(new LogoutException(LogoutException.EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED, jsonObject.getString(KEY_MESSAGE)));
                                        logoutCallback.onSuccess();
                                        break;
                                    case 0:
                                        logoutCallback.onSuccess();
                                        break;
                                    default:
                                        logoutCallback.onError(new LogoutException(LogoutException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
