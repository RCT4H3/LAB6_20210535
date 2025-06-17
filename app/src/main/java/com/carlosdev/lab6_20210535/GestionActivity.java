package com.carlosdev.lab6_20210535;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.carlosdev.lab6_20210535.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GestionActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private BottomNavigationView bottomNav;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion);

        auth = FirebaseAuth.getInstance();
        bottomNav = findViewById(R.id.bottom_navigation);

        //Necesario para cerrar la sesión cuando se ha ingresado con google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MovimientosLinea1Fragment())
                .commit();

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_linea1){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MovimientosLinea1Fragment())
                        .commit();
                return true;
            } else if (id == R.id.nav_limapass ) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MovimientosLimaPassFragment())
                        .commit();
                return true;
            } else if (R.id.nav_resumen == id) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ResumenFragment())
                        .commit();
                return true;
            } else if (id == R.id.nav_logout) {
                signOut();
                return true;
            }
            return false;
        });
    }

    private void signOut() {
        // Cerrar sesión de Firebase
        auth.signOut();

        // Cerrar sesión de Google
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent intent = new Intent(GestionActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al cerrar sesión de Google, pero sesión cerrada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GestionActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}