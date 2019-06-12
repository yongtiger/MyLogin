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
import android.util.Log;
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

public class FindPasswordStepOneFragment extends Fragment implements View.OnClickListener {

    private EditText etUsername;
    private ImageView ivClearUsername;

    private Button btnNext;
    private ProgressBar pbLoading;

    private FindPasswordViewModel findPasswordViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FindPasswordStepOneFragment() {}

    /**
     * Create a new instance of DetailsFragment.
     */
    public static FindPasswordStepOneFragment newInstance() {
        return new FindPasswordStepOneFragment();
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

        findPasswordViewModel.getFindPasswordStepOneFormState().observe(this, new Observer<FindPasswordStepOneFormState>() {
            @Override
            public void onChanged(@Nullable FindPasswordStepOneFormState findPasswordStepOneFormState) {
                if (findPasswordStepOneFormState == null) {
                    return;
                }
                btnNext.setEnabled(findPasswordStepOneFormState.isDataValid());

                ///[EditText错误提示]
                if (findPasswordStepOneFormState.getUsernameError() == null) {
                    etUsername.setError(null);
                } else {
                    etUsername.setError(getString(findPasswordStepOneFormState.getUsernameError()));
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
                        case R.string.find_password_exception_invalid_username:
                            etUsername.requestFocus();
                            break;
                        case R.string.find_password_exception_invalid_cannot_find_password:
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
                        ((FindPasswordActivity)getActivity()).showFindPasswordStepTwoFragment();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_find_password_step_one, container, false);

        initView(rootView);
        initListener();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_username:
                ///[EditText清除输入框]
                etUsername.setText("");
                break;
            case R.id.btn_next:
                pbLoading.setVisibility(View.VISIBLE);
                actionFind();
                break;
        }
    }

    private void initView(View rootView) {
        etUsername = rootView.findViewById(R.id.et_username);
        ivClearUsername = rootView.findViewById(R.id.iv_clear_username);

        btnNext = rootView.findViewById(R.id.btn_next);
        pbLoading = rootView.findViewById(R.id.pb_loading);
    }

    private void initListener() {
        etUsername.setOnClickListener(this);
        ivClearUsername.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                findPasswordViewModel.findPasswordStepOneDataChanged(etUsername.getText().toString());

                ///[EditText清除输入框]
                if (!TextUtils.isEmpty(s) && ivClearUsername.getVisibility() == View.GONE) {
                    ivClearUsername.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearUsername.setVisibility(View.GONE);
                }
            }
        });

        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    actionFind();
                }
                return false;
            }
        });
    }

    private void actionFind() {
        ///[FIX#IME_ACTION_DONE没检验form表单状态]
        if (findPasswordViewModel.getFindPasswordStepOneFormState().getValue() != null
                && findPasswordViewModel.getFindPasswordStepOneFormState().getValue().isDataValid()) {
            findPasswordViewModel.find(etUsername.getText().toString());
        }
    }
}
