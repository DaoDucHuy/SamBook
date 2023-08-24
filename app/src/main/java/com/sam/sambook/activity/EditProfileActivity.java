package com.sam.sambook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sam.sambook.R;
import com.sam.sambook.model.UserModel;

import java.io.IOException;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity {

    private Button button_edit;
    private EditText edit_name, edit_address, edit_password, edit_retype_password;
    private UserModel _userModelLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();

        _getUserLocal();
        init();

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_password.getText().length() == 0 && edit_retype_password.getText().length() != 0) {
                    Toast.makeText(getApplicationContext(), "Xin hãy nhập mật khẩu trước khi nhập xác nhận mật khẩu, nếu không mật khẩu sẽ không được thay đổi", Toast.LENGTH_SHORT).show();
                } else if (!edit_retype_password.getText().toString().equals(edit_password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Xác nhận mật khẩu không đúng, xin vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                } else {
                    String name = edit_name.getText().toString();
                    String address = edit_address.getText().toString();
                    String password = edit_password.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("NAME", name);
                    intent.putExtra("ADDRESS", address);
                    intent.putExtra("PASSWORD", password);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void _getUserLocal() {
        try {
            InputStream is = openFileInput("USERSAMBOOK.txt");
            int size = is.available();
            byte data[] = new byte[size];
            is.read(data);
            is.close();
            String s = new String(data);
            Gson gson = new Gson();
            _userModelLocal = gson.fromJson(s, UserModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void init() {
        button_edit = findViewById(R.id.edit_profile_btn_edit);
        edit_name = findViewById(R.id.edit_profile_edit_name);
        edit_address = findViewById(R.id.edit_profile_edit_address);
        edit_password = findViewById(R.id.edit_profile_edit_password);
        edit_retype_password = findViewById(R.id.edit_profile_edit_re_password);

        edit_name.setText(_userModelLocal.getName());
        edit_address.setText(_userModelLocal.getAddress());
        edit_password.setText(_userModelLocal.getPassword());
    }
}