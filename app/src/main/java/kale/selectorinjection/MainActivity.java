package kale.selectorinjection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;

import kale.injection.SelectorInjection;
import kale.ui.view.SelectorTextView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    SelectorTextView view;

    SelectorInjection injection;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (SelectorTextView) findViewById(R.id.stv);
//        view.setChecked(true);

        injection = view.getInjection();

        findCbAndSetListener(R.id.pressed_color_cb);
        findCbAndSetListener(R.id.pressed_drawable_cb);
        findCbAndSetListener(R.id.normal_stroke_cb);
        findCbAndSetListener(R.id.pressed_stroke_cb);
        findCbAndSetListener(R.id.isSmart_cb);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.pressed_color_cb:
                injection.setPressedColor(checked ? 0xff0097a7 : SelectorInjection.DEFAULT_COLOR);
                break;
            case R.id.pressed_drawable_cb:
                injection.setPressed(checked ? getResources().getDrawable(R.drawable.btn_rectangle_shadow_bg_layerlist) :
                            getResources().getDrawable(R.drawable.btn_oval_shape));
                break;
            case R.id.normal_stroke_cb:
                injection.setNormalStrokeColor(checked ? 0xff0288d1 : 0xffffffff);
                injection.setNormalStrokeWidth(10);
                break;
            case R.id.pressed_stroke_cb:
                injection.setPressedStrokeColor(checked ? 0xff82ebf2 : 0x000000);
                injection.setPressedStrokeWidth(10);
                break;
            case R.id.isSmart_cb:
                injection.setSmart(checked);
                // 如果不是smart模式，并且没有设置按下的颜色
                if (!checked && !((CompoundButton) findViewById(R.id.pressed_color_cb)).isChecked()) {
                    injection.setPressedColor(SelectorInjection.DEFAULT_COLOR);
                }
                break;
        }
        injection.injection(view);
    }

    private CompoundButton findCbAndSetListener(int id) {
        CompoundButton btn = (CompoundButton) findViewById(id);
        btn.setOnCheckedChangeListener(this);
        return btn;
    }
}
