package com.example.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DataSQl dataSQl;
    private DataAdapter dataAdapter;
    private List<Data> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        dataSQl=new DataSQl(this,"datastore.db",null,1);
        datas=new ArrayList<Data>();
        initdatalist();
        dataAdapter=new DataAdapter(this,datas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dataAdapter);
        FloatingActionButton floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddDataActivity.class);
                startActivityForResult(intent,Constants.ADD_DATA_REQUEST);
            }
        });


    }

    private void initdatalist() {
        datas.clear();
        SQLiteDatabase db = dataSQl.getWritableDatabase();
        Cursor cursor = db.query("Data", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                int titlecolor=cursor.getInt(cursor.getColumnIndex("titlecolor"));
                int bordercolor=cursor.getInt(cursor.getColumnIndex("bordercolor"));
                int contentcolor=cursor.getInt(cursor.getColumnIndex("contentcolor"));
                String typename=cursor.getString(cursor.getColumnIndex("typename"));
                int typeposition=cursor.getInt(cursor.getColumnIndex("typeposition"));
                int check=cursor.getInt(cursor.getColumnIndex("checked"));
                String startdatestr=cursor.getString(cursor.getColumnIndex("startdatestr"));
                boolean checked;
                if(check==1){
                    checked=true;
                }else{
                    checked=false;
                }
                long startdate=cursor.getLong(cursor.getColumnIndex("startdate"));
                Data data=new Data(id, title,content,date, titlecolor, bordercolor, contentcolor);
                data.setTypename(typename);
                data.setTypeposition(typeposition);
                data.setStartdate(startdate);
                data.setChecked(checked);
                data.setStartdatestr(startdatestr);
                datas.add(data);


            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intent=new Intent(MainActivity.this,AddDataActivity.class);
                startActivityForResult(intent,Constants.ADD_DATA_REQUEST);
                Toast.makeText(this,"search",Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(this,"help",Toast.LENGTH_SHORT).show();
                break;
            default: break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==Constants.ADD_DATA_REQUEST){
                Data data=(Data)intent.getSerializableExtra("data");
                save_data(data);
                dataAdapter.notifyDataSetChanged();
            }
            if(requestCode==Constants.EDIT_DATA_REQUEST){
                boolean action=intent.getBooleanExtra("action",true);
                if(action){
                    Data data=(Data)intent.getSerializableExtra("data");;
                    int position=intent.getIntExtra("position",0);
                    save_modify_data(data,position);
                    dataAdapter.notifyDataSetChanged();
                }else{
                    int position=intent.getIntExtra("position",-1);
                    save_delete_data(position);
                    dataAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void save_delete_data(int position) {
        SQLiteDatabase db = dataSQl.getWritableDatabase();
        int id=datas.get(position).getId();
        db.delete("Data","id=?",new String[]{String.valueOf(id)});
        datas.remove(position);
    }

    private void save_modify_data(Data data,int position) {
        SQLiteDatabase db = dataSQl.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",data.getTitle());
        values.put("content",data.getContent());
        values.put("date",data.getDate());
        values.put("titlecolor",data.getTitlecolor());
        values.put("bordercolor",data.getBordercolor());
        values.put("contentcolor",data.getContentcolor());
        values.put("typename",data.getTypename());
        values.put("typeposition",data.getTypeposition());
        values.put("checked",data.isChecked());
        values.put("startdate",data.getStartdate());
        values.put("startdatestr",data.getStartdatestr());
        int id=data.getId();
        db.update("Data",values,"id=?",new String[]{String.valueOf(id)});
        datas.set(position,data);

    }

    private void save_data(Data data) {
        SQLiteDatabase db = dataSQl.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",data.getTitle());
        values.put("content",data.getContent());
        values.put("date",data.getDate());
        values.put("titlecolor",data.getTitlecolor());
        values.put("bordercolor",data.getBordercolor());
        values.put("contentcolor",data.getContentcolor());
        values.put("typename",data.getTypename());
        values.put("typeposition",data.getTypeposition());
        values.put("checked",data.isChecked());
        values.put("startdate",data.getStartdate());
        values.put("startdatestr",data.getStartdatestr());
        db.insert("Data",null,values);
        String sql="select last_insert_rowid() from Data";
        Cursor cursor=db.rawQuery(sql,null);
        int id=-1;
        if(cursor.moveToFirst()){
            id=cursor.getInt(0);
        }
        data.setId(id);
        datas.add(data);

    }
}