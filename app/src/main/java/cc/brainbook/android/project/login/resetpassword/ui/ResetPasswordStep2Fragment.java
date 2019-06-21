package cc.brainbook.android.project.login.resetpassword.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class ResetPasswordStep2Fragment extends Fragment implements View.OnClickListener {

    private TextView tvCannotResetPassword;
    private TextView tvInputEmail;
    private EditText etEmail;
    private ImageView ivClearEmail;
    private TextView tvDividerOr;
    private TextView tvInputMobile;
    private EditText etMobile;
    private ImageView ivClearMobile;

    private Button btnNext;
    private ProgressBar pbLoading;

    private ResetPasswordViewModel resetPasswordViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResetPasswordStep2Fragment() {}

    /**
     * Create a new instance of fragment.
     */
    public static ResetPasswordStep2Fragment newInstance() {
        return new ResetPasswordStep2Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        // Note: A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
        ///https://developer.android.com/topic/libraries/architecture/viewmodel
        resetPasswordViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), new ResetPasswordViewModelFactory())
                .get(ResetPasswordViewModel.class);

        resetPasswordViewModel.getResetPasswordStep2FormState().observe(this, new Observer<ResetPasswordStep2FormState>() {
            @Override
            public void onChanged(@Nullable ResetPasswordStep2FormState resetPasswordStep2FormState) {
                if (resetPasswordStep2FormState == null) {
                    return;
                }
                btnNext.setEnabled(resetPasswordStep2FormState.isDataValid());

                ///[EditText错误提示]
                if (resetPasswordStep2FormState.getEmailError() == null) {
                    etEmail.setError(null);
                } else {
                    etEmail.setError(getString(resetPasswordStep2FormState.getEmailError()));
                }
                if (resetPasswordStep2FormState.getMobileError() == null) {
                    etMobile.setError(null);
                } else {
                    etMobile.setError(getString(resetPasswordStep2FormState.getMobileError()));
                }
            }
        });

        resetPasswordViewModel.setResult();
        resetPasswordViewModel.getResult().observe(this, new Observer<Result>() {
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
                        case R.string.result_error_invalid_user_id:
                            break;
                        case R.string.result_error_cannot_reset_password:
                            break;
                        case R.string.result_error_not_match_email:
                            etEmail.requestFocus();
                            break;
                        case R.string.result_error_not_match_mobile:
                            etMobile.requestFocus();
                            break;
                        case R.string.result_error_not_match_email_or_mobile:
                            etEmail.requestFocus();
                            break;
                        default:    ///R.string.error_unknown
                    }

                    ///Display failed message
                    if (getActivity() != null) {
                        ((ResetPasswordActivity)getActivity()).showFailedMessage(result.getError());
                    }
                } else {
                    if (getActivity() != null) {
                        if (result.getSuccess() != null)
                            ((ResetPasswordActivity) getActivity()).updateUI(result.getSuccess());
                        ((ResetPasswordActivity)getActivity()).showResetPasswordStep3Fragment();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_reset_password_step_2, container, false);

        initView(rootView);
        initListener();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_email:
                ///[EditText清除输入框]
                etEmail.setText("");
                break;
            case R.id.iv_clear_mobile:
                ///[EditText清除输入框]
                etMobile.setText("");
                break;
            case R.id.btn_next:
                pbLoading.setVisibility(View.VISIBLE);
                actionNext();
                break;
        }
    }

    private void initView(@NonNull View rootView) {
        tvCannotResetPassword = rootView.findViewById(R.id.tv_cannot_reset_password);
        tvInputEmail = rootView.findViewById(R.id.tv_input_email);
        tvInputMobile = rootView.findViewById(R.id.tv_input_mobile);

        etEmail = rootView.findViewById(R.id.et_email);
        ivClearEmail = rootView.findViewById(R.id.iv_clear_email);
        tvDividerOr = rootView.findViewById(R.id.tv_divider_or);
        etMobile = rootView.findViewById(R.id.et_mobile);
        ivClearMobile = rootView.findViewById(R.id.iv_clear_mobile);

        btnNext = rootView.findViewById(R.id.btn_next);
        pbLoading = rootView.findViewById(R.id.pb_loading);

        ///如果Email\Mobile都没有则cannot findUser password
        if (TextUtils.isEmpty(resetPasswordViewModel.getEmail())
                && TextUtils.isEmpty(resetPasswordViewModel.getMobile())) {
            tvCannotResetPassword.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(resetPasswordViewModel.getEmail())
                && !TextUtils.isEmpty(resetPasswordViewModel.getMobile())) {
            tvDividerOr.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(resetPasswordViewModel.getEmail())) {
            etEmail.setVisibility(View.VISIBLE);
            tvInputEmail.setVisibility(View.VISIBLE);
            tvInputEmail.setText(String.format("%s: %s", tvInputEmail.getText(), resetPasswordViewModel.getEmail()));
        }
        if (!TextUtils.isEmpty(resetPasswordViewModel.getMobile())) {
            etMobile.setVisibility(View.VISIBLE);
            tvInputMobile.setVisibility(View.VISIBLE);
            tvInputMobile.setText(String.format("%s: %s", tvInputMobile.getText(), resetPasswordViewModel.getMobile()));
        }
    }

    private void initListener() {
        etEmail.setOnClickListener(this);
        ivClearEmail.setOnClickListener(this);
        etMobile.setOnClickListener(this);
        ivClearMobile.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                resetPasswordViewModel.resetPasswordStep2DataChanged(etEmail.getText().toString(),
                        etMobile.getText().toString());

                ///[EditText清除输入框]
                if (!TextUtils.isEmpty(s) && ivClearEmail.getVisibility() == View.GONE) {
                    ivClearEmail.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearEmail.setVisibility(View.GONE);
                }
            }
        });
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                resetPasswordViewModel.resetPasswordStep2DataChanged(etEmail.getText().toString(),
                        etMobile.getText().toString());

                ///[EditText清除输入框]
                if (!TextUtils.isEmpty(s) && ivClearMobile.getVisibility() == View.GONE) {
                    ivClearMobile.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearMobile.setVisibility(View.GONE);
                }
            }
        });

        ///Email/Mobile都设置为DONE，即只要其中任何一个IME输入后即可DONE（不必依次进行IME输入）
        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    actionNext();
                }
                return false;
            }
        });
        etMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    actionNext();
                }
                return false;
            }
        });
    }

    private void actionNext() {
        if (resetPasswordViewModel.getResetPasswordStep2FormState().getValue() != null
                && resetPasswordViewModel.getResetPasswordStep2FormState().getValue().isDataValid()) {
            resetPasswordViewModel.checkSendMode(etEmail.getText().toString(), etMobile.getText().toString());
        }
    }
}
