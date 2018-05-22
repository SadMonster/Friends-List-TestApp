package com.example.gene.friendslisttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class Window extends AppCompatActivity {

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        extras = getIntent().getExtras();

        TextView name = findViewById(R.id.windowName);
        TextView email = findViewById(R.id.windowEmail);
        TextView birth = findViewById(R.id.windowBirthday);

        if (extras != null) {
            name.setText(extras.getString("name"));
            email.setText(extras.getString("email"));
            birth.setText(extras.getString("birth"));

            new UsersAdapter.DownloadImageTask((ImageView) findViewById(R.id.avatarBig),getBaseContext()).execute(extras.getString("avatar"));
        }


    }

}
