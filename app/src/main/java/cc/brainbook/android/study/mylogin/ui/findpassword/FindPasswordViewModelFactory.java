package cc.brainbook.android.study.mylogin.ui.findpassword;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import cc.brainbook.android.study.mylogin.data.FindPasswordRepository;
import cc.brainbook.android.study.mylogin.data.UserRepository;
import cc.brainbook.android.study.mylogin.ui.login.LoginViewModel;

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
            return (T) new FindPasswordViewModel(FindPasswordRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
