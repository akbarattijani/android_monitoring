package ip.signature.com.signatureapps.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.fragment.attendance.AttendanceMapsFragment;
import ip.signature.com.signatureapps.fragment.attendance.SearchAttendanceFragment;

public class AttendanceTrackActivity extends AppCompatActivity implements View.OnClickListener {
    private int position = 0;
    private ImageView back;

    private SearchAttendanceFragment searchAttendanceFragment = new SearchAttendanceFragment();
    private AttendanceMapsFragment attendanceMapsFragment = new AttendanceMapsFragment();

    private final int RC_SEARCH = 0;
    private final int RC_MAP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_track);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int request = bundle.getInt("request");
            if (request == RC_SEARCH) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.container, searchAttendanceFragment)
                        .commit();
            } else if (request == RC_MAP) {
                Bundle extras = new Bundle();
                extras.putString("json", bundle.getString("json"));
                attendanceMapsFragment.setArguments(extras);

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.container, attendanceMapsFragment)
                        .commit();
            }
        }
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
            if (attendanceMapsFragment.myDrawerLayout.isDrawerOpen(attendanceMapsFragment.myNavView)) {
                attendanceMapsFragment.myDrawerLayout.closeDrawers();
            } else {
                position = 0;
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .add(R.id.container, searchAttendanceFragment)
                        .remove(attendanceMapsFragment)
                        .commit();
            }
        }
    }

    public void toMaps(JSONArray data) {
        position = 1;

        Bundle bundle = new Bundle();
        bundle.putString("json", data.toString());
        attendanceMapsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.container, attendanceMapsFragment)
                .remove(searchAttendanceFragment)
                .commit();
    }
}
