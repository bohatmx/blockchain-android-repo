package com.aftarobot.blockchaintest;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aftarobot.blockchaintest.crudutils.CompaniesUtil;
import com.aftarobot.blockchaintest.crudutils.DoctorUtil;
import com.aftarobot.blockchaintest.crudutils.HospitalUtil;
import com.aftarobot.blockchaintest.crudutils.ParlourUtil;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Regulator;
import com.aftarobot.mlibrary.util.ClientUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrudActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txt;
    ChainDataAPI chainDataAPI;
    ChainListAPI chainListAPI;
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blockchain Driver");
        getSupportActionBar().setSubtitle("OneConnect Business Network");
        chainDataAPI = new ChainDataAPI(this);
        chainListAPI = new ChainListAPI(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
        getInsuranceCompanies();
    }

    private void confirm() {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Confirm")
                .setMessage("Do you really, really want to do this?\n\nThis generates all the base demo data needed on the blockchain. It will fuck up if this data has been generated before.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doCrud();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void confirmClients() {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Confirm")
                .setMessage("Do you really, really want to do this?\n\nThis generates  about " + MAX_CLIENTS
                        + " clients and beneficiaries demo data needed on the blockchain.\n\n" +
                        "It will take a few minutes to complete .....")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (company == null) {
                            if (!insuranceCompanies.isEmpty()) {
                                company = insuranceCompanies.get(0);
                            } else {
                                throw new RuntimeException("Company is null");
                            }
                        }
                        start = System.currentTimeMillis();
                        ClientUtil.generateClients(getApplicationContext(), company, MAX_CLIENTS + 3, new ClientUtil.ClientListener() {
                            @Override
                            public void clientsComplete() {
                                long end = System.currentTimeMillis();
                                showSnackbar("Client generation done."
                                        .concat(" Elapsed sec ".concat(String.valueOf(getElapsed(start,end)))),
                                        "OK","green");
                            }

                            @Override
                            public void onError(String message) {
                                showError(message);
                            }

                            @Override
                            public void onProgressMessage(String message) {
                                updateText(message);
                                showSnackbar(message,"OK","green");
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public static final int MAX_CLIENTS = 20;
    long start;
    private void doCrud() {
        showSnackbar("Starting CRUD for demo data", "ok", "yellow");
        FBApi api = new FBApi();
        api.removeBeneficiaries(new FBApi.FBListener() {
            @Override
            public void onResponse(Data data) {
                Log.e(TAG, "onResponse: beneficiaries deleted from Firebase" );
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
        start = System.currentTimeMillis();
        CompaniesUtil.generate(this, new CompaniesUtil.InsuranceCompanyListener() {
            @Override
            public void onInsuranceCompanysComplete() {
                HospitalUtil.generate(getApplicationContext(), new HospitalUtil.HospitalListener() {
                    @Override
                    public void onHospitalsComplete() {
                        updateText("\n#############################################\n");
                        ParlourUtil.generate(getApplicationContext(), new ParlourUtil.FuneralParlourListener() {
                            @Override
                            public void onFuneralParloursComplete() {
                                updateText("\n#############################################\n");
                                DoctorUtil.generate(getApplicationContext(), new DoctorUtil.DoctorListener() {
                                    @Override
                                    public void onDoctorsComplete() {
                                        updateText("\n######################################\n");
                                        if (company == null) {
                                            if (!insuranceCompanies.isEmpty()) {
                                                company = insuranceCompanies.get(0);
                                            } else {
                                                throw new RuntimeException("Company is null");
                                            }
                                        }
                                        ClientUtil.generateClients(getApplicationContext(),
                                                company, MAX_CLIENTS, new ClientUtil.ClientListener() {
                                                    @Override
                                                    public void clientsComplete() {
                                                        updateText("\n#############################################\n");
                                                        showSnackbar("Clients and Policies generated", "Cool", "green");
                                                        addRegulator();
                                                    }

                                                    @Override
                                                    public void onError(String message) {
                                                        showError(message);
                                                    }

                                                    @Override
                                                    public void onProgressMessage(String message) {
                                                        updateText(message);
                                                        showSnackbar(message, "OK", "green");
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onProgress(Doctor doc) {
                                        updateText(GSON.toJson(doc));
                                        showSnackbar("Doctor added: ".concat(doc.getFullName()), "OK", "green");
                                    }

                                    @Override
                                    public void onError(String message) {
                                        showError(message);
                                    }
                                });
                            }

                            @Override
                            public void onProgress(FuneralParlour parlour) {
                                updateText(GSON.toJson(parlour));
                                showSnackbar("Parlour added: ".concat(parlour.getName()), "OK", "green");
                            }

                            @Override
                            public void onError(String message) {
                                showError(message);
                            }
                        });
                    }

                    @Override
                    public void onProgress(Hospital hospital) {
                        hospitals.add(hospital);
                        updateText(GSON.toJson(hospital));
                        showSnackbar("Hospital added: ".concat(hospital.getName()), "OK", "green");
                    }

                    @Override
                    public void onError(String message) {
                        showError(message);
                    }
                });
            }

            @Override
            public void onProgress(InsuranceCompany company) {
                insuranceCompanies.add(company);
                updateText(GSON.toJson(company));
                showSnackbar("Company added: ".concat(company.getName()), "OK", "green");
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });


    }

    private void updateText(String data) {
        sb.append(data);
        txt.setText(sb.toString());
    }

    private void addRegulator() {
        showSnackbar("Adding regulator demo data", "ok", "yellow");
        Regulator d1 = new Regulator();
        d1.setIdNumber("1991010167880083");
        d1.setFirstName("Rethabile");
        d1.setLastName("Maseko");
        d1.setEmail("rethabile@regulator.com");
        d1.setCellPhone("087 738 3669");
        writeRegulator(d1);
    }

    private void writeRegulator(Regulator regulator) {
        chainDataAPI.addRegulator(regulator, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.d(TAG, "onResponse: ".concat(GSON.toJson(data)));
                Regulator x = (Regulator) data;

                updateText(GSON.toJson(data));
                long end = System.currentTimeMillis();
                showSnackbar("Regulator added: ".concat(x.getFullName())
                        .concat(" Elapsed minutes ".concat(String.valueOf(getElapsed(start,end)))),
                        "OK", "green");

            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });
    }

    double getElapsed(long start, long end) {
        long delta = end - start;
        Double d1 = Double.parseDouble(String.valueOf(delta)) / (1000 * 60);
        return d1.doubleValue();
    }
    List<InsuranceCompany> insuranceCompanies = new ArrayList<>();
    List<Client> clients = new ArrayList<>();
    List<Hospital> hospitals = new ArrayList<>();
    List<Doctor> doctors = new ArrayList<>();

   private void addCertViaTransaction() {
        showSnackbar("Adding Cert via Transaction", "OK", "cyan");
        Client client = clients.get(13);
        Hospital hospital = hospitals.get(0);
        Doctor doctor = doctors.get(0);

        DeathCertificate dc = new DeathCertificate();
        dc.setClass("com.oneconnect.insurenet.RegisterDeathCertificate");
        dc.setIdNumber(client.getIdNumber());
        dc.setCauseOfDeath("Natural Causes");
        dc.setDateTime(sdf.format(new Date()));
        dc.setClient("resource:".concat(client.getClassz().concat("#")
                .concat(client.getIdNumber())));
        dc.setHospital("resource:".concat(hospital.getClassz().concat("#")
                .concat(hospital.getHospitalId())));
        dc.setDoctor("resource:".concat(doctor.getClassz()).concat("#")
                .concat(doctor.getIdNumber()));

        writeDeathCertificate(dc);
    }

    private void writeDeathCertificate(DeathCertificate certificate) {
        Log.d(TAG, "about to writeDeathCertificate: %%%% ".concat(GSON.toJson(certificate)));

        chainDataAPI.registerDeathCertificate(certificate, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.i(TAG, "writeDeathCertificate onResponse: ".concat(GSON.toJson(data)));
                showSnackbar("Certificate registered via transaction call", "OK", "green");
                updateText(GSON.toJson(data));
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });
    }

    private void getInsuranceCompanies() {
        Snackbar.make(toolbar, "Getting companies", Snackbar.LENGTH_LONG).show();

        chainListAPI.getInsuranceCompanies(new ChainListAPI.CompanyListener() {
            @Override
            public void onResponse(List<InsuranceCompany> companies) {
                insuranceCompanies = companies;
                if (!insuranceCompanies.isEmpty()) {
                    company = insuranceCompanies.get(0);
                }
                updateText(GSON.toJson(companies));
                showSnackbar("Found " + companies.size() + " insurance companies", "OK", "green");
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });

    }

    private void getHospitals() {
        Snackbar.make(toolbar, "getting hospitals", Snackbar.LENGTH_LONG).show();

        chainListAPI.getHospitals(new ChainListAPI.HospitalListener() {
            @Override
            public void onResponse(List<Hospital> hospitals) {
                updateText(GSON.toJson(hospitals));
                Log.i(TAG, "getHospitals returns: ".concat(GSON.toJson(hospitals)));
                showSnackbar("Found " + hospitals.size() + " hospitals", "OK", "green");
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });

    }

    public static final String TAG = CrudActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    Snackbar snackbar;

    private void showSnackbar(String message, String action, String color) {
        snackbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void showFailureSnackbar(String message) {
        snackbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor("red"));
        snackbar.setAction("error", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_hospital:
                getHospitals();
                break;
            case R.id.nav_companies:
                getInsuranceCompanies();
                break;
            case R.id.nav_cert:
                confirmClients();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private InsuranceCompany company;

    private void showError(String message) {
        snackbar = Snackbar.make(toolbar, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor("red"));
        snackbar.setAction("Error", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
        updateText("\n\n******************** ERROR ***********\n");
        updateText(message);
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

}
