package com.lambertsoft.app18;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class MainActivity extends ActionBarActivity {

        private static final int LOGIN_REQUEST = 0;
        private Button btnLogin;

        private ParseUser currentUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
            btnLogin = (Button) findViewById(R.id.btnLogin);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null && currentUser.getUsername() != null ) {

                        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                        startActivity(intent);
                    } else {

                        ParseLoginBuilder loginBuilder = new ParseLoginBuilder(MainActivity.this);
                        startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
                    }
                }
            });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
            Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
            startActivity(intent);
        }
    }

        @Override
        protected void onResume() {
            super.onResume();

            currentUser = ParseUser.getCurrentUser();
            if (currentUser != null && currentUser.getUsername() != null ) {
                btnLogin.setText("Go");
            } else {
                btnLogin.setText("Login");
            }
        }


}
