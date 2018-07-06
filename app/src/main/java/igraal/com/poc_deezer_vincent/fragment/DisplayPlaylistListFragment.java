package igraal.com.poc_deezer_vincent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import igraal.com.poc_deezer_vincent.R;
import igraal.com.poc_deezer_vincent.Tools;
import igraal.com.poc_deezer_vincent.activity.DisplayPlaylistActivity;
import igraal.com.poc_deezer_vincent.adapter.AdapterIdCallBack;
import igraal.com.poc_deezer_vincent.adapter.AdapterLoadMore;
import igraal.com.poc_deezer_vincent.adapter.PlaylistCardViewAdapter;
import igraal.com.poc_deezer_vincent.manager.UserManager;
import igraal.com.poc_deezer_vincent.object.realmobject.RealmPlaylist;
import igraal.com.poc_deezer_vincent.object.realmobject.RealmUser;
import io.realm.RealmList;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by vincent on 12/04/2017.
 */

public class DisplayPlaylistListFragment extends RxFragment implements AdapterIdCallBack, AdapterLoadMore {

    @BindView(R.id.display_user_playlist_recyclerview)
    RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    public Observable<RealmList<RealmPlaylist>> loadMore(int index, int id) {
        return UserManager.getInstance().getNextPlaylists(index, id);
    }

    @Override
    public void onCallBack(long id) {
        Intent intent = new Intent(getActivity(), DisplayPlaylistActivity.class);
        intent.putExtra(Tools.INTENT_PLAYLIST_ID, id);
        getActivity().startActivity(intent);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_user_playlist_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrieveUser();
    }

    private void retrieveUser() {
        int userId = this.getArguments().getInt(Tools.BUNDLE_USER_ID);

        UserManager.getInstance().getUserById(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(bindToLifecycle())
                .subscribe(
                        this::initRecyclerView,
                        error -> {
                            Timber.e(error, error.getMessage());
                        });
    }

    private void initRecyclerView(RealmUser realmUser) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PlaylistCardViewAdapter(realmUser, this, getContext(), this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }
}