package com.payment.arthpay.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.payment.arthpay.R;
import com.payment.arthpay.activity.AepsWalletRequest;
import com.payment.arthpay.activity.MicoAtmWalletRequest;
import com.payment.arthpay.activity.PinReset;
import com.payment.arthpay.activity.UpdatePassword;
import com.payment.arthpay.activity.WalletRequest;
import com.payment.arthpay.adapter.MenuListAdapter;
import com.payment.arthpay.httpRequest.UpdateBillService;
import com.payment.arthpay.member.MemberListAll;
import com.payment.arthpay.member.MyUtil;
import com.payment.arthpay.model.ProfileMenuModel;
import com.payment.arthpay.utill.AppManager;
import com.payment.arthpay.utill.SharedPrefs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {
    TextView tvMicroAtmBalance;
    private ListView listView;
    private TextView tvBalance, tvName, tvNumber, tvAeps, tvAccount, tvBank, tvIFSC,
            tvAgentID, tvRole;
    private LinearLayout mainCon, secondCon;
    private ImageView imgSync;
    String slug;

    private void mAtmController(View view) {
        LinearLayout mainCon1 = view.findViewById(R.id.mainCon1);
        String mAtm = SharedPrefs.getValue(getActivity(), SharedPrefs.MICRO_ATM_BALANCE);
        if (mAtm != null && mAtm.equalsIgnoreCase("no")) {
            mainCon1.setVisibility(View.GONE);
        } else {
            mainCon1.setVisibility(View.VISIBLE);
            mainCon1.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MicoAtmWalletRequest.class);
                startActivity(intent);
            });
        }
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void init(View v) {
        tvBalance = v.findViewById(R.id.tvBalance);
        tvName = v.findViewById(R.id.tvName);
        tvRole = v.findViewById(R.id.tvRole);
        tvNumber = v.findViewById(R.id.tvNumber);
        tvAeps = v.findViewById(R.id.tvAeps);
        tvAccount = v.findViewById(R.id.tvAccount);
        tvBank = v.findViewById(R.id.tvBank);
        tvIFSC = v.findViewById(R.id.tvIFSC);
        mainCon = v.findViewById(R.id.mainCon);
        secondCon = v.findViewById(R.id.secondCon);
        imgSync = v.findViewById(R.id.imgSync);
        tvMicroAtmBalance = v.findViewById(R.id.tvMicroAtmBalance);
        tvAgentID = v.findViewById(R.id.tvAgentID);
        slug = SharedPrefs.getValue(getContext(), SharedPrefs.ROLE_SLUG);
        tvAgentID.setText(SharedPrefs.getValue(getActivity(),SharedPrefs.USER_ID)+" ("+slug+")");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        init(view);
        mAtmController(view);

        listView = view.findViewById(R.id.listView);
        initListView();
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(getActivity(), UpdatePassword.class));
                    break;
                case 1:
                    startActivity(new Intent(getActivity(), PinReset.class));
                    break;
                case 2:
                    slugPopup();
                    break;
            }
        });

        String name = SharedPrefs.getValue(getActivity(), SharedPrefs.USER_NAME);
        String balance = getString(R.string.rupee) + " " + SharedPrefs.getValue(getActivity(), SharedPrefs.MAIN_WALLET);
        String aeps = getString(R.string.rupee) + " " + SharedPrefs.getValue(getActivity(), SharedPrefs.APES_BALANCE);
        String contact = SharedPrefs.getValue(getActivity(), SharedPrefs.USER_CONTACT);
        String account = SharedPrefs.getValue(getActivity(), SharedPrefs.ACCOUNT);
        String bank = SharedPrefs.getValue(getActivity(), SharedPrefs.BANK);
        String ifsc = SharedPrefs.getValue(getActivity(), SharedPrefs.IFSC);
        String MaMOUNT = getString(R.string.rupee) + " " + SharedPrefs.getValue(getActivity(), SharedPrefs.MICRO_ATM_BALANCE);
        String role = SharedPrefs.getValue(getActivity(), SharedPrefs.ROLE_NAME);
        tvName.setText("Name : " + name);
        tvBalance.setText(balance);
        tvNumber.setText("Mobile Number : " + contact);
        tvAeps.setText(aeps);
        tvAccount.setText(account);
        tvBank.setText(bank);
        tvIFSC.setText(ifsc);
        tvMicroAtmBalance.setText(MaMOUNT);
        tvRole.setText(role);
        mainCon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WalletRequest.class);
            startActivity(intent);
        });

        secondCon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AepsWalletRequest.class);
            startActivity(intent);
        });


        TextView tv = view.findViewById(R.id.tvSettleMent);
        imgSync.setOnClickListener(v -> new UpdateBillService(getActivity(), tvBalance, tvAeps, tvMicroAtmBalance));
        FloatingActionButton sendBtn = view.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(v -> {
            try {
                //20 July 2019 Update
                // Change Email id and add add addition log file to share
                File logFile = new File(Environment.getExternalStorageDirectory(), getActivity().getString(R.string.app_name) + ".txt");
                //String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                //String FileName = "CCE_Log_" + timeStamp + ".txt";
                File logFile2 = new File(getFiles());
                System.out.println("logFile: " + logFile);
                if (logFile.exists() || logFile2.exists()) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    emailIntent.setType("vnd.android.cursor.dir/email");
                    String to[] = {"sumitkumarx95@gmail.com"};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    ArrayList<Uri> outputFileUri = new ArrayList<Uri>();
                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
                        if (logFile.exists()) {
                            outputFileUri.add(FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".files", logFile));
                        }
                        if (logFile2.exists()) {
                            outputFileUri.add(FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".files", logFile2));
                        }//  outputFileUri.add
                    } else {
                        if (logFile.exists()) {
                            outputFileUri.add(Uri.fromFile(logFile));
                        }
                        if (logFile2.exists()) {
                            outputFileUri.add(Uri.fromFile(logFile2));
                        }
                    }
                    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, outputFileUri);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Error log");
                    if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    } else {
                        Toast.makeText(getActivity(), "No email application is available to share error log file", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                AppManager.appendLog(e);
                e.printStackTrace();
            }
        });

        return view;
    }

    // 20 July Added
    // This code with return the newly created log file when user click on send error option.
    private String getFiles() {
        File sd = new File(Environment.getExternalStorageDirectory() + "/SECURE_PAYMENT/LOG/");
        File[] sdFileList = sd.listFiles();
        if (sdFileList != null && sdFileList.length > 1) {
            Arrays.sort(sdFileList, (object1, object2) -> (int) (Math.min(object2.lastModified(), object1.lastModified())));
            return sdFileList[sdFileList.length - 1].getAbsolutePath();
        } else if (sdFileList != null && sdFileList.length == 1) {
            return sdFileList[sdFileList.length - 1].getAbsolutePath();
        } else {
            return "";
        }
    }

    private void initListView() {
        List<ProfileMenuModel> dataList = new ArrayList<>();
        //dataList.add(new ProfileMenuModel(R.drawable.profile_icon, "Edit Profile"));
        dataList.add(new ProfileMenuModel(R.drawable.pass_ic, "Change Password"));
        dataList.add(new ProfileMenuModel(R.drawable.pass_ic, "Change TPin"));
        if(!slug.equalsIgnoreCase("retailer")){
            dataList.add(new ProfileMenuModel(R.drawable.pass_ic, "Manage Member"));
        }
        MenuListAdapter adapter = new MenuListAdapter(getActivity(), dataList);
        listView.setAdapter(adapter);
    }


    private void slugPopup() {
        if (MyUtil.isNN(slug) && (slug.equalsIgnoreCase("md") || slug.equalsIgnoreCase("distributor"))) {
            if (slug.equalsIgnoreCase("distributor")) {
                Intent i = new Intent(getContext(), MemberListAll.class);
                i.putExtra("type", "retailer");
                startActivity(i);
            } else {
                final CharSequence[] choice = {"distributor", "retailer"};
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Please select role");
                alert.setSingleChoiceItems(choice, -1, (dialog, which) -> {
                    String txt = choice[which].toString();
                    dialog.dismiss();
                    Intent i = new Intent(getContext(), MemberListAll.class);
                    i.putExtra("type", txt);
                    startActivity(i);
                });
                //alert.setCancelable(false);
                alert.show();
            }
        } else {
            Toast.makeText(getContext(), "You don't have permission for this option", Toast.LENGTH_SHORT).show();
        }
    }
}