package cc.brainbook.android.project.login.oauth;

import android.os.Parcel;
import android.os.Parcelable;

///https://github.com/maksim88/EasyLogin
public class AccessToken implements Parcelable {    ///[oAuth#NetworkAccessTokenMap]actionRegister()
    private final String token;
    private final String secret;
    private final String network;
    private final String open_id;
    private final String username;
    private final String email;
    private final String avatar;///[EasyLogin#avatar]

    private AccessToken(Builder builder) {
        token = builder.token;
        secret = builder.secret;
        network = builder.network;
        open_id = builder.open_id;
        username = builder.username;
        email = builder.email;
        avatar = builder.avatar;///[EasyLogin#avatar]
    }

    protected AccessToken(Parcel in) {
        token = in.readString();
        secret = in.readString();
        network = in.readString();
        open_id = in.readString();
        username = in.readString();
        email = in.readString();
        avatar = in.readString();
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

    public String getNetwork() {
        return network;
    }

    public String getOpenId() {
        return open_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeString(secret);
        dest.writeString(network);
        dest.writeString(open_id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(avatar);
    }

    public static class Builder {
        private final String token;
        private String secret;
        private String network;
        private String open_id;
        private String username;
        private String email;
        private String avatar;///[EasyLogin#avatar]

        public Builder(String token) {
            this.token = token;
        }

        public Builder(AccessToken oldToken) {
            token = oldToken.token;
            secret = oldToken.secret;
            network = oldToken.network;
            open_id = oldToken.open_id;
            username = oldToken.username;
            email = oldToken.email;
            avatar = oldToken.avatar;///[EasyLogin#avatar]
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder network(String network) {
            this.network = network;
            return this;
        }

        public Builder openId(String openId) {
            this.open_id = openId;
            return this;
        }

        public Builder username(String userName) {
            this.username = userName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }
        ///[EasyLogin#avatar]
        public Builder avatar(String avatar) {
            this.avatar = avatar;
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
                ", open_id='" + open_id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
