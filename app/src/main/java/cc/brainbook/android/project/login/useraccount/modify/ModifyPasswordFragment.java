package cc.brainbook.android.project.login.useraccount.modify;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;

import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.result.Result;

public class ModifyPasswordFragment extends Fragment implements View.OnClickListener {

    private EditText etPassword;
    private ImageView ivClearPassword;
    private ImageView ivPasswordVisibility;
    private EditText etRepeatPassword;
    private ImageView ivClearRepeatPassword;
    private ImageView ivRepeatPasswordVisibility;

    private Button btnSave;
    private ProgressBar pbLoading;

    private ModifyViewModel modifyViewModel;

    public ModifyPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of fragment.
     */
    public static ModifyPasswordFragment newInstance() {
        return new ModifyPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        // Note: A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
        ///https://developer.android.com/topic/libraries/architecture/viewmodel
        modifyViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), new ModifyViewModelFactory())
                .get(ModifyViewModel.class);

        modifyViewModel.getModifyPasswordFormState().observe(this, new Observer<ModifyPasswordFormState>() {
            @Override
            public void onChanged(@Nullable ModifyPasswordFormState modifyPasswordFormState) {
                if (modifyPasswordFormState == null) {
                    return;
                }
                btnSave.setEnabled(modifyPasswordFormState.isDataValid());

                ///[EditText错误提示]
                if (modifyPasswordFormState.getPasswordError() == null) {
                    etPassword.setError(null);
                } else {
                    etPassword.setError(getString(modifyPasswordFormState.getPasswordError()));
                }
                if (modifyPasswordFormState.getRepeatPasswordError() == null) {
                    etRepeatPassword.setError(null);
                } else {
                    etRepeatPassword.setError(getString(modifyPasswordFormState.getRepeatPasswordError()));
                }
            }
        });

        modifyViewModel.setResult();
        modifyViewModel.getResult().observe(this, new Observer<Result>() {
            @Override
            public void onChanged(@Nullable Result result) {
                if (result == null) {
                    return;
                }
                pbLoading.setVisibility(View.GONE);
                if (result.getError() != null) {
                    ///[Request focus#根据返回错误来请求表单焦点]
                    switch (result.getError()) {
                        case R.string.error_network_error:
                            break;
                        case R.string.error_unknown:
                            break;
                        case R.string.error_invalid_parameters:
                            break;
                        case R.string.error_invalid_username:
                            etPassword.requestFocus();
                            break;
                        case R.string.result_error_cannot_reset_password:
                            break;
                        default:    ///R.string.error_unknown
                    }

                    ///Display failed message
                    if (getActivity() != null) {
                        ((ModifyActivity)getActivity()).showFailedMessage(result.getError());
                    }
                } else {
                    if (getActivity() != null) {
                        if (result.getSuccess() != null)
                            ((ModifyActivity) getActivity()).updateUI(result.getSuccess());
                        ((ModifyActivity) getActivity()).showModifyFragment();
                        ///[关闭其它fragment后回退显示ModifyFragment]关闭当前的Fragment返回上一个fragment
                        if (getFragmentManager() != null) {
                            getFragmentManager().popBackStack();
                        }
                    }
                }
            }
        });

        modifyViewModel.getPasswordVisibility().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == null) {
                    return;
                }
                if(aBoolean){
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivPasswordVisibility.setImageResource(R.drawable.ic_visibility);
                }else{
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivPasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
                }
                final String pwd = etPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    etPassword.setSelection(pwd.length());
            }
        });

        modifyViewModel.getRepeatPasswordVisibility().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == null) {
                    return;
                }
                if(aBoolean){
                    etRepeatPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivRepeatPasswordVisibility.setImageResource(R.drawable.ic_visibility);
                }else{
                    etRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivRepeatPasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
                }
                final String pwd = etRepeatPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    etRepeatPassword.setSelection(pwd.length());
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user_account_password, container, false);

        initView(rootView);
        initListener();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_password:
                ///[EditText清除输入框]
                etPassword.setText("");
                break;
            case R.id.iv_password_visibility:
                ///[EditText显示/隐藏Password]
                ///注意：因为初始化了，所以不会产生NullPointerException
                modifyViewModel.setPasswordVisibility(!modifyViewModel.getPasswordVisibility().getValue());
                break;
            case R.id.iv_clear_repeat_password:
                ///[EditText清除输入框]
                etRepeatPassword.setText("");
                break;
            case R.id.iv_repeat_password_visibility:
                ///[EditText显示/隐藏Password]
                ///注意：因为初始化了，所以不会产生NullPointerException
                modifyViewModel.setRepeatPasswordVisibility(!modifyViewModel.getRepeatPasswordVisibility().getValue());
                break;
            case R.id.btn_save:
                pbLoading.setVisibility(View.VISIBLE);
                actionSave();
                break;
        }
    }

    private void initView(@NonNull View rootView) {
        etPassword = rootView.findViewById(R.id.et_password);
        ivClearPassword = rootView.findViewById(R.id.iv_clear_password);
        ivPasswordVisibility = rootView.findViewById(R.id.iv_password_visibility);
        etRepeatPassword = rootView.findViewById(R.id.et_repeat_password);
        ivClearRepeatPassword = rootView.findViewById(R.id.iv_clear_repeat_password);
        ivRepeatPasswordVisibility = rootView.findViewById(R.id.iv_repeat_password_visibility);

        btnSave = rootView.findViewById(R.id.btn_save);
        pbLoading = rootView.findViewById(R.id.pb_loading);
    }

    private void initListener() {
        ivClearPassword.setOnClickListener(this);
        ivPasswordVisibility.setOnClickListener(this);
        ivClearRepeatPassword.setOnClickListener(this);
        ivRepeatPasswordVisibility.setOnClickListener(this);

        btnSave.setOnClickListener(this);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                modifyViewModel.modifyPasswordDataChanged(etPassword.getText().toString(),
                        etRepeatPassword.getText().toString());

                ///[EditText清除输入框]
                if (!TextUtils.isEmpty(s) && ivClearPassword.getVisibility() == View.GONE) {
                    ivClearPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearPassword.setVisibility(View.GONE);
                }
            }
        });
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    actionSave();
                }
                return false;
            }
        });
        etRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                modifyViewModel.modifyPasswordDataChanged(etPassword.getText().toString(),
                        etRepeatPassword.getText().toString());

                ///[EditText清除输入框]
                if (!TextUtils.isEmpty(s) && ivClearRepeatPassword.getVisibility() == View.GONE) {
                    ivClearRepeatPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearRepeatPassword.setVisibility(View.GONE);
                }
            }
        });

        etRepeatPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    actionSave();
                }
                return false;
            }
        });
    }

    private void actionSave() {
        if (modifyViewModel.getModifyPasswordFormState().getValue() != null
                && modifyViewModel.getModifyPasswordFormState().getValue().isDataValid()) {
            modifyViewModel.modifyPassword(etPassword.getText().toString());
        }
    }
}

