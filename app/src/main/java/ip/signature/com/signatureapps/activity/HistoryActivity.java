package ip.signature.com.signatureapps.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.fragment.history.AttendanceHistoryFragment;
import ip.signature.com.signatureapps.fragment.history.SearchHistoryFragment;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private int position = 0;
    private ImageView back;

    private SearchHistoryFragment searchHistoryFragment = new SearchHistoryFragment();
    private AttendanceHistoryFragment attendanceHistoryFragment = new AttendanceHistoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.container, searchHistoryFragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (position == 0) {
            super.onBackPressed();
        } else if (position == 1) {
            position = 0;
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .add(R.id.container, searchHistoryFragment)
                    .remove(attendanceHistoryFragment)
                    .commit();
        }
    }

    public void toMaps(JSONArray data) {
        position = 1;

        Bundle bundle = new Bundle();
        bundle.putString("json", data.toString());
        attendanceHistoryFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.container, attendanceHistoryFragment)
                .remove(searchHistoryFragment)
                .commit();
    }
}
