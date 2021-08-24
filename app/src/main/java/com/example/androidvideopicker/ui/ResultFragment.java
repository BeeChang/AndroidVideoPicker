package com.example.androidvideopicker.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidvideopicker.R;


public class ResultFragment extends Fragment {
    public ResultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_result, container, false);
        Bundle bundle = getArguments();

        if(bundle != null){
            TextView textView = rootView.findViewById(R.id.textView);
            textView.setText(bundle.getString("contentUri"));
        }
        return rootView;
    }
}