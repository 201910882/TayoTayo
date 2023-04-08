package com.example.TayoTayo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class busDriverJoin extends Activity {

    EditText id, pwd, busNum;
    Button btn;
    String DloginId, DloginPwd, DriverBusNum;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busdriver_autologin);

        id = (EditText)findViewById(R.id.idbus);
        pwd = (EditText)findViewById(R.id.passwordbus);
        busNum = (EditText)findViewById(R.id.bnum_bus);
        btn = (Button)findViewById(R.id.login);
        SharedPreferences Dauto = getSharedPreferences("Dauto", Activity.MODE_PRIVATE);
        DloginId = Dauto.getString("inputId",null);
        DloginPwd = Dauto.getString("inputPwd",null);
        DriverBusNum = Dauto.getString("inputBusNum", null);

        if(DloginId !=null && DloginPwd != null && DriverBusNum != null) {
            if( (DloginId.equals("서울74사1601") && DloginPwd.equals("4567") && DriverBusNum.equals("1020")) ||
                (DloginId.equals("서울74사9498") && DloginPwd.equals("8901") && DriverBusNum.equals("1020")) ||
                (DloginId.equals("서울74사1605") && DloginPwd.equals("8901") && DriverBusNum.equals("1020")) ||
                (DloginId.equals("서울74사3014") && DloginPwd.equals("8901") && DriverBusNum.equals("1020")) ||
                (DloginId.equals("서울70사6555") && DloginPwd.equals("8901") && DriverBusNum.equals("1020")) ||
                (DloginId.equals("서울70사7266") && DloginPwd.equals("8901") && DriverBusNum.equals("7730")) ||
                (DloginId.equals("서울74사7119") && DloginPwd.equals("8901") && DriverBusNum.equals("7730")) ||
                (DloginId.equals("서울74사3073") && DloginPwd.equals("8901") && DriverBusNum.equals("110A고려대")) ||
                (DloginId.equals("서울75사3207") && DloginPwd.equals("8901") && DriverBusNum.equals("110A고려대")) ||
                (DloginId.equals("서울74사4062") && DloginPwd.equals("8901") && DriverBusNum.equals("153")) ||
                (DloginId.equals("서울74사5901") && DloginPwd.equals("8901") && DriverBusNum.equals("153")) ||
                (DloginId.equals("서울75사2677") && DloginPwd.equals("8901") && DriverBusNum.equals("7022")) ||
                (DloginId.equals("서울74사4502") && DloginPwd.equals("8901") && DriverBusNum.equals("7022"))) {
                Toast.makeText(busDriverJoin.this, DloginId +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();

                intent = new Intent(getApplicationContext(), mainBusDriver.class);
                intent.putExtra("busNum", DriverBusNum);
                startActivity(intent);
                finish();
            }
        }
        else if(DloginId == null && DloginPwd == null && DriverBusNum == null){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( (id.getText().toString().equals("서울74사1601") && pwd.getText().toString().equals("4567") && busNum.getText().toString().equals("1020")) ||
                        (id.getText().toString().equals("서울74사9498") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("1020")) ||
                        (id.getText().toString().equals("서울74사1605") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("1020")) ||
                        (id.getText().toString().equals("서울74사3014") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("1020")) ||
                        (id.getText().toString().equals("서울70사6555") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("1020")) ||
                        (id.getText().toString().equals("서울70사7266") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("7730")) ||
                        (id.getText().toString().equals("서울74사7119") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("7730")) ||
                        (id.getText().toString().equals("서울74사3073") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("110A고려대")) ||
                        (id.getText().toString().equals("서울75사3207") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("110A고려대")) ||
                        (id.getText().toString().equals("서울74사4062") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("153")) ||
                        (id.getText().toString().equals("서울74사5901") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("153")) ||
                        (id.getText().toString().equals("서울75사2677") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("7022")) ||
                        (id.getText().toString().equals("서울74사4502") && pwd.getText().toString().equals("8901") && busNum.getText().toString().equals("7022"))) {
                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor DautoLogin = Dauto.edit();
                        DautoLogin.putString("inputId", id.getText().toString());
                        DautoLogin.putString("inputPwd", pwd.getText().toString());
                        DautoLogin.putString("inputBusNum", busNum.getText().toString());
                        DautoLogin.commit();
                        Toast.makeText(busDriverJoin.this, id.getText().toString()+"님 환영합니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), mainBusDriver.class);
                        intent.putExtra("busNum", busNum.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(busDriverJoin.this, "잘못된 정보입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        id.setText("");
                        pwd.setText("");
                        busNum.setText("");
                    }
                }
            });
        }
    }
}