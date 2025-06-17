package com.carlosdev.lab6_20210535;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;



public class MainActivity extends AppCompatActivity {

    private TextView tvIrRegistro;

    private EditText etCorreo, etContrasena;
    private Button btnLogin;
    private FirebaseAuth auth;

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, GestionActivity.class));
            finish();
            return;
        }

        tvIrRegistro = findViewById(R.id.tvIrRegistro);

        tvIrRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            if (correo.isEmpty()) {
                etCorreo.setError("Ingrese su correo");
                etCorreo.requestFocus();
                return;
            }

            if (contrasena.isEmpty()) {
                etContrasena.setError("Ingrese su contraseña");
                etContrasena.requestFocus();
                return;
            }

            auth.signInWithEmailAndPassword(correo, contrasena)
                    .addOnSuccessListener(authResult -> {
                        Intent intent = new Intent(MainActivity.this, GestionActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    });
        });

        //Inicio de sesión con google:
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Este lo obtienes de google-services.json
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ImageButton btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(v -> signInWithGoogle());


    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Fallo al iniciar sesión con Google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Si el usuario no existe, se crea automáticamente
                        Intent intent = new Intent(MainActivity.this, GestionActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Autenticación con Firebase falló.", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}