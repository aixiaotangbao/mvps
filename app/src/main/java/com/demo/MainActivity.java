package com.demo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, MainFragment.newInstance(true))
        .commitAllowingStateLoss();
  }


}
