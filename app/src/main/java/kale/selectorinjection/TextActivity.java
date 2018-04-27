package kale.selectorinjection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

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

        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(TextActivity.this, "checked? " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });
        
        findViewById(R.id.disable_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
            }
        });
    }
}
