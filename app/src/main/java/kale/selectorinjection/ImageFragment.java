package kale.selectorinjection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import kale.injection.SelectorInjection;
import kale.ui.view.ISelectorView;

/**
 * @author Kale
 * @date 2016/9/6
 */
public class ImageFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private ISelectorView view;

    private SelectorInjection injection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.image_layout, null);
        setViews(root);
        return root;
    }

    private void setViews(View root) {
        view = root.findViewById(R.id.stv);
//        view.setChecked(true);
        ((View) view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
            }
        });

        injection = view.getSelectorInjection();

        findCbAndSetListener(root, R.id.pressed_color_cb);
        findCbAndSetListener(root, R.id.pressed_drawable_cb);
        findCbAndSetListener(root, R.id.normal_stroke_cb);
        findCbAndSetListener(root, R.id.pressed_stroke_cb);
        findCbAndSetListener(root, R.id.isSmart_cb);
        findCbAndSetListener(root, R.id.show_ripple_cb);
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
                injection.smartColor = checked;
                // 如果不是smart模式，并且没有设置按下的颜色
                if (!checked && !((CompoundButton) getView().findViewById(R.id.pressed_color_cb)).isChecked()) {
                    injection.pressed.color = SelectorInjection.DEFAULT_COLOR;
                }
                break;
            case R.id.show_ripple_cb:
                injection.showRipple = checked;
                break;
        }
        injection.injection();
    }

    private CompoundButton findCbAndSetListener(View root, int id) {
        CompoundButton btn = root.findViewById(id);
        btn.setOnCheckedChangeListener(this);
        return btn;
    }

    @Override
    public String getName() {
        return "IMAGE";
    }
}
