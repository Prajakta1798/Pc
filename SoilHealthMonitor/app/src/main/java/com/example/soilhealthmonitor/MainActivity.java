package com.example.soilhealthmonitor;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class MainActivity extends AppCompatActivity {


    private static final String TAG =MainActivity.class.getName() ;
    Button btnfeatch,btnhumgraph,btntemp,btnph;
    private Firebase mRef;
    String hum,ph,temp;

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;

    LineChartView lineChartView;
  //  String[] axisData;
    String[] axisData = {"Nasik", "Sinnar", "Dindori"};
    int[] yAxisData /*= {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18}*/;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseApp.initializeApp(getApplicationContext());

        btnfeatch=findViewById(R.id.btn_fetch_data);
        btnhumgraph=findViewById(R.id.btn_hum_graph);
        btnph=findViewById(R.id.btn_ph_graph);
        btntemp=findViewById(R.id.btn_temp_graph);



       // mRef = new Firebase("https://soiltestdemo.firebaseio.com/.json?auth=HHj3n7t0PM7zGseUsTemkI2R5yVgss0uXxI2QrxI");  // DataBase Profile Link

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
     //   final DatabaseReference ref = database.getReference("https://soiltestdemo.firebaseio.com/");
        final DatabaseReference ref = database.getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                hum = dataSnapshot.child("humidity").getValue().toString();
                Log.d(TAG, "oncreate onDataChange:hum "+hum);

                ph = dataSnapshot.child("ph").getValue().toString();
                Log.d(TAG, "oncreate onDataChange:ph "+ph);

                temp = dataSnapshot.child("temp").getValue().toString();
                Log.d(TAG, "oncreate onDataChange:temp "+temp);

                     /*   for (DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            String key= snapshot.getKey();
                            String value=snapshot.getValue().toString();

                            Log.d(TAG, "onDataChange:key "+key+ " value :"+value);


                        }



                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Log.d(TAG, "onDataChange: "+"Inside for loop");
                            String humidity = ds.child("hum").getValue().toString();
                            Log.d(TAG, "onDataChange:humidity "+humidity);
                            //etc
                        }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        // DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Video_Upload").child("Street");

        btnfeatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Button click ", Toast.LENGTH_SHORT).show();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                         hum = dataSnapshot.child("humidity").getValue().toString();
                        Log.d(TAG, "onDataChange:hum "+hum);

                         ph = dataSnapshot.child("ph").getValue().toString();
                        Log.d(TAG, "onDataChange:ph "+ph);

                         temp = dataSnapshot.child("temp").getValue().toString();
                        Log.d(TAG, "onDataChange:temp "+temp);

                     /*   for (DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            String key= snapshot.getKey();
                            String value=snapshot.getValue().toString();

                            Log.d(TAG, "onDataChange:key "+key+ " value :"+value);


                        }



                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Log.d(TAG, "onDataChange: "+"Inside for loop");
                            String humidity = ds.child("hum").getValue().toString();
                            Log.d(TAG, "onDataChange:humidity "+humidity);
                            //etc
                        }*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


        btnhumgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: hum : "+hum+" ph :"+ph+" temp :"+temp);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        hum = dataSnapshot.child("humidity").getValue().toString();
                        Log.d(TAG, "onDataChange:hum "+hum);

                        ph = dataSnapshot.child("ph").getValue().toString();
                        Log.d(TAG, "onDataChange:ph "+ph);

                        temp = dataSnapshot.child("temp").getValue().toString();
                        Log.d(TAG, "onDataChange:temp "+temp);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                String[] s_array = hum.split(",");
                yAxisData = new int[s_array.length];

                for(int i =0; i<yAxisData.length;i++)
                {
                    yAxisData[i]= Integer.parseInt(s_array[i]);// Parsing from string to int

                    Log.d(TAG, "onClick: "+yAxisData[i]);
                }
                Log.d(TAG, "onClick:sizes "+yAxisData.length+" x axis "+axisData.length);

                lineChartView = findViewById(R.id.chart);

                List yAxisValues = new ArrayList();
                List axisValues = new ArrayList();


                Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

                for (int i = 0; i < axisData.length; i++) {
                    axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
                }

                for (int i = 0; i < yAxisData.length; i++) {
                    yAxisValues.add(new PointValue(i, yAxisData[i]));
                }

                List lines = new ArrayList();
                lines.add(line);

                LineChartData data = new LineChartData();
                data.setLines(lines);

                Axis axis = new Axis();
                axis.setValues(axisValues);
                axis.setTextSize(16);
                axis.setTextColor(Color.parseColor("#03A9F4"));
                data.setAxisXBottom(axis);

                Axis yAxis = new Axis();
                yAxis.setName("Temperature");
                yAxis.setTextColor(Color.parseColor("#03A9F4"));
                yAxis.setTextSize(16);
                data.setAxisYLeft(yAxis);

                lineChartView.setLineChartData(data);
                Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                viewport.top = 60;
                lineChartView.setMaximumViewport(viewport);
                lineChartView.setCurrentViewport(viewport);

                // int[] array = Arrays.stream(strings).mapToInt(Integer::parseInt).toArray();
             //   yAxisData = Arrays.stream(hum).mapToInt(Integer::parseInt).toArray();

               // yAxisData = hum.split(",");

            }
        });

        btnph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: hum : "+hum+" ph :"+ph+" temp :"+temp);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        hum = dataSnapshot.child("humidity").getValue().toString();
                        Log.d(TAG, "onDataChange:hum "+hum);

                        ph = dataSnapshot.child("ph").getValue().toString();
                        Log.d(TAG, "onDataChange:ph "+ph);

                        temp = dataSnapshot.child("temp").getValue().toString();
                        Log.d(TAG, "onDataChange:temp "+temp);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                String[] s_array = ph.split(",");
                yAxisData = new int[s_array.length];

                for(int i =0; i<yAxisData.length;i++)
                {
                    yAxisData[i]= Integer.parseInt(s_array[i]);// Parsing from string to int

                    Log.d(TAG, "onClick: "+yAxisData[i]);
                }
                Log.d(TAG, "onClick:sizes "+yAxisData.length+" x axis "+axisData.length);

                lineChartView = findViewById(R.id.chart);

                List yAxisValues = new ArrayList();
                List axisValues = new ArrayList();


                Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

                for (int i = 0; i < axisData.length; i++) {
                    axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
                }

                for (int i = 0; i < yAxisData.length; i++) {
                    yAxisValues.add(new PointValue(i, yAxisData[i]));
                }

                List lines = new ArrayList();
                lines.add(line);

                LineChartData data = new LineChartData();
                data.setLines(lines);

                Axis axis = new Axis();
                axis.setValues(axisValues);
                axis.setTextSize(16);
                axis.setTextColor(Color.parseColor("#03A9F4"));
                data.setAxisXBottom(axis);

                Axis yAxis = new Axis();
                yAxis.setName("Temperature");
                yAxis.setTextColor(Color.parseColor("#03A9F4"));
                yAxis.setTextSize(16);
                data.setAxisYLeft(yAxis);

                lineChartView.setLineChartData(data);
                Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                viewport.top = 10;
                lineChartView.setMaximumViewport(viewport);
                lineChartView.setCurrentViewport(viewport);

                // int[] array = Arrays.stream(strings).mapToInt(Integer::parseInt).toArray();
             //   yAxisData = Arrays.stream(hum).mapToInt(Integer::parseInt).toArray();

               // yAxisData = hum.split(",");

            }
        });

        btntemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: hum : "+hum+" ph :"+ph+" temp :"+temp);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        hum = dataSnapshot.child("humidity").getValue().toString();
                        Log.d(TAG, "onDataChange:hum "+hum);

                        ph = dataSnapshot.child("ph").getValue().toString();
                        Log.d(TAG, "onDataChange:ph "+ph);

                        temp = dataSnapshot.child("temp").getValue().toString();
                        Log.d(TAG, "onDataChange:temp "+temp);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                String[] s_array = temp.split(",");
                yAxisData = new int[s_array.length];

                for(int i =0; i<yAxisData.length;i++)
                {
                    yAxisData[i]= Integer.parseInt(s_array[i]);// Parsing from string to int

                    Log.d(TAG, "onClick: "+yAxisData[i]);
                }
                Log.d(TAG, "onClick:sizes "+yAxisData.length+" x axis "+axisData.length);

                lineChartView = findViewById(R.id.chart);

                List yAxisValues = new ArrayList();
                List axisValues = new ArrayList();


                Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

                for (int i = 0; i < axisData.length; i++) {
                    axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
                }

                for (int i = 0; i < yAxisData.length; i++) {
                    yAxisValues.add(new PointValue(i, yAxisData[i]));
                }

                List lines = new ArrayList();
                lines.add(line);

                LineChartData data = new LineChartData();
                data.setLines(lines);

                Axis axis = new Axis();
                axis.setValues(axisValues);
                axis.setTextSize(16);
                axis.setTextColor(Color.parseColor("#03A9F4"));
                data.setAxisXBottom(axis);

                Axis yAxis = new Axis();
                yAxis.setName("Sales in millions");
                yAxis.setTextColor(Color.parseColor("#03A9F4"));
                yAxis.setTextSize(16);
                data.setAxisYLeft(yAxis);

                lineChartView.setLineChartData(data);
                Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                viewport.top = 30;
                lineChartView.setMaximumViewport(viewport);
                lineChartView.setCurrentViewport(viewport);

                // int[] array = Arrays.stream(strings).mapToInt(Integer::parseInt).toArray();
             //   yAxisData = Arrays.stream(hum).mapToInt(Integer::parseInt).toArray();

               // yAxisData = hum.split(",");

            }
        });



       /* lineChartView = findViewById(R.id.chart);

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Sales in millions");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);*/
    }


}