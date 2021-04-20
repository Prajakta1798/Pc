package com.example.soilhealthmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class UserDashboardActivity extends AppCompatActivity {

    private static final String TAG =UserDashboardActivity.class.getName() ;
    String ph,humidity,temp,moist;
    Spinner spnarea,spntimeinterval,spnsoilparam;

    String strspnarea,strspntimeint,strspnsoilparam;

    Button btngetresult;
    ArrayList<String> al_temp;
    LineChartView lineChartView;
    String[] axisData = {"1","2","3","4","5","6","7","8","9","10"};
    int[] yAxisData;
    Toolbar toolbar;
    SharedPreferences sharedPreferencesuser;
    String USER_LOGIN="USERLOGIN",flag;
     TextView tvplantsugg;

     String strhumsug,strtempsug,strphsugg,strmoissug;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        spnarea=(Spinner)findViewById(R.id.spn_area);
        spnsoilparam=(Spinner)findViewById(R.id.spn_soil_param);
        spntimeinterval=(Spinner)findViewById(R.id.spn_time);
        btngetresult=(Button)findViewById(R.id.btn_get_result);
        tvplantsugg=(TextView) findViewById(R.id.tv_data_shown);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Soil Testing");
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sharedPreferencesuser=getSharedPreferences(USER_LOGIN,MODE_PRIVATE);
        flag=sharedPreferencesuser.getString("flag","");
        Log.d(TAG, "onCreate:user_flag "+flag);


        spnarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strspnarea=spnarea.getSelectedItem().toString();
                Log.d(TAG, "onItemSelected:Area "+strspnarea);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnsoilparam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strspnsoilparam=spnsoilparam.getSelectedItem().toString();
                Log.d(TAG, "onItemSelected:Soil Param "+strspnsoilparam);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spntimeinterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strspntimeint=spntimeinterval.getSelectedItem().toString();
                Log.d(TAG, "onItemSelected:Time interval "+strspntimeint);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btngetresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strspnarea.matches("Select Area")) {
                    Toast.makeText(UserDashboardActivity.this, "Please select area first", Toast.LENGTH_SHORT).show();
                }
                if (strspnsoilparam.matches("Select Soil Parameter")) {
                    Toast.makeText(UserDashboardActivity.this, "Please select Soil Parameter first", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "onClick: Area : " + strspnarea + " Soil Param " + strspnsoilparam );

                    if (strspnarea.matches("Deola"))
                    {

                        if (strspnsoilparam.matches("Temperature")) {

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Deola");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strtempsug = dataSnapshot.child("temp_plant_sugg").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strtempsug " + strtempsug);
                                 //   tvplantsugg.setText(strtempsug);
                                    tvplantsugg.setText(" ");
                                    String[] str_array=strtempsug.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                         tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);


                                    String[] s_array = temp.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    axis.setName("days");
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Temp in deola");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 60;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });


                        }

                        if (strspnsoilparam.matches("Ph")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Deola");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    strphsugg = dataSnapshot.child("ph_plant_sug").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strphsugg " + strphsugg);
                                 //   tvplantsugg.setText(strphsugg);
                                    tvplantsugg.setText(" ");

                                    String[] str_array=strphsugg.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                        tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");
                                       // tvplantsugg.setText( str_array[i]+ System.getProperty ("line.separator"));
                                        //   tvplantsugg.append(str_array[i]);
                                      //  tvplantsugg.append("\n");

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);


                                    String[] s_array = ph.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Ph in deola");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 10;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });

                        }

                        if (strspnsoilparam.matches("Humidity")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Deola");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strhumsug = dataSnapshot.child("hum_plant_sug").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strhumsug " + strhumsug);
                                    tvplantsugg.setText(" ");

                                    String[] str_array=strhumsug.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                        tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");
                                        // tvplantsugg.setText(str_array[i]+"\n");--------------------------

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);


                                    String[] s_array = humidity.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Humidity in deola");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 20;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });

                        }

                        if (strspnsoilparam.matches("Moisture")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Deola");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strmoissug = dataSnapshot.child("most_plant_sug").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strhumsug " + strmoissug);
                                    tvplantsugg.setText(" ");

                                    String[] str_array=strmoissug.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                        tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");
                                        // tvplantsugg.setText(str_array[i]+"\n");--------------------------

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);

                                    moist = dataSnapshot.child("moist").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:moist " + moist);


                                    String[] s_array = moist.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Moisture in deola");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 60;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });

                        }
                    }

                    if (strspnarea.matches("Sinnar")) {

                        if (strspnsoilparam.matches("Temperature")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Sinnar");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strtempsug = dataSnapshot.child("temp_plant_sugg").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strtempsug " + strtempsug);
                                  //  tvplantsugg.setText(strtempsug);
                                    tvplantsugg.setText(" ");
                                    String[] str_array=strtempsug.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                         tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);


                                    String[] s_array = temp.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Temp in Sinnar");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 60;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });
                        }

                        if (strspnsoilparam.matches("Ph")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Sinnar");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strphsugg = dataSnapshot.child("ph_plant_sug").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strphsugg " + strphsugg);
                                   // tvplantsugg.setText(strphsugg);
                                    tvplantsugg.setText(" ");
                                    String[] str_array=strphsugg.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                     //    tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");
                                 tvplantsugg.setText( str_array[i]+ System.getProperty ("line.separator"));
                                    }


                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);


                                    String[] s_array = ph.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Ph in Sinnar");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 10;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });

                        }

                        if (strspnsoilparam.matches("Humidity")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Sinnar");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strhumsug = dataSnapshot.child("hum_plant_sug").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strhumsug " + strhumsug);
                                 //   tvplantsugg.setText(strhumsug);
                                    tvplantsugg.setText(" ");
                                    String[] str_array=strhumsug.split(" ,");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                         tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);


                                    String[] s_array = humidity.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Humidity in Sinnar");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 20;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });

                        }

                        if (strspnsoilparam.matches("Moisture")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Deola");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strmoissug = dataSnapshot.child("most_plant_sug").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strhumsug " + strmoissug);
                                    tvplantsugg.setText(" ");

                                    String[] str_array=strmoissug.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                        tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");
                                        // tvplantsugg.setText(str_array[i]+"\n");--------------------------

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);

                                    moist = dataSnapshot.child("moist").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:moist " + moist);


                                    String[] s_array = moist.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Moisture in Sinnar");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 60;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });

                        }


                    }

                    if (strspnarea.matches("Niphad")) {
                        if (strspnsoilparam.matches("Temperature")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Niphad");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strtempsug = dataSnapshot.child("temp_plant_sugg").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strtempsug " + strtempsug);
                                //    tvplantsugg.setText(strtempsug);
                                    tvplantsugg.setText(" ");
                                    String[] str_array=strtempsug.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                         tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);


                                    String[] s_array = temp.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Temp in Niphad");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 60;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });


                        }

                        if (strspnsoilparam.matches("Ph")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Niphad");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    strphsugg = dataSnapshot.child("ph_plant_sug").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strphsugg " + strphsugg);
                                 //   tvplantsugg.setText(strphsugg);
                                    tvplantsugg.setText(" ");
                                    String[] str_array=strphsugg.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                         tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);


                                    String[] s_array = ph.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Ph in Niphad");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 10;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });

                        }

                        if (strspnsoilparam.matches("Humidity")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Niphad");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strhumsug = dataSnapshot.child("hum_plant_sug").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strhumsug " + strhumsug);
                                   // tvplantsugg.setText(strhumsug);
                                    tvplantsugg.setText(" ");
                                    String[] str_array=strhumsug.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                         tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);


                                    String[] s_array = humidity.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Humidity in Niphad");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 20;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });

                        }

                        if (strspnsoilparam.matches("Moisture")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Deola");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    strmoissug = dataSnapshot.child("most_plant_sug").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:strhumsug " + strmoissug);
                                    tvplantsugg.setText(" ");

                                    String[] str_array=strmoissug.split(",");
                                    for (int i = 0; i < str_array.length; i++) {
                                        Log.d(TAG, "onDataChange:array value "+str_array[i]);
                                        tvplantsugg.append(str_array[i]);
                                        tvplantsugg.append("\n");
                                        // tvplantsugg.setText(str_array[i]+"\n");--------------------------

                                    }

                                    ph = dataSnapshot.child("ph").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:ph " + ph);

                                    humidity = dataSnapshot.child("humidity").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:humidity " + humidity);

                                    temp = dataSnapshot.child("temp").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:temp " + temp);

                                    moist = dataSnapshot.child("moist").getValue().toString();
                                    Log.d(TAG, "oncreate onDataChange:moist " + moist);


                                    String[] s_array = moist.split(",");
                                    yAxisData = new int[s_array.length];

                                    for (int i = 0; i < yAxisData.length; i++) {
                                        yAxisData[i] = Integer.parseInt(s_array[i]);// Parsing from string to int

                                        Log.d(TAG, "onClick: " + yAxisData[i]);
                                    }
                                    Log.d(TAG, "onClick:sizes " + yAxisData.length + " x axis " + axisData.length);

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
                                    axis.setTextSize(14);
                                    axis.setTextColor(Color.parseColor("#03A9F4"));
                                    data.setAxisXBottom(axis);

                                    Axis yAxis = new Axis();
                                    yAxis.setName("Moisture in Niphad");
                                    yAxis.setTextColor(Color.parseColor("#03A9F4"));
                                    yAxis.setTextSize(14);
                                    data.setAxisYLeft(yAxis);

                                    lineChartView.setLineChartData(data);
                                    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                                    viewport.top = 60;
                                    lineChartView.setMaximumViewport(viewport);
                                    lineChartView.setCurrentViewport(viewport);
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException(); // don't ignore errors
                                }
                            });

                        }

                    }
                }
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.item_logout:
                SharedPreferences.Editor editor=sharedPreferencesuser.edit();
                editor.putString("flag","0");
                editor.apply();
                editor.commit();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                // Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}