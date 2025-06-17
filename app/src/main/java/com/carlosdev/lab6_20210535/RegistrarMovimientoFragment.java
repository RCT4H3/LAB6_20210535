package com.carlosdev.lab6_20210535;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrarMovimientoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarMovimientoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText etIdTarjeta, etParaderoEntrada, etParaderoSalida;

    private String fecha = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
;
    private Button btnGuardar;

    private FirebaseFirestore db;


    public RegistrarMovimientoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarMovimientoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrarMovimientoFragment newInstance(String param1, String param2) {
        RegistrarMovimientoFragment fragment = new RegistrarMovimientoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registrar_movimiento, container, false);

        etIdTarjeta = view.findViewById(R.id.etIdTarjeta);
        etParaderoEntrada = view.findViewById(R.id.etParaderoEntrada);
        etParaderoSalida = view.findViewById(R.id.etParaderoSalida);
        btnGuardar = view.findViewById(R.id.btnRegistrarMovimiento);

        db = FirebaseFirestore.getInstance();

        btnGuardar.setOnClickListener(v -> registrarMovimiento());

        return view;
    }

    private void registrarMovimiento() {
        String idTarjeta = etIdTarjeta.getText().toString().trim();
        String entrada = etParaderoEntrada.getText().toString().trim();
        String salida = etParaderoSalida.getText().toString().trim();

        if (TextUtils.isEmpty(idTarjeta) || TextUtils.isEmpty(fecha) ||
                TextUtils.isEmpty(entrada) || TextUtils.isEmpty(salida)) {
            Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String idMovimiento = UUID.randomUUID().toString();

        String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        MovimientoLimaPass movimiento = new MovimientoLimaPass(idMovimiento, idTarjeta, fecha, entrada, salida, idUsuario);

        db.collection("movimientos_lima_pass")
                .document(idMovimiento)
                .set(movimiento)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Movimiento registrado", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed(); // volver
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}