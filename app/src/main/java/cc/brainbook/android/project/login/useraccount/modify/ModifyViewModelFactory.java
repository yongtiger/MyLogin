package cc.brainbook.android.project.login.useraccount.modify;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import cc.brainbook.android.project.login.useraccount.data.UserRepository;

/**
 * ViewModel provider factory to instantiate ResetPasswordViewModel.
 * Required given ModifyViewModel has a non-empty constructor
 */
public class ModifyViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ModifyViewModel.class)) {
            return (T) new ModifyViewModel(UserRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
