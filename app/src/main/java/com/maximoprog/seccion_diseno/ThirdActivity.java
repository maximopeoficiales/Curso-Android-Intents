package com.maximoprog.seccion_diseno;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.maximoprog.seccion_diseno.databinding.ActivityThirdBinding;

public class ThirdActivity extends AppCompatActivity {
    public final int PHONE_CALL_CODE = 100;
    ActivityThirdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_third);
        binding = ActivityThirdBinding.inflate(getLayoutInflater());
        //se carga la vista con el binding
        setContentView(binding.getRoot());
        binding.imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.editTextNumber.getText().toString();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
//                    Comprobar version actual de android estamos corriendo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        Pido permisos para usar la app de llamadas

//                        Comprobar si ha aceptado, no ha aceptado, o nunca se le ha preguntado
                        if (CheckPermission(Manifest.permission.CALL_PHONE)) {
//                            ha aceptado
                            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
//                        si el usuario rechaza el permiso sale del switch
                            if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                                return;
                            startActivity(i);
                        } else {
//                            ha aceptado o  es la primera que se le pregunta
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {

                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                            } else {
//                                ha denegado
                                sayMessage("Por favor habilita el permiso de llamadas");
                                /* Abre la aplicacion de configuracion con informacion de nuestra aplicacion
                                    Evita el historial para al dar atras no vuelva a las configuraciones
                                 */
                                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);

                            }

                        }
                    } else {
                        OlderVersions(phoneNumber);
                    }
                } else {
                    sayMessage("Por favor Rellena el campo telefono");
                }
            }


            //            para versiones antiguas menos a la 6.0
            private void OlderVersions(String phoneNumber) {
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("telf" + phoneNumber));
                if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                    startActivity(intentCall);
                } else {
                    sayMessage("El permiso fue denegado :C");
                }

            }
        });
    }

    //este metodo se ejecuta cada vez que solicita un permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String permission = permissions[0];
        int result = grantResults[0];
        switch (requestCode) {
            case PHONE_CALL_CODE:
                if (permission.equals(Manifest.permission.CALL_PHONE)) {
//                    comprobar si ha sido aceptadod o denegado la peticion del permiso
                    if (result == PackageManager.PERMISSION_GRANTED) {
//                            Concedio su permiso
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + binding.editTextNumber.getText().toString()));
//                        si el usuario rechaza el permiso sale del switch
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                            return;
                        startActivity(intentCall);
                    } else {
//                         no concendio el permiso
                        sayMessage("No se concendio el Permiso :C");
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    //    Verifica si un permiso esta permitido
    private boolean CheckPermission(String permission) {
        return this.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void sayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}