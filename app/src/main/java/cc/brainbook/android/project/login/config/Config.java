package cc.brainbook.android.project.login.config;

import com.amazonaws.regions.Regions;

public class Config {
    public static final String HTTP_DOMAIN = "http://192.168.1.104";
    public static final String DEBUG_HOST = "192.168.1.104";

//    public static final String REGISTER_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/register.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String REGISTER_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/register.php";

//        public static final String LOGIN_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/login.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
        public static final String LOGIN_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/login.php";

//    public static final String LOGOUT_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/logout.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String LOGOUT_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/logout.php";

//    public static final int CONNECT_TIMEOUT = 10;
    public static final int CONNECT_TIMEOUT = 300;  ///test

    public static final String REGEXP_USERNAME = "^[a-zA-Z0-9._-]{6,15}$";

    ///https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
//    "^                 # start-of-string\n" +
//    "(?=.*[0-9])       # a digit must occur at least once\n" +
//    "(?=.*[a-z])       # a lower case letter must occur at least once\n" +
//    "(?=.*[A-Z])       # an upper case letter must occur at least once\n" +
//    "(?=.*[@#$%^&+=])  # a special character must occur at least once\n" +
//    "(?=\\S+$)          # no whitespace allowed in the entire string\n" +
//    ".{8,}             # anything, at least eight places though\n" +
//    "$                 # end-of-string
//    public static final String REGEXP_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
    public static final String REGEXP_PASSWORD = "^(?=\\S+$).{6,}$";   ///test


    /* ---------------- reset password ---------------- */
    //        public static final String RESET_PASSWORD_FIND_USER_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/find_user.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String RESET_PASSWORD_FIND_USER_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/find_user.php";

    //    public static final String RESET_PASSWORD_CHECK_SEND_MODE_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/check_send_mode.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String RESET_PASSWORD_CHECK_SEND_MODE_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/check_send_mode.php";

    //    public static final String RESET_PASSWORD_SEND_VERIFICATION_CODE_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/send_verification_code.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String RESET_PASSWORD_SEND_VERIFICATION_CODE_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/send_verification_code.php";

    //    public static final String RESET_PASSWORD_VERIFY_CODE_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/verify_code.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String RESET_PASSWORD_VERIFY_CODE_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/verify_code.php";

//    public static final String RESET_PASSWORD_RESET_PASSWORD_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/reset_password.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String RESET_PASSWORD_RESET_PASSWORD_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/reset_password/reset_password.php";

    public static final String REGEXP_VERIFICATION_CODE = "^\\d{4}$";


    /* ---------------- user account ---------------- */
//    public static final String USER_ACCOUNT_MODIFY_USERNAME_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/modify_username.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String USER_ACCOUNT_MODIFY_USERNAME_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/modify_username.php";

//    public static final String USER_ACCOUNT_MODIFY_PASSWORD_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/modify_password.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String USER_ACCOUNT_MODIFY_PASSWORD_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/modify_password.php";

//    public static final String USER_ACCOUNT_MODIFY_EMAIL_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/modify_email.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String USER_ACCOUNT_MODIFY_EMAIL_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/modify_email.php";

//    public static final String USER_ACCOUNT_MODIFY_MOBILE_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/modify_mobile.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String USER_ACCOUNT_MODIFY_MOBILE_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/modify_mobile.php";


    /* ---------------- oAuth ---------------- */
//        public static final String OAUTH_LOGIN_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/oauth_login.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String OAUTH_LOGIN_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/oauth_login.php";

//    public static final String OAUTH_UNBIND_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/oauth_unbind.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=" + DEBUG_HOST;
    public static final String OAUTH_UNBIND_URL = HTTP_DOMAIN + "/_study/_login/MyLogin/member/user_account/oauth_unbind.php";


    /* ---------------- ///[avatar#上传#AWS S3 Transfer Utility] ---------------- */
    ///https://blog.csdn.net/codehxy/article/details/78105321
    /*
     * You should replace these values with your own. See the README for details
     * on what to fill in.
     */
    ///https://docs.aws.amazon.com/cognito/latest/developerguide/identity-pools.html
    ///https://console.aws.amazon.com/cognito/home
    public static final String COGNITO_POOL_ID = "ap-northeast-1:bffb22a3-e53a-4797-abbf-fd7f5ab00c91";

    /*
     * Region of your Cognito identity pool ID.
     */
    public static final String COGNITO_POOL_REGION = "ap-northeast-1";
//    public static final String COGNITO_POOL_REGION = Regions.AP_NORTHEAST_1.getName();

    /*
     * Note, you must first create a bucket using the S3 console before running
     * the sample (https://console.aws.amazon.com/s3/). After creating a bucket,
     * put it's name in the field below.
     */
    public static final String BUCKET_NAME = "androidlogin";

    /*
     * Region of your bucket.
     * https://docs.aws.amazon.com/zh_cn/general/latest/gr/rande.html
     */
    public static final String BUCKET_REGION = "ap-northeast-1";
//    public static final String BUCKET_REGION = Regions.AP_NORTHEAST_1.getName();


    private Config() {}
}
