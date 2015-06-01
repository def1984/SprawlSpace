package com.example.ai.babel.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ai.babel.R;
import com.example.ai.babel.ui.AddNewBook;

public class AddNewBookFragment extends Fragment {
    private Button btnDddNewBook;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_add_new_book, container, false);
        btnDddNewBook= (Button) rootView.findViewById(R.id.btn_add_new_book);
        btnDddNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewBook.class));
                getActivity().overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
            }
        });
        return rootView;
    }
}
