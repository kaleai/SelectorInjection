package kale.selectorinjection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

/**
 * @author Kale
 * @date 2018/4/27
 */
public class RippleFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ripple_layout, null);
        setViews(root);
        return root;
    }

    private void setViews(View root) {
        final RadioButton btn = root.findViewById(R.id.radio_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setChecked(true);
            }
        });
    }

    @Override
    public String getName() {
        return "RIPPLE";
    }
}
