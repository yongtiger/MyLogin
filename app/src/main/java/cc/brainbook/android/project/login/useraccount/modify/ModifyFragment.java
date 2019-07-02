package cc.brainbook.android.project.login.useraccount.modify;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cc.brainbook.android.project.login.R;
import cc.brainbook.android.project.login.useraccount.data.UserRepository;
import cc.brainbook.android.project.login.useraccount.data.model.LoggedInUser;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyFragment extends Fragment {
    private static final int REQUEST_CODE_PICK_FROM_GALLERY = 1;
    private static final int REQUEST_CODE_PICK_FROM_CAMERA = 2;

    private SuperTextView stvUsername;
    private SuperTextView stvPassword;
    private SuperTextView stvEmail;
    private SuperTextView stvMobile;
    private SuperTextView stvOauth;
    private ImageView ivAvatar;
    private ImageView ivModifyAvatar;

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
    private Uri photoURI;

    ///[avatar#相机或图库选择对话框]
    private void startCameraOrGallery() {
        new AlertDialog.Builder(getActivity()).setTitle("设置头像")
                .setItems(new String[] { "从相册中选取图片", "拍照" }, new DialogInterface.OnClickListener() {
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

    private void pickFromGallery() {
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }

        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_CODE_PICK_FROM_GALLERY);
    }

    private void pickFromCamera() {
        final File photoFile =  new File(getActivity().getExternalCacheDir(), "avatar_org.jpg");

        // create Intent to take a picture and return control to the calling application
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            ///[avatar#图片选择器#手机拍照]适应Android 7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoURI = FileProvider.getUriForFile(getActivity(),
                        "cc.brainbook.android.project.login.fileProvider",
                        photoFile);
            } else {
                photoURI = Uri.fromFile(photoFile);
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CODE_PICK_FROM_CAMERA);
        }
    }

    ///[avatar#裁剪/压缩#Yalantis/uCrop]https://github.com/Yalantis/uCrop
    private void startCrop(@NonNull Uri uri) {
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getExternalCacheDir(), "avatar.jpg")))
                .withAspectRatio(1, 1);

        uCrop.start(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    Drawable selectedDrawable;
                    try {
                        final InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedUri);
                        selectedDrawable = Drawable.createFromStream(inputStream, selectedUri.toString() );
                    } catch (FileNotFoundException e) {
                        selectedDrawable = getResources().getDrawable(R.drawable.avatar_default);
                    }
                    ivAvatar.setImageDrawable(selectedDrawable);  // 显示图片
                    startCrop(selectedUri);
                } else {
//                    Toast.makeText(SampleActivity.this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == REQUEST_CODE_PICK_FROM_CAMERA) {
            if (resultCode == RESULT_OK) {
                startCrop(photoURI);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

}
