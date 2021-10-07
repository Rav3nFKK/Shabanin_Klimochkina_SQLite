package com.example.mynewapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button readBt, addBt, clearBt;
    EditText NameET, MailET;
    TextView Out;
    DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBt = (Button) findViewById(R.id.AddBut);
        readBt = (Button) findViewById(R.id.ReadBut);
        clearBt = (Button) findViewById(R.id.ClearBut);

        addBt.setOnClickListener(this);
        readBt.setOnClickListener(this);
        clearBt.setOnClickListener(this);

        NameET = (EditText) findViewById(R.id.NameET);
        MailET = (EditText) findViewById(R.id.MailET);
        Out = (TextView) findViewById(R.id.Out);

        dBhelper = new DBhelper(this);


    }

    @Override
    public void onClick(View v) {
        String name = NameET.getText().toString();
        String mail = MailET.getText().toString();

        SQLiteDatabase database = dBhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (v.getId()) {
            case R.id.AddBut:
                if (name.isEmpty() && mail.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                    "Поля пусты...", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    contentValues.put(DBhelper.KEY_NAME, name);
                    contentValues.put(DBhelper.KEY_MAIL, mail);

                    database.insert(DBhelper.TABLE_CONTACTS, null, contentValues);
                }
                break;
            case R.id.ReadBut:
                Cursor cursor = database.query(DBhelper.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBhelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBhelper.KEY_NAME);
                    int mailIndex = cursor.getColumnIndex(DBhelper.KEY_MAIL);
                    Out.setText(null);
                    do {
                        Out.append("ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", email = " + cursor.getString(mailIndex) + "\n");

                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", email = " + cursor.getString(mailIndex));
                    }
                    while (cursor.moveToNext());
                } else {
                    Log.d("mLog", "0 rows");
                    Out.setText("Записей нет");
                }
                cursor.close();
                break;
            case R.id.ClearBut:
                database.delete(DBhelper.TABLE_CONTACTS, null, null);
                break;
        }
        dBhelper.close();
    }


}
