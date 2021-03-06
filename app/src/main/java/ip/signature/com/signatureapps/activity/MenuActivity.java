package ip.signature.com.signatureapps.activity;

import android.app.Dialog;
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
import ip.signature.com.signatureapps.component.AlertDialogWithTwoButton;
import ip.signature.com.signatureapps.fragment.MenuFragment;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.listener.AlertDialogListener;
import ip.signature.com.signatureapps.service.AttendanceService;

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
            AlertDialogWithTwoButton alertDialogWithTwoButton = new AlertDialogWithTwoButton(this)
                    .setContent("Anda yakin ingin keluar akun..?")
                    .setTextButtonRight("Keluar")
                    .setListener(new AlertDialogListener.TwoButton() {
                        @Override
                        public void onClickButtonLeft(Dialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onClickButtonRight(Dialog dialog) {
                            Intent service = new Intent(getApplicationContext(), AttendanceService.class);
                            service.addCategory("ATTENDANCE");
                            stopService(service);

                            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            AttendanceService.stop();
                            MenuActivity.this.finish();
                        }
                    });

            alertDialogWithTwoButton.show();
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
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
        } else if (position == 1) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (position == 2) {
            Intent intent = new Intent(this, AttendanceTrackActivity.class);
            intent.putExtra("request", 0);
            startActivity(intent);
        } else if (position == 3) {

        } else if (position == 4) {
            Intent service = new Intent(getApplicationContext(), AttendanceService.class);
            service.addCategory("ATTENDANCE");
            stopService(service);

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            AttendanceService.stop();
            this.finish();
        }
    }
}
