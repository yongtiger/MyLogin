package cc.brainbook.android.study.mylogin.useraccount.modify;


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
import cc.brainbook.android.study.mylogin.result.Result;

public class ModifyUsernameFragment extends Fragment implements View.OnClickListener {

    private EditText etUsername;
    private ImageView ivClearUsername;

    private Button btnSave;
    private ProgressBar pbLoading;

    private ModifyViewModel modifyViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ModifyUsernameFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of fragment.
     */
    public static ModifyUsernameFragment newInstance() {
        return new ModifyUsernameFragment();
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

        modifyViewModel.getModifyUsernameFormState().observe(this, new Observer<ModifyUsernameFormState>() {
            @Override
            public void onChanged(@Nullable ModifyUsernameFormState modifyUsernameFormState) {
                if (modifyUsernameFormState == null) {
                    return;
                }
                btnSave.setEnabled(modifyUsernameFormState.isDataValid());

                ///[EditText错误提示]
                if (modifyUsernameFormState.getUsernameError() == null) {
                    etUsername.setError(null);
                } else {
                    etUsername.setError(getString(modifyUsernameFormState.getUsernameError()));
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
                        case R.string.error_invalid_username:
                            etUsername.requestFocus();
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
                            ((ModifyActivity) getActivity()).updateUi(result.getSuccess());
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
        final View rootView = inflater.inflate(R.layout.fragment_user_account_username, container, false);

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
            case R.id.btn_save:
                pbLoading.setVisibility(View.VISIBLE);
                actionSave();
                break;
        }
    }

    private void initView(@NonNull View rootView) {
        etUsername = rootView.findViewById(R.id.et_username);
        ivClearUsername = rootView.findViewById(R.id.iv_clear_username);

        btnSave = rootView.findViewById(R.id.btn_save);
        pbLoading = rootView.findViewById(R.id.pb_loading);
    }

    private void initListener() {
        etUsername.setOnClickListener(this);
        ivClearUsername.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ///[EditText错误提示]
                modifyViewModel.modifyUsernameDataChanged(etUsername.getText().toString());

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
                    actionSave();
                }
                return false;
            }
        });
    }

    private void actionSave() {
        if (modifyViewModel.getModifyUsernameFormState().getValue() != null
                && modifyViewModel.getModifyUsernameFormState().getValue().isDataValid()) {
            modifyViewModel.modifyUsername(etUsername.getText().toString());
        }
    }
}
