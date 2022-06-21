package com.example.project_a17mariasm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class DatosMascota extends AppCompatActivity implements AddVeterinario.OnDialogVeterinarioListener, AddAfeccions.onAddAfeccions {

    public static final String EXTRA_ID_MASCOTA = "extraIdMascota";
    public static final String EXTRA_NOME_MASCOTA = "extraNomeMascota";
    public static final String EXTRA_EDAD_MASCOTA = "extraEdadMascota";
    public static final String EXTRA_ESPECIE_MASCOTA = "extraEspecieMascota";
    public static final String EXTRA_CASTRADO_MASCOTA = "extraCastradoMascota";
    public static final String EXTRA_FOTO_MASCOTA = "extraFotoMascota";
    public static final String EXTRA_TIPO_AFECCIONS = "extraTipoAfeccions";
    public static final String EXTRA_NOME_VET = "extraNomeVet";
    public static final String EXTRA_NUMERO_VET = "extraNumeroVet";
    public static final String EXTRA_ID_VET = "extraIdVet";
    private final int REQUEST_CODE_CAMARA = 3;
    private String currentPhotoPath;
    private Uri mImageUri = Uri.EMPTY;
    private File photoFile;

    private MascotasViewModel mascotasViewModel;
    private EditText etName;
    private AutoCompleteTextView etEspecie;
    private CheckBox cbCastrado;
    private Button btnAfeccions;
    private Button btnVeterinario;
    private String nomeVet = "";
    private String numeroVet = "";
    private ArrayList<String> tipoAf = new ArrayList<String>();
    int mascotaId;
    private ImageView imageViewFoto;
    private Spinner spYear, spMonth, spDay;
    private Context myContext;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_mascota);

        myContext = this;

        intent = getIntent();

        etName = (EditText) findViewById(R.id.et_nome_mascota);

        etEspecie =  findViewById(R.id.et_especie_datos);
        cbCastrado =  findViewById(R.id.cb_castrado);
        imageViewFoto =  findViewById(R.id.iv_mascota_datos_activity);
        btnVeterinario = findViewById(R.id.btn_engadir_veterinario);
        btnAfeccions = findViewById(R.id.btn_engadir_afeccion);
        spYear = findViewById(R.id.sp_year);
        spMonth = findViewById(R.id.sp_month);
        spDay = findViewById(R.id.sp_day);

        mascotasViewModel = new ViewModelProvider(this).get(MascotasViewModel.class);

        if (intent.hasExtra(EXTRA_ID_MASCOTA)) {
            setTitle(getString(R.string.edit) + " " + intent.getStringExtra(EXTRA_NOME_MASCOTA));
            mascotaId = intent.getIntExtra(EXTRA_ID_MASCOTA, 1);
            etName.setText(intent.getStringExtra(EXTRA_NOME_MASCOTA));
            etEspecie.setText(intent.getStringExtra(EXTRA_ESPECIE_MASCOTA));
            cbCastrado.setChecked(intent.getBooleanExtra(EXTRA_CASTRADO_MASCOTA, false));
            nomeVet = intent.getStringExtra(EXTRA_NOME_VET);
            numeroVet = intent.getStringExtra(EXTRA_NUMERO_VET);

            if (!intent.getStringExtra(EXTRA_FOTO_MASCOTA).equals("nofoto")) {
                imageViewFoto.setImageURI(Uri.parse(intent.getStringExtra(EXTRA_FOTO_MASCOTA)));
            }
            if (getIntent().getIntExtra(EXTRA_ID_VET, 1) != 1) {
                btnVeterinario.setText(getString(R.string.edit_vet));
            }
            btnAfeccions.setText(getString(R.string.edit_af));

            spinnersConDatos();

        } else {
            setTitle(R.string.add_m);
            adaptarSpYear();
            adaptarSpDay();
        }

        imageViewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sacarFoto();
            }
        });


        btnVeterinario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(nomeVet)) {
                    AddVeterinario vet_dialog = new AddVeterinario();
                    vet_dialog.show(getSupportFragmentManager(), "vet_dialog");
                } else {
                    AddVeterinario vet_dialog = AddVeterinario.newInstance(nomeVet, numeroVet);
                    vet_dialog.show(getSupportFragmentManager(), "vet_dialog");
                }
            }
        });


        btnAfeccions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent.hasExtra(EXTRA_TIPO_AFECCIONS)) {
                    AddAfeccions af_dialog = AddAfeccions.newInstance(mascotaId);
                    af_dialog.show(getSupportFragmentManager(), "af_dialog");
                } else {
                    AddAfeccions afeccions_dialog = new AddAfeccions();
                    afeccions_dialog.show(getSupportFragmentManager(), "afeccions_dialog");
                }
            }
        });

        spinners();
        adaptarAutoCompleteTextView();


    }

    @Override
    public void onVetData(String nome, String numero) {
        if (!"".equals(nome.trim())) {
            this.nomeVet = nome;
            this.numeroVet = numero;
        }
    }

    @Override
    public void onAfData(ArrayList<String> tipo) {
        if (tipo.size() != 0) {
            this.tipoAf = tipo;
        }
    }

    private void saveMascota() {

        int vetId = 1;
        int afId = 1;
        String foto;
        if (getIntent().hasExtra(EXTRA_FOTO_MASCOTA)) {
            foto = getIntent().getStringExtra(EXTRA_FOTO_MASCOTA);
        } else {
            foto = "nofoto";
        }

        if (!"".equals(nomeVet)) {
            if (mascotasViewModel.vetIdFromNomeNumber(nomeVet.trim(), numeroVet.trim()) == 0) {

                if (getIntent().hasExtra(EXTRA_ID_VET) && getIntent().getIntExtra(EXTRA_ID_VET, 1) != 1) {
                    vetId = getIntent().getIntExtra(EXTRA_ID_VET, 1);
                } else {
                    vetId = mascotasViewModel.countVeterinarios() + 1;
                }


                Veterinario veterinario = new Veterinario(vetId, nomeVet.trim(), numeroVet.trim());

                if (getIntent().hasExtra(EXTRA_ID_VET) && getIntent().getIntExtra(EXTRA_ID_VET, 1) != 1) {
                    mascotasViewModel.updateVeterinario(veterinario);
                } else {
                    mascotasViewModel.insertVeterinario(veterinario);
                }
            } else {
                vetId = mascotasViewModel.vetIdFromNomeNumber(nomeVet.trim(), numeroVet.trim());
            }
        }

        String nome = etName.getText().toString();
        String especie = etEspecie.getText().toString();
        boolean castrado = cbCastrado.isChecked();


        if (nome.trim().isEmpty() || especie.trim().isEmpty() || spYear.getSelectedItemPosition() == 0 || spMonth.getSelectedItemPosition() == 0 || spDay.getSelectedItemPosition() == 0) {
            Toast.makeText(this, R.string.missing_data, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mascotaId == 0) {
            mascotaId = mascotasViewModel.countMascotas() + 1;
        }

        if (mImageUri != null) {
            if (!mImageUri.equals(Uri.EMPTY)) {
                foto = mImageUri.toString();
            }
        }

        try {
            for(Especie esp : mascotasViewModel.getAllEspecieList()){
                if(!especie.trim().toLowerCase().equals(esp.getNome_especie().toLowerCase())){
                    mascotasViewModel.insertEspecie(new Especie(especie.trim()));
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        int auxDia= Integer.parseInt(spDay.getSelectedItem().toString());
        String dia = "";
        if (auxDia <10){
            dia = "0"+spDay.getSelectedItem().toString();
        }else{
            dia = spDay.getSelectedItem().toString();
        }

        String fecha = spYear.getSelectedItem().toString() + "/" + mesANumero() + "/" + dia;

        Mascota mascota = new Mascota(mascotaId, nome, fecha, especie, castrado, vetId, foto);

        if (getIntent().hasExtra(EXTRA_ID_MASCOTA)) {
            mascotasViewModel.updateMascota(mascota);
        } else {
            mascotasViewModel.insetMascota(mascota);
        }

        Log.e("vetId =", String.valueOf(vetId));

        if (tipoAf.size() > 0) {
            for (String af : tipoAf) {

                if (mascotasViewModel.afeccionIdFromTipo(af.trim()) == 0) {
                    afId = mascotasViewModel.countAfeccions() + 1;
                    Afeccions afeccions = new Afeccions(afId, af.trim());
                    mascotasViewModel.insertAfeccions(afeccions);
                } else {
                    afId = mascotasViewModel.afeccionIdFromTipo(af.trim());
                }

                MascotaAfeccions join = new MascotaAfeccions(mascotaId, afId);
                mascotasViewModel.insertJoin(join);

            }

        }


        try {
            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_preferences_key), Context.MODE_PRIVATE);
            boolean existeArray = sharedPref.getBoolean("existe_array", false);
            if (existeArray) {
                for (MascotaAfeccions mascotaAfeccions : mascotasViewModel.getAllMascotaAfeccionsList()) {
                    if (mascotaAfeccions.getMascotaId() == mascota.getId()) {
                        mascotasViewModel.deleteJoin(mascotaAfeccions);
                        if (tipoAf.size() > 0) {
                            for (String afeccion : tipoAf) {
                                mascotasViewModel.insertJoin(new MascotaAfeccions(mascota.getId(), mascotasViewModel.afeccionIdFromTipo(afeccion)));
                            }
                        }
                    }
                }
            }
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("existe_array", false);
            editor.apply();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        finish();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_satos_mascota_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_mascota:
                saveMascota();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void sacarFoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = null;

        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photoFile != null) {

            //mImageUri = Uri.fromFile(photoFile);
            mImageUri = FileProvider.getUriForFile(DatosMascota.this,
                    "com.example.project_a17mariasm.provider",
                    photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMARA);

        }

    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";

        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDirectory
        );

        currentPhotoPath = "file: " + image.getAbsolutePath();

        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMARA) {
            if (resultCode == RESULT_OK) {

                try {
                    imageViewFoto.setImageURI(Uri.parse(mImageUri.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //foto =(Bitmap) data.getExtras().get("data");
                //imageViewFoto.setImageBitmap(foto);


            } else {
                mImageUri = null;
                photoFile.delete();
            }
        }
    }


    public void spinners() {

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spMonth.getSelectedItemPosition() != 0 && spYear.getSelectedItemPosition() != 0) {

                    adaptarSpDay();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spMonth.getSelectedItemPosition() != 0 && spYear.getSelectedItemPosition() != 0) {

                    adaptarSpDay();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void adaptarSpYear() {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String yearString = yearFormat.format(date);
        int year = Integer.parseInt(yearString);

        ArrayList<String> yearsArray = new ArrayList<>();
        yearsArray.add(getString(R.string.ano));
        for (int i = year; i > year - 150; i--) {
            yearsArray.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_item, yearsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(adapter);

    }


    public void adaptarSpDay() {
        ArrayList<String> daysArray = new ArrayList<>();
        daysArray.add(getString(R.string.dia));

        int dias = 0;

        switch (spMonth.getSelectedItemPosition()) {

            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:

                dias = 31;
                break;
            case 2:
                if (isLeap(Integer.parseInt(spYear.getSelectedItem().toString()))) {
                    dias = 29;
                } else {
                    dias = 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
            case 13:
                dias = 30;
                break;

        }


        for (int i = 1; i <= dias; i++) {
            daysArray.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_item, daysArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDay.setAdapter(adapter);

        if (intent.hasExtra(EXTRA_ID_MASCOTA)) {
            Mascota mascota = null;
            try {
                mascota = mascotasViewModel.getMascota(intent.getIntExtra(EXTRA_ID_MASCOTA, 1));

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String[] fecha = mascota.getFecha().split(Pattern.quote("/"));

            String dia = fecha[2];

            spDay.setSelection(Integer.parseInt(dia));
        }
    }


    public boolean isLeap(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public String mesANumero() {
        int numero = spMonth.getSelectedItemPosition();
        if (numero < 10) {
            return "0" + String.valueOf(numero);
        } else {
            return String.valueOf(numero);
        }
    }


    public void spinnersConDatos(){

        Mascota mascota = null;
        try {
            mascota = mascotasViewModel.getMascota(intent.getIntExtra(EXTRA_ID_MASCOTA, 1));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] fecha = mascota.getFecha().split(Pattern.quote("/"));

        String ano = fecha[0];
        String mes = fecha[1];
        String dia = fecha[2];

        adaptarSpYear();
        spYear.setSelection(((ArrayAdapter)spYear.getAdapter()).getPosition(ano));
        spMonth.setSelection(Integer.parseInt(mes));
        adaptarSpDay();


        Log.i("FECHA", mascota.getFecha());
    }

    public void adaptarAutoCompleteTextView(){

        ArrayList<String> arrEspecies = new ArrayList<>();
        try {
            for(Especie esp : mascotasViewModel.getAllEspecieList()){
                arrEspecies.add(esp.getNome_especie());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arrEspecies);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.et_especie_datos);
        textView.setAdapter(adapter);
    }


}
