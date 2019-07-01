package cc.brainbook.android.project.login.useraccount.modify;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.useraccount.data.UserRepository;
import cc.brainbook.android.project.login.useraccount.data.model.LoggedInUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyFragment extends Fragment {

    private SuperTextView stvUsername;
    private SuperTextView stvPassword;
    private SuperTextView stvEmail;
    private SuperTextView stvMobile;
    private SuperTextView stvOauth;
    private ImageView ivUserAvatar;
    private ImageView ivAvatarCamera;

    public ModifyFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of fragment.
     */
    public static ModifyFragment newInstance() {
        return new ModifyFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user_account, container, false);

        initView(rootView);
        initListener();

        return rootView;
    }

    private void initView(@NonNull View rootView) {
        stvUsername = rootView.findViewById(R.id.stv_username);
        stvPassword = rootView.findViewById(R.id.stv_password);
        stvEmail = rootView.findViewById(R.id.stv_email);
        stvMobile = rootView.findViewById(R.id.stv_mobile);
        stvOauth = rootView.findViewById(R.id.stv_oauth);
        ivUserAvatar = rootView.findViewById(R.id.iv_user_avatar);
        ivAvatarCamera = rootView.findViewById(R.id.iv_avatar_camera);

        ///Glide下载图片（使用已经缓存的图片）给imageView
        ///https://muyangmin.github.io/glide-docs-cn/doc/getting-started.html
        final RequestOptions options = RequestOptions.bitmapTransform(new CircleCrop()) ///裁剪圆形
                .placeholder(R.drawable.avatar_default); ///   .placeholder(new ColorDrawable(Color.BLACK))   // 或者可以直接使用ColorDrawable
        Glide.with(getActivity())
                .load(UserRepository.getInstance().getLoggedInUser().getAvatar())
                .apply(options)
                .into(ivUserAvatar);

        final UserRepository userRepository = UserRepository.getInstance();
        final LoggedInUser loggedInUser = userRepository.getLoggedInUser();
        if (loggedInUser != null) {
            stvUsername.setRightString(loggedInUser.getUsername());
            stvEmail.setRightString(loggedInUser.getEmail());
            stvMobile.setRightString(loggedInUser.getMobile());
        }

    }

    private void initListener() {
        stvUsername.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                if (getActivity() != null) {
                    ((ModifyActivity)getActivity()).showModifyUsernameFragment();
                }
            }
        });
        stvPassword.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                if (getActivity() != null) {
                    ((ModifyActivity)getActivity()).showModifyPasswordFragment();
                }
            }
        });
        stvEmail.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                if (getActivity() != null) {
                    ((ModifyActivity)getActivity()).showModifyEmailFragment();
                }
            }
        });
        stvMobile.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                if (getActivity() != null) {
                    ((ModifyActivity)getActivity()).showModifyMobileFragment();
                }
            }
        });
        stvOauth.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                if (getActivity() != null) {
                    ((ModifyActivity)getActivity()).showModifyOauthFragment();
                }
            }
        });
        ivAvatarCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: ----------------");
            }
        });
    }

}
