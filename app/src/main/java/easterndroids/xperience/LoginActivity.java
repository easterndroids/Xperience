package easterndroids.xperience;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText UsernameEt,PasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UsernameEt = (EditText)findViewById(R.id.UName);
        PasswordEt = (EditText)findViewById(R.id.Password);

    }

    public void NavigateToRegistration(View view)
    {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void NavigateToUseXP(View view)
    {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        backgroundWork.execute(type,username,password);
//        System.out.println("result:" + backgroundWork.result);
//        Intent intent = new Intent(this, XperienceActivity.class);
//        startActivity(intent);
    }
}
