package igraal.com.poc_deezer_vincent.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import igraal.com.poc_deezer_vincent.R;
import igraal.com.poc_deezer_vincent.database.RealmManager;
import igraal.com.poc_deezer_vincent.manager.UserManager;
import igraal.com.poc_deezer_vincent.object.User;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by vincent on 03/04/2017.
 */

public class ResearchUser extends RxAppCompatActivity {

    @BindView(R.id.research_user_editext)
    EditText editText;
    @BindView(R.id.research_user_textview)
    TextView title;
    @BindView(R.id.research_user_button)
    Button button;

    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(getApplicationContext());
        setContentView(R.layout.research_user);
        ButterKnife.bind(this);
    }

    private void observeEditText() {
        subscription = RxTextView.textChanges(editText)
                .filter(charSequence -> charSequence.length() > 0)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    charSequence -> {
                        button.setEnabled(true);
                        subscription.unsubscribe();
                        Timber.e("OK");
                    },
                        error -> {
                            Timber.e(error, error.getMessage());
                        }
                );
    }
    @OnClick(R.id.research_user_button)
    public void retrieveUser() {
        UserManager userManager = UserManager.getInstance();
        userManager.getUser(editText.getText().toString())
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                        Timber.e(user.getName() + "  " + Thread.currentThread().getName());
                    /*RealmResults<User> users = RealmManager.getInstance().getAllUsers();
                    for (int i = 0; i < users.size(); i++) {
                        Timber.e("QUERY " + users.get(i).getName());
                    }*/
                },
                        error -> {
                            Timber.e(error, error.getMessage());
                        });
   }

    @Override
    protected void onStart() {
        super.onStart();
        observeEditText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null)
            subscription.unsubscribe();
    }
}
