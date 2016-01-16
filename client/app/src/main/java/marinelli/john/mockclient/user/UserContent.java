package marinelli.john.mockclient.user;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 1/15/16.
 */
public class UserContent {
    public static final List<UserModel> ITEMS = new ArrayList<>();
    public static final Map<String, UserModel> ITEM_MAP = new HashMap<String, UserModel>();

    static {

        JsonArrayRequest apireq = new JsonArrayRequest(Request.Method.GET,
                "http://arcane-savannah-2535.herokuapp.com",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {

                    }
                }
        );
    }
}
