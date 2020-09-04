package cc.brainbook.android.project.user.resetpassword.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Objects;

import cc.brainbook.android.project.user.R;
import cc.brainbook.android.project.user.resetpassword.util.CountDownTimerUtil;
import cc.brainbook.android.project.user.result.Result;

public class ResetPasswordStep3Fragment extends Fragment implements View.OnClickListener{

    private EditText etVerificationCode;

    private Button btnSendVerificationCode;
    private Button btnNext;
    private ProgressBar pbLoading;

    private CountDownTimerUtil mCountDownTimerUtils;

    private ResetPasswordViewModel resetPasswordViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResetPasswordStep3Fragment() {}

    /**
     * Create a new instance of fragment.
     */
    public static ResetPasswordStep3Fragment newInstance() {
        return new ResetPasswordStep3Fragment();
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

        resetPasswordViewModel.getResetPasswordStep3FormStateLiveData().observe(this, new Observer<ResetPasswordStep3FormState>() {
            @Override
            public void onChanged(@Nullable ResetPasswordStep3FormState resetPasswordStep3FormState) {
                if (resetPasswordStep3FormState == null) {
                    return;
                }

                btnNext.setEnabled(resetPasswordStep3FormState.isDataValid());

                ///[EditText错误提示]
                if (resetPasswordStep3FormState.getVerificationCodeError() == null) {
                    etVerificationCode.setError(null);
                } else {
                    etVerificationCode.setError(getString(resetPasswordStep3FormState.getVerificationCodeError()));
                }
            }
        });

        resetPasswordViewModel.setResultLiveData();
        resetPasswordViewModel.getResultLiveData().observe(this, new Observer<Result>() {
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
                        case R.string.result_error_failed_to_send_email:
                            break;
                        case R.string.result_error_failed_to_send_mobile:
                            break;
                        case R.string.result_error_failed_to_send_email_and_mobile:
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

                        if (result instanceof ResetPasswordSendVerificationCodeResult) {
                            ///[根据sendVerificationCode的结果来更新数据变化]
                            resetPasswordViewModel.resetPasswordStep3DataChanged(etVerificationCode.getText().toString());

                            ///[CountDownTimerUtil]
                            ///https://www.jianshu.com/p/b2c9fcee03c1
                            mCountDownTimerUtils = new CountDownTimerUtil(btnSendVerificationCode, 10000, 1000);
                            mCountDownTimerUtils.start();

                        } else {
                            ((ResetPasswordActivity)getActivity()).showResetPasswordStep4Fragment();
                        }
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_reset_password_step_3, container, false);

        initView(rootView);
        initListener();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ///[启动页面倒计时]停止CountDownTimer
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.cancel();
            mCountDownTimerUtils = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_verification_code:
                pbLoading.setVisibility(View.VISIBLE);
                actionSendVerificationCode();
                break;
            case R.id.btn_next:
                pbLoading.setVisibility(View.VISIBLE);
                actionNext();
                break;
        }
    }

    private void initView(@NonNull View rootView) {
        etVerificationCode = rootView.findViewById(R.id.et_verification_code);
        btnSendVerificationCode = rootView.findViewById(R.id.btn_send_verification_code);
        btnNext = rootView.findViewById(R.id.btn_next);

        pbLoading = rootView.findViewById(R.id.pb_loading);
    }

    private void initListener() {
        btnSendVerificationCode.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        etVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                resetPasswordViewModel.resetPasswordStep3DataChanged(etVerificationCode.getText().toString());
            }
        });
    }

    private void actionSendVerificationCode() {
        resetPasswordViewModel.sendVerificationCode();
    }

    private void actionNext() {
        if (resetPasswordViewModel.getResetPasswordStep3FormStateLiveData().getValue() != null
                && resetPasswordViewModel.getResetPasswordStep3FormStateLiveData().getValue().isDataValid()) {
            resetPasswordViewModel.verifyCode(etVerificationCode.getText().toString());
        }
    }

}
