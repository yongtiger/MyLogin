package cc.brainbook.android.study.mylogin.config;

public class Config {
//    public static final String REGISTER_URL = "http://192.168.1.108/_study/_login/MyLogin/member/register.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=192.168.1.108";
    public static final String REGISTER_URL = "http://192.168.1.108/_study/_login/MyLogin/member/register.php";

    //    public static final String LOGIN_URL = "http://192.168.1.108/_study/_login/MyLogin/member/login.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=192.168.1.108";
        public static final String LOGIN_URL = "http://192.168.1.108/_study/_login/MyLogin/member/login.php";

//    public static final String LOGOUT_URL = "http://192.168.1.108/_study/_login/MyLogin/member/logout.php?start_debug=1&debug_stop=1&use_remote=1&debug_host=192.168.1.108";
    public static final String LOGOUT_URL = "http://192.168.1.108/_study/_login/MyLogin/member/logout.php";

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

    private Config() {}
}
