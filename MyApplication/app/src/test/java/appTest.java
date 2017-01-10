import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.strozh.emailclient.BuildConfig;
import com.strozh.emailclient.R;
import com.strozh.emailclient.login.LoginActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Dzmitry on 09.01.2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
manifest = "AndroidManifest.xml",
sdk = 19)
public class appTest {
    @Test
    public void clickingLogin_shouldStartLoginActivity() throws Exception{
        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
        Button button = (Button) loginActivity.findViewById(R.id.email_sign_in_button);
        EditText editText = (EditText) loginActivity.findViewById(R.id.email);
        editText.setText("login");
        button.performClick();
        assertThat(editText.getText().toString()).isEqualTo("Error login");
    }
}
