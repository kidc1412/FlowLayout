package com.kidc.flowlayouttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);

        int cCount = flowLayout.getChildCount();
        for (int i = 0; i < cCount; i++){
            final TextView tv = (TextView) flowLayout.getChildAt(i);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), tv.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
