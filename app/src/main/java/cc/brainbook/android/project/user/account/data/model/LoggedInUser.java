package cc.brainbook.android.project.user.account.data.model;

/**
 * Data class that captures user information for logged in users retrieved from UserRepository
 */
public class LoggedInUser {
    private String user_id;
    private String username;
    private String email;
    private String mobile;
    private String avatar;
    private String token;
    private long token_expired_at;
    private long created_at;
    private long updated_at;

    public LoggedInUser(String user_id,
                        String username,
                        String email,
                        String mobile,
                        String avatar,
                        String token,
                        long token_expired_at,
                        long created_at,
                        long updated_at) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.avatar = avatar;
        this.token = token;
        this.token_expired_at = token_expired_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getUserId() {
        return user_id;
    }
    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpiredAt() {
        return token_expired_at;
    }

    public void setTokenExpiredAt(long token_expired_at) {
        this.token_expired_at = token_expired_at;
    }

    public long getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(long updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "LoggedInUser{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avatar='" + avatar + '\'' +
                ", token='" + token + '\'' +
                ", token_expired_at='" + token_expired_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
