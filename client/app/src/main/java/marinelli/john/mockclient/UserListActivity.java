package marinelli.john.mockclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import marinelli.john.mockclient.user.UserContent;
import marinelli.john.mockclient.user.UserModel;
import marinelli.john.mockserver.R;

import java.util.List;

/**
 * An activity representing a list of Users. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link UserDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class UserListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        final View recyclerView = findViewById(R.id.user_list);
        assert recyclerView != null;

        JsonArrayRequest apireq = new JsonArrayRequest(Request.Method.GET,
                "http://arcane-savannah-2535.herokuapp.com",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int len = response.length();
                        for (int i = 0; i < len; ++i) {
                            try {
                                JSONObject jsonUser = new JSONObject(response.getString(i));
                                UserModel user = new UserModel(jsonUser);
                                UserContent.ITEMS.add(user);
                                UserContent.ITEM_MAP.put(user.mName, user);
                            } catch (org.json.JSONException e) {
                                System.console().printf("%s", e.toString());
                            }

                        }
                        setupRecyclerView((RecyclerView) recyclerView);
                        mProgressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Toast.makeText(getApplicationContext(), err.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(apireq);

        if (findViewById(R.id.user_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(UserContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<UserModel> mValues;

        public SimpleItemRecyclerViewAdapter(List<UserModel> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            int maxDim = 0;

            // Set profile pic
            ImageRequest imgreq = new ImageRequest(holder.mItem.mProfileImageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            holder.mItem.mProfileImage = response;
                            holder.mProfilePicView.setImageBitmap(response);
                        }
                    }, maxDim, maxDim, ImageView.ScaleType.CENTER, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError err) {
                            Toast.makeText(getApplicationContext(), err.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            );

            holder.mContentView.setText(mValues.get(position).mName);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(UserDetailFragment.ARG_ITEM_ID, holder.mItem.mName);
                        UserDetailFragment fragment = new UserDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.user_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, UserDetailActivity.class);
                        intent.putExtra(UserDetailFragment.ARG_ITEM_ID, holder.mItem.mName);

                        context.startActivity(intent);
                    }
                }
            });

            Volley.newRequestQueue(getApplicationContext()).add(imgreq);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mProfilePicView;
            public final TextView mContentView;
            public UserModel mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
                mProfilePicView = (ImageView) view.findViewById(R.id.profile_pic);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
