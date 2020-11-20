package com.icoding.warranty;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.icoding.warranty.data.WarrantyData;
import com.icoding.warranty.data.WarrantyDataDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
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

        Intent intent = getIntent();
        final String warrantyIdString = intent.getAction();

        compositeDisposable = new CompositeDisposable();
        mWarrantyDataDatabase = Room.databaseBuilder(getApplicationContext(),WarrantyDataDatabase.class,"WarrantyData").build();
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fab1 = findViewById(R.id.fab1);

        if(warrantyIdString != null){
            int warrantyId = parseInt(warrantyIdString);
            loadWarranty(warrantyId);
        }





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


                if(warrantyIdString != null){
                    int warrantyId = parseInt(warrantyIdString);
                    WarrantyData warrantyData = new WarrantyData(warrantyId,name,description,date,duration);
                    newWarranty.add(warrantyData);
                }else {
                    WarrantyData warrantyData = new WarrantyData(name,description,date,duration);
                    newWarranty.add(warrantyData);
                }

                insertWarranty(newWarranty);
            }
        });


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int warrantyId = parseInt(warrantyIdString);
                String name = nameInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String date = dateInput.getText().toString();
                int duration = parseInt(durationInput.getText().toString());
                WarrantyData warrantyData = new WarrantyData(warrantyId,name, description,date,duration);
                deleteWarrranty(warrantyData);

            }

        });
    }

    public void insertWarranty(ArrayList<WarrantyData> newWarranty){
        compositeDisposable = mWarrantyDataDatabase.getWarrantyDataDao().insertWarranty(newWarranty).subscribeOn(Schedulers.io()).subscribe(new Action() {
            @Override
            public void run() throws Exception {

            }
        });

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }


    private void loadWarranty(int warrantyId){
        compositeDisposable = mWarrantyDataDatabase.getWarrantyDataDao().getSingleWarrenty(warrantyId).subscribe(new Consumer<List<WarrantyData>>() {
            @Override
            public void accept(List<WarrantyData> warrantyData) throws Exception {
                Log.e("TAG", "vratila se " + warrantyData.get(0).name);
                handleDatabaseQuery(warrantyData);
            }
        });

    }
    private void handleDatabaseQuery(List<WarrantyData> warrantyData){
        nameInput.setText(warrantyData.get(0).getName());
        descriptionInput.setText(warrantyData.get(0).getDescription());
        dateInput.setText(warrantyData.get(0).getDate());
        durationInput.setText(String.valueOf(warrantyData.get(0).getDuration()));
        compositeDisposable.dispose();

    }

    private void deleteWarrranty(WarrantyData warranty){
        compositeDisposable = mWarrantyDataDatabase.getWarrantyDataDao().deleteWarranty(warranty).subscribeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e("TAG","obrisan " + integer);
            }
        });
        finish();
    }
}