package com.example.lizhongbi.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class buttonFragment extends Fragment {

    onClickListener mCallback;


    public buttonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_button, container, false);
        /*Button button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                mCallback.onCanvasSelected();
            }
        });*/
        return view;
    }

    public void onClick(View view)
    {
        mCallback.onCanvasSelected();
    }

    public void setOnClickListener(Activity activity)
    {
        mCallback = (onClickListener) activity;
    }

    public interface onClickListener{
        public void onCanvasSelected();
    }

}
