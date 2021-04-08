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

//        boton para hacer llamadas
        binding.imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.txtPhone.getText().toString();
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


//        boton para la direccion web
        binding.imageButtonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = binding.txtWeb.getText().toString();
                if (url != null && !url.isEmpty()) {
                    Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + url));
                    /*intentWeb.setAction(Intent.ACTION_VIEW);
                    intentWeb.setData(Uri.parse("http://" + url));*/
                    startActivity(intentWeb);
                } else {
                    sayMessage("Por favor Rellenar el campo url");
                }
            }
        });
//         boton marcar al telefono pero no llamar
        binding.imageButtonPhoneMarcar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberPhone = binding.txtPhone2.getText().toString();
                if (numberPhone != null && !numberPhone.isEmpty()) {
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numberPhone));
                    intentPhone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentPhone.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intentPhone.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intentPhone);
                } else {
                    sayMessage("Por favor Rellenar el campo del telefono a marcar");
                }
            }
        });
//        enviar correo de manera implicita
        binding.imageButtonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEmail.getText().toString();
                if (email != null && !email.isEmpty()) {
                    Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                    startActivity(intentEmail);
                } else {
                    sayMessage("Por favor Rellenar el campo email");
                }
            }
        });
//        abrir aplicacion de contactos
        binding.imageButtonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people")));
            }
        });
//        intent Explicito porque tambien se puede hacer explicito espefico con que app quieres que se habra
        binding.imageButtonSendEmailBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iEmail = new Intent(Intent.ACTION_SEND);
                iEmail.setType("message/rfc822");
                iEmail.putExtra(Intent.EXTRA_SUBJECT, "Titulo de Email");
                iEmail.putExtra(Intent.EXTRA_TEXT, "Hola bro esto es un correo");
                iEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"maximopeoficiales1@gmail.com", "maximopeoficiales2@gmail.com"});
                iEmail.setPackage("com.google.android.gm");
                if (iEmail.resolveActivity(getPackageManager()) != null) {
                    startActivity(iEmail);
//                startActivity(Intent.createChooser(iEmail, "Elige cliente de correo"));
                } else {
                    sayMessage("Gmail App is not installed");
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
                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + binding.txtPhone.getText().toString()));
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