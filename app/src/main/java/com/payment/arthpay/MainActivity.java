package com.payment.arthpay;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.payment.arthpay.app.Constents;
import com.payment.arthpay.fragment.HelpFragment;
import com.payment.arthpay.fragment.HomeFragment;
import com.payment.arthpay.fragment.ProfileFragment;
import com.payment.arthpay.fragment.ReportFragment;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.model.MicroATMModel;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.PrefLoginManager;

import java.util.ArrayList;
import java.util.List;

import static com.payment.arthpay.utill.AppManager.getImei;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS = 2000;
    MicroATMModel model;
    int fragPosition = 0;
    boolean permissionState;
    AlertDialog alert;
    private FrameLayout frameLayout;
    private BottomNavigationView navigation;
    private FloatingActionButton fabButton;
    private Context context;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.home:
                fragPosition = 0;
                fragment = new HomeFragment();
                loadFragment(fragment);
                return true;
            case R.id.shop:
                fragPosition = 1;
                fragment = new ReportFragment();
                loadFragment(fragment);
                return true;
            case R.id.help:
                fragPosition = 2;
                fragment = new HelpFragment();
                loadFragment(fragment);
                return true;
            case R.id.profile:
                fragPosition = 3;
                fragment = new ProfileFragment();
                loadFragment(fragment);
                return true;
        }
        return false;
    };

    private void init() {
        context = MainActivity.this;
        frameLayout = findViewById(R.id.frame_container);
        fabButton = findViewById(R.id.fabButton);
        Constents.MOBILE_ID = getImei(MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            viewInit();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        int accessCourseLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int readPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        List<String> permissions = new ArrayList<>();
        if (accessCourseLocation != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (permissions.size() > 0) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), MY_PERMISSIONS);
        } else {
            viewInit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        boolean isAllPermissionGranted = false;
        switch (requestCode) {
            case MY_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (!permissionState)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermission();
                                boolean showRationale = shouldShowRequestPermissionRationale(permissions[i]);
                                if (!showRationale) {
                                    permissionState = true;
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                            MainActivity.this);
                                    alertDialogBuilder.setTitle("Permission Deny");
                                    alertDialogBuilder.setMessage("Application unable to run while " + permissions[i] + " permission deny! Open app setting and enable permission.");
                                    alertDialogBuilder
                                            .setCancelable(false)
                                            .setPositiveButton("Setting",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            dialog.cancel();
                                                            Intent intent = new Intent();
                                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                            intent.setData(uri);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void viewInit() {
        init();
        navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new HomeFragment());
        fragPosition = 0;

        fabButton.setOnClickListener(v -> {
            if (fragPosition == 3) {
                AppManager.getInstance().logoutFromServer(MainActivity.this);
            } else {
                new UpdateBillService(this);
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        changeFabIcon();
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (fragPosition == 0) {
            confirmPopup();
        } else {
            Fragment fragment = new HomeFragment();
            loadFragment(fragment);
            navigation.setSelectedItemId(R.id.home);
        }
    }

    private void confirmPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you really want to exit?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> finish())
                .setNegativeButton(android.R.string.no, (dialog, whichButton) -> alert.dismiss());
        alert = builder.create();
        alert.show();
    }

    private void changeFabIcon() {
        switch (fragPosition) {
            case 0:
            case 1:
            case 2:
                setDrawable(R.drawable.refresh_icon);
                break;
            case 3:
                setDrawable(R.drawable.logout_icon);
        }
    }

    private void setDrawable(int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabButton.setImageDrawable(getResources().getDrawable(drawable, this.getTheme()));
        } else {
            fabButton.setImageDrawable(getResources().getDrawable(drawable));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PrefLoginManager pref = new PrefLoginManager(this);
        String status = pref.getFarmerLoginRes();
        if (status.length() == 0) {
            AppManager.getInstance().logoutApp(MainActivity.this);
        }
    }
}
