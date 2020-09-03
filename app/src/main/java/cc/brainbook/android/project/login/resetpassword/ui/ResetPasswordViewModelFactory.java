package cc.brainbook.android.project.login.resetpassword.ui;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import cc.brainbook.android.project.login.resetpassword.data.ResetPasswordRepository;

/**
 * ViewModel provider factory to instantiate ResetPasswordViewModel.
 * Required given ResetPasswordViewModel has a non-empty constructor
 */
public class ResetPasswordViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ResetPasswordViewModel.class)) {
            return (T) new ResetPasswordViewModel(ResetPasswordRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
