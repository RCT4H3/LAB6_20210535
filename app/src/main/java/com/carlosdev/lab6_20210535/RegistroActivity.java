package com.carlosdev.lab6_20210535;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombres, etApellidos, etDni, etTelefono,
            etCorreoRegistro, etContrasenaRegistro, etConfirmarContrasena;
    private Button btnRegistrar;
    private TextView tvVolverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etDni = findViewById(R.id.etDni);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreoRegistro = findViewById(R.id.etCorreoRegistro);
        etContrasenaRegistro = findViewById(R.id.etContrasenaRegistro);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        tvVolverLogin = findViewById(R.id.tvVolverLogin);

        btnRegistrar.setOnClickListener(v -> {
            if (!validarCampos()) {
                return;
            }

            String nombres = etNombres.getText().toString().trim();
            String apellidos = etApellidos.getText().toString().trim();
            String dni = etDni.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String correo = etCorreoRegistro.getText().toString().trim();
            String contrasena = etContrasenaRegistro.getText().toString().trim();

            Usuario usuario = new Usuario();
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setDni(dni);
            usuario.setTelefono(telefono);
            usuario.setCorreo(correo);

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            auth.createUserWithEmailAndPassword(usuario.getCorreo(), contrasena)
                    .addOnSuccessListener(authResult -> {
                        String uid = authResult.getUser().getUid();
                        usuario.setUid(uid);

                        db.collection("usuarios").document(uid).set(usuario);
                    })
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    })

                    .addOnFailureListener(e -> {
                        Log.e("Auth", "Error al registrar en Auth", e);
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        });

        tvVolverLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    //Se usó IA para las validaciones

    private boolean validarCampos() {
        if (etNombres.getText().toString().trim().isEmpty()) {
            etNombres.setError("Ingrese sus nombres");
            etNombres.requestFocus();
            return false;
        }

        if (etApellidos.getText().toString().trim().isEmpty()) {
            etApellidos.setError("Ingrese sus apellidos");
            etApellidos.requestFocus();
            return false;
        }

        String dni = etDni.getText().toString().trim();
        if (dni.isEmpty() || dni.length() != 8) {
            etDni.setError("El DNI debe tener 8 dígitos");
            etDni.requestFocus();
            return false;
        }

        if (etTelefono.getText().toString().trim().isEmpty()) {
            etTelefono.setError("Ingrese su teléfono");
            etTelefono.requestFocus();
            return false;
        }

        String correo = etCorreoRegistro.getText().toString().trim();
        if (correo.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreoRegistro.setError("Ingrese un correo válido");
            etCorreoRegistro.requestFocus();
            return false;
        }

        String contrasena = etContrasenaRegistro.getText().toString().trim();
        if (contrasena.isEmpty() || contrasena.length() < 6) {
            etContrasenaRegistro.setError("La contraseña debe tener al menos 6 caracteres");
            etContrasenaRegistro.requestFocus();
            return false;
        }

        String confirmarContrasena = etConfirmarContrasena.getText().toString().trim();
        if (!contrasena.equals(confirmarContrasena)) {
            etConfirmarContrasena.setError("Las contraseñas no coinciden");
            etConfirmarContrasena.requestFocus();
            return false;
        }

        return true;
    }
}