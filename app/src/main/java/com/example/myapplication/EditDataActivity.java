package com.example.myapplication;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditDataActivity extends AppCompatActivity {
    private EditText title;
    private EditText content;
    private int position;
    private  Data data;
    private CardView cardView;
    private SwitchCompat switchCompat;
    private LinearLayout timelayout;
    private TextView timedate;
    private Spinner spinner;
    private String typename;
    private String timestr="";
    private String datestr="";
    private String datatimestr="";
    private Long startdate;
    private boolean checked;
    private int typeposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title=findViewById(R.id.titletext);
        content=findViewById(R.id.contenttext);
        cardView=findViewById(R.id.editcard);
        Intent intent=getIntent();
        position=intent.getIntExtra("position",0);
        data=(Data)intent.getSerializableExtra("data");
        title.setText(data.getTitle());
        content.setText(data.getContent());
        cardView.setCardBackgroundColor(data.getBordercolor());
        switchCompat=findViewById(R.id.switchcompat);
        timelayout=findViewById(R.id.timelayout);
        timedate=findViewById(R.id.timedate);
        spinner=findViewById(R.id.spinnertype);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Constants.typenames);
        spinner.setAdapter(adapter);
        spinner.setSelection(data.getTypeposition());
        switchCompat.setChecked(data.isChecked());
        checked=data.isChecked();
        datatimestr=data.getStartdatestr();
        if(checked){
            timelayout.setVisibility(View.VISIBLE);
        }else {
            timelayout.setVisibility(View.GONE);
        }
        timedate.setText(data.getStartdatestr());
        startdate=data.getStartdate();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typename=Constants.typenames[position];
                typeposition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    timelayout.setVisibility(View.VISIBLE);
                    timedate.setText(datatimestr);
                }else {
                    timelayout.setVisibility(View.GONE);
                }
                checked=isChecked;
            }
        });
        timedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                new DatePickerDialog(EditDataActivity.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datestr=year+"年"+month+"月"+dayOfMonth+"日";
                        new TimePickerDialog(EditDataActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                timestr=hourOfDay+"时"+minute+ "分";
                                datatimestr=datestr+timestr;
                                timedate.setText(datatimestr);
                                Calendar ca=Calendar.getInstance();
                                ca.set(year,month,dayOfMonth,hourOfDay,minute);
                                startdate=ca.getTimeInMillis();
                                Toast.makeText(EditDataActivity.this,datatimestr,Toast.LENGTH_SHORT).show();
                            }
                        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();

                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_editdata, menu);
        return true;
    }
    private boolean checkdata(){
        String titlestr=title.getText().toString();
        String contentstr=content.getText().toString();
        if(!TextUtils.isEmpty(titlestr) && !TextUtils.isEmpty(contentstr)){
            return true;
        }
        return false;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                if(checkdata()){
                Intent intent=new Intent();
                data.setTitle(title.getText().toString());
                data.setContent(content.getText().toString());
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy:HH:mm");
                String datestring=simpleDateFormat.format(date);
                data.setDate(datestring);
                data.setStartdatestr(datatimestr);
                data.setTypename(typename);
                data.setTypeposition(typeposition);
                data.setChecked(checked);
                data.setStartdate(startdate);

                    Date nowdate=new Date();
                    if(checked && startdate> nowdate.getTime()) {
                        //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent noticeintent = new Intent(EditDataActivity.this, MyService.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("data",data);
                        noticeintent.putExtra("data", bundle);
                        noticeintent.putExtra("position",position);
                        startService(noticeintent);
                        //PendingIntent pendingIntent = PendingIntent.getService(this, 0, noticeintent,PendingIntent.FLAG_UPDATE_CURRENT);
                        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, data.getStartdate(), pendingIntent);
                        //alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(data.getStartdate(),pendingIntent),pendingIntent);

                    }
                    intent.putExtra("action",true);
                    intent.putExtra("position",position);
                    intent.putExtra("data",data);
                setResult(Activity.RESULT_OK,intent);
                }
                finish();
                break;
            case R.id.delete:
                Intent deleteintent=new Intent();
                deleteintent.putExtra("action",false);
                deleteintent.putExtra("position",position);
                setResult(Activity.RESULT_OK,deleteintent);
                finish();
                break;
            case R.id.review:
                Intent reviewintent=new Intent(this,PreviewActivity.class);
                data.setTitle(title.getText().toString());
                data.setContent(content.getText().toString());
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy:HH:mm");
                String datestring=simpleDateFormat.format(date);
                data.setDate(datestring);
                data.setTypename(typename);
                reviewintent.putExtra("data",data);
                startActivityForResult(reviewintent,1);
                break;
            case android.R.id.home:
                finish();
                break;
            default: break;
        }
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode==1){
            if(resultCode==Activity.RESULT_OK){
                data=(Data)intent.getSerializableExtra("data");
                cardView.setCardBackgroundColor(data.getBordercolor());
            }
        }
    }

}