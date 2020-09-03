package cc.brainbook.android.project.login.useraccount.authentication.ui.register;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import cc.brainbook.android.project.login.useraccount.data.UserRepository;

/**
 * ViewModel provider factory to instantiate RegisterViewModel.
 * Required given RegisterViewModel has a non-empty constructor
 */
public class RegisterViewModelFactory implements ViewModelProvider.Factory {
    ///[EditText显示/隐藏Password]初始化
    private boolean passwordVisibility;
    private boolean repeatPasswordVisibility;

    public RegisterViewModelFactory(boolean passwordVisibility, boolean repeatPasswordVisibility) {
        this.passwordVisibility = passwordVisibility;
        this.repeatPasswordVisibility = repeatPasswordVisibility;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(UserRepository.getInstance(), passwordVisibility, repeatPasswordVisibility);    ///[EditText显示/隐藏Password]初始化
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
