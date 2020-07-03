package com.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.kegy.mvps.annotation.Inject;
import com.kegy.mvps.annotation.Provider;

public class MainActivity extends AppCompatActivity {

  @Provider("a")
  String a;

  @Inject("b")
  String c = "a";

  @Inject()
  String d = "d";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
}
