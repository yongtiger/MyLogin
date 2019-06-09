package cc.brainbook.android.study.mylogin.ui.login.view;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String username;
    //... other data fields that may be accessible to the UI

    public LoggedInUserView(String username) {
        this.username = username;
    }

    public String getUserName() {
        return username;
    }
}
