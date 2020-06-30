package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class StatusActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar mToolbar;
    private TextInputLayout mStatus;
    private Button mSavebtn;
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    //Progress Dialog
    private ProgressDialog mStatusProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mToolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String status_value = getIntent().getStringExtra("status_value");
        mStatus =(TextInputLayout) findViewById(R.id.status);
        mStatus.getEditText().setText(status_value);
        mSavebtn = (Button) findViewById(R.id.button);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusProgress = new ProgressDialog(StatusActivity.this);
                mStatusProgress.setTitle("Saving Changes");
                mStatusProgress.setMessage("Please wait while we save the changes");
                mStatusProgress.show();
                String status = mStatus.getEditText().getText().toString();
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mStatusProgress.dismiss();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"There was some error",Toast.LENGTH_LONG).show();;
                        }
                    }
                });
            }
        });
    }
}