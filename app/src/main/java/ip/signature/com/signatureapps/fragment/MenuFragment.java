package ip.signature.com.signatureapps.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.activity.AttendanceTrackActivity;
import ip.signature.com.signatureapps.activity.BreakInActivity;
import ip.signature.com.signatureapps.activity.BreakOutActivity;
import ip.signature.com.signatureapps.activity.EndAttendanceActivity;
import ip.signature.com.signatureapps.activity.AttendanceActivity;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.global.GlobalMaps;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.model.MediaType;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.transport.body.BodyBuilder;
import ip.signature.com.signatureapps.util.GPSUtil;

public class MenuFragment extends Fragment implements View.OnClickListener, TransportListener, OnMapReadyCallback {
    private LinearLayout llAbsen;
    private LinearLayout llEndAbsen;
    private LinearLayout llBreak;
    private LinearLayout llEndBreak;

    private MapView mapView;
    private LinearLayout llDetail;
    private JSONArray jsonArray;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);

        llAbsen = (LinearLayout) view.findViewById(R.id.llAbsen);
        llEndAbsen = (LinearLayout) view.findViewById(R.id.llEndAbsen);
        llBreak = (LinearLayout) view.findViewById(R.id.llBreak);
        llEndBreak = (LinearLayout) view.findViewById(R.id.llEndBreak);
        mapView = (MapView) view.findViewById(R.id.mapView);
        llDetail = (LinearLayout) view.findViewById(R.id.llDetail);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            llAbsen.setOnClickListener(this);
            llEndAbsen.setOnClickListener(this);
            llBreak.setOnClickListener(this);
            llEndBreak.setOnClickListener(this);
            llDetail.setOnClickListener(this);
        }

        GlobalMaps.setMenuFragment(this);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        new Transporter()
                .context(getActivity())
                .listener(this)
                .url("https://monitoring-api.herokuapp.com")
                .route("/api/v1/attendance/get/" + Global.id + "/" + new SimpleDateFormat("yyyy-MM-dd").format(timestamp))
                .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                .body(new BodyBuilder().setMediaType(MediaType.PLAIN.toString()))
                .gets()
                .execute();

        return view;
    }

    public void updateMaps() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        new Transporter()
                .context(getActivity())
                .listener(this)
                .url("https://monitoring-api.herokuapp.com")
                .route("/api/v1/attendance/get/" + Global.id + "/" + new SimpleDateFormat("yyyy-MM-dd").format(timestamp))
                .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                .body(new BodyBuilder().setMediaType(MediaType.PLAIN.toString()))
                .gets()
                .silent(true)
                .execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == llAbsen) {
            intent = new Intent(getActivity(), AttendanceActivity.class);
            intent.putExtra("bundle", this.getArguments());
        } else if (v == llEndAbsen) {
            intent = new Intent(getActivity(), EndAttendanceActivity.class);
            intent.putExtra("bundle", this.getArguments());
        } else if (v == llBreak) {
            intent = new Intent(getActivity(), BreakOutActivity.class);
            intent.putExtra("bundle", this.getArguments());
        } else if (v == llEndBreak) {
            intent = new Intent(getActivity(), BreakInActivity.class);
            intent.putExtra("bundle", this.getArguments());
        } else if (v == llDetail) {
            intent = new Intent(getActivity(), AttendanceTrackActivity.class);
            intent.putExtra("request", 1);
            intent.putExtra("json", jsonArray.toString());
        }

        startActivity(intent);
    }

    @Override
    public void onTransportDone(Object code, Object message, Object body, int id, Object... packet) {
        try {
            String result = String.valueOf(body);
            JSONObject json = new JSONObject(result);

            if (Integer.parseInt(json.get("code").toString()) == 200) {
                String data = json.getString("result");
                jsonArray = new JSONArray(data);
            } else {
                jsonArray = new JSONArray();
            }

            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransportFail(Object code, Object message, Object body, int id, Object... packet) {
        Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            MapsInitializer.initialize(getContext());
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                LatLng position = new LatLng(object.getDouble("latitude"), object.getDouble("longitude"));

                //add marker
                int custom = object.getInt("custom");
                if (custom == Global.ATTENDANCE) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(object.getString("date")))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (custom == Global.BREAK_OUT) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(object.getString("date")))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                } else if (custom == Global.BREAK_IN) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(object.getString("date")))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                } else if (custom == Global.END_ATTENDANCE) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(object.getString("date")))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else {
                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(object.getString("date")))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                }
            }

            // Pin to current location
            CameraPosition camera = CameraPosition.builder()
                    .target(new LatLng(GPSUtil.getLatitude(), GPSUtil.getLongitude()))
                    .zoom(16).bearing(0)
                    .tilt(45).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
