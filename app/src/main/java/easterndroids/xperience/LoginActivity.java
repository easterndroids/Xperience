package easterndroids.xperience;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static easterndroids.xperience.R.id.Register;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText UsernameEt,PasswordEt;
    private Button buttonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UsernameEt = (EditText)findViewById(R.id.UName);
        PasswordEt = (EditText)findViewById(R.id.Password);
        buttonRegister = (Button) findViewById(R.id.Register);
        buttonRegister.setOnClickListener(this);
    }

    public void NavigateToRegistration(View view)
    {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        if(v == buttonRegister){
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }
    }

    public void NavigateToUseXP(View view)
    {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        backgroundWork.execute(type,username,password);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 900);
    }
}
