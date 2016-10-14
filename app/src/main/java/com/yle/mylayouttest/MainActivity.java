package com.yle.mylayouttest;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.notification)
    TextView notification;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.singleChoice)
    TextView singleChoice;
    @BindView(R.id.multiChoice)
    TextView multiChoice;
    @BindView(R.id.toast)
    TextView toast;
    @BindView(R.id.define_toast)
    TextView defineToast;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.textInputLayout_u)
    TextInputLayout textInputLayoutU;
    @BindView(R.id.textInputLayout_p)
    TextInputLayout textInputLayoutP;

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        textInputLayoutU.setError("First name is required");
        textInputLayoutU.setError(null);

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ListAdapter adapter = listView.getAdapter();
                if (adapter instanceof Filterable) {
                    Filter filter = ((Filterable) adapter).getFilter();
                    if (!TextUtils.isEmpty(newText)) {
//                        listView.setFilterText(newText);
                        filter.filter(newText);
                    } else {
//                        listView.clearTextFilter();
                        filter.filter(null);
                    }
                }
                return false;
            }
        });

        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            View view = getLayoutInflater().inflate(R.layout.textview, null);
            viewList.add(view);
        }
        viewPager.setAdapter(new TabPagerAdapter(viewList));
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tabCall = tabLayout.getTabAt(0);
        tabCall.setIcon(R.drawable.selector_call);
        TabLayout.Tab tabFavor = tabLayout.getTabAt(1);
        tabFavor.setIcon(R.drawable.selector_favorite);
//        TabLayout.Tab tabSearch = tabLayout.getTabAt(2);
//        tabSearch.setIcon(R.drawable.selector_search);
    }

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    public boolean validateEmail(String email) {
//        matcher = pattern.matcher(email);
//        return matcher.matches();
        return email.length() > 5;
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }

    public void submit(View v) {
        hideKeyboard();
        String username = textInputLayoutU.getEditText().getText().toString();
        String password = textInputLayoutP.getEditText().getText().toString();
        if (!validateEmail(username)) {
            textInputLayoutU.setError("Not a valid email address!");
            return;
        } else {
            textInputLayoutU.setError(null);
        }
        if (!validatePassword(password)) {
            textInputLayoutP.setError("Not a valid password!");
            return;
        } else {
            textInputLayoutP.setError(null);
        }
        Snackbar.make(activityMain, "success", Snackbar.LENGTH_LONG).show();
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    class TabPagerAdapter extends PagerAdapter {
        List<View> viewList;

        public TabPagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Item One";
                case 1:
                    return "Item Two";
                case 2:
                    return "Item Three";
            }
            return super.getPageTitle(position);
        }
    }

    @OnClick(R.id.dialog)
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.MyDialogTheme, this, 2016, 10, 3);
        datePickerDialog.show();
    }

    @OnClick(R.id.notification)
    public void showNotification() {
        final long[] vibratePattern = new long[]{
                0, 500, 250, 500
        };
        File file = new File(Environment.getExternalStorageDirectory() + "/aa.mp3");
        Log.w("Sound:", file.exists() + "--sound");
        final Uri uri = Uri.fromFile(file);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:15056251525"));
        // 创建一个PendingIntent，以指定点击Notification后跳转启动到哪个Activity
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);//参数：上下文，请求码，意图，int flags

//        //自定义notification布局
//        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);//改了布局之后，需要清除缓存
//        remoteViews.setTextViewText(R.id.tv_title, "这是自定义的标题");
//        remoteViews.setTextViewText(R.id.tv_content, "这是自定义的内容");
//        remoteViews.setOnClickPendingIntent(R.id.ll_notifi, pendingIntent);
//
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
//        builder.setSmallIcon(R.drawable.ic_notifications_white_small);
//        Notification notification = builder.build();
//
//        if (Build.VERSION.SDK_INT >= 16) {
//            notification.bigContentView = remoteViews;
//        }
//        notification.contentView = remoteViews;
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
//        notificationManagerCompat.notify(0x1234, notification);

        //系统的notification
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Notification notification = builder.setContentTitle("Title")
                            .setContentText("This is a notification!")
                            .setSmallIcon(R.drawable.ic_notifications_white_small)//设置小图标
                            .setColor(Color.parseColor("#4B8A08"))//设置小图标背景色
                            .setTicker("您有新短消息，请注意查收!")//设置在status bar上显示的提示文字
                            .setWhen(System.currentTimeMillis())
                            .setVibrate(vibratePattern)//设置震动
                            .setLights(Color.MAGENTA, 1000, 1000)//设置呼吸灯（关闭屏幕有效）
                            .setSound(uri)//设置自定义铃声
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notifications_white_small))//设置大图标
                            .setContentIntent(pendingIntent)
                            .setNumber(1)//显示数字
                            .setAutoCancel(true)//自动取消
                            .build();
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                    notificationManagerCompat.notify(0x1234, notification);
                } catch (
                        InterruptedException e
                        )

                {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @OnClick(R.id.singleChoice)
    public void showSingleChoiceDialog() {
        showChoiceDialog(R.string.singleChoice);
    }

    private void showChoiceDialog(int strId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle(strId);

        String[] items = getResources().getStringArray(R.array.ringtone_list);
        if (R.string.singleChoice == strId) {

            builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        } else {
            builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                }
            });
        }

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.multiChoice)
    public void showMultiChoiceDialog() {
        showChoiceDialog(R.string.multiChoice);
    }

    @OnClick(R.id.toast)
    public void showToast() {
        Toast toast = Toast.makeText(this, "toast", Toast.LENGTH_LONG);
        //设置消息颜色
        TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
        textView.setTextColor(Color.YELLOW);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.width = 400;
        layoutParams.height = 100;
        layoutParams.setMargins(10, 10, 10, 10);

        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        //设置背景颜色
        toast.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));

        toast.show();
    }

    @OnClick(R.id.define_toast)
    public void showDefineToast() {
        Toast toast = new Toast(getApplicationContext());

        View view = getLayoutInflater().inflate(R.layout.toast_view, null);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        int margin = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, margin);

        toast.show();
    }

    @OnClick(R.id.snackBar)
    public void showSnackbar() {
        Snackbar snackbar = Snackbar
                .make(activityMain, "snackbar", Snackbar.LENGTH_LONG)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                    }
                })
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));

        View snackView = snackbar.getView();

        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView) snackView.findViewById(snackbarTextId);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        snackView.setBackgroundColor(Color.MAGENTA);


        snackbar.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Snackbar.make(activityMain, "Favorite", Snackbar.LENGTH_LONG).show();
                return true;
            case R.id.action_search:
                Snackbar.make(activityMain, "Search", Snackbar.LENGTH_LONG).show();
                return true;
        }
        return false;
    }
}
