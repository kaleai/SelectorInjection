package kale.selectorinjection;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Checkable;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Checkable view = (Checkable) findViewById(R.id.view);
        view.setChecked(true);
    }
}
