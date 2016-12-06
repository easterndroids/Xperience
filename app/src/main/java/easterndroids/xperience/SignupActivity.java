package easterndroids.xperience;

/**
 * Created by Yuan on 2016-11-01.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity
{
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_firstName) EditText _firstNameText;
    @Bind(R.id.input_lastName) EditText _lastNameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.UserName) EditText _Username;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void signup()
    {

        if (!validate())
        {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        /*******************************************************************************************
         *post user's information to database
         ******************************************************************************************/
        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String username = _Username.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String type = "register";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        backgroundWork.execute(type,firstName,lastName,email,mobile,username,reEnterPassword);

        new android.os.Handler().postDelayed(
                new Runnable()
                {
                    public void run()
                    {

                        onSignupSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess()
    {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed()
    {
        Toast.makeText(getBaseContext(), "Sign In failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }
    /*******************************************************************************************
     *check if user's information are valid
     ******************************************************************************************/
    public boolean validate()
    {
        boolean valid = true;

        String name = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String username = _Username.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3)
        {
            _firstNameText.setError("at least 3 characters");
            valid = false;
        }
        else
        {
            _firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3)
        {
            _lastNameText.setError("at least 3 characters");
            valid = false;
        }
        else
        {
            _lastNameText.setError(null);
        }

        if (username.isEmpty())
        {
            _Username.setError("Enter Valid Username");
            valid = false;
        }
        else
        {
            _Username.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            _emailText.setError("enter a valid email address");
            valid = false;
        }
        else
        {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10)
        {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        }
        else
        {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10)
        {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }
        else
        {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password)))
        {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        }
        else
        {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}