package com.icoding.warranty;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.icoding.warranty.data.WarrantyData;
import com.icoding.warranty.data.WarrantyDataDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    ImageButton cameraButton;
    ImageView imageView;
    String currentPhotoPath;
    File photoFile = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;


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
        cameraButton = findViewById(R.id.cameraButton);
        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        final String warrantyIdString = intent.getAction();

        compositeDisposable = new CompositeDisposable();
        mWarrantyDataDatabase = Room.databaseBuilder(getApplicationContext(),WarrantyDataDatabase.class,"WarrantyData").build();
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fab1 = findViewById(R.id.fab1);
        fab1.setVisibility(View.INVISIBLE);

        if(warrantyIdString != null){
            int warrantyId = parseInt(warrantyIdString);
            loadWarranty(warrantyId);
            fab1.setVisibility(View.VISIBLE);
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

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.icoding.warranty.fileprovider",
                        photoFile);
                System.out.println("Uri" + photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

                    setPic();

                    //full size
//                File imgFile = new  File(currentPhotoPath);
//                System.out.println("putanja je " + currentPhotoPath);
//                if(imgFile.exists()) {
//                    System.out.println("Ima fajla");
//                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                    imageView.setImageBitmap(myBitmap);
//                }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }


}