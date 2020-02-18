package com.mealChooser.v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

public class InstructionsActivity extends AppCompatActivity {
    CheckBox dontShowCheckbox;
    ImageButton closePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Intent intent = getIntent();
        final MyDBHandler dbHandler = MainActivity.getDbHandler();
        dontShowCheckbox = findViewById(R.id.checkBoxDontShow);
        closePage = findViewById(R.id.closeHelpPageButton);
        dbHandler.addShowHelp();
        closePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        dontShowCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    dbHandler.updateShowHelp();
                } else {

                }
            }
        });
    }
}
