package com.maximoprog.seccion_diseno;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maximoprog.seccion_diseno.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public final String GRETTER = "Texto de MainActivity";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//se intancia el binding
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
//        se carga la vista con el binding
        setContentView(binding.getRoot());
//        se cargan listerner
        this.binding.btnNewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sayMessage("Me diste Click ");
//                cargara la segunda actividad
                startActivity(SecondActivity.class);
            }
        });
    }


    public void sayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("greeter", GRETTER);
        startActivity(intent);
    }

}