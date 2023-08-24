package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sam.sambook.MainActivity;
import com.sam.sambook.R;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.TypeBook;
import com.sam.sambook.model.UserModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
//    private FirebaseAuth auth;

    private UserModel _userModel;
    private UserModel _userModelLocal;

    private ImageButton btRegister;
    TextView tvLogin;
    Button btnLogin;
    EditText edtUsername, edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

//        auth = FirebaseAuth.getInstance();

        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
//                intent.putExtra("ALLBOOK", (Serializable) _allBook);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(tvLogin, "login");
                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                }
                assert activityOptions != null;
                startActivity(intent, activityOptions.toBundle());
            }
        });

//        even();
    }

    @Override
    public void onBackPressed() {

    }

    private void checkUser() {
        if (edtUsername.length() == 0) {
            Toast.makeText(getApplicationContext(), "Vui lòng điền tên tài khoản", Toast.LENGTH_SHORT).show();
        } else if (edtPassword.length() == 0) {
            Toast.makeText(getApplicationContext(), "Vui lòng điền mật khẩu", Toast.LENGTH_SHORT).show();
        } else {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Users");

//            myRef.child(edtUsername.getText().toString()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                    Toast.makeText(getApplicationContext(), "" + snapshot, Toast.LENGTH_SHORT).show();
//                    if (snapshot.getValue() == null) {
////                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//                        showDialogDontHaveUser();
//                    } else {
//                        _userModel = new UserModel();
//                        _userModel = snapshot.getValue(UserModel.class);
//
//                        if (edtPassword.getText().toString().equals(_userModel.getPassword().toString())) {
//                            saveUserToLocal(_userModel);
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.putExtra("USERS", _userModel);
//                            intent.putExtra("ALLBOOK", (Serializable) _allBook);
//                            startActivity(intent);
//                            LoginActivity.this.finish();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Sai Mật Khẩu", Toast.LENGTH_SHORT).show();
//                        }
////                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getApplicationContext(), "haha", Toast.LENGTH_SHORT).show();
//                }
//            });

            myRef.child(edtUsername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        showDialogDontHaveUser();
                    } else {
                        _userModel = new UserModel();
                        _userModel = snapshot.getValue(UserModel.class);

                        if (edtPassword.getText().toString().equals(_userModel.getPassword())) {
                            saveUserToLocal(_userModel);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("USER", _userModel);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sai Mật Khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });

//            auth.signInWithEmailAndPassword(edtUsername.getText().toString(), edtPassword.getText().toString())
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

        }
    }

    private void saveUserToLocal(UserModel _userModel) {
        try {
            Gson gson = new Gson();
            String user_gson = gson.toJson(_userModel);
            OutputStream saveFile = openFileOutput("USERSAMBOOK.txt", MODE_PRIVATE);
            saveFile.write(user_gson.getBytes());
            saveFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDialogDontHaveUser() {
        Log.e("LOGIN", "LOGIN");
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Tài khoản chưa được đăng ký! Bạn có muốn đăng ký tài khoản mới?");
        builder.setCancelable(false);
        builder.setPositiveButton("Đăng ký", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(tvLogin, "login");
                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                }
                assert activityOptions != null;
                startActivity(intent, activityOptions.toBundle());
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void init() {
        btRegister = findViewById(R.id.btRegister);
        btnLogin = findViewById(R.id.btnLogin);

        tvLogin = findViewById(R.id.tvLogin);

        edtUsername = findViewById(R.id.edt_user_password);
        edtPassword = findViewById(R.id.edt_re_password);
    }
}