package com.payment.arthpay.on_boarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.payment.arthpay.R;
import com.payment.arthpay.databinding.ActivityOnBoardingBinding;
import com.payment.arthpay.on_boarding.select_state_district.SearchWithListActivity;
import com.payment.arthpay.utill.ExtensionFunction;
import com.payment.arthpay.utill.SharedPrefs;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OnBoardingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ActivityOnBoardingBinding binding;
    OnBoardingModel model;
    EditText fNameEt, mNameEt, lNameEt, emailEt, phoneOneEt, phoneTwoEt, addressEt, blockEt, cityEt, landmarkEt,
            mohallaEt, locEt, pincodeEt, panEt, shopNameEt;
    TextView stateEt, districtEt, dobEt;
    String firstName, middleName, lastName, email, phoneOne, phoneTwo, dob, state, district, address, block, city, landmark, mohalla, locType, loc, pincode, pan, shopName, shopType, qualification, population;
    String userId, stateId, stateName, districtId;
    Calendar myCalendar;
    private SimpleDateFormat simpleDateFormat;

    private void init() {
        model = new OnBoardingModel();
        fNameEt = binding.fNameEt;
        mNameEt = binding.mNameEt;
        lNameEt = binding.lNameEt;
        emailEt = binding.emailEt;
        phoneOneEt = binding.phoneOneEt;
        phoneTwoEt = binding.phoneTwoEt;
        dobEt = binding.dobEt;

        addressEt = binding.addressEt;
        blockEt = binding.blockEt;
        cityEt = binding.cityEt;
        stateEt = binding.stateEt;
        districtEt = binding.districtEt;
        landmarkEt = binding.landmarkEt;
        mohallaEt = binding.mohallaEt;

        locEt = binding.locEt;
        pincodeEt = binding.pincodeEt;
        panEt = binding.panEt;
        shopNameEt = binding.shopNameEt;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        binding.nextBtn.setOnClickListener(v -> {
            if (validate()) {
                setForm();
                startActivity(new Intent(this, UploadDocumentActivity.class).putExtra("data", model));
            }
        });

        binding.locTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.shopTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shopType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.qualificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qualification = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.populationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                population = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stateEt.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, SearchWithListActivity.class);
            bundle.putString("type", "state");
            intent.putExtras(bundle);
            startActivityForResult(intent, 100);
        });

        districtEt.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, SearchWithListActivity.class);
            bundle.putString("type", "district");
            bundle.putString("stateId", stateId);
            intent.putExtras(bundle);
            startActivityForResult(intent, 101);
        });

        dobEt.setOnClickListener(v -> {
//            ExtensionFunction.datePicker(this, dobEt);

            new SpinnerDatePickerDialogBuilder()
                    .context(OnBoardingActivity.this)
                    .callback(OnBoardingActivity.this)
                    .spinnerTheme(R.style.NumberPickerStyle)
                    .showTitle(true)

                    .showDaySpinner(true)
                    .defaultDate(2017, 0, 1)
                    .maxDate(2021, 0, 1)
                    .minDate(1800, 0, 1)
                    .build()
                    .show();
        });
    }

    private void setForm() {
        model.setUserId(SharedPrefs.getValue(this, SharedPrefs.USER_ID));
        model.setFirstName(firstName);
        model.setMiddleName(middleName);
        model.setLastName(lastName);
        model.setEmail(email);
        model.setPhoneOne(phoneOne);
        model.setPhoneTwo(phoneTwo);
        model.setDob(dob);
        model.setState(stateId);
        model.setDistrict(districtId);
        model.setAddress(address);
        model.setBlock(block);
        model.setCity(city);
        model.setLandmark(landmark);
        model.setMohalla(mohalla);
        model.setLocType(locType);
        model.setLoc(loc);
        model.setPincode(pincode);
        model.setPan(pan);
        model.setShopName(shopName);
        model.setShopType(shopType);
        model.setQualification(qualification);
        model.setPopulation(population);

    }

    private Boolean validate() {
        firstName = fNameEt.getText().toString().trim();
        middleName = mNameEt.getText().toString().trim();
        lastName = lNameEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        phoneOne = phoneOneEt.getText().toString().trim();
        phoneTwo = phoneTwoEt.getText().toString().trim();
        dob = dobEt.getText().toString().trim();

        address = addressEt.getText().toString().trim();
        block = blockEt.getText().toString().trim();
        city = cityEt.getText().toString().trim();
        state = stateEt.getText().toString().trim();
        district = districtEt.getText().toString().trim();
        landmark = landmarkEt.getText().toString().trim();
        mohalla = mohallaEt.getText().toString().trim();

        loc = locEt.getText().toString().trim();
        pincode = pincodeEt.getText().toString().trim();
        pan = panEt.getText().toString().trim();
        shopName = shopNameEt.getText().toString().trim();


        if (firstName.isEmpty()) {
            ExtensionFunction.checkValidation(fNameEt, "First Name Required");
            return false;
        } else if (email.isEmpty() || (!email.contains("@") && !email.contains("."))) {
            ExtensionFunction.checkValidation(emailEt, "Email Required");
            return false;
        } else if (phoneOne.isEmpty()) {
            ExtensionFunction.checkValidation(phoneOneEt, "Phone Number Required");
            return false;
        } else if (phoneTwo.isEmpty()) {
            ExtensionFunction.checkValidation(phoneTwoEt, "Phone Number Required");
            return false;
        } else if (dob.isEmpty()) {
//            ExtensionFunction.checkValidation(dobEt, "Date Of Birth Required");
            ExtensionFunction.showToast(this, "Date Of Birth Required");
            return false;
        } else if (state.isEmpty()) {
            ExtensionFunction.showToast(this, "Select State");
            return false;
        } else if (district.isEmpty()) {
            ExtensionFunction.showToast(this, "Select District");
            return false;
        } else if (address.isEmpty()) {
            ExtensionFunction.checkValidation(addressEt, "Address Required");
            return false;
        } else if (block.isEmpty()) {
            ExtensionFunction.checkValidation(blockEt, "Block Required");
            return false;
        } else if (city.isEmpty()) {
            ExtensionFunction.checkValidation(cityEt, "City Required");
            return false;
        } else if (landmark.isEmpty()) {
            ExtensionFunction.checkValidation(landmarkEt, "Landmark Required");
            return false;
        } else if (mohalla.isEmpty()) {
            ExtensionFunction.checkValidation(mohallaEt, "Mohalla Required");
            return false;
        } else if (locType.isEmpty() || locType.equalsIgnoreCase("Location Type")) {
            ExtensionFunction.showToast(this, "Select Location Type");
            return false;
        } else if (loc.isEmpty()) {
            ExtensionFunction.checkValidation(locEt, "Location Required");
            return false;
        } else if (pincode.isEmpty()) {
            ExtensionFunction.checkValidation(pincodeEt, "Pincode Required");
            return false;
        } else if (pan.isEmpty()) {
            ExtensionFunction.checkValidation(panEt, "Pancard Required");
            return false;
        } else if (shopName.isEmpty()) {
            ExtensionFunction.checkValidation(shopNameEt, "Shop Name Required");
            return false;
        } else if (shopType.isEmpty() || shopType.equalsIgnoreCase("Shop Type")) {
            ExtensionFunction.showToast(this, "Select Shop Type");
            return false;
        } else if (qualification.isEmpty() || qualification.equalsIgnoreCase("Qualification Type")) {
            ExtensionFunction.showToast(this, "Select Qualification Type");
            return false;
        } else if (population.isEmpty() || population.equalsIgnoreCase("Select Population")) {
            ExtensionFunction.showToast(this, "Select Population");
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 100) {
                stateName = data.getStringExtra("stateName");
                stateId = data.getStringExtra("stateId");
                stateEt.setText(stateName);
                ExtensionFunction.showToast(this, stateId);
            } else if (requestCode == 101) {
                String districtName = data.getStringExtra("districtname");
                districtId = data.getStringExtra("districtId");
                districtEt.setText(districtName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.e("dte", "" + year + "   " + monthOfYear + "   " + dayOfMonth);
    }*/

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        myCalendar = Calendar.getInstance();
        myCalendar.set(year, monthOfYear, dayOfMonth);
        dobEt.setText(simpleDateFormat.format(myCalendar.getTime()));
    }
}