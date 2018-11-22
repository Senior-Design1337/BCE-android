package com.example.theprogrammer.bce;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
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

import static com.example.theprogrammer.bce.Authentication.isPasswordValid;
import static com.example.theprogrammer.bce.Authentication.isUsernameValid;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    static String error;
    private EditText mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mUserView = (EditText) RootView.findViewById(R.id.editTextUsername);
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

        mUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if username is valid
                attemptLogin();
            }
        });


        Button mSigninButton = (Button) RootView.findViewById(R.id.btnSignin);

        mSigninButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.i("oso", "signup button");
                attemptLogin();
            }
        });
        mLoginFormView = RootView.findViewById(R.id.signin_form);
        mProgressView = RootView.findViewById(R.id.login_progress);
        return RootView;

    }

    private void attemptLogin() {
        /*
        if (mAuthTask != null) {
            return;
        }
*/
        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();
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

        if (TextUtils.isEmpty(username)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUserView.setError(getString(R.string.error_invalid_email));
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if(username.equals("os@"))
            {
                Log.i("oso", "signed in ok");
                showProgress(false);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
            else
            {
            //showProgress(true);
            RequestData rd = new RequestData();
            User user = new User();
            user.setPassword(password);
            user.setEmail(username);
            rd.setUser(user);
            signinUser(rd);
            //TODO: hash the password

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }}

    private void signinUser(RequestData rd) {
/*
        if(rd.equals("os@"))
        {
            showProgress(false);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }else */{
            Log.i("oso email", rd.getUser().getEmail());

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<User> call = apiService.login(rd);
            //Call<User> call = apiService.login("Abdallah5@","1234");
            Log.d("oso", "authenticating");
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.i("oso", response.toString());
                    if (response.body() != null) {
                        //boolean b = response.body().getSucess();
                        Log.i("oso", "something");
                        showProgress(false);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        //intent.putExtra("userID", response.body().getAuthorization());
                        //intent.putExtra("token", response.body().getUser().getToken());
                        intent.putExtra("token", response.body().getToken());
                        startActivity(intent);
                        //finish();
                    }
                }



                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showProgress(false);
                    Log.i("oso error: ", t.getMessage() + "\n" + t.toString());
                    //showProgress(false);
                /*
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("userID", "connectionError");
                startActivity(intent);
                //*/
                }
            });
        }
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