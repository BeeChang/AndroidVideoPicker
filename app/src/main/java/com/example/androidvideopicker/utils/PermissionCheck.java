package com.example.androidvideopicker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;

public
class PermissionCheck {

    private SharedPreferences sharedPreferences;

    private final String APP_PAKAGE_NAME = "com.example.androidvideopicker";

    private final int REQUEST_CODE_READ_STORAGE_PERMISSION = 101;

    public boolean isReadStagePermission(Activity activity, View snackBar) {
        boolean isPermission = false;
        sharedPreferences = activity.getSharedPreferences("permissionCheck", MODE_PRIVATE);
        boolean isNotAskedPermissionBefore = sharedPreferences.getBoolean("isReadStorageCheckBefore", false);
        if (ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (!isNotAskedPermissionBefore) {
//                처음 입장하는 경우에는 무조건 리퀘스트를 처음꺼를 보여주어야한다.
                activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_STORAGE_PERMISSION);
                SharedPreferences.Editor editorName = sharedPreferences.edit();
                editorName.putBoolean("isReadStorageCheckBefore", true);
                editorName.apply();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, READ_EXTERNAL_STORAGE)) {
                    // 사용자가 다시 보지 않기에 체크를 하지 않고, 권한 설정을 거절한 이력이 있는 경우
                    snackBarReadStorage("onlyDeny", activity, snackBar);
                } else {
                    // 사용자가 다시 보지 않기에 체크하고, 권한 설정을 거절한 이력이 있는 경우
                    snackBarReadStorage("dontAskStatus", activity, snackBar);
                }
            }
        } else {
//            권한을 가지고 있는 경우
            isPermission = true;
        }

        return isPermission;
    }

    private void snackBarReadStorage(String flag, Activity activity, View snackBar) {

        if (flag.equals("onlyDeny")) {
            final Snackbar snackBarStorage = Snackbar.make(snackBar, "갤러리를 호출하기 위해서 저장소 접근권한이 필요합니다", Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackBarStorage.getView();
            TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setMaxLines(4);
            snackBarStorage.setAction("권한 승인하러 가기", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_STORAGE_PERMISSION);
                    snackBarStorage.dismiss();
                }
            });
            snackBarStorage.show();
        } else if (flag.equals("dontAskStatus")) {

            new MaterialAlertDialogBuilder(activity)
                    .setTitle("저장소 권한 설정")
                    .setMessage("갤러리를 호출하기 위해서 저장소 접근권한이 필요합니다")
                    .setPositiveButton("권한 승인하러 가기", /* listener = */ new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse(APP_PAKAGE_NAME));
                                activity.startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                activity.startActivity(intent);
                            }
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("취소", /* listener = */ new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(activity, "저장소 접근거부로 이용이 불가능합니다.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
