package cc.brainbook.android.study.mylogin.data.model;

/**
 * Data class that captures user information for logged in users retrieved from UserRepository
 */
public class LoggedInUser {
    private String userId;
    private String username;
    private String token;
    private long tokenExpiredAt;
    private long createdAt;
    private long updatedAt;

    public LoggedInUser(String userId,
                        String username,
                        String token,
                        long tokenExpiredAt,
                        long createdAt,
                        long updatedAt) {
        this.userId = userId;
        this.username = username;
        this.token = token;
        this.tokenExpiredAt = tokenExpiredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpiredAt() {
        return tokenExpiredAt;
    }

    public void setTokenExpiredAt(long tokenExpiredAt) {
        this.tokenExpiredAt = tokenExpiredAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "LoggedInUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", tokenExpiredAt='" + tokenExpiredAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
