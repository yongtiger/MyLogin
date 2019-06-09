package cc.brainbook.android.study.mylogin.ui.login;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import cc.brainbook.android.study.mylogin.data.UserDataSource;
import cc.brainbook.android.study.mylogin.data.UserRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {
    ///[EditText显示/隐藏Password]初始化
    private boolean passwordVisibility;

    public LoginViewModelFactory(boolean passwordVisibility) {
        this.passwordVisibility = passwordVisibility;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(UserRepository.getInstance(), passwordVisibility);    ///[EditText显示/隐藏Password]初始化
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
