package com.mleszynski.fmc;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import java.net.MalformedURLException;
import java.net.URL;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import request.*;
import result.*;

public class LoginFragment extends Fragment {
    private static Context sContext;
    private String serverHost;
    private String serverPort;
    private RegisterRequest mRegisterReq;
    private LoginRequest mLoginReq;
    private EditText mHostEText;
    private EditText mPortEText;
    private EditText mUsernameEText;
    private EditText mPasswordEText;
    private EditText mFirstNameEText;
    private EditText mLastNameEText;
    private EditText mEmailEText;
    private RadioGroup mGenderButton;
    private RadioButton mMButton;
    private RadioButton mFButton;
    private Button mRegisterButton;
    private Button mLoginButton;

    public LoginFragment() {

    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRegisterReq = new RegisterRequest();
        this.mLoginReq = new LoginRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        this.mRegisterButton = (Button)view.findViewById(R.id.register_button);
        this.mRegisterButton.setEnabled(false);
        this.mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClick();
            }
        });
        this.mLoginButton = (Button)view.findViewById(R.id.login_button);
        this.mLoginButton.setEnabled(false);
        this.mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick();
            }
        });
        this.mHostEText = (EditText)view.findViewById(R.id.host_field);
        this.mHostEText.addTextChangedListener(new RegisterWatcher(mHostEText));
        this.mHostEText.addTextChangedListener(new LoginWatcher(mHostEText));
        this.mPortEText = (EditText)view.findViewById(R.id.port_field);
        this.mPortEText.addTextChangedListener(new RegisterWatcher(mPortEText));
        this.mPortEText.addTextChangedListener(new LoginWatcher(mPortEText));
        this.mUsernameEText = (EditText)view.findViewById(R.id.username_field);
        this.mUsernameEText.addTextChangedListener(new RegisterWatcher(mUsernameEText));
        this.mUsernameEText.addTextChangedListener(new LoginWatcher(mUsernameEText));
        this.mPasswordEText = (EditText)view.findViewById(R.id.pass_field);
        this.mPasswordEText.addTextChangedListener(new RegisterWatcher(mPasswordEText));
        this.mPasswordEText.addTextChangedListener(new LoginWatcher(mPasswordEText));
        this.mFirstNameEText = (EditText)view.findViewById(R.id.fname_field);
        this.mFirstNameEText.addTextChangedListener(new RegisterWatcher(mFirstNameEText));
        this.mLastNameEText = (EditText)view.findViewById(R.id.lname_field);
        this.mLastNameEText.addTextChangedListener(new RegisterWatcher(mLastNameEText));
        this.mEmailEText = (EditText)view.findViewById(R.id.email_field);
        this.mEmailEText.addTextChangedListener(new RegisterWatcher(mEmailEText));
        this.mGenderButton = (RadioGroup)view.findViewById(R.id.gender_button);
        this.mGenderButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                validateRegister();
            }
        });
        this.mMButton = (RadioButton)view.findViewById(R.id.male_button);
        this.mMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterReq.setGender("m");
            }
        });
        this.mFButton = (RadioButton)view.findViewById(R.id.female_button);
        this.mFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterReq.setGender("f");
            }
        });

        return view;
    }

    private class RegisterWatcher implements TextWatcher {
        private View mView;
        private RegisterWatcher(View view) { this.mView = view; }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            validateRegister();
            final int id1 = R.id.host_field;
            final int id2 = R.id.port_field;
            final int id3 = R.id.username_field;
            final int id4 = R.id.pass_field;
            final int id5 = R.id.fname_field;
            final int id6 = R.id.lname_field;
            final int id7 = R.id.email_field;

            switch(mView.getId()) {
                case id1:
                    serverHost = text;
                case id2:
                    serverPort = text;
                case id3:
                    mRegisterReq.setUsername(text);
                case id4:
                    mRegisterReq.setPassword(text);
                case id5:
                    mRegisterReq.setFirstName(text);
                case id6:
                    mRegisterReq.setLastName(text);
                case id7:
                    mRegisterReq.setEmail(text);
            }
        }
    }

    private class LoginWatcher implements TextWatcher {
        private View mView;
        private LoginWatcher(View view) { this.mView = view; }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            validateLogin();
            final int id1 = R.id.host_field;
            final int id2 = R.id.port_field;
            final int id3 = R.id.username_field;
            final int id4 = R.id.pass_field;

            switch(mView.getId()) {
                case id1:
                    serverHost = text;
                case id2:
                    serverPort = text;
                case id3:
                    mLoginReq.setUsername(text);
                case id4:
                    mLoginReq.setPassword(text);
            }
        }
    }

    private class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResult> {
        private Fragment mFragment;
        private Context mContext;
        private RegisterResult mResult;

        public RegisterTask(Fragment fragment, Context context) {
            this.mFragment = fragment;
            this.mContext = context;
            this.mResult = new RegisterResult();
        }

        @Override
        protected RegisterResult doInBackground(RegisterRequest... registerRequests) {
            try {
                ServerProxy proxy = new ServerProxy();
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
                this.mResult = proxy.register(url, registerRequests[0]);
                return mResult;
            } catch (MalformedURLException ex) {
                this.mResult.setMessage("Error: Failed register task in LoginFragment");
                this.mResult.setSuccess(false);
                return mResult;
            }
        }

        @Override
        protected void onPostExecute(RegisterResult result) {
            if (result.isSuccess()) {
                FamilyTree tree = FamilyTree.getInstance();
                tree.setAuthtoken(result.getAuthtoken());
                GetEventsTask eventsTask = new GetEventsTask(mFragment, mContext);
                eventsTask.execute(tree.getAuthtoken());
                Toast.makeText(mContext, "Registration Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Registration Failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class LoginTask extends AsyncTask<LoginRequest, Void, LoginResult> {
        private Fragment mFragment;
        private Context mContext;
        private LoginResult mResult;

        public LoginTask(Fragment fragment, Context context) {
            this.mFragment = fragment;
            this.mContext = context;
            this.mResult = new LoginResult();
        }

        @Override
        protected LoginResult doInBackground(LoginRequest... loginRequests) {
            try {
                ServerProxy proxy = new ServerProxy();
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
                this.mResult = proxy.login(url, loginRequests[0]);
                return mResult;
            } catch (MalformedURLException ex) {
                this.mResult.setMessage("Error: Failed login task in LoginFragment");
                this.mResult.setSuccess(false);
                return mResult;
            }
        }

        @Override
        protected void onPostExecute(LoginResult result) {
            if (result.isSuccess()) {
                FamilyTree tree = FamilyTree.getInstance();
                tree.setAuthtoken(result.getAuthtoken());
                GetEventsTask eventsTask = new GetEventsTask(mFragment, mContext);
                eventsTask.execute(tree.getAuthtoken());
                Toast.makeText(mContext, "Login Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Login Failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetPersonsTask extends AsyncTask<String, Void, AllPersonResult> {
        private Fragment mFragment;
        private Context mContext;
        private AllPersonResult mResult;

        public GetPersonsTask(Fragment fragment, Context context) {
            this.mFragment = fragment;
            this.mContext = context;
            this.mResult = new AllPersonResult();
        }

        @Override
        protected AllPersonResult doInBackground(String... authtoken) {
            try {
                ServerProxy proxy = new ServerProxy();
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/");
                this.mResult = proxy.persons(url, authtoken[0]);
                return mResult;
            } catch (MalformedURLException ex) {
                this.mResult.setMessage("Error: Failed persons task in LoginFragment");
                this.mResult.setSuccess(false);
                return mResult;
            }
        }

        @Override
        protected void onPostExecute(AllPersonResult result) {
            if (result.isSuccess()) {
                FamilyTree tree = FamilyTree.getInstance();
                tree.setPersons(result.getData());
                MainActivity mainActivity = (MainActivity)mContext;
                mainActivity.loginInit();
//                Person user = result.getData()[0];
//                String str = user.getFirstName() + " " + user.getLastName();
//                Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
                Toast.makeText(mContext, "Getting Persons Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Getting Persons Failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetEventsTask extends AsyncTask<String, Void, AllEventResult> {
        private Fragment mFragment;
        private Context mContext;
        private AllEventResult mResult;

        public GetEventsTask(Fragment fragment, Context context) {
            this.mFragment = fragment;
            this.mContext = context;
            this.mResult = new AllEventResult();
        }

        @Override
        protected AllEventResult doInBackground(String... authtoken) {
            try {
                ServerProxy proxy = new ServerProxy();
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/event/");
                this.mResult = proxy.events(url, authtoken[0]);
                return mResult;
            } catch (MalformedURLException ex) {
                this.mResult.setMessage("Error: Failed events task in LoginFragment");
                this.mResult.setSuccess(false);
                return mResult;
            }
        }

        @Override
        protected void onPostExecute(AllEventResult result) {
            if (result.isSuccess()) {
                FamilyTree tree = FamilyTree.getInstance();
                tree.setEvents(result.getData());
                GetPersonsTask personsTask = new GetPersonsTask(mFragment, mContext);
                personsTask.execute(tree.getAuthtoken());
                Toast.makeText(mContext, "Getting Events Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Getting Events Failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static LoginFragment newInstance(Context newCont) {
        sContext = newCont;
        LoginFragment frag = new LoginFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    private void validateRegister() {
        boolean genderNotClicked = false;

        if (mGenderButton.getCheckedRadioButtonId() == -1) {
            genderNotClicked = true;
        }
        String str0 = mHostEText.getText().toString();
        String str1 = mPortEText.getText().toString();
        String str2 = mUsernameEText.getText().toString();
        String str3 = mPasswordEText.getText().toString();
        String str4 = mFirstNameEText.getText().toString();
        String str5 = mLastNameEText.getText().toString();
        String str6 = mEmailEText.getText().toString();

        if (str0.isEmpty() || str1.isEmpty() || str2.isEmpty() || str3.isEmpty() ||
            str4.isEmpty() || str5.isEmpty() || str6.isEmpty() || genderNotClicked) {
            this.mRegisterButton.setEnabled(false);
        } else this.mRegisterButton.setEnabled(true);
    }

    private void onRegisterClick() {
        RegisterTask regTask = new RegisterTask(this, sContext);
        regTask.execute(mRegisterReq);
    }

    private void validateLogin() {
        String str0 = mHostEText.getText().toString();
        String str1 = mPortEText.getText().toString();
        String str2 = mUsernameEText.getText().toString();
        String str3 = mPasswordEText.getText().toString();

        if (str0.isEmpty() || str1.isEmpty() || str2.isEmpty() || str3.isEmpty()) {
            this.mLoginButton.setEnabled(false);
        } else this.mLoginButton.setEnabled(true);
    }

    private void onLoginClick() {
        LoginTask logTask = new LoginTask(this, sContext);
        logTask.execute(mLoginReq);
    }
}