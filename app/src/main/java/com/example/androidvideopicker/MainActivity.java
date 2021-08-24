package com.example.androidvideopicker;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidvideopicker.ui.VideoPickerFragment;
import com.example.androidvideopicker.utils.PermissionCheck;
import com.google.android.material.snackbar.Snackbar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    public FragmentManager fragmentManager;
    private VideoPickerFragment videoPickerFragment;
    private final PermissionCheck permissionCheck =  new PermissionCheck();
    private final int REQUEST_CODE_READ_STORAGE_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fmInit();

        Button startPickerBtn = findViewById(R.id.startPickerBtn);
        startPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checkStoragePermission = permissionCheck.isReadStagePermission(MainActivity.this, MainActivity.this.findViewById(android.R.id.content));
                if (checkStoragePermission) {
                    openFragment();
                }
            }
        });

    }

    private void openFragment() {
        videoPickerFragment = new VideoPickerFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, videoPickerFragment)
//                        .hide("이전뷰")
                .replace(R.id.fragmentContainerView, videoPickerFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .show(videoPickerFragment)
                .addToBackStack(null)
                .commit();
    }

    private void fmInit() {
        fragmentManager = getSupportFragmentManager();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_READ_STORAGE_PERMISSION:
                if (ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    View viewForSnackBar = MainActivity.this.findViewById(android.R.id.content);
                    final Snackbar snackBar = Snackbar.make(viewForSnackBar, "저장소 권한 거절로 이용이 불가능합니다", Snackbar.LENGTH_INDEFINITE);
                    snackBar.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();
                } else {
                    openFragment();
                }
                break;
        }
    }

}