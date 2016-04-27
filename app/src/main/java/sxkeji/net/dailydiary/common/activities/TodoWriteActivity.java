package sxkeji.net.dailydiary.common.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.common.BaseActivity;
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
    @Bind(R.id.ll_edit)
    LinearLayout llEdit;
    @Bind(R.id.userToDoRemindMeTextView)
    TextView userToDoRemindMeTextView;
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

    private Date currentDate, selectDate;
    private int currentYear, currentMonth, currentDay, currentHour, currentMinute;
    private int selectYear, selectMonth, selectDay, selectHour, selectMinute;

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
//                if(mUserToDoItem.getToDoDate()!=null){
////                    date = mUserToDoItem.getToDoDate();
//                    date = mUserReminderDate;
//                }
//                else{
//                    date = new Date();
//                }
        if (selectDate == null) {
            selectDate = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        selectYear = currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        selectMonth = currentMonth + 1;
        selectDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectMinute = currentMinute = calendar.get(Calendar.MINUTE);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_clear_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setText2DateEditText();
        setText2TimeEditText();

    }

    private void setListeners() {
        switchSetReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                visibleOrInvisible(isChecked);
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
    }


    /**
     * 隐藏、显示 日期选择
     *
     * @param show
     */
    private void visibleOrInvisible(final boolean show) {
        float start, end;
        if (show) {
            start = 0f;
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
        } else {
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
            tvSelectDateResult.setTextColor(getResources().getColor(R.color.accent));
        } else {
            tvSelectDateResult.setText("");
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
        String selectTimeStr = getFormatStr(selectHour) + " : " +getFormatStr(selectMinute);
        etTime.setText(selectTimeStr);

    }

    /**
     * 格式化数字
     * @param number
     * @return  小于0 时 加0前缀
     */
    private String getFormatStr(int number){
        return number < 10 ? ("0" + number) : ("" + number);
    }


}
