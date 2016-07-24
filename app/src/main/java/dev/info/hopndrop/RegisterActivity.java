package dev.info.hopndrop;


import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity  {
    //private UserLoginTask mAuthTask = null;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mContactNumberView;
    private EditText mAddressView;
    private View mProgressView;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private View mRegistrationFormView;
    Intent intent;
    private SessionManager session;
    private SQLiteHandler db;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);
        mFirstNameView = (EditText) findViewById(R.id.firstname);
        mLastNameView = (EditText) findViewById(R.id.lastname);
        mAddressView = (EditText) findViewById(R.id.address);
        mContactNumberView = (EditText) findViewById(R.id.contact_number);

        mRegistrationFormView = findViewById(R.id.registration_form);
        mProgressView = findViewById(R.id.registration_progress);
        Button mSignUpButton = (Button)findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();

                /*OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                    @Override
                    public void idsAvailable(String userId, String registrationId) {
                        Log.d("debug", "User:" + userId);
                        if (registrationId != null)
                            Log.d("debug", "registrationId:" + registrationId);
                    }
                });
                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Log.d("Android","Android ID : "+android_id);*/

            }
        });




        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // attemptLogin();
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void attemptRegister() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirm_password = mConfirmPasswordView.getText().toString();
        String first_name = mFirstNameView.getText().toString();
        String last_name = mLastNameView.getText().toString();
        String contact_number = mContactNumberView.getText().toString();
        String address = mAddressView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        // Check for a valid email address.
         if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

         else if (!isEmailValid(email)) {
             mEmailView.setError(getString(R.string.error_invalid_email));
             focusView = mEmailView;
             cancel = true;
         }

        else if (TextUtils.isEmpty(password) || !isPasswordValid(password) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


         else if (!confirm_password.equals(password)) {
             mConfirmPasswordView.setError("Password did not match!");
             focusView = mConfirmPasswordView;
             cancel = true;
         }

        else if (TextUtils.isEmpty(first_name)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(last_name)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(address)) {
            mAddressView.setError(getString(R.string.error_field_required));
            focusView = mAddressView;
            cancel = true;
        } else if (TextUtils.isEmpty(contact_number) && !isContactNumberValid(contact_number)) {
            mContactNumberView.setError(getString(R.string.error_field_required));
            focusView = mContactNumberView;
            cancel = true;
        }







        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            /*showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/

            checkRegister(email, password , first_name, last_name,address, contact_number);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }
    private boolean isContactNumberValid(String contact_number) {
        //TODO: Replace this with your own logic
        return contact_number.length() > 10 || contact_number.length()<12;
    }

    private void checkRegister(final String email,final String password ,final String firstname,final String lastname,final String address,final String contact_number) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Signing up ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Signup Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String  result = jObj.getString("result");
                    Log.d(TAG, "result Response: " + result);
                    // Check for error node in json
                    if (result.equals("success")) {

                        //JSONObject user = jObj.getJSONObject("data");
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Registration Failed", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("address", address);
                params.put("mobile",contact_number);
                params.put("type","User");
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
