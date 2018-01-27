package com.aftarobot.blockchaintest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aftarobot.mlibrary.util.ListUtil;
import com.aftarobot.mlibrary.api.ChainDataAPI;
import com.aftarobot.mlibrary.api.ChainListAPI;
import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Doctor;
import com.aftarobot.mlibrary.data.FuneralParlour;
import com.aftarobot.mlibrary.data.Hospital;
import com.aftarobot.mlibrary.data.InsuranceCompany;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.Regulator;
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
        getSupportActionBar().setTitle("Blockchain CRUD App");
        getSupportActionBar().setSubtitle("OneConnect Business Network");
        chainDataAPI = new ChainDataAPI(this);
        chainListAPI = new ChainListAPI(this);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addDoctors();
                doCrud();
                //addClients();
                //startPolicyLookups();
               // startDCLookups();
            }
        });
        getInsuranceCompanies();
    }

    private void doCrud() {
        Snackbar.make(toolbar, "Starting CRUD for demo data", Snackbar.LENGTH_LONG).show();
        addCompanies();
        addHospitals();
        addParlours();
        addClients();
        addBeneficiaries();
        addDoctors();
        addRegulator();
        //getInsuranceCompanies();
    }

    private void addClients() {
        Snackbar.make(toolbar, "Adding client demo data", Snackbar.LENGTH_LONG).show();
//        Client c1 = new Client();
//        c1.setIdNumber("CLIENT_001");
//        c1.setFirstName("John");
//        c1.setLastName("Mathebula");
//        c1.setEmail("john.m@gmail.com");
//        c1.setAccountBalance(0.00);
//        c1.setAddress("34 Kruger Road, Springs");
//
//        writeClient(c1);
//        Client c2 = new Client();
//        c2.setIdNumber("CLIENT_002");
//        c2.setFirstName("Sarah");
//        c2.setLastName("Sithole");
//        c2.setEmail("sarah.sit.m@gmail.com");
//        c2.setAddress("364 Kruger Road, Springs");
//        c2.setAccountBalance(0.00);
//        writeClient(c2);
//
//        Client c3 = new Client();
//        c3.setIdNumber("CLIENT_003");
//        c3.setFirstName("Kevin");
//        c3.setLastName("Browne");
//        c3.setEmail("kevinb.m@gmail.com");
//        c3.setAddress("34 Maude Road, Sandton");
//        c3.setAccountBalance(0.00);
//        writeClient(c3);
//
//        Client c4 = new Client();
//        c4.setIdNumber("CLIENT_004");
//        c4.setFirstName("Samuel L");
//        c4.setLastName("Jackson");
//        c4.setEmail("samljack.m@gmail.com");
//        c4.setAddress("34 Coronation Road, Johannesburg");
//        c4.setAccountBalance(0.00);
//        writeClient(c4);
//
//        Client c5 = new Client();
//        c5.setIdNumber("CLIENT_005");
//        c5.setFirstName("John");
//        c5.setLastName("Billman");
//        c5.setEmail("johnbill.m@gmail.com");
//        c5.setAddress("887 Paris Boulevard, Durban");
//        c5.setAccountBalance(0.00);
//        writeClient(c5);
        controlClients();
    }

    private void updateText(String data) {
        sb.append(data);
        txt.setText(sb.toString());
    }

    private void writeClient(Client client) {
        chainDataAPI.addClient(client, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.d(TAG, "onResponse: ".concat(GSON.toJson(data)));
                showSnackbar("Client added", "OK", "green");
                updateText(GSON.toJson(data));
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });
    }

    private void addBeneficiaries() {
        Snackbar.make(toolbar, "Adding beneficiary demo data", Snackbar.LENGTH_LONG).show();

        Beneficiary c1 = new Beneficiary();
        c1.setIdNumber("BENEFICIARY_001");
        c1.setFirstName("Suzan Anne");
        c1.setLastName("Mathebula");
        c1.setEmail("sarah.m@gmail.com");
        c1.setAddress("34 Kruger Road, Springs");
        c1.setAccountBalance(0.00);
        writeBeneficiary(c1);

        Beneficiary c2 = new Beneficiary();
        c2.setIdNumber("BENEFICIARY_002");
        c2.setFirstName("Benjamin");
        c2.setLastName("Sithole");
        c2.setEmail("ben.sit.m@gmail.com");
        c2.setAddress("364 Kruger Road, Springs");
        c2.setAccountBalance(0.00);

        writeBeneficiary(c2);

        Beneficiary c3 = new Beneficiary();
        c3.setIdNumber("BENEFICIARY_003");
        c3.setFirstName("Sammy");
        c3.setLastName("Browne");
        c3.setEmail("sammyb.m@gmail.com");
        c3.setAddress("34 Maude Road, Sandton");
        c3.setAccountBalance(0.00);

        writeBeneficiary(c3);

        Beneficiary c4 = new Beneficiary();
        c4.setIdNumber("BENEFICIARY_004");
        c4.setFirstName("Joe");
        c4.setLastName("Jackson");
        c4.setEmail("joljack.m@gmail.com");
        c4.setAddress("34 Coronation Road, Johannesburg");
        c4.setAccountBalance(0.00);

        writeBeneficiary(c4);

        Beneficiary c5 = new Beneficiary();
        c5.setIdNumber("BENEFICIARY_005");
        c5.setFirstName("Daniella");
        c5.setLastName("Billman");
        c5.setEmail("danbill.m@gmail.com");
        c5.setAddress("887 Paris Boulevard, Durban");
        c5.setAccountBalance(0.00);

        writeBeneficiary(c5);
    }

    private void writeBeneficiary(Beneficiary beneficiary) {
        chainDataAPI.addBeneficiary(beneficiary, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.d(TAG, "onResponse: ".concat(GSON.toJson(data)));
                showSnackbar("Beneficiary added", "OK", "green");
                updateText(GSON.toJson(data));
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });
    }

    private void addDoctors() {
        Snackbar.make(toolbar, "Adding doctor  demo data", Snackbar.LENGTH_LONG).show();

        Doctor d1 = new Doctor();
        d1.setIdNumber("DOCTOR_001");
        d1.setFirstName("Rirhandzu");
        d1.setLastName("Maluleke");
        d1.setEmail("riri@gmail.com");
        d1.setCellPhone("087 344 3690");
        writeDoctor(d1);

        Doctor d2 = new Doctor();
        d2.setIdNumber("DOCTOR_002");
        d2.setFirstName("Mmathebe");
        d2.setLastName("Jika");
        d2.setEmail("mathebe@gmail.com");
        d2.setCellPhone("087 727 9546");
        writeDoctor(d2);

        Doctor d3 = new Doctor();
        d3.setIdNumber("DOCTOR_003");
        d3.setFirstName("Fanyana");
        d3.setLastName("Shiburi");
        d3.setEmail("selwyn@gmail.com");
        d3.setCellPhone("087 987 1212");
        writeDoctor(d3);

        Doctor d4 = new Doctor();
        d4.setIdNumber("DOCTOR_004");
        d4.setFirstName("MaryAnne");
        d4.setLastName("Pieterse");
        d4.setEmail("maryanne@gmail.com");
        d4.setCellPhone("087 727 7355");
        writeDoctor(d4);
    }

    private void writeDoctor(Doctor doc) {
        chainDataAPI.addDoctor(doc, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.d(TAG, "onResponse: ".concat(GSON.toJson(data)));
                showSnackbar("Doctor added", "OK", "green");
                updateText(GSON.toJson(data));
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });
    }

    private void addRegulator() {
        Snackbar.make(toolbar, "Adding regulator demo data", Snackbar.LENGTH_LONG).show();

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
                showSnackbar("Regulator added", "OK", "green");
                updateText(GSON.toJson(data));
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });
    }

    private void addParlours() {
        Snackbar.make(toolbar, "Adding funeral parlour demo data", Snackbar.LENGTH_LONG).show();

        FuneralParlour fp1 = new FuneralParlour();
        fp1.setFuneralParlourId("FUNERAL_PARLOUR_001");
        fp1.setName("Soweto Funeral Services");
        fp1.setEmail("info@sowetofs.com");
        fp1.setAddress("635 Khunou Street, Moroka, Soweto");
        fp1.setLatitude(-25.363868);
        fp1.setLongitude(27.367578);
        writeParlour(fp1);
        FuneralParlour fp2 = new FuneralParlour();
        fp2.setFuneralParlourId("FUNERAL_PARLOUR_002");
        fp2.setName("Diepkloof Funeral Home");
        fp2.setEmail("info@sowetofs.com");
        fp2.setAddress("1243 Mazomba Street, Diepkloof Zone 3, Soweto");
        fp2.setLatitude(-25.870656);
        fp2.setLongitude(27.367578);
        writeParlour(fp2);
    }

    private void writeParlour(FuneralParlour parlour) {
        Snackbar.make(toolbar, "Registering funeral parlour ...", Snackbar.LENGTH_LONG).show();
        chainDataAPI.addFuneralParlour(parlour, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.d(TAG, "onResponse: ".concat(GSON.toJson(data)));
                showSnackbar("Funeral Parlour added", "OK", "green");
                updateText(GSON.toJson(data));
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });
    }

    private void addHospitals() {
        Snackbar.make(toolbar, "Adding hospital demo data", Snackbar.LENGTH_LONG).show();

        Hospital h1 = new Hospital();
        h1.setHospitalId("HOSPITAL_001");
        h1.setName("Durban Hospital");
        h1.setAddress("8 Mandela Road, Durban");
        h1.setEmail("info@durbanhosp.co.za");
        h1.setLatitude(-25.8373874);
        h1.setLongitude(27.3536478);
        writeHospital(h1);
        Hospital h2 = new Hospital();
        h2.setHospitalId("HOSPITAL_002");
        h2.setName("Dobsonville Hospital");
        h2.setAddress("45 Moloko Road, Dobsonville");
        h2.setEmail("info@dobson.co.za");
        h2.setLatitude(-25.8373874);
        h2.setLongitude(27.3536478);
        writeHospital(h2);
        Hospital h3 = new Hospital();
        h3.setHospitalId("HOSPITAL_003");
        h3.setName("Bryanston Life Hospital");
        h3.setAddress("677 Kingfisher Road, Bryanston");
        h3.setEmail("info@lifebryans.co.za");
        h3.setLatitude(-25.8373874);
        h3.setLongitude(27.3536478);
        writeHospital(h3);
    }

    private void writeHospital(Hospital hospital) {
        Snackbar.make(toolbar, "Registering hospital ...", Snackbar.LENGTH_LONG).show();
        chainDataAPI.addHospital(hospital, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.d(TAG, "onResponse: ".concat(GSON.toJson(data)));
                showSnackbar("Hospital added ok", "OK", "green");
                updateText(GSON.toJson(data));
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });
    }

    private void addCompanies() {
        Snackbar.make(toolbar, "Adding company demo data", Snackbar.LENGTH_LONG).show();

        InsuranceCompany co = new InsuranceCompany();
        co.setInsuranceCompanyID("COMPANY_001");
        co.setName("Regent Insurance LLC");
        co.setEmail("info@reginsurance.com");
        co.setAddress("234 Maude Street, Sandton");
        writeCompany(co);

        InsuranceCompany co1 = new InsuranceCompany();
        co1.setInsuranceCompanyID("COMPANY_002");
        co1.setName("Phila Insurance");
        co1.setEmail("info@phila.com");
        co1.setAddress("26 Remington Street, Cape Town");
        writeCompany(co1);

        InsuranceCompany co3 = new InsuranceCompany();
        co3.setInsuranceCompanyID("COMPANY_003");
        co3.setName("Cape Insurance");
        co3.setEmail("info@kzninsurance.com");
        co3.setAddress("12 Beach Street, Cape Town");
        writeCompany(co3);
    }

    private void writeCompany(InsuranceCompany co) {
        Snackbar.make(toolbar, "Adding company ...", Snackbar.LENGTH_LONG).show();
        chainDataAPI.addInsuranceCompany(co, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                showSnackbar("Company added", "OK", "green");
                updateText(GSON.toJson(data));
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError: ".concat(message));
                showFailureSnackbar(message);
            }
        });
    }

    List<InsuranceCompany> insuranceCompanies;
    List<Client> clients = new ArrayList<>();
    List<Beneficiary> beneficiaries;
    List<Hospital> hospitals;
    List<Doctor> doctors;

    private void startPolicyLookups() {
        chainListAPI.getClients(new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(final List<Client> list) {
                clients = list;
                Log.w(TAG, "onResponse: clients found: " + list.size() );
                chainListAPI.getBeneficiaries(new ChainListAPI.BeneficiaryListener() {
                    @Override
                    public void onResponse(List<Beneficiary> list) {
                        beneficiaries = list;
                        Log.w(TAG, "onResponse: beneficiaries found: " + list.size() );
                        chainListAPI.getInsuranceCompanies(new ChainListAPI.CompanyListener() {
                            @Override
                            public void onResponse(List<InsuranceCompany> companies) {
                                insuranceCompanies = companies;
                                Log.w(TAG, "onResponse: companies found: " + companies.size() );
                                arrangePolicies();
                            }

                            @Override
                            public void onError(String message) {
                                showFailureSnackbar(message);
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        showFailureSnackbar(message);

                    }
                });
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);

            }
        });
    }

    private void arrangePolicies() {
        Log.d(TAG, "arrangePolicies: ***********************");
        InsuranceCompany company = null;
        if (!insuranceCompanies.isEmpty()) {
            company = insuranceCompanies.get(0);
        } else {
            showFailureSnackbar("No insurance companies found");
            return;
        }
        if (company != null) {
            Policy p1 = new Policy();
            p1.setInsuranceCompany("resource:".concat(company.getClassz().concat("#")
            .concat(company.getInsuranceCompanyID())));
            p1.setPolicyNumber("POLICY_001");
            p1.setClient("resource:".concat(clients.get(0).getClassz()).concat("#")
            .concat(clients.get(0).getIdNumber()));
            p1.setBeneficiaries(new ArrayList<String>());
            p1.getBeneficiaries().add(
                    "resource:".concat(beneficiaries.get(0).getClassz().concat("#")
                    .concat(beneficiaries.get(0).getIdNumber())));
            p1.setDescription("Death Benefit Policy");
            p1.setAmount(7800000);
            writePolicy(p1);

            Policy p2 = new Policy();
            p2.setPolicyNumber("POLICY_002");
            p2.setInsuranceCompany("resource:".concat(company.getClassz().concat("#")
                    .concat(company.getInsuranceCompanyID())));
            p2.setClient("resource:".concat(clients.get(1).getClassz()).concat("#")
                    .concat(clients.get(1).getIdNumber()));
            p2.setBeneficiaries(new ArrayList<String>());
            p2.getBeneficiaries().add(
                    "resource:".concat(beneficiaries.get(1).getClassz().concat("#")
                            .concat(beneficiaries.get(1).getIdNumber())));
            p2.setDescription("Death Benefit Policy");
            p2.setAmount(10000000);
            writePolicy(p2);

            Policy p3 = new Policy();
            p3.setPolicyNumber("POLICY_003");
            p3.setInsuranceCompany("resource:".concat(company.getClassz().concat("#")
                    .concat(company.getInsuranceCompanyID())));
            p3.setClient("resource:".concat(clients.get(2).getClassz()).concat("#")
                    .concat(clients.get(2).getIdNumber()));
            p3.setBeneficiaries(new ArrayList<String>());
            p3.getBeneficiaries().add(
                    "resource:".concat(beneficiaries.get(2).getClassz().concat("#")
                            .concat(beneficiaries.get(2).getIdNumber())));
            p3.setDescription("Death Benefit Policy");
            p3.setAmount(9750000);
            writePolicy(p3);

            Policy p4 = new Policy();
            p4.setPolicyNumber("POLICY_004");
            p4.setInsuranceCompany("resource:".concat(company.getClassz().concat("#")
                    .concat(company.getInsuranceCompanyID())));
            p4.setClient("resource:".concat(clients.get(3).getClassz()).concat("#")
                    .concat(clients.get(3).getIdNumber()));
            p4.setBeneficiaries(new ArrayList<String>());
            p4.getBeneficiaries().add(
                    "resource:".concat(beneficiaries.get(3).getClassz().concat("#")
                            .concat(beneficiaries.get(3).getIdNumber())));
            p4.setDescription("Death Benefit Policy");
            p4.setAmount(7800000);
            writePolicy(p4);

            Policy p5 = new Policy();
            p5.setPolicyNumber("POLICY_005");
            p5.setInsuranceCompany("resource:".concat(company.getClassz().concat("#")
                    .concat(company.getInsuranceCompanyID())));
            p5.setClient("resource:".concat(clients.get(4).getClassz()).concat("#")
                    .concat(clients.get(4).getIdNumber()));
            p5.setBeneficiaries(new ArrayList<String>());
            p5.getBeneficiaries().add(
                    "resource:".concat(beneficiaries.get(4).getClassz().concat("#")
                            .concat(beneficiaries.get(4).getIdNumber())));
            p5.setDescription("Death Benefit Policy");
            p5.setAmount(12500000);
            writePolicy(p5);
        }
    }

    void writePolicy(Policy policy) {
        Log.d(TAG, "writePolicy: %%%%%%%%%%%%%%%%%%%%%%%% ".concat(policy.getPolicyNumber()));
        chainDataAPI.addPolicy(policy, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Log.i(TAG, "writePolicy onResponse: ".concat(GSON.toJson(data)));
                showSnackbar("Policy added added", "OK", "green");
                updateText(GSON.toJson(data));
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);
            }
        });
    }
    private void startDCLookups() {
        chainListAPI.getClients(new ChainListAPI.ClientListener() {
            @Override
            public void onResponse(final List<Client> list) {
                clients = list;
                Log.w(TAG, "onResponse: clients found: " + list.size() );
                chainListAPI.getHospitals(new ChainListAPI.HospitalListener() {
                    @Override
                    public void onResponse(List<Hospital> list) {
                        hospitals = list;
                        Log.w(TAG, "onResponse: hospitals found: " + list.size() );
                        chainListAPI.getDoctors(new ChainListAPI.DoctorListener() {
                            @Override
                            public void onResponse(List<Doctor> list) {
                                doctors = list;
                                Log.w(TAG, "onResponse: doctors found: " + list.size() );
                                addCertViaTransaction();
                            }

                            @Override
                            public void onError(String message) {
                                showFailureSnackbar(message);
                            }
                        });
                    }

                    @Override
                    public void onError(String message) {
                        showFailureSnackbar(message);

                    }
                });
            }

            @Override
            public void onError(String message) {
                showFailureSnackbar(message);

            }
        });
    }
    void addCertViaTransaction() {
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
    void writeDeathCertificate(DeathCertificate certificate) {
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
                startDCLookups();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int count;
    public static final int MAX_BENNIES = 120, MAX_CLIENTS = 60;
    private void controlBennies() {
        if (count < MAX_BENNIES) {
            addRandomBenny();
        } else {
            showSnackbar("Clients and Beneficiaries generated", "OK", "green");
        }

    }

    private void addRandomBenny() {

        Beneficiary beneficiary = ListUtil.getRandomBeneficiary();
        chainDataAPI.addBeneficiary(beneficiary, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Beneficiary v = (Beneficiary) data;
                count++;
                Log.i(TAG, "onResponse: bennie added to blockchain".concat(GSON.toJson(v)));
                Log.e(TAG, "onResponse: total beneficiaries:".concat(String.valueOf(count)) );

                controlBennies();
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });

    }
    private void controlClients() {
        if (count < MAX_CLIENTS) {
            addRandomClient();
        } else {
            count = 0;
            controlBennies();
        }

    }


    private void addRandomClient() {
        Client client = ListUtil.getRandomClient();
        chainDataAPI.addClient(client, new ChainDataAPI.Listener() {
            @Override
            public void onResponse(Data data) {
                Client randomClient = (Client) data;
                Log.w(TAG, "onResponse: random client added: "
                        .concat(GSON.toJson(randomClient)));
                clients.add(0, randomClient);
                Log.e(TAG, "onResponse: total clients:".concat(String.valueOf(clients.size())) );
                count++;
                controlClients();

            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "onError: ".concat(message));
                showError(message);
            }
        });
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
    }

    private List<String> firstNames = new ArrayList<>();
    private List<String> lastNames = new ArrayList<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

}
