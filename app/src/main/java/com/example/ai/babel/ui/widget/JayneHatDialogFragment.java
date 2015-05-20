package com.example.ai.babel.ui.widget;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.example.ai.babel.R;

/**
 * Sample implementation of custom dialog by extending {@link SimpleDialogFragment}.
 *
 * @author David Vávra (david@inmite.eu)
 */
public class JayneHatDialogFragment extends SimpleDialogFragment {

    public static String TAG = "jayne";

    public static void show(FragmentActivity activity) {
        new JayneHatDialogFragment().show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    public BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        builder.setTitle("Jayne's hat");
        builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.view_jayne_hat, null));
        builder.setPositiveButton("I want one", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (IPositiveButtonDialogListener listener : getPositiveButtonDialogListeners()) {
                    listener.onPositiveButtonClicked(mRequestCode);
                }
                dismiss();
            }
        });
        return builder;
    }
}