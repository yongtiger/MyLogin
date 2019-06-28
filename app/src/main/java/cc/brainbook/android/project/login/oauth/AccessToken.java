package cc.brainbook.android.project.login.oauth;

import android.os.Parcel;
import android.os.Parcelable;

///https://github.com/maksim88/EasyLogin
public class AccessToken implements Parcelable {    ///[oAuth#NetworkAccessTokenMap]actionRegister()
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

    protected AccessToken(Parcel in) {
        token = in.readString();
        secret = in.readString();
        userId = in.readString();
        userName = in.readString();
        email = in.readString();
        photoUrl = in.readString();
    }

    public static final Creator<AccessToken> CREATOR = new Creator<AccessToken>() {
        @Override
        public AccessToken createFromParcel(Parcel in) {
            return new AccessToken(in);
        }

        @Override
        public AccessToken[] newArray(int size) {
            return new AccessToken[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(secret);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(photoUrl);
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

    @Override
    public String toString() {
        return "{" +
                "token='" + token + '\'' +
                ", secret='" + secret + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
