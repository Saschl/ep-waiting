package com.example.sascha.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sascha.myapplication.ui.main.ItemFragment;
import com.example.sascha.myapplication.ui.main.dummy.WaitingTimeContent;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {


    public static final String TAG = "MainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

/*        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ItemFragment fragment = new ItemFragment();
            transaction.replace(R.id.list, fragment);
            transaction.commit();
        }*/
    }

    @Override
    public void onListFragmentInteraction(WaitingTimeContent.WaitingTimeItem item) {

        Toast.makeText(this, item.content, Toast.LENGTH_LONG).show();

    }
}
