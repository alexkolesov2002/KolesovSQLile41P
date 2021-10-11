package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnRead, btnClear;
    EditText etName, etEmail;
    DBHelper dbHelper;
    ContentValues contentValues;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.Add);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.Clear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.Name);
        etEmail = (EditText) findViewById(R.id.Mail);


        dbHelper = new DBHelper(this);
        database= dbHelper.getWritableDatabase();
        UpdateTable();
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int mailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
            TableLayout tb2 = findViewById(R.id.tableLayout2);
            tb2.removeAllViews();
            do {
                TableRow tbOUT = new TableRow(this);
                tbOUT.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView ID = new TextView(this);
                params.weight = 1.0f;
                ID.setLayoutParams(params);
                ID.setText(cursor.getString(idIndex));
                tbOUT.addView(ID);


                TextView NAME = new TextView(this);
                params.weight = 1.0f;

                NAME.setLayoutParams(params);
                NAME.setText(cursor.getString(nameIndex));
                tbOUT.addView(NAME);


                TextView MAIL = new TextView(this);
                params.weight = 3.0f;
                MAIL.setLayoutParams(params);
                MAIL.setText(cursor.getString(mailIndex));
                tbOUT.addView(MAIL);


                Button DEL = new Button(this);
                DEL.setOnClickListener(this);
                params.weight = 1.0f;
                DEL.setLayoutParams(params);
                DEL.setText("Удалить запись");
                DEL.setId(cursor.getInt(idIndex));
                tbOUT.addView(DEL);

                tb2.addView(tbOUT);


            } while (cursor.moveToNext());

        }
        cursor.close();
    }
    @Override
    public void onClick(View v) {
        dbHelper = new DBHelper(this);
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        contentValues = new ContentValues();
        switch (v.getId()) {

            case R.id.Add:
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_MAIL, email);

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                UpdateTable();
                break;

            case R.id.Clear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                UpdateTable();
                break;
            default:
                View outBDRow = (View) v.getParent();
                ViewGroup outBD = (ViewGroup)  outBDRow.getParent();
                outBD.removeView(outBDRow);
                outBD.invalidate();

                database.delete(DBHelper.TABLE_CONTACTS,DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUPD = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                if (cursorUPD.moveToFirst()) {
                    int idIndex = cursorUPD.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUPD.getColumnIndex(DBHelper.KEY_NAME);
                    int mailIndex = cursorUPD.getColumnIndex(DBHelper.KEY_MAIL);
                    int realID =1;
                    do{
                        if(cursorUPD.getInt(idIndex)>realID)
                        {
                            contentValues.put(DBHelper.KEY_ID,realID);
                            contentValues.put(DBHelper.KEY_NAME,cursorUPD.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_MAIL,cursorUPD.getString(mailIndex));
                            database.replace(DBHelper.TABLE_CONTACTS,null,contentValues);


                        }
                            realID++;
                    }while (cursorUPD.moveToNext());
                    if(cursorUPD.moveToLast()){
                        database.delete(DBHelper.TABLE_CONTACTS,DBHelper.KEY_ID+ " = ?", new String[]{cursorUPD.getString(idIndex)});
                    }
                    UpdateTable();
                }


                break;


        }
        dbHelper.close();
    }
}
