package com.icoding.warranty;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.icoding.warranty.data.WarrantyData;
import com.icoding.warranty.data.WarrantyDataDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Integer.parseInt;

public class AddEditWarranty extends AppCompatActivity {

    WarrantyDataDatabase mWarrantyDataDatabase;
    Disposable compositeDisposable;
    EditText nameInput;
    EditText descriptionInput;
    EditText dateInput;
    EditText durationInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_warrenty);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameInput = findViewById(R.id.wName);
        descriptionInput = findViewById(R.id.wDescription);
        dateInput = findViewById(R.id.wDate);
        durationInput = findViewById(R.id.wDuration);



        compositeDisposable = new CompositeDisposable();
        mWarrantyDataDatabase = Room.databaseBuilder(getApplicationContext(),WarrantyDataDatabase.class,"WarrantyData").build();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                ArrayList<WarrantyData> newWarranty = new ArrayList<>();
                String name = nameInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String date = dateInput.getText().toString();
                int duration = parseInt(durationInput.getText().toString());

                WarrantyData warrantyData = new WarrantyData(name,description,date,duration);
                newWarranty.add(warrantyData);


                insertWarranty(newWarranty);


            }
        });



    }

    public void insertWarranty(ArrayList<WarrantyData> newWarranty){
        compositeDisposable = mWarrantyDataDatabase.getWarrantyDataDao().insertWarranty(newWarranty).subscribeOn(Schedulers.io()).subscribe(new Action() {
            @Override
            public void run() throws Exception {

            }
        });

    }
}