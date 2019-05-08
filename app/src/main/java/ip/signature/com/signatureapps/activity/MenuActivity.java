package ip.signature.com.signatureapps.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.adapter.NavigationMenuAdapter;
import ip.signature.com.signatureapps.fragment.MenuFragment;
import ip.signature.com.signatureapps.global.Global;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, NavigationMenuAdapter.OnMenuClickListener {
    private DrawerLayout myDrawerLayout;
    private NavigationView myNavView;
    private FrameLayout flBurgeMenu;
    private RecyclerView rvMenu;
    private NavigationMenuAdapter menuAdapter;
    private MenuFragment menuFragment = new MenuFragment();

    private TextView tvName;
    private TextView tvNip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        flBurgeMenu = (FrameLayout) findViewById(R.id.flBurgeMenu);
        myNavView = (NavigationView) findViewById(R.id.navigation_view);
        rvMenu = (RecyclerView) findViewById(R.id.rvMenu);

        tvName = (TextView) findViewById(R.id.tvName);
        tvNip = (TextView) findViewById(R.id.tvNip);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            tvName.setText(Global.name);
            tvNip.setText(Global.nip);

            myDrawerLayout.setScrimColor(Color.parseColor("#66ffffff"));
            flBurgeMenu.setOnClickListener(this);

            menuAdapter = new NavigationMenuAdapter(this);
            menuAdapter.setMenuClickListener(this);

            rvMenu.setLayoutManager(new LinearLayoutManager(this));
            rvMenu.setAdapter(menuAdapter);

            menuFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, menuFragment)
                    .show(menuFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(myDrawerLayout.isDrawerOpen(myNavView)){
            myDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == flBurgeMenu) {
            if (myDrawerLayout != null) {
                myDrawerLayout.openDrawer(myNavView);
            }
        }
    }

    @Override
    public void onMenuClick(int position) {
        if (position == 0) {

        } else if (position == 1) {

        } else if (position == 2) {
            Intent intent = new Intent(this, AttendanceTrackActivity.class);
            startActivity(intent);
        } else if (position == 3) {

        } else if (position == 4) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            this.finish();
        }
    }
}
