package cc.brainbook.android.project.login.useraccount.data;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cc.brainbook.android.project.login.oauth.AccessToken;
import cc.brainbook.android.project.login.oauth.networks.SocialNetwork;
import cc.brainbook.android.project.login.useraccount.authentication.exception.LogoutException;
import cc.brainbook.android.project.login.useraccount.modify.exception.ModifyException;
import cc.brainbook.android.project.login.useraccount.modify.interfaces.ModifyCallback;
import cc.brainbook.android.project.login.useraccount.authentication.exception.LoginException;
import cc.brainbook.android.project.login.useraccount.authentication.exception.RegisterException;
import cc.brainbook.android.project.login.useraccount.authentication.interfaces.RegisterCallback;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cc.brainbook.android.project.login.useraccount.authentication.interfaces.LoginCallback;
import cc.brainbook.android.project.login.useraccount.authentication.interfaces.LogoutCallback;
import cc.brainbook.android.project.login.useraccount.data.model.LoggedInUser;

import static cc.brainbook.android.project.login.config.Config.CONNECT_TIMEOUT;
import static cc.brainbook.android.project.login.config.Config.LOGIN_URL;
import static cc.brainbook.android.project.login.config.Config.LOGOUT_URL;
import static cc.brainbook.android.project.login.config.Config.OAUTH_LOGIN_URL;
import static cc.brainbook.android.project.login.config.Config.REGISTER_URL;
import static cc.brainbook.android.project.login.config.Config.USER_ACCOUNT_MODIFY_EMAIL_URL;
import static cc.brainbook.android.project.login.config.Config.USER_ACCOUNT_MODIFY_MOBILE_URL;
import static cc.brainbook.android.project.login.config.Config.USER_ACCOUNT_MODIFY_PASSWORD_URL;
import static cc.brainbook.android.project.login.config.Config.USER_ACCOUNT_MODIFY_USERNAME_URL;

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
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_NETWORK = "network";
    private static final String KEY_OPEN_ID = "openId";
    private static final String KEY_NETWORK_ACCESS_TOKEN_MAP = "networkAccessTokenMap";

    public void register(String username, String password, final RegisterCallback registerCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the sendVerificationCode parameters
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

                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -3:
                                        registerCallback.onError(new RegisterException(RegisterException.EXCEPTION_IO_EXCEPTION, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -2:
                                        registerCallback.onError(new RegisterException(RegisterException.EXCEPTION_UNKNOWN, jsonObject.getString(KEY_MESSAGE)));
                                        break;
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

    public void login(String username, String password,
                      HashMap<SocialNetwork.Network, AccessToken> networkAccessTokenMap, final LoginCallback loginCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the sendVerificationCode parameters
            jsonObject.put(KEY_USERNAME, username);
            jsonObject.put(KEY_PASSWORD, password);
            jsonObject.put(KEY_NETWORK_ACCESS_TOKEN_MAP, new Gson().toJson(networkAccessTokenMap)); ///using Gson to convert Map to Json
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

                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -3:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_IO_EXCEPTION, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -2:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_UNKNOWN, jsonObject.getString(KEY_MESSAGE)));
                                        break;
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

    public void logout(LoggedInUser loggedInUser, final LogoutCallback logoutCallback) {
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
                        logoutCallback.onError(new LogoutException(LogoutException.EXCEPTION_IO_EXCEPTION, e.getCause()));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = null;
                            if (response.body() != null) {
                                jsonObject = new JSONObject(response.body().string());
                            }

                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -3:
                                        logoutCallback.onError(new LogoutException(LogoutException.EXCEPTION_IO_EXCEPTION, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -2:
                                        logoutCallback.onError(new LogoutException(LogoutException.EXCEPTION_UNKNOWN, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -1:
                                        logoutCallback.onError(new LogoutException(LogoutException.EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED, jsonObject.getString(KEY_MESSAGE)));
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


    /* --------------------- ///[Modify Account] --------------------- */
    public void modifyUsername(LoggedInUser loggedInUser, String username, final ModifyCallback modifyCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the sendVerificationCode parameters
            jsonObject.put(KEY_USERNAME, username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(USER_ACCOUNT_MODIFY_USERNAME_URL)
                .header(KEY_TOKEN, loggedInUser.getToken())
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
                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_IO_EXCEPTION, e.getCause()));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = null;
                            if (response.body() != null) {
                                jsonObject = new JSONObject(response.body().string());
                            }

                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -4:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -3:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_IO_EXCEPTION, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -2:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_UNKNOWN, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -1:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        modifyCallback.onSuccess();
                                        break;
                                    case 1:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_FAILED_TO_MODIFY_USERNAME, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void modifyPassword(LoggedInUser loggedInUser, String password, final ModifyCallback modifyCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the sendVerificationCode parameters
            jsonObject.put(KEY_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(USER_ACCOUNT_MODIFY_PASSWORD_URL)
                .header(KEY_TOKEN, loggedInUser.getToken())
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
                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_IO_EXCEPTION, e.getCause()));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = null;
                            if (response.body() != null) {
                                jsonObject = new JSONObject(response.body().string());
                            }

                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -4:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -3:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_IO_EXCEPTION, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -2:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_UNKNOWN, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -1:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        modifyCallback.onSuccess();
                                        break;
                                    case 1:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_FAILED_TO_MODIFY_PASSWORD, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void modifyEmail(LoggedInUser loggedInUser, String email, final ModifyCallback modifyCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the sendVerificationCode parameters
            jsonObject.put(KEY_EMAIL, email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(USER_ACCOUNT_MODIFY_EMAIL_URL)
                .header(KEY_TOKEN, loggedInUser.getToken())
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
                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_IO_EXCEPTION, e.getCause()));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = null;
                            if (response.body() != null) {
                                jsonObject = new JSONObject(response.body().string());
                            }

                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -4:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -3:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_IO_EXCEPTION, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -2:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_UNKNOWN, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -1:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        modifyCallback.onSuccess();
                                        break;
                                    case 1:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_FAILED_TO_MODIFY_EMAIL, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void modifyMobile(LoggedInUser loggedInUser, String mobile, final ModifyCallback modifyCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the sendVerificationCode parameters
            jsonObject.put(KEY_MOBILE, mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(USER_ACCOUNT_MODIFY_MOBILE_URL)
                .header(KEY_TOKEN, loggedInUser.getToken())
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
                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_IO_EXCEPTION, e.getCause()));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = null;
                            if (response.body() != null) {
                                jsonObject = new JSONObject(response.body().string());
                            }

                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -4:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -3:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_IO_EXCEPTION, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -2:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_UNKNOWN, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -1:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        modifyCallback.onSuccess();
                                        break;
                                    case 1:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_FAILED_TO_MODIFY_MOBILE, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        modifyCallback.onError(new ModifyException(ModifyException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /* --------------------- ///[oAuth] --------------------- */
    ///[oAuth#oAuthLogin()]
    public void oAuthLogin(SocialNetwork.Network network, AccessToken accessToken,
                           HashMap<SocialNetwork.Network, AccessToken> networkAccessTokenMap, final LoginCallback loginCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the sendVerificationCode parameters
            jsonObject.put(KEY_NETWORK, network);
            jsonObject.put(KEY_OPEN_ID, accessToken.getUserId());
            jsonObject.put(KEY_NETWORK_ACCESS_TOKEN_MAP, new Gson().toJson(networkAccessTokenMap)); ///using Gson to convert Map to Json
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(OAUTH_LOGIN_URL)
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

                            if (jsonObject != null) {
                                ///[返回结果及错误处理]
                                switch (jsonObject.getInt(KEY_STATUS)) {
                                    case -3:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_IO_EXCEPTION, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -2:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_UNKNOWN, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case -1:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        final LoggedInUser loggedInUser =  new Gson().fromJson(jsonObject.getString(KEY_USER), LoggedInUser.class);
                                        loginCallback.onSuccess(loggedInUser);
                                        break;
                                    case 1:
                                        loginCallback.onError(new LoginException(LoginException.EXCEPTION_INVALID_OAUTH_NETWORK_AND_OPENID, jsonObject.getString(KEY_MESSAGE)));
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

}
