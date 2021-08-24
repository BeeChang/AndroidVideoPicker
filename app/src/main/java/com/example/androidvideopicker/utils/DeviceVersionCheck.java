package com.example.androidvideopicker.utils;

import android.os.Build;

public
class DeviceVersionCheck {
    public DeviceVersionCheck() {
    }

    /**
     * 스코프스토리지 때문에 사용함
     * 29이상이면 트루 리턴
     * @return x >= 29 -> true / x < 29 -> false
     */
    public boolean isVersionUpper29(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
}
