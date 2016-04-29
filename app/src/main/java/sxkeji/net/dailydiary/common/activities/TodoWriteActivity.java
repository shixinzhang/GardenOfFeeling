package sxkeji.net.dailydiary.common.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.Todo;
import sxkeji.net.dailydiary.common.BaseActivity;
import sxkeji.net.dailydiary.common.BaseApplication;
import sxkeji.net.dailydiary.http.HttpClient;
import sxkeji.net.dailydiary.storage.Constant;
import sxkeji.net.dailydiary.storage.SharedPreferencesUtils;
import sxkeji.net.dailydiary.utils.ColorGeneratorUtils;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.utils.UIUtils;

/**
 * 写备忘录/待做事项
 * Created by zhangshixin on 4/27/2016.
 */
public class TodoWriteActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener,
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.text_input_layout)
    TextInputLayout textInputLayout;
    @Bind(R.id.switch_set_reminder)
    SwitchCompat switchSetReminder;
    @Bind(R.id.et_date)
    EditText etDate;
    @Bind(R.id.et_time)
    EditText etTime;
    @Bind(R.id.tv_select_date_result)
    TextView tvSelectDateResult;
    @Bind(R.id.ll_select_date)
    LinearLayout llSelectDate;
    @Bind(R.id.fab_done)
    FloatingActionButton fabDone;

    private final String TAG = "TodoWriteActivity";
    private final String UPLOAD_TODO = "upload_todo";   //上传过云端的toDo,有了ObjectId
    private Date currentDate, selectDate;
    private int selectYear, selectMonth, selectDay, selectHour, selectMinute;
    private boolean hasReminder;    //是否要提醒
    private boolean showOnLockScreen;   //是否显示到锁屏
    private boolean isFinished;     //是否完成
    private boolean autoSync;
    private Todo newToDo;           //新建的todo
    private Todo updateToDo;       //要更新的todo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_todo);
        ButterKnife.bind(this);

        initData();
        initViews();
        setListeners();
    }

    private void initData() {
        currentDate = new Date();
        Calendar calendar = Calendar.getInstance();

        updateToDo = (Todo) getIntent().getSerializableExtra(Constant.EXTRA_TODO);
        if (updateToDo != null) {           //要更新todo，设置内容为之前的
            LogUtils.e(TAG, "get data : " + updateToDo.getContent());
            String content = updateToDo.getContent();
            showOnLockScreen = updateToDo.getShowOnLockScreen();
            hasReminder = updateToDo.getHasReminder();
            switchSetReminder.setChecked(hasReminder);
            isFinished = updateToDo.getIsFinished();
            if (hasReminder) {
                visibleOrInvisibleSelectDate(hasReminder);
            }
            etContent.setText(content);
            selectDate = updateToDo.getDate();
        } else {                    //新增todo
            selectDate = currentDate;
        }

        calendar.setTime(selectDate);
        selectYear = calendar.get(Calendar.YEAR);
        selectMonth = calendar.get(Calendar.MONTH) + 1;
        selectDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectMinute = calendar.get(Calendar.MINUTE);

        autoSync = (boolean) SharedPreferencesUtils.get(this, Constant.SETTING_TODO_AUTO_SYNC, false);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon_clear_white);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setText2DateEditText();
        setText2TimeEditText();

    }

    private void setListeners() {
        switchSetReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hasReminder = isChecked;
                visibleOrInvisibleSelectDate(isChecked);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hideKeyboard(etDate);
                showMaterialDatePicker();

            }
        });
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hideKeyboard(etTime);
                showMaterialTimePicker();
            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDo();
            }
        });
    }

    /**
     * 保存ToDo
     * 如果要自动上传的话，先上传、后保存到DB
     */
    private void saveToDo() {
        String todoContent = etContent.getText().toString();
        int todoColor = ColorGeneratorUtils.MATERIAL.getRandomColor();
        String todoObjectId = null;
        if (TextUtils.isEmpty(todoContent)) {
            // 新增时提示
            if (updateToDo == null) {
                UIUtils.showToastSafe(TodoWriteActivity.this, "没有新增可以保存");
            }
            return;
        }
        UIUtils.showToastSafe(this, "自动保存");
        newToDo = new Todo(null, selectDate, todoContent, todoColor, hasReminder, showOnLockScreen, isFinished, todoObjectId);
        if (autoSync) {      //开启自动上传
            uploadTodo2Cloud(this, newToDo);
        } else {
            saveToDo2DB(newToDo);
        }

        finish();
    }

    private void saveToDo2DB(Todo todo) {
        if (updateToDo == null) {
            BaseApplication.getDaoSession().getTodoDao().insert(todo);
            UIUtils.showToastSafe(TodoWriteActivity.this, "保存成功");
        } else {
            BaseApplication.getDaoSession().getTodoDao().delete(updateToDo);
            BaseApplication.getDaoSession().getTodoDao().insert(todo);
            UIUtils.showToastSafe(TodoWriteActivity.this, "更新成功");
        }
        LogUtils.e(TAG, "new ToDo :" + todo.getDate() + "/" + todo.getContent() + "/ "
                + todo.getColor() + "/" + todo.getHasReminder() + "/ objectId " + todo.getObjectId());
    }

    /**
     * 上传ToDo到云端、然后设置此ToDo的objectId,再存储
     *
     * @param context
     * @param todo
     */
    public void uploadTodo2Cloud(final Context context, Todo todo) {
        String userNumber = (String) SharedPreferencesUtils.get(context, Constant.ACCOUNT_USER_NUMBER, "");
        if (TextUtils.isEmpty(userNumber)) {
            LogUtils.e("uploadTodo2Cloud", "userNumber is null , upload failed!");
            return;
        }
        String objectId = todo.getObjectId();

        final AVObject uploadTodo = new AVObject(Constant.LEANCLOUD_TABLE_TODO);
        if (!TextUtils.isEmpty(objectId)) {
            uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_OBJECTID, objectId);
            LogUtils.e(TAG, "Update todo " + objectId);
        }
        uploadTodo.put(Constant.LEANCLOUD_TABLE_USERNUMBER, userNumber);
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_DATE, todo.getDate());
        //TODO:到底要不要内容呢？还是只一个标题就好了,在"一起改进"里问一下
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_TITLE, todo.getContent());
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_COLOR, todo.getColor());
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_ISFINISHED, false);
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_REMINDER, todo.getHasReminder());
        uploadTodo.put(Constant.LEANCLOUD_TODO_PROPERTY_SHOW_ON_SCREEN, todo.getShowOnLockScreen());

        uploadTodo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(UPLOAD_TODO, uploadTodo);
                    Message msg = new Message();
                    msg.setData(bundle);
                    msg.what = 1;
                    handler.sendMessage(msg);
                    UIUtils.showToastSafe(context, "上传云端成功");
                } else {
                    UIUtils.showToastSafe(context, "上传云端失败" + e.getMessage());
//                    LogUtils.e("upload2LeanCloud", "LeanCloud save result : " + e.getMessage());
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:     //更新
                    AVObject upLoadObject = msg.getData().getParcelable(UPLOAD_TODO);
                    newToDo.setObjectId(upLoadObject.getObjectId());
                    saveToDo2DB(newToDo);
                    break;
            }
        }
    };

    /**
     * 隐藏、显示 日期选择
     *
     * @param show
     */
    private void visibleOrInvisibleSelectDate(final boolean show) {
        float start, end;
        if (show) {
            start = 0.5f;
            end = 1f;
        } else {
            start = 1f;
            end = 0f;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(llSelectDate, "alpha", start, end).setDuration(500);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (show) {
                    llSelectDate.setVisibility(View.VISIBLE);
                } else {
                    llSelectDate.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    /**
     * 弹出MD风格的日期选择器
     */
    private void showMaterialDatePicker() {
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog =
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(TodoWriteActivity.this, selectYear, selectMonth - 1, selectDay);
//                if(theme.equals(MainActivity.DARKTHEME)){
//                    datePickerDialog.setThemeDark(true);
//                }
        datePickerDialog.show(getFragmentManager(), "DateFragment");
    }

    /**
     * 弹出MD风格的日期选择器
     */
    private void showMaterialTimePicker() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(TodoWriteActivity.this, selectHour, selectMinute, true);
//        if(theme.equals(MainActivity.DARKTHEME)){
//            timePickerDialog.setThemeDark(true);
//        }
        timePickerDialog.show(getFragmentManager(), "TimeFragment");
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selectYear = year;
        selectMonth = monthOfYear + 1;
        selectDay = dayOfMonth;

        setText2DateEditText();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectDate);
        calendar.set(year, monthOfYear, dayOfMonth);
        selectDate = calendar.getTime();

        if (selectDate.before(currentDate)) {
            tvSelectDateResult.setText("您选择的日期已经过去，无法提醒");
            tvSelectDateResult.setTextColor(getResources().getColor(R.color.accent));
            hasReminder = false;
        } else {
            hasReminder = true;
            tvSelectDateResult.setText("");
//            tvSelectDateResult.setTextColor(getResources().getColor(R.color.secondary_text));
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        selectHour = hourOfDay;
        selectMinute = minute;
        setText2TimeEditText();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectDate);
        calendar.set(selectYear, selectMonth - 1, selectDay, hourOfDay, minute);
        selectDate = calendar.getTime();

        if (selectDate.before(currentDate)) {
            tvSelectDateResult.setText("您选择的时间已经过去，无法提醒");
            hasReminder = false;
            tvSelectDateResult.setTextColor(getResources().getColor(R.color.accent));
        } else {
            tvSelectDateResult.setText("");
            hasReminder = true;
//            tvSelectDateResult.setTextColor(getResources().getColor(R.color.secondary_text));
        }
    }

    /**
     * 设置选择日期
     */
    private void setText2DateEditText() {
        String selectDateStr = selectYear + "年" + selectMonth + "月" + selectDay + "日";
        etDate.setText(selectDateStr);
    }

    /**
     * 设置选择时间
     */
    private void setText2TimeEditText() {
        String selectTimeStr = getFormatStr(selectHour) + " : " + getFormatStr(selectMinute);
        etTime.setText(selectTimeStr);
    }

    /**
     * 格式化数字
     *
     * @param number
     * @return 小于0 时 加0前缀
     */
    private String getFormatStr(int number) {
        return number < 10 ? ("0" + number) : ("" + number);
    }

    @Override
    public void onBackPressed() {
        if (updateToDo == null) {
            saveToDo();
        }
        super.onBackPressed();
    }
}
