package app.com.free.todo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

import app.com.free.todo.db.dBCategory;
import app.com.free.todo.db.dBTasks;
import app.com.free.todo.db.dBUserPreferences;
import app.com.free.todo.util.Constants;
import app.com.free.todo.util.OnSpinnerSelected;
import app.com.free.todo.util.SetReminder;
import app.com.free.todo.util.categorySimpleCursorAdapter;
import app.com.free.todo.util.dateTime;
import app.com.free.todo.util.notifyUser;

public class EditTask extends Activity implements OnInitListener {
    private AlarmManager am;
    private boolean isEditMode;
    private Spinner mCategory;
    private categorySimpleCursorAdapter mCategoryAdapter;
    private Cursor mCategoryCursor;
    private OnSpinnerSelected mCategorySelection;
    private dBCategory mCategorydb;
    private Button mClearDuedttm;
    private Button mClearStartdttm;
    private int mCurDay;
    private int mCurHour;
    private int mCurMinute;
    private int mCurMonth;
    private int mCurYear;
    private dateTime mDTTM;
    private EditText mDetail;
    private String mDetailOriginal;
    private Button mDetailVoiceButton;
    private Button mDueDate;
    private OnDateSetListener mDueDateSetListener;
    private Button mDueTime;
    private OnTimeSetListener mDueTimeSetListener;
    private Button mEMail;
    private Spinner mPriority;
    private ArrayAdapter<CharSequence> mPriorityAdapter;
    private String mPriorityCodeValue;
    private String mPriorityValue;
    private String mReminderAlarm;
    private boolean mReminderEnabled;
    private Long mRowId;
    private Button mSave;
    private int mSelDay;
    private int mSelHour;
    private int mSelMinute;
    private int mSelMonth;
    private int mSelYear;
    private Button mStartDate;
    private OnDateSetListener mStartDateSetListener;
    private Button mStartTime;
    private OnTimeSetListener mStartTimeSetListener;
    private CheckBox mStatus;
    private TextToSpeech mTTS;
    private Intent mTTSIntent;
    private Cursor mTaskCursor;
    private dBTasks mTaskdb;
    private EditText mTitle;
    private String mTitleOriginal;
    private Button mTitleVoiceButton;
    private char mUserDateFormat;
    private char mUserDateSeparator;
    private char mUserTimeFormat;
    private Intent myIntent;


    private MultiAutoCompleteTextView _autoCompleteTask;


    /* renamed from: com.crazelle.app.todo.free.EditTask.1 */
    class C00031 implements OnDateSetListener {
        C00031() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            EditTask.this.mSelYear = year;
            EditTask.this.mSelMonth = monthOfYear;
            EditTask.this.mSelDay = dayOfMonth;
            EditTask.this.updateDateDisplay(EditTask.this.mStartDate);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.EditTask.2 */
    class C00042 implements OnDateSetListener {
        C00042() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            EditTask.this.mSelYear = year;
            EditTask.this.mSelMonth = monthOfYear;
            EditTask.this.mSelDay = dayOfMonth;
            EditTask.this.updateDateDisplay(EditTask.this.mDueDate);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.EditTask.3 */
    class C00053 implements OnTimeSetListener {
        C00053() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            EditTask.this.mSelHour = hourOfDay;
            EditTask.this.mSelMinute = minute;
            EditTask.this.updateTimeDisplay(EditTask.this.mStartTime);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.EditTask.4 */
    class C00064 implements OnTimeSetListener {
        C00064() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            EditTask.this.mSelHour = hourOfDay;
            EditTask.this.mSelMinute = minute;
            EditTask.this.updateTimeDisplay(EditTask.this.mDueTime);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.EditTask.5 */
    class C00075 implements OnClickListener {
        C00075() {
        }

        public void onClick(View v) {
            EditTask.this.showDialog(9);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.EditTask.6 */
    class C00086 implements OnClickListener {
        C00086() {
        }

        public void onClick(View v) {
            EditTask.this.showDialog(11);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.EditTask.7 */
    class C00097 implements OnClickListener {
        C00097() {
        }

        public void onClick(View v) {
            EditTask.this.mStartDate.setText(R.string.selectdate);
            EditTask.this.mStartTime.setText(R.string.selecttime);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.EditTask.8 */
    class C00108 implements OnClickListener {
        C00108() {
        }

        public void onClick(View v) {
            EditTask.this.mDueDate.setText(R.string.selectdate);
            EditTask.this.mDueTime.setText(R.string.selecttime);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.EditTask.9 */
    class C00119 implements OnClickListener {
        C00119() {
        }

        public void onClick(View v) {
            EditTask.this.showDialog(10);
        }
    }

    public EditTask() {
        this.mUserDateFormat = dateTime.MonthFormat;
        this.mUserDateSeparator = '/';
        this.mUserTimeFormat = dateTime.MonthFormat;
        this.mStartDateSetListener = new C00031();
        this.mDueDateSetListener = new C00042();
        this.mStartTimeSetListener = new C00053();
        this.mDueTimeSetListener = new C00064();
    }

    public void onCreate(Bundle savedInstanceState) {
        Long l;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);
        this.am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        this.myIntent = new Intent(this, ReceiveAlarm.class);
        UserPreferenceValues up = new UserPreferenceValues(this);
        this.mUserDateFormat = up.getUserDateFormat();
        this.mUserDateSeparator = up.getUserDateSeparator();
        this.mUserTimeFormat = up.getTimeFormat();
        this.mReminderEnabled = up.getReminder();
        this.mReminderAlarm = up.getReminderAlarm();
        this.mDTTM = new dateTime(this.mUserDateSeparator, this.mUserDateFormat, this.mUserTimeFormat);
        if (savedInstanceState == null) {
            l = null;
        } else {
            l = (Long) savedInstanceState.getSerializable(dBUserPreferences.KEY_ROWID);
        }
        this.mRowId = l;
        if (this.mRowId == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.mRowId = Long.valueOf(extras.getLong(dBUserPreferences.KEY_ROWID));
                this.isEditMode = true;
            } else {
                this.mRowId = null;
                this.isEditMode = false;
            }
        }
        this.mCurYear = this.mDTTM.cal.get(1);
        this.mCurMonth = this.mDTTM.cal.get(2);
        this.mCurDay = this.mDTTM.cal.get(5);
        this.mCurHour = this.mDTTM.cal.get(11);
        this.mCurMinute = this.mDTTM.cal.get(12);
        this.mTaskdb = new dBTasks(this);
        this.mTaskdb.Open();
        this.mCategorydb = new dBCategory(this);
        this.mCategorydb.Open();
        this.mStartDate = (Button) findViewById(R.id.TaskStartDate_e);
        this.mStartTime = (Button) findViewById(R.id.TaskStartTime_e);
        this.mDueDate = (Button) findViewById(R.id.TaskDueDate_e);
        this.mDueTime = (Button) findViewById(R.id.TaskDueTime_e);
        this.mClearStartdttm = (Button) findViewById(R.id.clear_startdttm);
        this.mClearDuedttm = (Button) findViewById(R.id.clear_duedttm);
        this.mEMail = (Button) findViewById(R.id.email_button);
        this.mCategory = (Spinner) findViewById(R.id.category_spinner);
        this.mPriority = (Spinner) findViewById(R.id.priority_spinner);
        this.mSave = (Button) findViewById(R.id.save_button);
        this.mTitle = (EditText) findViewById(R.id.tasktitle_e);
        this.mDetail = (EditText) findViewById(R.id.TaskDetail_e);
        this.mStatus = (CheckBox) findViewById(R.id.status);


        this.mDetailVoiceButton = (Button) findViewById(R.id.detailvoicebutton);
        this.mTitleVoiceButton = (Button) findViewById(R.id.titlevoicebutton);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tags_suggestion));


        //autocomplete data

        _autoCompleteTask = (MultiAutoCompleteTextView) findViewById(R.id.tag_auto_complete);
        _autoCompleteTask.setAdapter(adapter);
        _autoCompleteTask.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());




        //---------------------------------------------------------------------------------------

        this.mStartDate.setOnClickListener(new C00075());
        this.mStartTime.setOnClickListener(new C00086());
        this.mClearStartdttm.setOnClickListener(new C00097());
        this.mClearDuedttm.setOnClickListener(new C00108());
        this.mDueDate.setOnClickListener(new C00119());
        this.mDueTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                EditTask.this.showDialog(12);
            }
        });
        this.mSave.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EditTask.this.setResult(-1);
                EditTask.this.saveTask();
                EditTask.this.CloseResources();
            }
        });
        this.mEMail.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String str;
                if (EditTask.this.mRowId == null) {
                    EditTask.this.saveTask();
                } else if (!((EditTask.this.mTitleOriginal == null || EditTask.this.mTitleOriginal.equals(EditTask.this.mTitle.getText().toString())) && (EditTask.this.mDetailOriginal == null || EditTask.this.mDetailOriginal.equals(EditTask.this.mDetail.getText().toString())))) {
                    EditTask.this.saveTask();
                }
                Intent emailIntent = new Intent("android.intent.action.SEND");
                emailIntent.setType("text/plain");
                emailIntent.putExtra("android.intent.extra.SUBJECT", EditTask.this.mTitle.getText().toString());
                emailIntent.putExtra("android.intent.extra.TEXT", EditTask.this.mDetail.getText().toString());
                emailIntent.putExtra("android.intent.category.INFO", EditTask.this.mCategorySelection.mValue);
                emailIntent.putExtra(dBTasks.D_PRIORITY, EditTask.this.mPriorityCodeValue);
                String str2 = dBTasks.D_STATUS;
                if (EditTask.this.mStatus.isChecked()) {
                    str = "Y";
                } else {
                    str = "N";
                }
                emailIntent.putExtra(str2, str);
                emailIntent.putExtra("start_date", EditTask.this.mStartDate.getText().toString());
                emailIntent.putExtra("start_time", EditTask.this.mStartTime.getText().toString());
                emailIntent.putExtra("due_date", EditTask.this.mDueDate.getText().toString());
                emailIntent.putExtra("due_time", EditTask.this.mDueTime.getText().toString());
                emailIntent.putExtra("android.intent.action.EDIT", "Y");
                EditTask.this.startActivity(Intent.createChooser(emailIntent, EditTask.this.getString(R.string.sendemail)));
            }
        });
        if (getPackageManager().queryIntentActivities(new Intent("android.speech.action.RECOGNIZE_SPEECH"), 0).size() != 0) {
            this.mTitleVoiceButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                    intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                    intent.putExtra("android.speech.extra.PROMPT", EditTask.this.getString(R.string.titlevoiceprompt));
                    EditTask.this.startActivityForResult(intent, Constants.Title_Request_Code);
                }
            });
            this.mDetailVoiceButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                    intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                    intent.putExtra("android.speech.extra.PROMPT", EditTask.this.getString(R.string.detailvoiceprompt));
                    EditTask.this.startActivityForResult(intent, Constants.Detail_Request_Code);
                }
            });
        } else {
            this.mTitleVoiceButton.setEnabled(false);
            this.mTitleVoiceButton.setVisibility(8);
            this.mDetailVoiceButton.setEnabled(false);
            this.mDetailVoiceButton.setVisibility(8);
        }
        populateCategories();
        populatePriorities();
        populateFields();
    }

    private void saveTask() {
        long createStatus = -1;
        boolean updateStatus = false;
        String sStartDate = null;
        String sDueDate = null;
        String sStartTime = null;
        String sDueTime = null;
        String tagOfTask = null;

        if (!this.mStartDate.getText().toString().trim().equals(getString(R.string.selectdate).trim())) {
            sStartDate = this.mDTTM.convertDate(this.mStartDate.getText().toString(), Character.valueOf(this.mUserDateFormat), Character.valueOf(dateTime.YearFormat));
        }
        if (!this.mDueDate.getText().toString().trim().equals(getString(R.string.selectdate).trim())) {
            sDueDate = this.mDTTM.convertDate(this.mDueDate.getText().toString(), Character.valueOf(this.mUserDateFormat), Character.valueOf(dateTime.YearFormat));
        }
        if (!this.mStartTime.getText().toString().trim().equals(getString(R.string.selecttime).trim())) {
            sStartTime = this.mDTTM.convertTime(this.mStartTime.getText().toString(), Character.valueOf(this.mUserTimeFormat), Character.valueOf(dateTime.MonthFormat));
        }
        if (!this.mDueTime.getText().toString().trim().equals(getString(R.string.selecttime).trim())) {
            sDueTime = this.mDTTM.convertTime(this.mDueTime.getText().toString(), Character.valueOf(this.mUserTimeFormat), Character.valueOf(dateTime.MonthFormat));
        }
        if (this.mTitle.getText().toString().equals("")) {
            new notifyUser(getApplicationContext()).Notify(getString(R.string.blanktitle));
            return;
        }

        if(this._autoCompleteTask.getText().toString().equals("")){
            Toast.makeText(EditTask.this,getString(R.string.blankTag),Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(EditTask.this,_autoCompleteTask.getText().toString(),Toast.LENGTH_SHORT).show();

        String dStatus = this.mStatus.isChecked() ? "Y" : "N";
        this.mTaskdb.ResetUpdateBuffer();
        this.mTaskdb.setTitle(this.mTitle.getText().toString());
        this.mTaskdb.setDetail(this.mDetail.getText().toString());
        this.mTaskdb.setCategory(this.mCategorySelection.mValue);
        this.mTaskdb.setStatus(dStatus);
        this.mTaskdb.setPriority(this.mPriorityValue);
        this.mTaskdb.setPriorityCode(this.mPriorityCodeValue);
        this.mTaskdb.setStartdate(sStartDate);
        this.mTaskdb.setDuedate(sDueDate);
        this.mTaskdb.setStarttime(sStartTime);
        this.mTaskdb.setDuetime(sDueTime);
        this.mTaskdb.setTag(this._autoCompleteTask.getText().toString());
        this.mTaskdb.setHidden(null);
        if (this.isEditMode) {
            this.mTaskdb.set_id(this.mRowId.toString());
            updateStatus = this.mTaskdb.update();
        } else {
            createStatus = this.mTaskdb.create();
            if (createStatus > 0) {
                this.mRowId = Long.valueOf(createStatus);
            }
        }
        if (createStatus != -1 || updateStatus) {
            new notifyUser(getApplicationContext()).Notify(getString(R.string.saved));
            SetReminder sr = new SetReminder(getApplicationContext(), ReceiveAlarm.class, this.am);
            sr.setmReminderEnabled(this.mReminderEnabled);
            sr.setmReminderAlarm(this.mReminderAlarm);
            sr.setmKey(dBUserPreferences.KEY_ROWID);
            sr.setmKeyValue(this.mRowId.longValue());
            sr.setmSubject(dBTasks.D_TITLE);
            sr.setmSubjectValue(this.mTitle.getText().toString());
            sr.setmDetail(dBTasks.D_DETAIL);
            sr.setmDetailValue(this.mDetail.getText().toString());
            sr.setmDateFormat(dateTime.YearFormat);
            sr.setmDateSeparator(this.mUserDateSeparator);
            sr.setmAlarmDate(sDueDate);
            sr.setmTimeFormat(dateTime.MonthFormat);
            sr.setmAlarmTime(sDueTime);
            if (!this.mReminderEnabled || dStatus == "Y") {
                sr.Cancel();
                return;
            } else if (sDueDate != null) {
                sr.Set();
                return;
            } else {
                sr.Cancel();
                return;
            }
        }
        new notifyUser(getApplicationContext()).Notify(getString(R.string.error));
    }

    private void populateCategories() {
        this.mCategoryCursor = this.mCategorydb.FetchAllRows(false, dBCategory.D_CODE);
        this.mCategoryAdapter = new categorySimpleCursorAdapter(this, 17367048, this.mCategoryCursor, new String[]{dBCategory.D_CODE}, new int[]{16908308});
        this.mCategoryAdapter.setDropDownViewResource(17367049);
        this.mCategory.setAdapter(this.mCategoryAdapter);
        this.mCategory.setPromptId(R.string.selectcategory);
        this.mCategorySelection = new OnSpinnerSelected();
        this.mCategory.setOnItemSelectedListener(this.mCategorySelection);
    }

    private void populatePriorities() {
        this.mPriorityAdapter = ArrayAdapter.createFromResource(this, R.array.priorityarray, 17367048);
        this.mPriorityAdapter.setDropDownViewResource(17367049);
        this.mPriority.setAdapter(this.mPriorityAdapter);
        this.mPriority.setPromptId(R.string.selectpriority);
        this.mPriority.setOnItemSelectedListener(new OnSpinnerSelected() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                EditTask.this.mPriorityValue = parent.getItemAtPosition(pos).toString();
                EditTask.this.mPriorityCodeValue = Integer.toString(pos);
            }
        });
    }

    private void populateFields() {
        if (this.mRowId != null) {
            this.mTaskCursor = this.mTaskdb.FetchRow(this.mRowId.longValue());
            if (this.mTaskCursor.getCount() > 0) {
                this.mTitle.setText(this.mTaskCursor.getString(this.mTaskCursor.getColumnIndexOrThrow(dBTasks.D_TITLE)));
                this.mDetail.setText(this.mTaskCursor.getString(this.mTaskCursor.getColumnIndexOrThrow(dBTasks.D_DETAIL)));
                this.mTitleOriginal = this.mTaskCursor.getString(this.mTaskCursor.getColumnIndexOrThrow(dBTasks.D_TITLE));
                this.mDetailOriginal = this.mTaskCursor.getString(this.mTaskCursor.getColumnIndexOrThrow(dBTasks.D_DETAIL));
                this.mCategory.setSelection(this.mCategoryAdapter.getRowPos(dBCategory.D_CODE, this.mTaskCursor.getString(this.mTaskCursor.getColumnIndexOrThrow(dBTasks.D_CATEGORY))));
                this.mPriority.setSelection(Integer.valueOf(this.mTaskCursor.getString(this.mTaskCursor.getColumnIndexOrThrow(dBTasks.D_PRIORITY_CODE))).intValue());
                this.mStatus.setChecked("Y".equals(this.mTaskCursor.getString(this.mTaskCursor.getColumnIndex(dBTasks.D_STATUS))));
                String sStartDate = this.mDTTM.convertDate(this.mTaskCursor.getString(this.mTaskCursor.getColumnIndex(dBTasks.D_STARTDATE)), Character.valueOf(dateTime.YearFormat), Character.valueOf(this.mUserDateFormat));
                String sDueDate = this.mDTTM.convertDate(this.mTaskCursor.getString(this.mTaskCursor.getColumnIndex(dBTasks.D_DUEDATE)), Character.valueOf(dateTime.YearFormat), Character.valueOf(this.mUserDateFormat));
                String sStartTime = this.mDTTM.convertTime(this.mTaskCursor.getString(this.mTaskCursor.getColumnIndex(dBTasks.D_STARTTIME)), Character.valueOf(dateTime.MonthFormat), Character.valueOf(this.mUserTimeFormat));
                String sDueTime = this.mDTTM.convertTime(this.mTaskCursor.getString(this.mTaskCursor.getColumnIndex(dBTasks.D_DUETIME)), Character.valueOf(dateTime.MonthFormat), Character.valueOf(this.mUserTimeFormat));
                if (this.mTaskCursor.getString(this.mTaskCursor.getColumnIndex(dBTasks.D_STARTDATE)) != null) {
                    this.mStartDate.setText(sStartDate);
                }
                if (this.mTaskCursor.getString(this.mTaskCursor.getColumnIndex(dBTasks.D_STARTTIME)) != null) {
                    this.mStartTime.setText(sStartTime);
                }
                if (this.mTaskCursor.getString(this.mTaskCursor.getColumnIndex(dBTasks.D_DUEDATE)) != null) {
                    this.mDueDate.setText(sDueDate);
                }
                if (this.mTaskCursor.getString(this.mTaskCursor.getColumnIndex(dBTasks.D_DUETIME)) != null) {
                    this.mDueTime.setText(sDueTime);
                    return;
                }
                return;
            }
            new notifyUser(getApplicationContext()).Notify(getString(R.string.messagetaskdeleted));
            CloseResources();
        }
    }

    public void CloseResources() {
        if (this.mTaskCursor != null) {
            this.mTaskCursor.close();
        }
        this.mTaskdb.Close();
        if (this.mCategoryCursor != null) {
            this.mCategoryCursor.close();
        }
        this.mCategorydb.Close();
        finish();
    }

    private void updateDateDisplay(Button date) {
        StringBuilder sdt = new StringBuilder();
        switch (this.mUserDateFormat) {
            case 'D':
                sdt.append(this.mSelDay).append(this.mUserDateSeparator).append(this.mSelMonth + 1).append(this.mUserDateSeparator).append(this.mSelYear).append(" ");
                break;
            case 'M':
                sdt.append(this.mSelMonth + 1).append(this.mUserDateSeparator).append(this.mSelDay).append(this.mUserDateSeparator).append(this.mSelYear).append(" ");
                break;
            case 'Y':
                sdt.append(this.mSelYear).append(this.mUserDateSeparator).append(this.mSelMonth + 1).append(this.mUserDateSeparator).append(this.mSelDay).append(" ");
                break;
            default:
                sdt.append(this.mSelMonth + 1).append(this.mUserDateSeparator).append(this.mSelDay).append(this.mUserDateSeparator).append(this.mSelYear).append(" ");
                break;
        }
        date.setText(sdt);
    }

    private void updateTimeDisplay(Button time) {
        String ampm = "";
        switch (this.mUserTimeFormat) {
            case 'M':
                if (this.mSelHour == 24) {
                    this.mSelHour = 0;
                }
                time.setText(new StringBuilder().append(pad(this.mSelHour)).append(":").append(pad(this.mSelMinute)));
            case 'S':
                int hr = this.mSelHour;
                if (this.mSelHour > 12) {
                    hr = this.mSelHour - 12;
                }
                if (this.mSelHour == 24) {
                    hr = 0;
                }
                if (this.mSelHour < 12 || this.mSelHour >= 24) {
                    ampm = " AM";
                } else {
                    ampm = " PM";
                }
                time.setText(new StringBuilder().append(pad(hr)).append(":").append(pad(this.mSelMinute)).append(ampm));
            default:
        }
    }

    private static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        }
        return "0" + String.valueOf(c);
    }

    protected Dialog onCreateDialog(int id) {
        boolean z = true;
        Dialog datepickerdialog;
        OnTimeSetListener onTimeSetListener;
        int i;
        int i2;
        Dialog timepickerdialog;
        switch (id) {
            case Constants.START_DATE_DIALOG_ID /*9*/:
                if (this.mStartDate.getText().toString().equals(getString(R.string.selectdate))) {
                    this.mSelYear = this.mCurYear;
                    this.mSelMonth = this.mCurMonth;
                    this.mSelDay = this.mCurDay;
                } else {
                    this.mSelYear = this.mDTTM.getYear(this.mUserDateFormat, this.mStartDate.getText().toString());
                    this.mSelMonth = this.mDTTM.getMonth(this.mUserDateFormat, this.mStartDate.getText().toString()) - 1;
                    this.mSelDay = this.mDTTM.getDay(this.mUserDateFormat, this.mStartDate.getText().toString());
                    if (this.mSelYear == 0) {
                        this.mSelYear = this.mCurYear;
                        this.mSelMonth = this.mCurMonth;
                        this.mSelDay = this.mCurDay;
                    }
                }
                datepickerdialog = new DatePickerDialog(this, this.mStartDateSetListener, this.mSelYear, this.mSelMonth, this.mSelDay);
                datepickerdialog.setTitle(R.string.selectstartdate);
                return datepickerdialog;
            case Constants.DUE_DATE_DIALOG_ID /*10*/:
                if (this.mDueDate.getText().toString().equals(getString(R.string.selectdate))) {
                    this.mSelYear = this.mCurYear;
                    this.mSelMonth = this.mCurMonth;
                    this.mSelDay = this.mCurDay;
                } else {
                    this.mSelYear = this.mDTTM.getYear(this.mUserDateFormat, this.mDueDate.getText().toString());
                    this.mSelMonth = this.mDTTM.getMonth(this.mUserDateFormat, this.mDueDate.getText().toString()) - 1;
                    this.mSelDay = this.mDTTM.getDay(this.mUserDateFormat, this.mDueDate.getText().toString());
                    if (this.mSelYear == 0) {
                        this.mSelYear = this.mCurYear;
                        this.mSelMonth = this.mCurMonth;
                        this.mSelDay = this.mCurDay;
                    }
                }
                datepickerdialog = new DatePickerDialog(this, this.mDueDateSetListener, this.mSelYear, this.mSelMonth, this.mSelDay);
                datepickerdialog.setTitle(R.string.selectduedate);
                return datepickerdialog;
            case Constants.START_TIME_DIALOG_ID /*11*/:
                if (this.mStartTime.getText().toString().equals(getString(R.string.selecttime))) {
                    this.mSelHour = this.mCurHour;
                    this.mSelMinute = this.mCurMinute;
                } else {
                    this.mSelHour = this.mDTTM.getHour(this.mUserTimeFormat, this.mStartTime.getText().toString());
                    this.mSelMinute = this.mDTTM.getMinute(this.mUserTimeFormat, this.mStartTime.getText().toString());
                }
                onTimeSetListener = this.mStartTimeSetListener;
                i = this.mSelHour;
                i2 = this.mSelMinute;
                if (this.mUserTimeFormat != dateTime.MonthFormat) {
                    z = false;
                }
                timepickerdialog = new TimePickerDialog(this, onTimeSetListener, i, i2, z);
                timepickerdialog.setTitle(R.string.selectstarttime);
                return timepickerdialog;
            case Constants.DUE_TIME_DIALOG_ID /*12*/:
                if (this.mDueTime.getText().toString().equals(getString(R.string.selecttime))) {
                    this.mSelHour = this.mCurHour;
                    this.mSelMinute = this.mCurMinute;
                } else {
                    this.mSelHour = this.mDTTM.getHour(this.mUserTimeFormat, this.mDueTime.getText().toString());
                    this.mSelMinute = this.mDTTM.getMinute(this.mUserTimeFormat, this.mDueTime.getText().toString());
                }
                onTimeSetListener = this.mDueTimeSetListener;
                i = this.mSelHour;
                i2 = this.mSelMinute;
                if (this.mUserTimeFormat != dateTime.MonthFormat) {
                    z = false;
                }
                timepickerdialog = new TimePickerDialog(this, onTimeSetListener, i, i2, z);
                timepickerdialog.setTitle(R.string.selectduetime);
                return timepickerdialog;
            default:
                return null;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(1, 3, 0, R.string.menu_delete);
        menu.add(1, 8, 0, R.string.save);
        menu.add(0, 6, 1, R.string.menu_return);
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case Constants.DELETE_ID /*3*/:
                if (this.mTaskdb.delete(this.mRowId.longValue())) {
                    new notifyUser(getApplicationContext()).Notify(getString(R.string.deleted));
                    SetReminder sr = new SetReminder(getApplicationContext(), ReceiveAlarm.class, this.am);
                    sr.setmReminderEnabled(this.mReminderEnabled);
                    sr.setmReminderAlarm(this.mReminderAlarm);
                    sr.setmKey(dBUserPreferences.KEY_ROWID);
                    sr.setmKeyValue(this.mRowId.longValue());
                    sr.setmSubject(dBTasks.D_TITLE);
                    sr.setmSubjectValue(this.mTitle.getText().toString());
                    sr.Cancel();
                } else {
                    new notifyUser(getApplicationContext()).Notify(getString(R.string.error));
                }
                CloseResources();
                return true;
            case Constants.EXIT_ID /*6*/:
                new notifyUser(getApplicationContext()).Notify(getString(R.string.discarded));
                CloseResources();
                return true;
            case Constants.SAVE_ID /*8*/:
                setResult(-1);
                saveTask();
                CloseResources();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        new notifyUser(getApplicationContext()).Notify(getString(R.string.discarded));
        CloseResources();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case Constants.Title_Request_Code /*1000*/:
                    this.mTitle.setText((CharSequence) data.getStringArrayListExtra("android.speech.extra.RESULTS").get(0));
                case Constants.Detail_Request_Code /*2000*/:
                    this.mDetail.append(" " + ((String) data.getStringArrayListExtra("android.speech.extra.RESULTS").get(0)));
                case Constants.TTS_Code /*3000*/:
                    if (resultCode == 1) {
                        this.mTTS = new TextToSpeech(this, this);
                        this.mTTS.setLanguage(Locale.US);
                        return;
                    }
                    Intent installIntent = new Intent();
                    installIntent.setAction("android.speech.tts.engine.INSTALL_TTS_DATA");
                    startActivity(installIntent);
                default:
            }
        }
    }

    public void onInit(int status) {
    }
}
