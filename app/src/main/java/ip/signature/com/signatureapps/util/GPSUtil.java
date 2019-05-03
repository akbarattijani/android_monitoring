package ip.signature.com.signatureapps.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * @author AKBAR <dicky.akbar@dwidasa.com>
 */

public class GPSUtil {

    private static LocationManager locationManager;
    private static double longitude = 0.0;
    private static double latitude = 0.0;
    private static AlertDialog alert;

    public static void requestUpdates(Activity activity, int min_time, int min_meter) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        getLocation(min_time, min_meter);
    }

    public static boolean forceGps(final Context context) {
        if (locationManager != null) {
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gps && !network) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("GPS tidak aktif. Silahkan aktifkan..!!");
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        });

                if (alert == null || !alert.isShowing()) {
                    alert = builder.create();
                    alert.show();
                }

                return false;
            }

            return true;
        }

        return true;
    }

    private static void getLocation(int min_time, int min_meter) {
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, min_time, min_meter, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    Log.d("Location", "Longitude : " + longitude + "\tLatitude : " + latitude);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public static double getLongitude() {
        return longitude;
    }

    public static double getLatitude() {
        return latitude;
    }
}