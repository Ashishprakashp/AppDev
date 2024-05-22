package com.example.e_courier;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class calculator extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        Spinner dropdown = findViewById(R.id.spinner);
//create a list of items for the spinner.
        String[] items = new String[]{"Normal", "Premium", "Express","Premium express"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        Button calculate=(Button) findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double weight=Double.parseDouble(((EditText) findViewById(R.id.weightET)).getText().toString());
                int distance=Integer.parseInt(((EditText) findViewById(R.id.distanceET)).getText().toString());
                String type= ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();
                int Normal_min=50;
                int express_min=75;
                int premium_min=100;
                int expresspremium_min=150;
                double cost=0;
                if(type.equals("Normal")){
                    cost=distance/10 + (weight*distance)/5;
                    cost=Math.max(cost,Normal_min);
                }else if(type.equals("Premium")){
                    cost=distance/9 + (weight*distance)/3;
                    cost=Math.max(cost,express_min);
                }else if(type.equals("Express")){
                    cost=distance/10 + (weight*distance)/3;
                    cost=Math.max(cost,premium_min);
                }else if(type.equals("Premium express")){
                    cost=distance/7 + (weight*distance)/3;
                    cost=Math.max(cost,expresspremium_min);
                }
                TextView tv = (TextView) findViewById(R.id.op);
                tv.setText(String.valueOf(cost));
            }
        });
    }
}
