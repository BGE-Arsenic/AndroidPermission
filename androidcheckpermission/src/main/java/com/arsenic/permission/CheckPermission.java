package com.arsenic.permission;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import com.arsenic.demo.androidcheckpermission.R;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static com.arsenic.permission.BaseAndroidPermission.getActivity;
import static com.arsenic.permission.BaseAndroidPermission.getFragmentManager;
import static com.arsenic.permission.BaseAndroidPermission.getSupportFragmentManager;
import static com.arsenic.permission.BaseAndroidPermission.shouldShouldRequestPermissionRationale;

/**
 * Created by BGE-Arsenic on 2017/12/21.
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
        checkObjectsValid(mObject);
        boolean showRationale = false;
        for (String perm : mPermissions) {
            showRationale = showRationale || shouldShouldRequestPermissionRationale(mObject, perm);
        }
        if (showRationale) {
            Activity activity = getActivity(mObject);
            if (null == activity) {
                throw new IllegalStateException("Can't show rationale dialog for null Activity");
            }
            if (null != getSupportFragmentManager(mObject)) {
                showRationalAppCompatDialogFragment(activity);
            } else if (null != getFragmentManager(mObject)) {
                showRationalDialogFragment(activity);
            } else {
                showRationaleAlertDialog(activity);
            }
        } else {
            executePermissionsRequest();
        }
    }

    private void showRationaleAlertDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage(activity.getResources().getString(R.string.rationale_ask))
                .setTitle(activity.getResources().getString(R.string.rationale_ask_title))
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executePermissionsRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executePermissionsRequest();
                    }
                })
                .show();
    }

    private void showRationalDialogFragment(Activity activity) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage(activity.getResources().getString(R.string.rationale_ask))
                .setTitle(activity.getResources().getString(R.string.rationale_ask_title))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executePermissionsRequest();
                    }
                })
                .show();
    }

    private void showRationalAppCompatDialogFragment(Activity activity) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage(activity.getResources().getString(R.string.rationale_ask))
                .setTitle(activity.getResources().getString(R.string.rationale_ask_title))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executePermissionsRequest();
                    }
                })
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void executePermissionsRequest() {
        checkObjectsValid(mObject);
        if (mObject instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) mObject, mPermissions, mRequestPermissionCode);
        } else if (mObject instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) mObject).requestPermissions(mPermissions, mRequestPermissionCode);
        } else if (mObject instanceof android.app.Fragment) {
            ((android.app.Fragment) mObject).requestPermissions(mPermissions, mRequestPermissionCode);
        }
    }

    public static void onRequestPermissionsResult(@NonNull final Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, Object... callback) {
        ArrayList<String> granted = new ArrayList<>();
        ArrayList<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }
        if (null != callback && callback.length > 0) {
            for (Object permissionCallback : callback) {
                if (!granted.isEmpty() && permissionCallback instanceof OnAndroidPermissionGrantedCallback) {
                    ((OnAndroidPermissionGrantedCallback) permissionCallback).androidpermissionGrantResult(requestCode, granted);
                }
                if (!denied.isEmpty() && permissionCallback instanceof OnAndroidPermissionDeniedCallback) {
                    ((OnAndroidPermissionDeniedCallback) permissionCallback).androidPermissionDenyResult(requestCode, denied);
                    if (CheckPermission.somePermissionPermanentlyDenied(activity, denied)) {
                        new AlertDialog.Builder(activity)
                                .setTitle(activity.getResources().getString(R.string.rationale_title_settings))
                                .setMessage(activity.getResources().getString(R.string.rationale_ask_again))
                                .setNegativeButton(activity.getResources().getString(R.string.rationale_cancel), null)
                                .setPositiveButton(activity.getResources().getString(R.string.rationale_setting), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        CheckPermission.openSettings(activity);
                                    }
                                }).show();

                    }
                }
            }
        }
        if (!granted.isEmpty() && denied.isEmpty()) {
            annotatedMethod(activity, requestCode);
        }
    }

    private static void annotatedMethod(@NonNull Object object, int requestCode) {
        Method method = getMethod(object, AndroidPermissionCode.class, requestCode);
        if (null != method) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                method.invoke(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static <A extends Annotation> Method getMethod(@NonNull Object object, Class<A> annotation, int requestCode) {
        Class clazz = object.getClass();
        if (isUsingAndroidAnnotations(object)) {
            clazz = clazz.getSuperclass();
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                int code = method.getAnnotation(AndroidPermissionCode.class).value();
                if (code == requestCode) {
                    if (method.getParameterTypes().length > 0) {
                        throw new RuntimeException(
                                "Cannot execute method " + method.getName() + " because it is non-void method and/or has input parameters.");
                    }
                    return method;
                }
            }
        }
        return null;
    }

}
