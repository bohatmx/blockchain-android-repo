package com.aftarobot.blockchaintest;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.aftarobot.blockchaintest.crudutils.BankUtil;
import com.aftarobot.blockchaintest.crudutils.CompaniesUtil;
import com.aftarobot.blockchaintest.crudutils.DoctorUtil;
import com.aftarobot.blockchaintest.crudutils.HospitalUtil;
import com.aftarobot.blockchaintest.crudutils.ParlourUtil;
import com.aftarobot.blockchaintest.crudutils.PolicyUtil;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.api.FBApi;
import com.aftarobot.mlibrary.data.Bank;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Regulator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CrudActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txt, txtNumber;
    ChainDataAPI chainDataAPI;
    ChainListAPI chainListAPI;
    StringBuilder sb = new StringBuilder();
    SeekBar seekBar;
    int numberOfClients = 3;
    public static final int MINIMUM_CLIENTS = 4;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blockchain Driver");
        getSupportActionBar().setSubtitle("OCT Business Network");

        chainDataAPI = new ChainDataAPI(this);
        chainListAPI = new ChainListAPI(this);

        getInsuranceCompanies();

    }

    FloatingActionButton fab;

    private void setup() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
        txt = findViewById(R.id.text);
        txtNumber = findViewById(R.id.txtNumberClients);
        toolbar = findViewById(R.id.toolbar);
        seekBar = findViewById(R.id.seekBar);
        spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    seekBar.setProgress(MINIMUM_CLIENTS);
                    txtNumber.setText(String.valueOf(MINIMUM_CLIENTS));
                    numberOfClients = MINIMUM_CLIENTS;
                } else {
                    txtNumber.setText(String.valueOf(progress));
                    numberOfClients = progress;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(MINIMUM_CLIENTS);
    }

    private void confirm() {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Confirm")
                .setMessage("Do you really, really want to do this?\n\nThis generates all the base demo data needed on the blockchain. It will fuck up if this data has been generated before.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (seekBar.getProgress() == MINIMUM_CLIENTS) {
                            seekBar.setProgress(75);
                        }
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

    private boolean busy;

    private void confirmClients() {
        AlertDialog.Builder x = new AlertDialog.Builder(this);
        x.setTitle("Confirm")
                .setMessage("Do you really, really want to do this?\n\nThis generates  about " + numberOfClients
                        + " clients and beneficiaries demo data needed on the blockchain.\n\n" +
                        "It will take a few minutes to complete .....")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<InsuranceCompany> list = new ArrayList<>();
                        if (insuranceCompanies.isEmpty()) {
                            showError("Companies do not exist yet");
                            return;
                        } else {
                            if (company != null) {
                                list.add(company);
                            } else {
                                list = insuranceCompanies;
                            }
                        }
                        spinner.setEnabled(false);
                        busy = true;
                        start = System.currentTimeMillis();
                        if (banks.isEmpty()) {
                            showError("There are no banks, cannot go on");
                            return;
                        }
                        PolicyUtil.generateClientsAndPolicies(getApplicationContext(), numberOfClients, banks.get(0), list, new PolicyUtil.PolicyUtilListener() {
                            @Override
                            public void clientsComplete() {
                                long end = System.currentTimeMillis();
                                showSnackbar("Clients done."
                                                .concat(" Elapsed: ".concat(df.format(getElapsed(start, end)).concat(" minutes"))),
                                        "OK", "green");
                                company = null;
                                busy = false;
                                if (insuranceCompanies.size() > 0) {
                                    spinner.setSelection(0, true);
                                    spinner.setEnabled(true);
                                }
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
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    long start;
    public static final DecimalFormat df = new DecimalFormat("###,##0.00");

    private void doCrud() {
        showSnackbar("Starting CRUD for demo data", "ok", "yellow");
        busy = true;
        fab.setEnabled(false);
        fab.setAlpha(0.3f);
        final FBApi api = new FBApi();
        start = System.currentTimeMillis();
        api.addAuthRemoval(new FBApi.FBListener() {
            @Override
            public void onResponse(Data data) {
                Log.e(TAG, "onResponse: +++++++++++++++++++ auth removal trigger added, sleeping for 10 seconds");
                SystemClock.sleep(10000);
                startGrinding();
//                api.removeBeneficiaries(new FBApi.FBListener() {
//                    @Override
//                    public void onResponse(Data data) {
//                        Log.e(TAG, "doCrud onResponse: beneficiaries deleted from Firebase");
//                        api.removeClients(new FBApi.FBListener() {
//                            @Override
//                            public void onResponse(Data data) {
//                                Log.e(TAG, "doCrud onResponse: clients deleted from Firebase");
//                                try {
//                                    showSnackbar("Waiting for 5 seconds ...", "ok", "white");
//                                    SystemClock.sleep()(5000);
//                                    startGrinding();
//
//                                } catch (InterruptedException e) {
//                                    startGrinding();
//                                }
//                            }
//
//                            @Override
//                            public void onError(String message) {
//                                showError(message);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(String message) {
//                        showError(message);
//                    }
//                });

            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });


    }
    private void startGrinding() {
        Log.w(TAG, "startGrinding: $$$$$$$$$$$$$$$$$$$$$$$$$$ ......." );
        CompaniesUtil.generate(getApplicationContext(), new CompaniesUtil.InsuranceCompanyListener() {
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
                                        BankUtil.generate(getApplicationContext(), new BankUtil.BankListener() {
                                            @Override
                                            public void onBanksComplete(List<Bank> banks) {
                                                Bank bank = banks.get(0);
                                                PolicyUtil.generateClientsAndPolicies(getApplicationContext(),
                                                        numberOfClients, bank, insuranceCompanies, new PolicyUtil.PolicyUtilListener() {
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
                                            public void onProgress(Bank doc) {
                                                showSnackbar("Bank added: ".concat(doc.getName()), "ok", "yellow");
                                            }

                                            @Override
                                            public void onError(String message) {
                                                showError(message);
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
                                .concat(" Elapsed: ".concat(df.format(getElapsed(start, end)).concat(" minutes"))),
                        "OK", "green");
                busy = false;
                fab.setEnabled(false);
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
                busy = false;
                fab.setEnabled(false);
                getInsuranceCompanies();
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
    InsuranceCompany company;

    private void setSpinner() {
        List<String> list = new ArrayList<>();
        for (InsuranceCompany c : insuranceCompanies) {
            list.add(c.getName());
        }
        list.add(0, "Select Insurance Company");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    company = null;
                } else {
                    company = insuranceCompanies.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private List<Bank> banks = new ArrayList<>();

    private void getInsuranceCompanies() {
        Snackbar.make(toolbar, "Getting companies", Snackbar.LENGTH_LONG).show();

        chainListAPI.getInsuranceCompanies(new ChainListAPI.CompanyListener() {
            @Override
            public void onResponse(List<InsuranceCompany> companies) {
                insuranceCompanies = companies;
                if (!insuranceCompanies.isEmpty()) {
                    spinner.setVisibility(View.VISIBLE);
                    setSpinner();
                    updateText(GSON.toJson(companies));
                    fab.setEnabled(false);
                    fab.setAlpha(0.3f);
                }
                chainListAPI.getBanks(new ChainListAPI.BankListener() {
                    @Override
                    public void onResponse(List<Bank> list) {
                        banks = list;
                        Log.w(TAG, "onResponse: banks found:".concat(String.valueOf(banks.size())));
                    }

                    @Override
                    public void onError(String message) {
                        showError(message);
                    }
                });
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
        if (busy) {
            return super.onOptionsItemSelected(item);
        }
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
