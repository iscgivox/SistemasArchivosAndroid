package net.ivanvega.sistemasarchivosandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener{

    Button btnClear, btnSave ;
    RadioGroup gpoOption;
    EditText txt ;
    String  LOGMSJ = "SISTEMA-ARCHIVOS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicialziar();
        checkPermissionExternalStorage(this);

    }

    private void inicialziar() {
      gpoOption =   (RadioGroup)findViewById(R.id.rgAlamacenamiento);
        btnClear =(Button)findViewById(R.id.btnClearText); btnClear.setOnClickListener(this);
        btnSave  =(Button)findViewById(R.id.btnSave); btnSave.setOnClickListener(this);
            txt = (EditText) findViewById(R.id.txtText);
        gpoOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.optExterna){

                }
            }
        });

    }

    private void saveInternal(){
        try {
            FileOutputStream fileOutputStream = openFileOutput("mi-archivo-interno.txt", MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(txt.getText().toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fileOutputStream.close();
            File.createTempFile("otro-texto",".txt");


            for(String item :  getFilesDir().list()){
                Log.d("ARCHIVO-INTERNO-CONTEXT", item );
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"Revisa logcat para detalle de error",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Revisa logcat para detalle de error",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                //premission to read storage
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    doThings();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "We Need permission Storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void doThings() {
        Log.d(LOGMSJ, "LECTURA/ESCRITURA: " + (isExternalStorageWritable() ? "SI":"NO"));
        Log.d(LOGMSJ, "LECTURA/ESCRITURA: " + (isExternalStorageReadable() ? "SI":"NO"));
        Log.d(LOGMSJ, "ESTADO DE LA TARJETA: " + Environment.getExternalStorageState());
        File sdExternal = Environment.
                getExternalStorageDirectory();

        Log.d(LOGMSJ, "ESTADO DE LA TARJETA: " + sdExternal.getAbsolutePath());
        Log.d(LOGMSJ, ": " + sdExternal.getPath());
        Log.d(LOGMSJ, "LEER: " + (sdExternal.canRead()?"si":"no"));

        for (File item : sdExternal.listFiles()){
            Log.d("ARCHIVOSSD", item.getName());
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void checkPermissionExternalStorage ( AppCompatActivity thisActivity){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this,"No hay permiso", Toast.LENGTH_SHORT).show();


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1001);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            doThings();
        }
    }

    @Override
    public void onClick(View v) {
            if(v.equals(btnSave)){
                this.saveInternal();
            }
        if(v.equals(btnClear)){
            this.openInternal();
        }

    }

    private void openInternal() {
        try {

            for(String item :  getCacheDir().list()){
                Log.d("ARCHIVO-INTERNO-CONTEXT", item );
            }

            FileInputStream fileInputStream = openFileInput("mi-archivo-interno.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedInputStream = new BufferedReader(inputStreamReader);
            String line="";

            char[] buff = new char[1024];
            String texto="";
            while (  bufferedInputStream.read(buff) != -1){
                 texto += String.copyValueOf(buff);
                buff = new char[1024];
            }
            txt.setText(texto);
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
