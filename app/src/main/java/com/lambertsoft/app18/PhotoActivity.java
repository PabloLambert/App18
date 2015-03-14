package com.lambertsoft.app18;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class PhotoActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_FILE = 2;

    Button btnTakePicture, btnDisk, btnUpload, btnDownload, btnLogout, btnViewDownloadedPhoto;
    ImageView imageView;
    Bitmap bitmap;
    List<ParseObject> downloadedParseObject;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        btnTakePicture = (Button) findViewById(R.id.buttonTakePicture);
        btnDisk = (Button) findViewById(R.id.buttonDisk);
        btnUpload = (Button) findViewById(R.id.buttonUpload);
        btnDownload = (Button) findViewById(R.id.buttonDownload);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnViewDownloadedPhoto = (Button) findViewById(R.id.btnViewDownloadedPhoto);

        imageView = (ImageView) findViewById(R.id.imageView);


        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

        });

        btnDisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_SELECT_FILE);
                /*
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                }
                */
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( bitmap != null ) {
                    // Convert it to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Compress image to lower quality scale 1 - 100
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();

                    // Create the ParseFile
                    ParseFile file = new ParseFile("androidbegin.png", image);
                    // Upload the image into Parse Cloud
                    file.saveInBackground();

                    ParseObject photo = new ParseObject("Photo");
                    photo.put("Name", "Fotografía");
                    photo.put("file", file);
                    photo.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Subiendo imagen...", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "No hay imagen para subir", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadedParseObject = null;
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Photo");
                query.whereExists("Name");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "Total number is:" + parseObjects.size(), Toast.LENGTH_SHORT).show();
                            downloadedParseObject = parseObjects;
                        } else
                            Log.e("MainActivity", e.toString());
                    }
                });
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.logOut();
                finish();
            }
        });

        btnViewDownloadedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadedParseObject != null) {
                    if (counter >= downloadedParseObject.size()) counter = 0;
                    ParseObject currentPhoto = downloadedParseObject.get(counter);
                    counter++;
                    ParseFile file = (ParseFile) currentPhoto.get("file");
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageView.setImageBitmap(bmp);

                        }
                    });


                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_SELECT_FILE && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            if (selectedImage == null ) {
                Log.d("Photo", "No data selected..");
                return;
            }
            String[] filePathColumn = { MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int colunmIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(colunmIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(picturePath);
            imageView.setImageBitmap(bitmap);
        }
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

}
