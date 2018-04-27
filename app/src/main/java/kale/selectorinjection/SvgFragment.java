package kale.selectorinjection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Kale
 * @date 2016/9/6
 */
public class SvgFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.svg_layout, null);
        setViews(root);
        return root;
    }

    private void setViews(View root) {
        
    }

    @Override
    public String getName() {
        return "SVG";
    }
}
