package com.gtappdevelopers.gfgsqldatabase;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //creating variables for our edit text
    private EditText courseNameEdt, courseDurationEdt, courseDescriptionEdt;
    //creating variable for button
    private Button submitCourseBtn;
    //creating a strings for storing our values from edittext fields.
    private String courseName, courseDuration, courseDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing our edittext and button
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription);
        courseDurationEdt = findViewById(R.id.idEdtCourseDuration);
        submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);
        submitCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting data from edittext fields.
                courseName = courseNameEdt.getText().toString();
                courseDescription = courseDescriptionEdt.getText().toString();
                courseDuration = courseDurationEdt.getText().toString();

                //validating the text fileds if empty or not.
                if (TextUtils.isEmpty(courseName)) {
                    courseNameEdt.setError("Please enter Course Name");
                } else if (TextUtils.isEmpty(courseDescription)) {
                    courseDescriptionEdt.setError("Please enter Course Description");
                } else if (TextUtils.isEmpty(courseDuration)) {
                    courseDurationEdt.setError("Please enter Course Duration");
                } else {
                    //calling method to add data to Firebase Firestore.
                    addDataToDatabase(courseName, courseDescription, courseDuration);
                }
            }
        });
        
    }

    private void addDataToDatabase(String courseName, String courseDescription, String courseDuration) {
        //url to post our data
        String url = "https://exclusive-cottons.000webhostapp.com/addCourses.php";
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //on below line we are calling a string request method to post the data to our API
        //in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG","RESPONSE IS "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //on below line we are displaying a success toast message.
                    Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //and setting data to edit text as empty
                courseNameEdt.setText("");
                courseDescriptionEdt.setText("");
                courseDurationEdt.setText("");
                  }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                //as we are passing data in the form of url encoded so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                //below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                //on below line we are passing our key and value pair to our parameters.
                params.put("courseName", courseName);
                params.put("courseDuration", courseDuration);
                params.put("courseDescription", courseDescription);
                //at last we are returning our params.
                return params;
            }
        };
        //below line is to make a json object request.
        queue.add(request);
    }
}