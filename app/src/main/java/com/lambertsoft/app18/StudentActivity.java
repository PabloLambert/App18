package com.lambertsoft.app18;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;


public class StudentActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    TextView textStudentFirstName, getTextStudentLastName;
    Spinner spinnerSchools;
    ImageButton btnTakePicture;
    Button btnCreate, btnModify, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        textStudentFirstName = (TextView) findViewById(R.id.textStudentFirstName);
        getTextStudentLastName = (TextView) findViewById(R.id.textStudentLastName);
        spinnerSchools = (Spinner) findViewById(R.id.spinnerSchools);
        btnTakePicture = (ImageButton) findViewById(R.id.btnTakePicture);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnModify = (Button)findViewById(R.id.btnModify);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!textStudentFirstName.getText().toString().isEmpty() && !getTextStudentLastName.getText().toString().isEmpty()) {

                    ParseObject photo = new ParseObject("Student");
                    photo.put("FirstName", textStudentFirstName.getText().toString());
                    photo.put("LastName", getTextStudentLastName.getText().toString());
                    photo.saveInBackground();
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese valores", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            btnTakePicture.setImageBitmap(imageBitmap);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student, menu);
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

/*
public class StudentActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    TextView textCreateStudentFirstName, textCreateStudentLastName;
    Button buttonCreateStudent;
    ImageButton buttonTakePicture;
    Client myKinveyClient;
    Spinner spinnerSchools;
    ArrayAdapter<String> dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        myKinveyClient = SplashActivity.myKinveyClient;

        textCreateStudentFirstName = (TextView) findViewById(R.id.textCreateStudentFirstName);
        textCreateStudentLastName = (TextView) findViewById(R.id.textCreateStudentLastName);
        spinnerSchools = (Spinner) findViewById(R.id.spinnerSchools);
        buttonTakePicture = (ImageButton) findViewById(R.id.buttonTakePicture);
        buttonCreateStudent = (Button) findViewById(R.id.buttonCreateStudent);

        List<String> list = new ArrayList<String>();
        list.add("       ");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSchools.setAdapter(dataAdapter);

        AsyncAppData<Schools> allSchools = myKinveyClient.appData("Schools", Schools.class);
        allSchools.get(new KinveyListCallback<Schools>() {
            @Override
            public void onSuccess(Schools[] schoolArray) {
                dataAdapter.clear();
                for (int i = 0; i < schoolArray.length; i++)
                    dataAdapter.add(schoolArray[i].getName());
            }

            @Override
            public void onFailure(Throwable throwable) {
                CharSequence text = "Could not get all schools...";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

            }
        });

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        buttonCreateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Students students = new Students();
                students.setData(textCreateStudentFirstName.getText().toString(), textCreateStudentLastName.getText().toString(), myKinveyClient.user().getId());

                AsyncAppData<Students> myStudents = myKinveyClient.appData("Students", Students.class);
                myStudents.save(students, new KinveyClientCallback<Students>() {
                    @Override
                    public void onSuccess(Students students) {
                        CharSequence text = "Your new student has been created.";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        CharSequence text = "Could not create a student...";
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            buttonTakePicture.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student, menu);
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
 */