package com.eder.rodriguez.minersup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        nameText = (EditText) findViewById(R.id.nameText);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this::singUpClicked);
    }

    private void singUpClicked(View view) {
        String userName = nameText.getText().toString().trim();
        String userEmail = emailText.getText().toString().trim();
        String userPassword = passwordText.getText().toString().trim();
        if(!TextUtils.isEmpty(userName)
                && !TextUtils.isEmpty(userEmail)
                && !TextUtils.isEmpty(userPassword)) {
            auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();
                        DatabaseReference currentUserDB = databaseReference.child(userId);
                        currentUserDB.child("Name").setValue(userName);

                        Intent main = new Intent(SignUpActivity.this,MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(main);
                    }
                }
            });
        }
    }
}
