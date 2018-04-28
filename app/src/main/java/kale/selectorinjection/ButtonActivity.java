package kale.selectorinjection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import kale.ui.view.SelectorTextView;

/**
 * @author Kale
 * @date 2018/4/25
 */
public class ButtonActivity extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.button_layout, null);
        setViews(root);
        return root;
    }

    private void setViews(View root) {
        final SelectorTextView button = root.findViewById(R.id.check_btn);

        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    button.setText("is checked");
                } else {
                    button.setText("unchecked");
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.toggleCompat();
            }
        });

        root.findViewById(R.id.disable_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) v).setText("disable");
                v.setEnabled(false);
            }
        });
    }

    @Override
    public String getName() {
        return "BUTTON";
    }
}
