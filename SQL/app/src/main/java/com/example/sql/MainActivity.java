package com.example.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.SQLData;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText ed_sport;
    private Button btn_query, btn_insert, btn_update, btn_delete;

    private ListView listView;
    private RadioGroup rg;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw;
    private String yn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_sport = findViewById(R.id.ed_sport);
        rg = findViewById(R.id.rg);
        btn_query = findViewById(R.id.btn_query);
        btn_insert = findViewById(R.id.btn_insert);
        btn_delete = findViewById(R.id.btn_delete);
        listView = findViewById(R.id.listView);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.rby:
                        yn = "有做";
                        break;
                    case R.id.rbn:
                        yn = "沒做";
                        break;
                }
            }
        });


        btn_query.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Cursor c;
                if (ed_sport.length() < 1)
                    c = dbrw.rawQuery("SELECT * FROM myTable", null);
                else
                    c = dbrw.rawQuery("SELECT * FROM myTable WHERE sport LIKE '" + ed_sport.getText().toString() + "'", null);

                c.moveToFirst();
                items.clear();
                Toast.makeText(MainActivity.this, "共有" + c.getCount() +
                        "筆資料", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < c.getCount(); i++) {
                    items.add("運動項目" + c.getString(0) + "\t\t\t\t執行狀況" + c.getString(1));
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();
                c.close();
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(ed_sport.length()<1 || yn.length()<1)
                    Toast.makeText(MainActivity.this, "ffi8firJ5^82E",
                            Toast.LENGTH_SHORT).show();
                else{
                    try{

                        dbrw.execSQL("INSERT INTO myTable(sport, yn) VALUES(?,?)", new Object[]{ed_sport.getText().toString(),
                                yn});
                        Toast.makeText(MainActivity.this,
                                "運動項目："+ ed_sport.getText().toString()
                                        + "執行狀況："+ yn,
                                Toast.LENGTH_SHORT).show();

                        ed_sport.setText("");
                        //yn.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "新增失敗:"+
                                e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ed_sport.length()<1)
                    Toast.makeText(MainActivity.this,"運動項目請勿留空",Toast.LENGTH_SHORT).show();
                else{
                    try{
                        dbrw.execSQL("DELETE FROM myTable WHERE sport LIKE '" +ed_sport.getText().toString() + "'");
                        Toast.makeText(MainActivity.this, "刪除運動"+
                                ed_sport.getText().toString() , Toast.LENGTH_SHORT).show();

                        ed_sport.setText("");
                        //yn.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "刪除失敗:"+
                                e.toString(),Toast.LENGTH_LONG).show();


                    }
                }
            }
        });



        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        dbrw = new MyDBHelper(this).getWritableDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbrw.close();
    }
}

