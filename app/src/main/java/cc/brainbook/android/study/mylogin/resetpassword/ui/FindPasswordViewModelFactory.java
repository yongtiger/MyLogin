package cc.brainbook.android.study.mylogin.resetpassword.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import cc.brainbook.android.study.mylogin.resetpassword.data.ResetPasswordRepository;

/**
 * ViewModel provider factory to instantiate FindPasswordViewModel.
 * Required given FindPasswordViewModel has a non-empty constructor
 */
public class FindPasswordViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FindPasswordViewModel.class)) {
            return (T) new FindPasswordViewModel(ResetPasswordRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
