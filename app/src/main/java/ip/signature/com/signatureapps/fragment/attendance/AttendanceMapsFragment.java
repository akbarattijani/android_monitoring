package ip.signature.com.signatureapps.fragment.attendance;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

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

import java.util.ArrayList;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.util.GPSUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AttendanceMapsFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    private View mView;
    private MapView mapView;
    public DrawerLayout myDrawerLayout;
    public NavigationView myNavView;
    private FrameLayout flBurgeMenu;
    private ListView rvMenu;

    private LatLng[] latLngs;
    private GoogleMap mGoogleMap;

    public AttendanceMapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_attendance_maps, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize map
        mapView = (MapView) view.findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        myDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer);
        flBurgeMenu = (FrameLayout) view.findViewById(R.id.flBurgeMenu);
        myNavView = (NavigationView) view.findViewById(R.id.navigation_view);
        rvMenu = (ListView) view.findViewById(R.id.rvMenu);

        myDrawerLayout.setScrimColor(Color.parseColor("#66ffffff"));
        flBurgeMenu.setOnClickListener(this);
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
    public void onMapReady(GoogleMap googleMap) {
        try {
            MapsInitializer.initialize(getContext());

            mGoogleMap = googleMap;
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);

            Bundle bundle = getArguments();
            ArrayList<String> list = new ArrayList<String>();
            JSONArray json = new JSONArray(bundle.getString("json"));
            latLngs = new LatLng[json.length()];

            for (int i = 0; i < json.length(); i++) {
                JSONObject object = json.getJSONObject(i);
                LatLng position = new LatLng(object.getDouble("latitude"), object.getDouble("longitude"));
                latLngs[i] = position;
                list.add(object.getString("date"));

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

            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
            rvMenu.setAdapter(itemsAdapter);

            // Pin to current location
            CameraPosition camera = CameraPosition.builder()
                    .target(new LatLng(GPSUtil.getLatitude(), GPSUtil.getLongitude()))
                    .zoom(16).bearing(0)
                    .tilt(45).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));

            rvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CameraPosition cameraPosition = CameraPosition.builder().target(latLngs[position]).zoom(16).bearing(0).tilt(45).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    myDrawerLayout.closeDrawers();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
