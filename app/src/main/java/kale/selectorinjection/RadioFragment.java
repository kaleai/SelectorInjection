package kale.selectorinjection;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * @author Kale
 * @date 2018/4/25
 */
public class RadioFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.radio_layout, null);
        setViews(root);
        return root;
    }

    private void setViews(View root) {
        RadioGroup imageRg = root.findViewById(R.id.image_rg);
        imageRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(getActivity(), "checked", Toast.LENGTH_SHORT).show();
            }
        });

        // FIXME: 2018/4/25 這裏有個bug是必須在代碼中設置默認的選中狀態 
        ((RadioButton) imageRg.getChildAt(0)).setChecked(true);

        ((RadioButton) imageRg.getChildAt(0)).setOnCheckedChangeListener(this);
        ((RadioButton) imageRg.getChildAt(1)).setOnCheckedChangeListener(this);
        ((RadioButton) imageRg.getChildAt(2)).setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        button.setTypeface(isChecked ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }

    @Override
    public String getName() {
        return "RADIO";
    }
}
