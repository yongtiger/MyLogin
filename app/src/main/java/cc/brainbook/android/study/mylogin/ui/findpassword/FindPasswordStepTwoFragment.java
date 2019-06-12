package cc.brainbook.android.study.mylogin.ui.findpassword;

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

import cc.brainbook.android.study.mylogin.R;

public class FindPasswordStepTwoFragment extends Fragment implements View.OnClickListener {

    private TextView tvFindPasswordCannotFindPassword;
    private TextView tvFindPasswordInputEmail;
    private EditText etEmail;
    private ImageView ivClearEmail;
    private TextView tvDividerOr;
    private TextView tvFindPasswordInputMobile;
    private EditText etMobile;
    private ImageView ivClearMobile;

    private Button btnNext;
    private ProgressBar pbLoading;

    private FindPasswordViewModel findPasswordViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FindPasswordStepTwoFragment() {}

    /**
     * Create a new instance of DetailsFragment.
     */
    public static FindPasswordStepTwoFragment newInstance() {
        return new FindPasswordStepTwoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        // Note: A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
        ///https://developer.android.com/topic/libraries/architecture/viewmodel
        findPasswordViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), new FindPasswordViewModelFactory())
                .get(FindPasswordViewModel.class);

        findPasswordViewModel.getFindPasswordStepTwoFormState().observe(this, new Observer<FindPasswordStepTwoFormState>() {
            @Override
            public void onChanged(@Nullable FindPasswordStepTwoFormState findPasswordStepTwoFormState) {
                if (findPasswordStepTwoFormState == null) {
                    return;
                }
                btnNext.setEnabled(findPasswordStepTwoFormState.isDataValid());

//                findPasswordStepTwoFormState.isDataValid()
                ///[EditText错误提示]
                if (findPasswordStepTwoFormState.getEmailError() == null) {
                    etEmail.setError(null);
                } else {
                    etEmail.setError(getString(findPasswordStepTwoFormState.getEmailError()));
                }
                if (findPasswordStepTwoFormState.getMobileError() == null) {
                    etMobile.setError(null);
                } else {
                    etMobile.setError(getString(findPasswordStepTwoFormState.getMobileError()));
                }
            }
        });

        findPasswordViewModel.resetFindPasswordResult();
        findPasswordViewModel.getFindPasswordResult().observe(this, new Observer<FindPasswordResult>() {
            @Override
            public void onChanged(@Nullable FindPasswordResult findPasswordResult) {
                if (findPasswordResult == null) {
                    return;
                }
                pbLoading.setVisibility(View.GONE);
                if (findPasswordResult.getError() != null) {
                    ///[Request focus#根据返回错误来请求表单焦点]
                    switch (findPasswordResult.getError()) {
                        case R.string.find_password_exception_invalid_parameters:
                            break;
                        case R.string.find_password_exception_invalid_user_id:
                            break;
                        case R.string.find_password_exception_invalid_cannot_find_password:
                            break;
                        case R.string.find_password_exception_no_match_email:
                            etEmail.requestFocus();
                            break;
                        case R.string.find_password_exception_no_match_mobile:
                            etMobile.requestFocus();
                            break;
                        case R.string.find_password_exception_no_match_email_or_mobile:
                            etEmail.requestFocus();
                            break;
                        default:
                    }

                    ///Display failed message
                    if (getActivity() != null) {
                        ((FindPasswordActivity)getActivity()).showFailedMessage(findPasswordResult.getError());
                    }
                }
                if (findPasswordResult.getSuccess()) {
                    if (getActivity() != null) {
                        ((FindPasswordActivity)getActivity()).showFindPasswordStepThreeFragment();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_find_password_step_two, container, false);

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
                actionCheck();
                break;
        }
    }

    private void initView(View rootView) {
        tvFindPasswordCannotFindPassword = rootView.findViewById(R.id.tv_find_password_cannot_find_password);
        tvFindPasswordInputEmail = rootView.findViewById(R.id.tv_find_password_input_email);
        tvFindPasswordInputMobile = rootView.findViewById(R.id.tv_find_password_input_mobile);

        etEmail = rootView.findViewById(R.id.et_email);
        ivClearEmail = rootView.findViewById(R.id.iv_clear_email);
        tvDividerOr = rootView.findViewById(R.id.tv_divider_or);
        etMobile = rootView.findViewById(R.id.et_mobile);
        ivClearMobile = rootView.findViewById(R.id.iv_clear_mobile);

        btnNext = rootView.findViewById(R.id.btn_next);
        pbLoading = rootView.findViewById(R.id.pb_loading);

        ///如果Email\Mobile都没有则cannot find password
        if (TextUtils.isEmpty(findPasswordViewModel.getEmail())
                && TextUtils.isEmpty(findPasswordViewModel.getMobile())) {
            tvFindPasswordCannotFindPassword.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(findPasswordViewModel.getEmail())
                && !TextUtils.isEmpty(findPasswordViewModel.getMobile())) {
            tvDividerOr.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(findPasswordViewModel.getEmail())) {
            etEmail.setVisibility(View.VISIBLE);
            tvFindPasswordInputEmail.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(findPasswordViewModel.getMobile())) {
            etMobile.setVisibility(View.VISIBLE);
            tvFindPasswordInputMobile.setVisibility(View.VISIBLE);
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
                findPasswordViewModel.findPasswordStepTwoDataChanged(etEmail.getText().toString(),
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
                findPasswordViewModel.findPasswordStepTwoDataChanged(etEmail.getText().toString(),
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
                    actionCheck();
                }
                return false;
            }
        });
        etMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    actionCheck();
                }
                return false;
            }
        });
    }

    private void actionCheck() {
        ///[FIX#IME_ACTION_DONE没检验form表单状态]
        if (findPasswordViewModel.getFindPasswordStepTwoFormState().getValue() != null
                && findPasswordViewModel.getFindPasswordStepTwoFormState().getValue().isDataValid()) {
            findPasswordViewModel.check(etEmail.getText().toString(), etMobile.getText().toString());
        }
    }
}
