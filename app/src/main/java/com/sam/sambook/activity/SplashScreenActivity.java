package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sam.sambook.MainActivity;
import com.sam.sambook.R;
import com.sam.sambook.model.UserModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getName();

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private UserModel _userModel;
    private UserModel _userModelLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        checkInternet();
    }

    private void checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Toast.makeText(getApplicationContext(), "Đã kết nối Internet", Toast.LENGTH_SHORT).show();
                userLocalLogin();
//                loginAuto();
            } else {
                showAlertDialogInternet();
            }
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

    private void userLocalLogin() {
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

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                if (_userModelLocal == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("Users");

                    myRef.child(_userModelLocal.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                _userModel = new UserModel();
                                _userModel = snapshot.getValue(UserModel.class);
                                saveUserToLocal(_userModel);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("USER", _userModel);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }

    private void loginAuto() {
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (_userModelLocal == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("Users");

                    myRef.child(_userModelLocal.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                _userModel = new UserModel();
                                _userModel = snapshot.getValue(UserModel.class);
                                saveUserToLocal(_userModel);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("USER", _userModel);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }

    private void showAlertDialogInternet() {
        Log.e(TAG, "no internet");
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
        builder.setTitle("Ứng dụng cần có kết nối Internet để sử dụng, vui lòng kết nối và thử lại!");
        builder.setCancelable(false);
        builder.setPositiveButton("Kết nối lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkInternet();
            }
        });
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                SplashScreenActivity.this.finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}