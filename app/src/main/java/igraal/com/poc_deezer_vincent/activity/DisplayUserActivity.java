package igraal.com.poc_deezer_vincent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import igraal.com.poc_deezer_vincent.R;
import igraal.com.poc_deezer_vincent.Tools;
import igraal.com.poc_deezer_vincent.manager.UserManager;
import igraal.com.poc_deezer_vincent.object.User;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by vincent on 05/04/2017.
 */

public class DisplayUserActivity extends RxAppCompatActivity {

    @BindView(R.id.display_user_country_textview)
    TextView user_country;
    @BindView(R.id.display_user_header_imageview)
    ImageView user_photo;
    @BindView(R.id.display_user_name_textview)
    TextView user_name;

    private Observable <User> user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_user);
        ButterKnife.bind(this);
        loadUser();
    }

    private void loadUser() {
        String userId = PreferenceManager.getDefaultSharedPreferences(this).getString(Tools.PREFERENCE_USER_ID, null);
        Timber.e("USER ID PREFERENCES :" + userId);
        if (userId == null)
            noUserToLoad();
        else {
            user = UserManager.getInstance().getUserById(userId);
            user.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            user1 -> {
                                user_country.setText(user1.getCountry());
                                user_name.setText(user1.getName());
                                Glide
                                        .with(this)
                                        .load(user1.getPicture())
                                        .centerCrop()
                                        .into(user_photo);
                            },
                            error -> {
                                Timber.e(error, error.getMessage());
                            });
        }
    }

    private void noUserToLoad() {
        Intent intent = new Intent(this, ResearchUserActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "No user found", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}