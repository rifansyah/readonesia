package com.dev.reef.readonesia.Beradu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dev.reef.readonesia.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeraduFragment extends Fragment implements View.OnClickListener {
    private Button mulaiButton;

    public BeraduFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beradu, container, false);

        mulaiButton = (Button) v.findViewById(R.id.button_mulai);

        mulaiButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_mulai:
                Intent intent = new Intent(getContext(), BeraduPilihBacaanActivity.class);
                startActivity(intent);
                break;
        }
    }
}
