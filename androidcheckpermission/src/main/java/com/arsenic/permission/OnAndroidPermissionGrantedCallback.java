package com.arsenic.permission;

import java.util.List;

/**
 * Created by apple on 2017/12/21.
 */

public interface OnAndroidPermissionGrantedCallback {
    void androidpermissionGrantResult(int requestCode, List<String> androidPermissions);
}
