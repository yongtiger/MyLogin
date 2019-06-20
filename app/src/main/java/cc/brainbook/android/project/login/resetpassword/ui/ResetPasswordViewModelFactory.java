package cc.brainbook.android.project.login.resetpassword.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

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
