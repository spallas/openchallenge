package com.gnufsociety.openchallenge.mainfrags;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnufsociety.openchallenge.ApiHelper;
import com.gnufsociety.openchallenge.NoConnectionActivity;
import com.gnufsociety.openchallenge.R;
import com.gnufsociety.openchallenge.adapters.ParticipantAdapter;
import com.gnufsociety.openchallenge.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sdc on 1/11/17.
 */

public class FavoriteFragment extends Fragment {

    public static String TAG = "fragment4_favorites";
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @BindView(R.id.favorite_recycler) public RecyclerView followedRecycler;
    @BindView(R.id.favorite_refresh)  public SwipeRefreshLayout refreshFollowed;

    public FavoriteFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected) {
            // already signed in
            Intent intent = new Intent(getActivity(), NoConnectionActivity.class);
            startActivity(intent);
            return null;
        }

        View thisView = inflater.inflate(R.layout.fragment4_favorites, container, false);

        ButterKnife.bind(this, thisView);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        refresh();

        followedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        followedRecycler.addItemDecoration(dividerItemDecoration);

        refreshFollowed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return thisView;
    }

    public void refresh() {
        System.out.println("Called");
        AsyncTask<Void, Void, ArrayList<User>> task = new AsyncTask<Void, Void, ArrayList<User>>() {
            @Override
            protected ArrayList<User> doInBackground(Void... params) {
                System.out.println("Executed");
                ApiHelper api = new ApiHelper();
                return api.getFollowed(currentUser.getUid());
            }

            @Override
            protected void onPostExecute(ArrayList<User> users) {
                followedRecycler.setAdapter(new ParticipantAdapter(users));
            }
        };
        task.execute();
        refreshFollowed.setRefreshing(false);
    }
}
