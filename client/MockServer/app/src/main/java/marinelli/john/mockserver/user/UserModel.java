package marinelli.john.mockserver.user;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Model for user class.
 */
public class UserModel {
    public String mName;
    public String mEmail = "";
    public String mCCN = "";
    public String mProfileImageUrl = "";
    public int mAge = 0;
    public String mBio = "";
    public Date mCreatedAt = new Date();

    public UserModel(final JSONObject json) {
        try {
            this.mName = json.getString("name");
            this.mEmail = json.getString("email");
            this.mCCN = json.getString("ccn");
            this.mProfileImageUrl = json.getString("profile_image");
            this.mAge = json.getInt("age");
            this.mBio = json.getString("bio");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            this.mCreatedAt = dateFormat.parse(json.getString("created_at"));
        } catch (org.json.JSONException|java.text.ParseException e) {

        }
    }

    @Override
    public String toString() {
        return mName;
    }
}
