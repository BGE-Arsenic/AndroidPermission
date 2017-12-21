package com.arsenic.permission;

import java.util.List;

/**
 * Created by apple on 2017/12/21.
 */

public interface OnAndroidPermissionDeniedCallback {
    void androidPermissionDenyResult(int requestCode, List<String> androidPermissions);
}
