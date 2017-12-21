package com.arsenic.demo.androidpermission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arsenic.permission.CheckPermission;
import com.arsenic.permission.PermissionSingle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_camera_permission,tv_contact_permission,tv_sms_permission,tv_phone_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_camera_permission = (TextView) findViewById(R.id.tv_camera_permission);
        tv_contact_permission = (TextView) findViewById(R.id.tv_contact_permission);
        tv_sms_permission = (TextView) findViewById(R.id.tv_sms_permission);
        tv_phone_permission = (TextView) findViewById(R.id.tv_phone_permission);

        tv_camera_permission.setOnClickListener(this);
        tv_contact_permission.setOnClickListener(this);
        tv_sms_permission.setOnClickListener(this);
        tv_phone_permission.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_camera_permission:
                if (CheckPermission.hasPermission(MainActivity.this, PermissionSingle.CAMERA)){
                    Toast.makeText(this,"有开启相机的权限",Toast.LENGTH_SHORT).show();
                } else {
                    CheckPermission.checkWith(this)
                                   .setPermissionSingle(PermissionSingle.CAMERA)
                                   .setRequestPermissionCode(100)
                                   .requestPermissions();
                }
                break;
            case R.id.tv_contact_permission:
                if (CheckPermission.hasPermission(MainActivity.this,PermissionSingle.READ_CONTACTS)){
                    Toast.makeText(this,"开启了读取手机联系人的权限",Toast.LENGTH_SHORT).show();
                } else {
                    CheckPermission.checkWith(this)
                                   .setPermissionSingle(PermissionSingle.READ_CONTACTS)
                                   .setRequestPermissionCode(200)
                                   .requestPermissions();
                }
                break;
            case R.id.tv_sms_permission:
                if (CheckPermission.hasPermission(MainActivity.this,PermissionSingle.READ_SMS)){
                    Toast.makeText(this,"开启了读取手机短信的权限",Toast.LENGTH_SHORT).show();
                } else {
                    CheckPermission.checkWith(this)
                            .setPermissionSingle(PermissionSingle.READ_SMS)
                            .setRequestPermissionCode(300)
                            .requestPermissions();
                }
                break;
            case R.id.tv_phone_permission:
                if (CheckPermission.hasPermission(MainActivity.this,PermissionSingle.CALL_PHONE)){
                    Toast.makeText(this,"开启了拨打电话的权限",Toast.LENGTH_SHORT).show();
                } else {
                    CheckPermission.checkWith(this)
                            .setPermissionSingle(PermissionSingle.CALL_PHONE)
                            .setRequestPermissionCode(400)
                            .requestPermissions();
                }
                break;
        }
    }
}
