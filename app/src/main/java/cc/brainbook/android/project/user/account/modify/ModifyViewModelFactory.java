package cc.brainbook.android.project.user.account.modify;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import cc.brainbook.android.project.user.account.data.UserRepository;

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
