package com.bcstudio.androidsqlitetoolboxexemple;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bcstudio.androidsqlitetoolbox.Database.Column;
import com.bcstudio.androidsqlitetoolbox.Database.DBHandler;

public class FirstFragment extends Fragment {
    private DBHandler db;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        view.findViewById(R.id.buttonCreateDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DBHandler(v.getContext(), "TEST_DB", null, 1);
            }
        });
        view.findViewById(R.id.buttonAddTableDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db != null)
                    db.addTable("Exemple", new Column("Col1", "text"), new Column("Col2", "text"));
            }
        });
    }
}
