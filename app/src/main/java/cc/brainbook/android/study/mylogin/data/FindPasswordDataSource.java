package cc.brainbook.android.study.mylogin.data;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cc.brainbook.android.study.mylogin.data.model.FindPasswordUser;
import cc.brainbook.android.study.mylogin.exception.FindPasswordCheckException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordFindException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordRequestException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordResetException;
import cc.brainbook.android.study.mylogin.exception.FindPasswordSendException;
import cc.brainbook.android.study.mylogin.interfaces.FindPasswordCallback;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cc.brainbook.android.study.mylogin.config.Config.CONNECT_TIMEOUT;
import static cc.brainbook.android.study.mylogin.config.Config.FIND_PASSWORD_CHECK_URL;
import static cc.brainbook.android.study.mylogin.config.Config.FIND_PASSWORD_FIND_URL;

public class FindPasswordDataSource {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_FIND_PASSWORD_USER = "find_password_user";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";

    public void find(String username, FindPasswordCallback.FindCallback findPasswordFindCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the request parameters
            jsonObject.put(KEY_USERNAME, username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(FIND_PASSWORD_FIND_URL)
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
                        findPasswordFindCallback.onError(new FindPasswordFindException(FindPasswordFindException.EXCEPTION_IO_EXCEPTION, e.getCause()));
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
                                        findPasswordFindCallback.onError(new FindPasswordFindException(FindPasswordFindException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        final FindPasswordUser findPasswordUser =  new Gson().fromJson(jsonObject.getString(KEY_FIND_PASSWORD_USER), FindPasswordUser.class);
                                        findPasswordFindCallback.onSuccess(findPasswordUser);
                                        break;
                                    case 1:
                                        findPasswordFindCallback.onError(new FindPasswordFindException(FindPasswordFindException.EXCEPTION_INVALID_USERNAME, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 2:
                                        findPasswordFindCallback.onError(new FindPasswordFindException(FindPasswordFindException.EXCEPTION_CANNOT_FIND_PASSWORD, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        findPasswordFindCallback.onError(new FindPasswordFindException(FindPasswordFindException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void check(String userId, String email, String mobile, FindPasswordCallback.CheckCallback findPasswordCheckCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the request parameters
            jsonObject.put(KEY_USER_ID, userId);
            jsonObject.put(KEY_EMAIL, email);
            jsonObject.put(KEY_MOBILE, mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(FIND_PASSWORD_CHECK_URL)
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
                        findPasswordCheckCallback.onError(new FindPasswordCheckException(FindPasswordCheckException.EXCEPTION_IO_EXCEPTION, e.getCause()));
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
                                        findPasswordCheckCallback.onError(new FindPasswordCheckException(FindPasswordCheckException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        findPasswordCheckCallback.onSuccess();
                                        break;
                                    case 1:
                                        findPasswordCheckCallback.onError(new FindPasswordCheckException(FindPasswordCheckException.EXCEPTION_INVALID_USER_ID, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 2:
                                        findPasswordCheckCallback.onError(new FindPasswordCheckException(FindPasswordCheckException.EXCEPTION_CANNOT_FIND_PASSWORD, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 3:
                                        findPasswordCheckCallback.onError(new FindPasswordCheckException(FindPasswordCheckException.EXCEPTION_NO_MATCH_EMAIL, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 4:
                                        findPasswordCheckCallback.onError(new FindPasswordCheckException(FindPasswordCheckException.EXCEPTION_NO_MATCH_MOBILE, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 5:
                                        findPasswordCheckCallback.onError(new FindPasswordCheckException(FindPasswordCheckException.EXCEPTION_NO_MATCH_EMAIL_OR_MOBILE, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        findPasswordCheckCallback.onError(new FindPasswordCheckException(FindPasswordCheckException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void request(String userId, FindPasswordCallback.RequestCallback findPasswordRequestCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the request parameters
            jsonObject.put(KEY_USER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(FIND_PASSWORD_FIND_URL)
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
                        findPasswordRequestCallback.onError(new FindPasswordRequestException(FindPasswordRequestException.EXCEPTION_IO_EXCEPTION, e.getCause()));
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
                                        findPasswordRequestCallback.onError(new FindPasswordRequestException(FindPasswordRequestException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        findPasswordRequestCallback.onSuccess();
                                        break;
                                    case 1:
                                        findPasswordRequestCallback.onError(new FindPasswordRequestException(FindPasswordRequestException.EXCEPTION_INVALID_USER_ID, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 2:
                                        findPasswordRequestCallback.onError(new FindPasswordRequestException(FindPasswordRequestException.EXCEPTION_CANNOT_FIND_PASSWORD, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 3:
                                        findPasswordRequestCallback.onError(new FindPasswordRequestException(FindPasswordRequestException.EXCEPTION_NO_MATCH_EMAIL, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 4:
                                        findPasswordRequestCallback.onError(new FindPasswordRequestException(FindPasswordRequestException.EXCEPTION_NO_MATCH_MOBILE, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        findPasswordRequestCallback.onError(new FindPasswordRequestException(FindPasswordRequestException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void send(String userId, String verificationCode, FindPasswordCallback.SendCallback findPasswordSendCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the request parameters
            jsonObject.put(KEY_USER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(FIND_PASSWORD_FIND_URL)
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
                        findPasswordSendCallback.onError(new FindPasswordSendException(FindPasswordSendException.EXCEPTION_IO_EXCEPTION, e.getCause()));
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
                                        findPasswordSendCallback.onError(new FindPasswordSendException(FindPasswordSendException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        findPasswordSendCallback.onSuccess();
                                        break;
                                    case 1:
                                        findPasswordSendCallback.onError(new FindPasswordSendException(FindPasswordSendException.EXCEPTION_INVALID_USER_ID, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 2:
                                        findPasswordSendCallback.onError(new FindPasswordSendException(FindPasswordSendException.EXCEPTION_CANNOT_FIND_PASSWORD, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 3:
                                        findPasswordSendCallback.onError(new FindPasswordSendException(FindPasswordSendException.EXCEPTION_NO_MATCH_EMAIL, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 4:
                                        findPasswordSendCallback.onError(new FindPasswordSendException(FindPasswordSendException.EXCEPTION_NO_MATCH_MOBILE, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        findPasswordSendCallback.onError(new FindPasswordSendException(FindPasswordSendException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void reset(String userId, String password, FindPasswordCallback.ResetCallback findPasswordResetCallback) {
        ///https://stackoverflow.com/questions/34179922/okhttp-post-body-as-json
        final JSONObject jsonObject = new JSONObject();
        try {
            //Populate the request parameters
            jsonObject.put(KEY_USER_ID, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 构建post的RequestBody
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        // 创建一个Request
        final Request request = new Request.Builder()
                .url(FIND_PASSWORD_FIND_URL)
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
                        findPasswordResetCallback.onError(new FindPasswordResetException(FindPasswordResetException.EXCEPTION_IO_EXCEPTION, e.getCause()));
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
                                        findPasswordResetCallback.onError(new FindPasswordResetException(FindPasswordResetException.EXCEPTION_INVALID_PARAMETERS, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    case 0:
                                        findPasswordResetCallback.onSuccess();
                                        break;
                                    case 1:
                                        findPasswordResetCallback.onError(new FindPasswordResetException(FindPasswordResetException.EXCEPTION_INVALID_USER_ID, jsonObject.getString(KEY_MESSAGE)));
                                        break;
                                    default:
                                        findPasswordResetCallback.onError(new FindPasswordResetException(FindPasswordResetException.EXCEPTION_UNKNOWN));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
