package kale.selectorinjection;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import kale.injection.SelectorInjection;
import kale.ui.view.ISelectorView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ISelectorView view;

    private SelectorInjection injection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.stv);
//        view.setChecked(true);
        ((View) view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
        
        injection = view.getSelectorInjection();

        findCbAndSetListener(R.id.pressed_color_cb);
        findCbAndSetListener(R.id.pressed_drawable_cb);
        findCbAndSetListener(R.id.normal_stroke_cb);
        findCbAndSetListener(R.id.pressed_stroke_cb);
        findCbAndSetListener(R.id.isSmart_cb);
        findCbAndSetListener(R.id.show_ripple_cb);

        findViewById(R.id.svg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SvgTextActivity.class));
            }
        });
        findViewById(R.id.radio_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RadioActivity.class));
            }
        });
        findViewById(R.id.button_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ButtonActivity.class));
            }
        });
        findViewById(R.id.text_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TextActivity.class));
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.pressed_color_cb:
                injection.pressed.color = checked ? 0xff0097a7 : SelectorInjection.DEFAULT_COLOR;
                break;
            case R.id.normal_stroke_cb:
                injection.normal.strokeColor = checked ? 0xff0288d1 : 0xffffffff;
                injection.normal.strokeWidth = 10;
                break;
            case R.id.pressed_stroke_cb:
                injection.pressed.strokeColor = checked ? 0xff82ebf2 : 0x000000;
                injection.pressed.strokeWidth = 10;
                break;
            case R.id.isSmart_cb:
                injection.isSmart = checked;
                // 如果不是smart模式，并且没有设置按下的颜色
                if (!checked && !((CompoundButton) findViewById(R.id.pressed_color_cb)).isChecked()) {
                    injection.pressed.color = SelectorInjection.DEFAULT_COLOR;
                }
                break;
            case R.id.show_ripple_cb:
                injection.showRipple = checked;
                break;
        }
        injection.injection();
    }

    private CompoundButton findCbAndSetListener(int id) {
        CompoundButton btn = findViewById(id);
        btn.setOnCheckedChangeListener(this);
        return btn;
    }

    public Drawable getDrawableCompat(int drawableResId) {
        return getResources().getDrawable(drawableResId);
    }
}
