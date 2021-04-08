package com.maximoprog.seccion_diseno;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maximoprog.seccion_diseno.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_second);
//se intancia el binding
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
//        se carga la vista con el binding
        setContentView(binding.getRoot());
//        obtengo los extras
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getString("greeter") != null) {
            String greeter = bundle.getString("greeter");
//            muestro un msg y seteo el texto en el input
            Toast.makeText(this, greeter, Toast.LENGTH_LONG).show();
            binding.txtTextEmpty.setText(greeter);
        } else {
            Toast.makeText(this, "El bundle es null", Toast.LENGTH_LONG).show();
        }

        binding.btnGoToShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }


}