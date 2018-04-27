package kale.selectorinjection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import kale.ui.view.SelectorTextView;

/**
 * @author Kale
 * @date 2018/4/26
 */
public class TextFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.text_layout, null);
        setViews(root);
        return root;
    }

    private void setViews(View root) {
        final SelectorTextView view = root.findViewById(R.id.check_tv);

        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getActivity(), "checked? " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        root.findViewById(R.id.disable_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
            }
        });
    }

    @Override
    public String getName() {
        return "TEXT";
    }
}
