package cc.brainbook.android.project.user.account.modify;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import cc.brainbook.android.project.user.R;
import cc.brainbook.android.project.user.oauth.config.Config;
import cc.brainbook.android.project.user.account.data.UserRepository;
import cc.brainbook.android.project.user.account.data.model.LoggedInUser;
import cc.brainbook.android.project.user.account.modify.exception.OauthUnbindException;
import cc.brainbook.android.project.user.account.modify.interfaces.OauthUnbindCallback;

import static cc.brainbook.android.project.user.account.authentication.exception.LogoutException.EXCEPTION_INVALID_PARAMETERS;
import static cc.brainbook.android.project.user.account.authentication.exception.LogoutException.EXCEPTION_IO_EXCEPTION;
import static cc.brainbook.android.project.user.account.authentication.exception.LogoutException.EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED;
import static cc.brainbook.android.project.user.account.authentication.exception.LogoutException.EXCEPTION_UNKNOWN;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyOauthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyOauthFragment extends ListFragment {
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> listItem =new ArrayList<HashMap <String, Object>>();

    public ModifyOauthFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of fragment.
     */
    public static ModifyOauthFragment newInstance() {
        return new ModifyOauthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionOauthUnbind(new String[]{});

        // 生成适配器的Item和动态aa数组对应的元素
        adapter = new SimpleAdapter(getActivity(), listItem,// 数据源
                R.layout.list_item_uaser_account_oauth,    // ListItem的XML实现
                new String[] { "ItemImage" },  // 动态数组与ImageItem对应的子项
                new int[] { R.id.ItemImage }    // list_items中对应的的ImageView和TextView
        ) {///https://blog.csdn.net/jushenziao/article/details/52168830
            @Override
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                ///ViewHolder
                ///Because we use a ViewHolder, we avoid having to call findViewById().
                ///http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2013/0501/1186.html
                final ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = View.inflate(getActivity(), R.layout.list_item_uaser_account_oauth, null);
                    holder.img = (ImageView) convertView.findViewById(R.id.ItemImage);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.img.setBackgroundResource((Integer) listItem.get(position).get("ItemImage"));

                return convertView;
            }

            final class ViewHolder {
                ImageView img;
            }
        };

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_account_oauth, container, false);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        new AlertDialog.Builder(getActivity())
                .setTitle("Do you want to unbind?")
                .setPositiveButton("Unbind", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final HashMap hashMap = (HashMap) listView.getAdapter().getItem(position);
                        final String network = (String) hashMap.get("network");
                        final String[] networks = new String[] {network};
                        actionOauthUnbind(networks);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * 解绑networks
     *
     * @return  返回unbind后该用户绑定的所有networks
     */
    private void actionOauthUnbind(String[] unbindNetworks) {
        final UserRepository userRepository = UserRepository.getInstance();
        final LoggedInUser loggedInUser = userRepository.getLoggedInUser();
        userRepository.oAuthUnbind(loggedInUser, unbindNetworks, new OauthUnbindCallback() {
            @Override
            public void onSuccess(String[] networks) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listItem.clear();
                        getData(networks);
                        ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();

                        if (unbindNetworks.length > 0) {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.result_success_oauth_unbind), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onError(OauthUnbindException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), getString(getErrorIntegerRes(e)), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private @StringRes int getErrorIntegerRes(OauthUnbindException e) {
        @StringRes final int error;
        switch (e.getCode()) {
            case EXCEPTION_TOKEN_IS_INVALID_OR_EXPIRED:
                error = R.string.error_token_is_invalid_or_expired;
                break;
            case EXCEPTION_IO_EXCEPTION:
                error = R.string.error_network_error;
                break;
            case EXCEPTION_UNKNOWN:
                error = R.string.error_unknown;
                break;
            case EXCEPTION_INVALID_PARAMETERS:
                error = R.string.error_invalid_parameters;
                break;
            default:
                error = R.string.error_unknown;
        }
        return error;
    }

    private void getData(String[] networks) {
        for (String network : networks) {
            final HashMap<String, Object> map = new HashMap<String , Object>();

            if (network.equals(Config.Network.GOOGLE.toString())) {
                map.put("ItemImage", R.drawable.layer_list_oauth_google);
            } else if (network.equals(Config.Network.FACEBOOK.toString())) {
                map.put("ItemImage", R.drawable.layer_list_oauth_facebook);
            } else if (network.equals(Config.Network.TWITTER.toString())) {
                map.put("ItemImage", R.drawable.layer_list_oauth_twitter);
            } else if (network.equals(Config.Network.MOB_FACEBOOK.toString())) {
                map.put("ItemImage", R.drawable.layer_list_oauth_facebook);
            } else if (network.equals(Config.Network.MOB_TWITTER.toString())) {
                map.put("ItemImage", R.drawable.layer_list_oauth_twitter);
            } else if (network.equals(Config.Network.MOB_LINKEDIN.toString())) {
                map.put("ItemImage", R.drawable.layer_list_oauth_linkedin);
            } else if (network.equals(Config.Network.MOB_QQ.toString())) {
                map.put("ItemImage", R.drawable.layer_list_oauth_qq);
            } else if (network.equals(Config.Network.MOB_WECHAT.toString())) {
                map.put("ItemImage", R.drawable.layer_list_oauth_wechat);
            } else if (network.equals(Config.Network.MOB_SINAWEIBO.toString())) {
                map.put("ItemImage", R.drawable.layer_list_oauth_sinaweibo);
            }

            map.put("network", network);
            listItem.add(map);
        }

    }
}
