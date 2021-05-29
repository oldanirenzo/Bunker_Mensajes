package com.bunker.bunkermensaje;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS=0;

    EditText numeroAgregar, numeroEliminar;
    Button btnAgregar, btnEliminar;
    TextView textAgregar, textEliminar;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            //Si el permiso no esta concedido, reviso si el usuario lo denego
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS)){
                //No hago nada porque el usuario denego los permisos
            }else{
                //Un popup aparecera pidiendo por los servicios
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }
        numeroAgregar = findViewById(R.id.numeroAgregar);
        numeroEliminar = findViewById(R.id.numeroEliminar);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnEliminar = findViewById(R.id.btnEliminar);
        textAgregar = findViewById(R.id.textAgregar);
        textEliminar = findViewById(R.id.textEliminar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numeroAgregar.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "El número no puede estar vacio",Toast.LENGTH_SHORT).show();
                }else{
                    Registrar(v);
                }
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numeroEliminar.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "El número no puede estar vacio",Toast.LENGTH_SHORT).show();
                }else{
                    Eliminar(v);
                }
            }
        });
    }
    //    Método para eliminar teléfono
    public void Eliminar(View view){
        AndroidSQLiteOpenHelper admin = new AndroidSQLiteOpenHelper(this, "mensajes", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String numeroAEliminar = numeroEliminar.getText().toString();

            int cantidad = db.delete("numeros","numeroTel = '" +  numeroAEliminar+"'",null);
        db.close();

        numeroEliminar.setText("");
            if (cantidad == 1) {
                Toast.makeText(this, "Numero eliminado",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No existe el numero",Toast.LENGTH_SHORT).show();
            }
    }

//    Método para agregar teléfono
    public void Registrar(View view){
        AndroidSQLiteOpenHelper admin = new AndroidSQLiteOpenHelper(this,"mensajes",null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("numeroTel", numeroAgregar.getText().toString());

        db.insert("numeros", null, registro);
        db.close();
        numeroAgregar.setText("");

        Toast.makeText(this, "Numero agregado", Toast.LENGTH_SHORT).show();
    }



    //Despues de tener el resultado de los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        //Miro el requestCode
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:
            {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startService(new Intent(MainActivity.this, MyService.class));
                    Toast.makeText(this, "Permisos aceptados.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "No se puede hacer nada sin permisos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}