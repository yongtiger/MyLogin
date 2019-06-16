package cc.brainbook.android.study.mylogin.useraccount;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import cc.brainbook.android.study.mylogin.userauthentication.data.UserRepository;

/**
 * ViewModel provider factory to instantiate ResetPasswordViewModel.
 * Required given UserAccountViewModel has a non-empty constructor
 */
public class UserAccountViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserAccountViewModel.class)) {
            return (T) new UserAccountViewModel(UserRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
