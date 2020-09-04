package cc.brainbook.android.project.user.account.modify;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.allen.library.SuperTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import cc.brainbook.android.project.user.R;
import cc.brainbook.android.project.user.account.data.UserRepository;
import cc.brainbook.android.project.user.account.data.model.LoggedInUser;
import cc.brainbook.android.project.user.util.FileUtil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyFragment extends Fragment {
    private static final int REQUEST_CODE_PICK_FROM_GALLERY = 1;
    private static final int REQUEST_CODE_PICK_FROM_CAMERA = 2;

    private Uri photoUri;
    private SuperTextView stvUsername;
    private SuperTextView stvPassword;
    private SuperTextView stvEmail;
    private SuperTextView stvMobile;
    private SuperTextView stvOauth;
    private ImageView ivAvatar;
    private ImageView ivModifyAvatar;

    private File avatarOriginalFile;
    private File avatarFile;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            photoUri = Uri.parse((String) savedInstanceState.get("photo_uri"));
        }

        avatarOriginalFile = new File(getActivity().getExternalCacheDir(), "avatar_original.jpg");
        avatarFile = new File(getActivity().getExternalCacheDir(), "avatar.jpg");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (photoUri != null) {
            outState.putString("photo_uri", photoUri.toString());
        }
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
        ivAvatar = rootView.findViewById(R.id.iv_avatar);   ///[avatar]
        ivModifyAvatar = rootView.findViewById(R.id.iv_modify_avatar);  ///[avatar]

        final UserRepository userRepository = UserRepository.getInstance();
        final LoggedInUser loggedInUser = userRepository.getLoggedInUser();
        if (loggedInUser != null) {
            stvUsername.setRightString(loggedInUser.getUsername());
            stvEmail.setRightString(loggedInUser.getEmail());
            stvMobile.setRightString(loggedInUser.getMobile());
        }

        ///[avatar]
        ///Glide下载图片（使用已经缓存的图片）给imageView
        ///https://muyangmin.github.io/glide-docs-cn/doc/getting-started.html
        final RequestOptions options = RequestOptions.bitmapTransform(new CircleCrop()) ///裁剪圆形
                .placeholder(R.drawable.avatar_default); ///   .placeholder(new ColorDrawable(Color.BLACK))   // 或者可以直接使用ColorDrawable
        Glide.with(getActivity())
                .load(UserRepository.getInstance().getLoggedInUser().getAvatar())
                .apply(options)
                .into(ivAvatar);
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
        ///[avatar]
        ivModifyAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///[头像#相机或图库选择对话框]
                startCameraOrGallery();
            }
        });
    }


    /* ------------------ ///[avatar] ------------------ */
    ///[avatar#相机或图库选择对话框]
    private void startCameraOrGallery() {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.label_setup_avatar)
                .setItems(new String[] { getString(R.string.label_pick_picture), getString(R.string.label_camera) },
                        new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    pickFromGallery();
                                } else {
                                    pickFromCamera();
                                }
                            }
                        })
                        .show()
                        .getWindow()
                        .setGravity(Gravity.BOTTOM);
    }

    ///[avatar#图片选择器#相册图库]
    private void pickFromGallery() {
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }

        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)),
                REQUEST_CODE_PICK_FROM_GALLERY);
    }

    ///[avatar#图片选择器#相机拍照]
    private void pickFromCamera() {
        // create Intent to take a picture and return control to the calling application
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            photoUri = FileUtil.getUriFromFile(getActivity(), avatarOriginalFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_CODE_PICK_FROM_CAMERA);
        }
    }

    ///[avatar#裁剪/压缩#Yalantis/uCrop]https://github.com/Yalantis/uCrop
    private void startCrop(Activity activity, @NonNull Uri source, @NonNull Uri destination) {
        final UCrop uCrop = UCrop.of(source, destination)
                .withAspectRatio(1, 1);

        uCrop.start(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ///[avatar#图片选择器#相册图库]
        if (requestCode == REQUEST_CODE_PICK_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCrop(getActivity(), selectedUri, Uri.fromFile(avatarFile));
                } else {
                    Toast.makeText(getActivity(), R.string.message_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), R.string.message_image_select_cancelled, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), R.string.message_image_select_failed, Toast.LENGTH_SHORT).show();
            }
        }

        ///[avatar#图片选择器#相机拍照]
        else if (requestCode == REQUEST_CODE_PICK_FROM_CAMERA) {
            if (resultCode == RESULT_OK) {
                startCrop(getActivity(), photoUri, Uri.fromFile(avatarFile));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getActivity(), R.string.message_image_capture_cancelled, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), R.string.message_image_capture_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //////??????删除头像文件（当Camera拍照会产生）
    private void removeAvatarFiles() {
        if (avatarOriginalFile != null && avatarOriginalFile.exists()) {
            avatarOriginalFile.delete();
        }
        if (avatarFile != null && avatarFile.exists()) {
            avatarFile.delete();
        }
    }

}
