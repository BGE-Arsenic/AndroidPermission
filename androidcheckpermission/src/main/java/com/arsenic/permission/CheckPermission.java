package com.arsenic.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import static com.arsenic.permission.BaseAndroidPermission.getActivity;
import static com.arsenic.permission.BaseAndroidPermission.getFragmentManager;
import static com.arsenic.permission.BaseAndroidPermission.getSupportFragmentManager;
import static com.arsenic.permission.BaseAndroidPermission.shouldShouldRequestPermissionRationale;

/**
 * Created by apple on 2017/12/21.
 */

public class CheckPermission extends BaseAndroidPermission{

    private Object mObject;
    private String[] mPermissions;
    private int mRequestPermissionCode;

    private  CheckPermission(Object object){
        this.mObject = object;
    }

    //open fuc
    public static boolean hasPermission(@NonNull Context context,@NonNull @PermissionSingle String... permissions){
        if (!isAndroidM()){
            return true;
        }
        for (String permission :permissions){
            boolean granted = ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
            if (!granted){
                return false;
            }
        }
        return true;
    }

    public static boolean isAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static CheckPermission checkWith(@NonNull Activity activity){
        if (null == activity){
            throw new NullPointerException("activity should not be null");
        }
        return new CheckPermission(activity);
    }

    public static CheckPermission checkWith(@NonNull android.support.v4.app.Fragment fragment){
        if (null == fragment){
            throw new NullPointerException("fragment should not be null");
        }
        return new CheckPermission(fragment);
    }

    public static CheckPermission checkWith(@NonNull android.app.Fragment fragment){
        if (null == fragment){
            throw new NullPointerException("fragment should not be null");
        }
        return new CheckPermission(fragment);
    }

    public CheckPermission setPermissionSingle(@NonNull @PermissionSingle String... permissionSingle){
        if (null == permissionSingle || permissionSingle.length == 0){
            throw new IllegalPermissionException("permissionSingle should not bu null or permissionSingle.length==0");
        }
        this.mPermissions = permissionSingle;
        return this;
    }

    public CheckPermission setPermissionGroup(@NonNull @PermissionGroup String... permissionGroup){
        if (null == permissionGroup || permissionGroup.length == 0){
            throw new IllegalPermissionException("permissionGroup should not bu null or permissionGroup.length==0");
        }
        SparseArray<String> array = new SparseArray<>();
        for (String group:permissionGroup){
            switch (group){
                case PermissionGroup.GROUP_CALENDAR:
                    for (String single:PermissionGroup.Group.CALENDAR){
                        array.append(array.size(),single);
                    }
                    break;
                case PermissionGroup.GROUP_CAMERA:
                    for (String single:PermissionGroup.Group.CAMERA){
                        array.append(array.size(),single);
                    }
                    break;
                case PermissionGroup.GROUP_CONTACTS:
                    for (String single:PermissionGroup.Group.CONTACTS){
                        array.append(array.size(),single);
                    }
                    break;
                case PermissionGroup.GROUP_LOCATION:
                    for (String single:PermissionGroup.Group.LOCATION){
                        array.append(array.size(),single);
                    }
                    break;
                case PermissionGroup.GROUP_MICROPHONE:
                    for (String single:PermissionGroup.Group.MICROPHONE){
                        array.append(array.size(),single);
                    }
                    break;
                case PermissionGroup.GROUP_PHONE:
                    for (String single:PermissionGroup.Group.PHONE){
                        array.append(array.size(),single);
                    }
                    break;
                case PermissionGroup.GROUP_SENSORS:
                    for (String single:PermissionGroup.Group.SENSORS){
                        array.append(array.size(),single);
                    }
                    break;
                case PermissionGroup.GROUP_SMS:
                    for (String single:PermissionGroup.Group.SMS){
                        array.append(array.size(),single);
                    }
                    break;
                case PermissionGroup.GROUP_STORAGE:
                    for (String single:PermissionGroup.Group.STORAGE){
                        array.append(array.size(),single);
                    }
                    break;
                default:
                    throw new IllegalPermissionException("please set valid permissiongGroup");
            }
        }
        mPermissions = new String[array.size()];
        for (int i=0;i<array.size();i++){
            mPermissions[i] = array.get(i);
        }
        return this;
    }

    public CheckPermission setRequestPermissionCode(int requestPermissionCode){
        this.mRequestPermissionCode = requestPermissionCode;
        return this;
    }

    @SuppressLint("NewApi")
    public void requestPermissions(){
        if (null == mObject || null == mPermissions) {
            throw new NullPointerException("activity or fragment or permissionSingle or permissionGroup should not null");
        }
        checkObjectValid(mObject);

    }


}
