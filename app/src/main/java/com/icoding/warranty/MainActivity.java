package com.icoding.warranty;

import android.content.Intent;
import android.os.Bundle;

import com.chootdev.recycleclick.RecycleClick;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.icoding.warranty.data.WarrantyData;
import com.icoding.warranty.data.WarrantyDataDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WarrantyAdapter mAdapter;
    WarrantyDataDatabase mDeviceDatabase;
    Disposable compositeDisposable;
    private List<WarrantyData> mDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startActivity(new Intent(MainActivity.this,AddEditWarranty.class));
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDeviceDatabase = Room.databaseBuilder(getApplicationContext(), WarrantyDataDatabase.class, "WarrantyData").build();

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void loadData() {
        compositeDisposable = new CompositeDisposable();
        compositeDisposable = mDeviceDatabase.getWarrantyDataDao().getWarranties().subscribe(new Consumer<List<WarrantyData>>() {

            @Override
            public void accept(final List<WarrantyData> warranties) throws Exception {
                //Log.e("data manager", "accept: data manager called async" + devices.size());
                handleResponse(warranties);


                //THIS WILL ADD OnItemClickListener
                RecycleClick.addTo(mRecyclerView).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        //Device currentDevice = mDeviceList.get(position);
                        Toast.makeText(getApplicationContext(), "Clicked on postition " + position + " view " + warranties.get(position).getName() + " " + warranties.get(position).getId(),Toast.LENGTH_SHORT).show();


                        String deviceId = String.valueOf(warranties.get(position).getId());
                        Intent intent = new Intent(MainActivity.this, AddEditWarranty.class);
                        intent.setAction(deviceId);
                        startActivity(intent);
                    }
                });
            }


        });
    }


    public void handleResponse(List<WarrantyData> warranties) {

        mAdapter = new WarrantyAdapter(warranties);

        mRecyclerView.setAdapter(mAdapter);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    protected void onRestart() {
        //workaround to refresh screen :D
        super.onRestart();
        finish();
        startActivity(getIntent());

    }

}
