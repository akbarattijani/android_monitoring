package ip.signature.com.signatureapps.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

/**
 * @author AKBAR <dicky.akbar@dwidasa.com>
 */

public class GPSUtil {

    private static LocationManager locationManager;
    private static double longitude = 0.0;
    private static double latitude = 0.0;
    private static Location mLocation;
    private static AlertDialog alert;
    private static Activity activity;
    private static boolean mMock = false;

    public static void requestUpdates(Activity activity, int min_time, int min_meter) {
        GPSUtil.activity = activity;
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

    public static void mock(boolean isMock) {
        mMock = isMock;
    }

    private static void getLocation(int min_time, int min_meter) {
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, min_time, min_meter, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mLocation = location;
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    if (mMock) {
                        isFake();
                    }

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

    private static void isFake() {
        boolean method1 = !Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
        boolean method2 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && mLocation != null && mLocation.isFromMockProvider();
        boolean method3 = areThereMockPermissionApps();
        boolean isFake = method1 || method2;

        int count = 0;
        if (method1) {
            count++;
        }
        if (method2) {
            count++;
        }
        if (method3) {
            count++;
        }

        Log.d("Checking Fake GPS", "Method 1 => " + method1);
        Log.d("Checking Fake GPS", "Method 2 => " + method2);
        Log.d("Checking Fake GPS", "Method 3 => " + method3);
        Log.d("Checking Fake GPS", "Result => " + isFake);
        if (count > 1) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
            builder.setMessage("You have a fake GPS");
            builder.setCancelable(false);

            builder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert.dismiss();
                            isFake();
                        }
                    });

            if (alert == null || !alert.isShowing()) {
                alert = builder.create();
                alert.show();
            }
        }
    }

    private static boolean areThereMockPermissionApps() {
        int count = 0;

        PackageManager pm = activity.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(activity.getPackageName())) {
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return count > 0;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static double getLatitude() {
        return latitude;
    }
}