package com.carlosdev.lab6_20210535;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovimientosLimaPassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovimientosLimaPassFragment extends Fragment {

    private static final String TAG = "MovimientosFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private MovimientoAdapter adapter;
    private List<MovimientoLimaPass> movimientos = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Button btnAgregar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovimientosLimaPassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovimientosLimaPassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovimientosLimaPassFragment newInstance(String param1, String param2) {
        MovimientosLimaPassFragment fragment = new MovimientosLimaPassFragment();
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
        View view = inflater.inflate(R.layout.fragment_movimientos_lima_pass, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewMovimientos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAgregar = view.findViewById(R.id.btnAgregarMovimiento);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        adapter = new MovimientoAdapter(movimientos, this::eliminarMovimiento);
        recyclerView.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RegistrarMovimientoFragment())
                    .addToBackStack(null)
                    .commit();
        });

        cargarMovimientos();
        return view;
    }

    private void cargarMovimientos() {
        if (auth.getCurrentUser() == null) {
            Log.e(TAG, "Usuario no autenticado");
            return;
        }

        String uid = auth.getCurrentUser().getUid();
        Log.d(TAG, "Cargando movimientos para usuario: " + uid);

        db.collection("movimientos_lima_pass")
                .whereEqualTo("idUsuario", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Consulta exitosa. Documentos encontrados: " + queryDocumentSnapshots.size());
                    movimientos.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d(TAG, "Procesando documento: " + doc.getId());
                        Log.d(TAG, "Datos del documento: " + doc.getData());

                        MovimientoLimaPass mov = doc.toObject(MovimientoLimaPass.class);
                        mov.setId(doc.getId());
                        movimientos.add(mov);

                        Log.d(TAG, "Movimiento agregado: " + mov.getFecha() + " - " + mov.getIdTarjeta());
                    }

                    Log.d(TAG, "Total movimientos cargados: " + movimientos.size());
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar movimientos", e);
                });
    }

    private void eliminarMovimiento(String id) {
        Log.d(TAG, "Eliminando movimiento: " + id);
        db.collection("movimientos_lima_pass").document(id).delete()
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Movimiento eliminado exitosamente");
                    cargarMovimientos();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar movimiento", e);
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarMovimientos();
    }
}