package com.ador.tourmateex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userNameET,passwordET,emailET;
    private TextView signInTV;
    private Button signBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            return;

        }
        userNameET= (EditText) findViewById(R.id.userNameET);
        passwordET= (EditText) findViewById(R.id.passwordET);
        emailET= (EditText) findViewById(R.id.emailET);
        signBtn= (Button) findViewById(R.id.signBtn);
        signInTV= (TextView) findViewById(R.id.signInTV);

        progressDialog= new ProgressDialog(this);

        signBtn.setOnClickListener(this);
        signInTV.setOnClickListener(this);
    }

    private void registerUser(){
        final String userName = userNameET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (userName.isEmpty() || password.isEmpty() || email.isEmpty())
                {
                    Toast.makeText(SignUpActivity.this, "Empty Fields!!!", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("username",userName);
                params.put("password",password);
                params.put("email",email);


                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view == signBtn)
            registerUser();
        if (view == signInTV)
            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
    }
}
