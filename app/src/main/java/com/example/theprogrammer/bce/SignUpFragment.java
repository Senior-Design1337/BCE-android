package com.example.theprogrammer.bce;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.theprogrammer.bce.model.RequestData;
import com.example.theprogrammer.bce.model.Result;
import com.example.theprogrammer.bce.model.User;
import com.example.theprogrammer.bce.rest.ApiClient;
import com.example.theprogrammer.bce.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.theprogrammer.bce.Authentication.isEmailValid;
import static com.example.theprogrammer.bce.Authentication.isPasswordValid;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    static String error;
    //private EditText mUserView;
    private EditText mPasswordView;
    private EditText mEmailView;
    private View mProgressView;
    private View mLoginFormView;

    public SignUpFragment() {
        // Required empty public constructor
    }

    //TODO: Progressbar timeout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        //mUserView = (EditText) RootView.findViewById(R.id.editTextUsername);
        error = new String();
        mPasswordView = (EditText) RootView.findViewById(R.id.editTextPass);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //check if password is valid here
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mEmailView = (EditText) RootView.findViewById(R.id.editTextEmail);
/*
        mUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if username is valid
                attemptLogin();
            }
        });
*/
        mEmailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if username is valid
                attemptLogin();
            }
        });

        Button mSignUpButton = (Button) RootView.findViewById(R.id.btnSignup);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.i("oso", "signup button");
                attemptLogin();
            }
        });
        mLoginFormView = RootView.findViewById(R.id.signup_form);
        mProgressView = RootView.findViewById(R.id.progressBar);
        return RootView;
    }


    private void attemptLogin() {
        /*
        if (mAuthTask != null) {
            return;
        }
*/
        // Reset errors.
        // mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        // String username = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();
        String email = mEmailView.getText().toString();
        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
/*
        if (TextUtils.isEmpty(username)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUserView.setError(getString(R.string.error_invalid_email));
            focusView = mUserView;
            cancel = true;
        }
*/
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            //TODO: hash the password
            //TODO: SignUp is here

            RequestData rd = new RequestData();
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setName("osama");
            rd.setUser(user);

            signUpUser(rd);

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }

    private void signUpUser(RequestData rd) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call = apiService.CreateUser(rd);
        Log.d("oso", "authenticating");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.i("oso", response.toString());
                showProgress(false);
                if (response.body() != null) {
                    //boolean b = response.body().getSucess();
                    Log.i("oso","something");
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.i("oso error: ", t.getMessage() + "\n" + t.toString());
                showProgress(false);

            }
        });

    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
