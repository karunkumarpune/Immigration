package com.immigration.view.statisticReport.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.immigration.R;

public class MainActivity extends AppCompatActivity {

    private Button btnDownload;
    String URL="http://www.pdf995.com/samples/pdf.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dwonload);

        btnDownload= (Button) findViewById(R.id.btnDownload);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  new DownloadTask(MainActivity.this,URL);



            }
        });
    }
}
