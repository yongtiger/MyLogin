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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyMobileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyMobileFragment extends Fragment implements View.OnClickListener {

    private EditText etMobile;
    private ImageView ivClearMobile;

    private Button btnSave;
    private ProgressBar pbLoading;

    private ModifyViewModel modifyViewModel;

    public ModifyMobileFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of fragment.
     */
    public static ModifyMobileFragment newInstance() {
        return new ModifyMobileFragment();
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

        modifyViewModel.getModifyMobileFormState().observe(this, new Observer<ModifyMobileFormState>() {
            @Override
            public void onChanged(@Nullable ModifyMobileFormState modifyMobileFormState) {
                if (modifyMobileFormState == null) {
                    return;
                }
                btnSave.setEnabled(modifyMobileFormState.isDataValid());

                ///[EditText错误提示]
                if (modifyMobileFormState.getMobileError() == null) {
                    etMobile.setError(null);
                } else {
                    etMobile.setError(getString(modifyMobileFormState.getMobileError()));
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
                        case R.string.error_token_is_invalid_or_expired:
                            break;
                        case R.string.error_network_error:
                            break;
                        case R.string.error_unknown:
                            break;
                        case R.string.error_invalid_parameters:
                            break;
                        case R.string.error_invalid_mobile:
                            etMobile.requestFocus();
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user_account_mobile, container, false);

        initView(rootView);
        initListener();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_mobile:
                ///[EditText清除输入框]
                etMobile.setText("");
                break;
            case R.id.btn_save:
                pbLoading.setVisibility(View.VISIBLE);
                actionSave();
                break;
        }
    }

    private void initView(@NonNull View rootView) {
        etMobile = rootView.findViewById(R.id.et_mobile);
        ivClearMobile = rootView.findViewById(R.id.iv_clear_mobile);

        btnSave = rootView.findViewById(R.id.btn_save);
        pbLoading = rootView.findViewById(R.id.pb_loading);
    }

    private void initListener() {
        etMobile.setOnClickListener(this);
        ivClearMobile.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                modifyViewModel.modifyMobileDataChanged(etMobile.getText().toString());

                ///[EditText清除输入框]
                if (!TextUtils.isEmpty(s) && ivClearMobile.getVisibility() == View.GONE) {
                    ivClearMobile.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivClearMobile.setVisibility(View.GONE);
                }
            }
        });

        etMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        if (modifyViewModel.getModifyMobileFormState().getValue() != null
                && modifyViewModel.getModifyMobileFormState().getValue().isDataValid()) {
            modifyViewModel.modifyMobile(etMobile.getText().toString());
        }
    }
}
