package com.example.passgen;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.content.res.ResourcesCompat;

        import com.example.passwordgenerator.RandomStrings;
        import com.github.hariprasanths.floatingtoast.FloatingToast;

        import android.app.Activity;
        import android.content.ClipData;
        import android.content.ClipboardManager;
        import android.content.Context;
        import android.content.pm.ActivityInfo;
        import android.content.res.Resources;
        import android.graphics.Color;
        import android.graphics.Typeface;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.SeekBar;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView showMessage, showLength, showPasswordStrength;
    private Button btnSetRandomString;
    private Switch setUppercase, setLowercase, setNumbers, setSymbols;
    private ImageView passwordCopy;
    private SeekBar passwordLength;

    private int length = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Get Viewrs by ID
        setUppercase = findViewById(R.id.setUppercase);
        setLowercase = findViewById(R.id.setLowercase);
        setNumbers = findViewById(R.id.setNumbers);
        setSymbols = findViewById(R.id.setSymbols);
        showLength = findViewById(R.id.showLength);
        showMessage = findViewById(R.id.showPassword);
        btnSetRandomString = findViewById(R.id.btnSetRandomString);
        showPasswordStrength = findViewById(R.id.passwordStrength);
        passwordLength = findViewById(R.id.passwordLength);
        passwordCopy = findViewById(R.id.passwordCopy);

        // Start code with a random string in text field
        String password = generateRandomPassword();

        showPasswordStrength.setText(passwordStrength(password));
        showMessage.setText(password);

        // String length based in seek bar progress
        passwordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                displayProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // If showMessage field is not empty
        // Copy text in showMessage field to the clipboard
        passwordCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String invalidValue = "No option selected!";
                if (showMessage.getText().toString().isEmpty() || showMessage.getText().toString().contains(invalidValue)) {
                    return;
                }

                copyToClipboard(showMessage.getText().toString());
                showCopyToast();
            }
        });

        // Main function to generate random password
        btnSetRandomString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPassword();
            }
        });
    }

    private void displayPassword(){
        String alphaNumericString = generateAlphaNumericString();

        if(alphaNumericString.isEmpty()) {
            String invalidValue = "No option selected! X_x";
            showMessage.setText(invalidValue);
            return;
        }

        String password = generateRandomPassword();
        showPasswordStrength.setText(passwordStrength(password));
        showMessage.setText(password);
    }


    private void displayProgress(int progress){
        showLength.setText(String.valueOf(progress));
        length = progress;
        String realTimeUpdatePassword = generateRandomPassword();
        showPasswordStrength.setText(passwordStrength(realTimeUpdatePassword));
        showMessage.setText(realTimeUpdatePassword);
    }

    private String generateAlphaNumericString(){
        StringBuilder alphaNumericString = new StringBuilder();

        if (setUppercase.isChecked()) {
            alphaNumericString.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if (setLowercase.isChecked()) {
            alphaNumericString.append("abcdefghijklmnopqrstuvwxyz");
        }
        if (setNumbers.isChecked()) {
            alphaNumericString.append("123456789");
        }
        if (setSymbols.isChecked()) {
            alphaNumericString.append("!@#$%^&*(){}/_+-?[]<>");
        }

        return alphaNumericString.toString();
    }

    private String generateRandomPassword(){
        String alphaNumericString = generateAlphaNumericString();

        if (alphaNumericString.isEmpty()){
            alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789!@#$%^&*(){}/_+-?[]<>";
        }

        RandomStrings randomStrings = new RandomStrings(length, alphaNumericString);

        return randomStrings.getRandomString();
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Password", text);
        clipboardManager.setPrimaryClip(clip);
    }

    private void showCopyToast() {
        String invalidValue = "No option selected!";
        try {
            if(showMessage.getText().toString().isEmpty() || showMessage.getText().toString().contains(invalidValue)) {
                return;
            }

            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Password", showMessage.getText());
            clipboardManager.setPrimaryClip(clip);

            String clashFont = "clash.ttf";
            Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), clashFont);

            FloatingToast toast = (FloatingToast) FloatingToast.makeToast(passwordCopy, "Password copied to clipboard!", FloatingToast.LENGTH_MEDIUM)
                    .setTextColor(Color.parseColor("#ffffff"))
                    .setShadowLayer(5, 1, 1, Color.parseColor("#000000"))            .setGravity(FloatingToast.GRAVITY_MID_TOP)
                    .setFadeOutDuration(FloatingToast.FADE_DURATION_LONG)
                    .setTextSizeInDp(18)
                    .setTextTypeface(customFont)
                    .setBackgroundBlur(true);
            toast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String passwordStrength(String password){
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        String specialCharacters = "!@#$%^&*(){}/_+-?[]<>";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (specialCharacters.contains(String.valueOf(c))) {
                hasSpecialCharacter = true;
            }
        }

        if ((hasUppercase && hasLowercase && hasNumber && hasSpecialCharacter)) {
            showPasswordStrength.setTextColor(Color.parseColor("#5fb891"));
            return "Strong";
        } else if ((hasUppercase && hasLowercase && hasNumber)|| (hasUppercase && hasLowercase && hasSpecialCharacter)|| (hasNumber && hasLowercase && hasSpecialCharacter)) {
            showPasswordStrength.setTextColor(Color.parseColor("#edd37b"));
            return "Medium";
        } else {
            showPasswordStrength.setTextColor(Color.parseColor("#f08675"));
            return "Weak";
        }
    }

    public Context getContext(){
        return MainActivity.this;
    }
}

