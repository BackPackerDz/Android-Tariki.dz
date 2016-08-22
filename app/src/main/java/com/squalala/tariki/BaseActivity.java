package com.squalala.tariki;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.squalala.tariki.App;
import com.squalala.tariki.AppComponent;

/**
 * Created by Fayçal KADDOURI on 14/08/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(App.get(this).component());
    }

    protected abstract void setupComponent(AppComponent appComponent);

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
