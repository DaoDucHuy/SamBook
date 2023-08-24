package com.sam.sambook.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sam.sambook.MainActivity;
import com.sam.sambook.R;
import com.sam.sambook.model.BookModel;
import com.sam.sambook.model.UserModel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtRePassword, edtName, edtAddress;
    private Button btnRegisterLogin;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth auth;

//    List<BookModel> _allBook = new ArrayList<>();
    private int checkLoadserver = 0; //if server already run it will return 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        init();

//        auth = FirebaseAuth.getInstance();

        btnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUsername.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Xin vui lòng điền Tên đăng nhập", Toast.LENGTH_SHORT).show();
                } else if (edtPassword.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Xin vui lòng điền Mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (edtRePassword.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Xin vui lòng điền Xác nhận Mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (!edtRePassword.getText().toString().equals(edtPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Xác nhận mật khẩu không đúng, xin vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                }
                else if (edtName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Xin vui lòng điền họ tên", Toast.LENGTH_SHORT).show();
                }
                else if (edtAddress.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Xin vui lòng điền địa chỉ", Toast.LENGTH_SHORT).show();
                } else {
                    _addUser();
                }
            }
        });

//        even();
    }

//    private void even() {
//
//    }

//    private void register(final String username, String email, String password){
//        auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser firebaseUser = auth.getCurrentUser();
//                            String userid = firebaseUser.getUid();
//
//                            myRef = FirebaseDatabase.getInstance().getReference("Users2").child(userid);
//
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("id", userid);
//                            hashMap.put("username", username);
//                            hashMap.put("imageURL", "default");
//
//                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
////                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                                        startActivity(intent);
////                                        finish();
//                                    }
//                                }
//                            });
//                        } else {
//                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(getApplicationContext(), "You can't register with this email or password", Toast.LENGTH_SHORT).show();
////                            Log.d("taks", "" + task);
//                        }
//                    }
//                });
//    }

    private void _addUser() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        //to check user already have or not
        myRef.child(edtUsername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (checkLoadserver == 0) {
//                    if (snapshot.getValue() != null) {
//                        showDialogAlreadyHaveUser(); //bug
//                    } else {
//                        register(edtUsername.getText().toString(), edtAddress.getText().toString(), edtPassword.getText().toString());
//                        addingUser();
//                        checkLoadserver = 1;
//                    }
//                }

                if (snapshot.getValue() != null) {
                    showDialogAlreadyHaveUser();
                } else {
//                    register(edtUsername.getText().toString(), edtAddress.getText().toString(), edtPassword.getText().toString());
                    addingUser();
//                    checkLoadserver = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addingUser() {
        UserModel userModel = new UserModel(
                edtUsername.getText().toString(),
                edtPassword.getText().toString(),
                edtName.getText().toString(),
                edtAddress.getText().toString());

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        myRef.child(userModel.getUsername()).setValue(userModel);

        Toast.makeText(getApplicationContext(), "Đăng Ký thành công!", Toast.LENGTH_SHORT).show();

        saveUserToLocal(userModel);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("USER", userModel);
//        intent.putExtra("ALLBOOK", (Serializable) _allBook);
//        intent.putExtra("ALLBOOK", (Serializable) _allBook);
        startActivity(intent);
        RegisterActivity.this.finish();
    }

    private void showDialogAlreadyHaveUser() {
        Log.e("USER", "user already exists");
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Tên đăng nhập đã tồn tại, vui lòng điền Tên đăng nhập khác!");
        builder.setCancelable(false);
        builder.setNegativeButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void init() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_user_password);
        edtRePassword = findViewById(R.id.edt_re_password);
        edtName = findViewById(R.id.edt_name);
        edtAddress = findViewById(R.id.edt_user_address);

        btnRegisterLogin = findViewById(R.id.btn_register_login);

//        _allBook = (List<BookModel>) getIntent().getSerializableExtra("ALLBOOK");
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
}