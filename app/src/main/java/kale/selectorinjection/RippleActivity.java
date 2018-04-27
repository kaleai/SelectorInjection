package kale.selectorinjection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * @author Kale
 * @date 2018/4/27
 */
public class RippleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ripple_layout);

        final RadioButton btn = findViewById(R.id.radio_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setChecked(true);
            }
        });
    }
}
