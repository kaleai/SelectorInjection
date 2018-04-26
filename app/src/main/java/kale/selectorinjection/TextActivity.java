package kale.selectorinjection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import kale.ui.view.SelectorTextView;

/**
 * @author Kale
 * @date 2018/4/26
 */
public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_layout);

        final SelectorTextView view = findViewById(R.id.check_tv);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setChecked(true);
            }
        });
    }
}
