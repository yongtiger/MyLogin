package cc.brainbook.android.project.login.oauth;

///https://github.com/maksim88/EasyLogin
public class AccessToken {

    private final String token;
    private final String secret;
    private final String userId;
    private final String userName;
    private final String email;
    private final String photoUrl;///[EasyLogin#photoUrl]

    private AccessToken(Builder builder) {
        token = builder.token;
        secret = builder.secret;
        userId = builder.userId;
        userName = builder.userName;
        email = builder.email;
        photoUrl = builder.photoUrl;///[EasyLogin#photoUrl]
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public static class Builder {
        private final String token;
        private String secret;
        private String userId;
        private String userName;
        private String email;
        private String photoUrl;///[EasyLogin#photoUrl]

        public Builder(String token) {
            this.token = token;
        }

        public Builder(AccessToken oldToken) {
            token = oldToken.token;
            secret = oldToken.secret;
            userId = oldToken.userId;
            userName = oldToken.userName;
            email = oldToken.email;
            photoUrl = oldToken.photoUrl;///[EasyLogin#photoUrl]
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }
        ///[EasyLogin#photoUrl]
        public Builder photoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public AccessToken build() {
            return new AccessToken(this);
        }

    }
}
