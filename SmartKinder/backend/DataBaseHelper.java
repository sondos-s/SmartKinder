package androidArmy.SmartKinder.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydatabase.db";


    // kid table
    public static final String TABLE_KIDS_NAME = "kids";
    public static final String KID_COLUMN_ID = "id";
    public static final String KID_COLUMN_NAME = "name";
    public static final String KID_COLUMN_MOTHER = "motherId";
    public static final String KID_COLUMN_FATHER = "fatherId";
    public static final String KID_COLUMN_BIRTHDATE = "birthdate";
    public static final String KID_COLUMN_NEEDS = "needs";

    // Attendance table
    public static final String TABLE_ATTENDANCE_NAME = "attendance";

    public static final String ATTENDANCE_COLUMN_DATE = "date";

    public static final String ATTENDANCE_COLUMN_KIDID = "kidId";

    public static final String ATTENDANCE_COLUMN_ATTEND = "isAttended";


    private static final String[] TABLE_KID_COLUMNS = {KID_COLUMN_ID, KID_COLUMN_NAME,
            KID_COLUMN_FATHER, KID_COLUMN_MOTHER, KID_COLUMN_BIRTHDATE};
    // Teacher table
    public static final String TABLE_TEACHER_NAME = "teacher";
    public static final String TEACHER_COLUMN_ID = "teacherid";
    public static final String TEACHER_COLUMN_FIRSTNAME = "tfirstname";
    public static final String TEACHER_COLUMN_LASTNAME = "tlastname";

    // Progress table
    public static final String TABLE_PROGRESS_NAME = "progress";
    public static final String PROGRESS_COLUMN_KIDNAME = "name"; // Updated column name
    public static final String PROGRESS_COLUMN_TEACHERID = "teacherId";
    public static final String PROGRESS_COLUMN_PROGRESS = "kidProgress";
    public static final String PROGRESS_COLUMN_PROGRESSDATE = "progressDate";
    // Meal table
    public static final String TABLE_MEAL_NAME = "meal";
    public static final String MEAL_COLUMN_ID = "id";
    public static final String MEAL_COLUMN_MEALNAME = "mealName";
    public static final String MEAL_COLUMN_TYPE = "type";
    // ChildMeal table
    public static final String TABLE_CHILDMEAL_NAME = "childmeal";
    public static final String CHILDMEAL_COLUMN_KIDNAME = "kidname";
    public static final String CHILDMEAL_COLUMN_MEALID = "mealid";
    public static final String CHILDMEAL_COLUMN_MEALDATE = "mealdate";


    // Allergy table
    public static final String TABLE_ALLERGY_NAME = "allergy";
    public static final String ALLERGY_COLUMN_NAME = "allergyname";

    // childAllergy table
    public static final String TABLE_CHILDS_ALLERGY_NAME = "childsAllergy";
    public static final String CHILD_ALLERGY_COLUMN_KID_ID = "kidId";
    public static final String CHILD_ALLERGY_COLUMN_ALLERGY_NAME = "allergyName";

    // trackHour table
    public static final String TABLE_TRACKHOUR_NAME = "trackHour";
    public static final String TRACKHOUR_COLUMN_DATE = "date";
    public static final String TRACKHOUR_COLUMN_TEACHER_ID = "teacherId";
    public static final String TRACKHOUR_COLUMN_START_TIME = "startTime";
    public static final String TRACKHOUR_COLUMN_END_TIME = "endTime";


    // parent table
    public static final String TABLE_PARENT_NAME = "parent";
    public static final String PARENT_COLUMN_ID = "id";
    public static final String PARENT_COLUMN_FULLNAME = "fullName";
    ///////////////>>>>>>>>>>>>start

    // payment table
    public static final String TABLE_PAYMENT_NAME = "payment";
    public static final String PAYMENT_COLUMN_KIDNAME = "kidname";
    public static final String PAYMENT_COLUMN_YEAR = "year";
    public static final String PAYMENT_COLUMN_FIRST_PAYMENT = "firstpayment";
    public static final String PAYMENT_COLUMN_SECOND_PAYMENT = "secondpayment";
    public static final String PAYMENT_COLUMN_THIRD_PAYMENT = "thirdpayment";
////////////>>>>>>>>>end

    // messages table
    public static final String TABLE_MESSAGE_NAME = "meesages";
    public static final String MESSAGE_COLUMN_NUM = "messageId";
    public static final String MESSAGE_COLUMN_DATE = "date";
    public static final String MESSAGE_COLUMN_CONTENT = "content";
    public static final String MESSAGE_COLUMN_SENDERID = "senderId";
    public static final String MESSAGE_COLUMN_RECEIVERID = "receiverId";


    private static DataBaseHelper instance;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context.getApplicationContext());
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // SQL statement to create kid table
            String CREATE_KID_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_KIDS_NAME + " ( "
                    + KID_COLUMN_ID + " INTEGER PRIMARY KEY , "
                    + KID_COLUMN_NAME + " TEXT, "
                    + KID_COLUMN_MOTHER + " TEXT, "
                    + KID_COLUMN_FATHER + " TEXT, "
                    + KID_COLUMN_BIRTHDATE + " DATE, "
                    + KID_COLUMN_NEEDS + " TEXT)";
            db.execSQL(CREATE_KID_TABLE);

            //SQL statement to create attendance table
            // SQL statement to create kid table
            String CREATE_ATTENDANCE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDANCE_NAME + " ( "
                    + ATTENDANCE_COLUMN_DATE + " DATE, "
                    + ATTENDANCE_COLUMN_KIDID + " INTEGER, "
                    + ATTENDANCE_COLUMN_ATTEND + " INTEGER, "
                    + "PRIMARY KEY (" + ATTENDANCE_COLUMN_DATE + ", " + ATTENDANCE_COLUMN_KIDID + "))";
            db.execSQL(CREATE_ATTENDANCE_TABLE);

            //if (!isTableExist(TABLE_FOLDER_NAME, db)) {
            // SQL statement to create item table

            //}

            // SQL statement to create teacher table
            String CREATE_TEACHER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TEACHER_NAME + " ( "
                    + TEACHER_COLUMN_ID + " INTEGER PRIMARY KEY , "
                    + TEACHER_COLUMN_FIRSTNAME + " TEXT, "
                    + TEACHER_COLUMN_LASTNAME + " TEXT)";
            db.execSQL(CREATE_TEACHER_TABLE);


            // SQL statement to create progress table
            String CREATE_PROGRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROGRESS_NAME + " ( "
                    + PROGRESS_COLUMN_KIDNAME + " TEXT, " // Updated column name
                    + PROGRESS_COLUMN_TEACHERID + " INTEGER, "
                    + PROGRESS_COLUMN_PROGRESS + " TEXT, "
                    + PROGRESS_COLUMN_PROGRESSDATE + " DATE, "
                    + "PRIMARY KEY (" + PROGRESS_COLUMN_KIDNAME + ", " + PROGRESS_COLUMN_TEACHERID + ","+PROGRESS_COLUMN_PROGRESSDATE+"))";
            db.execSQL(CREATE_PROGRESS_TABLE);

            // SQL statement to create meal table
            String CREATE_MEAL_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEAL_NAME + " ( "
                    + MEAL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MEAL_COLUMN_MEALNAME + " TEXT, "
                    + MEAL_COLUMN_TYPE + " TEXT)";
            db.execSQL(CREATE_MEAL_TABLE);
            // SQL statement to create childmeal table
            String CREATE_CHILDMEAL_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CHILDMEAL_NAME + " ( "
                    + CHILDMEAL_COLUMN_KIDNAME + " TEXT, "
                    + CHILDMEAL_COLUMN_MEALID + " INTEGER, "
                    + CHILDMEAL_COLUMN_MEALDATE + " DATE, "
                    + "PRIMARY KEY (" + CHILDMEAL_COLUMN_KIDNAME + ", " + CHILDMEAL_COLUMN_MEALID + ", " + CHILDMEAL_COLUMN_MEALDATE + "))";
            db.execSQL(CREATE_CHILDMEAL_TABLE);
            // SQL statement to create allergy table
            String CREATE_ALLERGY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ALLERGY_NAME + " ( "
                    + ALLERGY_COLUMN_NAME + " TEXT PRIMARY KEY)";
            db.execSQL(CREATE_ALLERGY_TABLE);

            // SQL statement to create childAllergy table
            String CREATE_CHILD_ALLERGY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CHILDS_ALLERGY_NAME + " ( "
                    + CHILD_ALLERGY_COLUMN_KID_ID + " INTEGER, "
                    + CHILD_ALLERGY_COLUMN_ALLERGY_NAME + " TEXT, "
                    + "PRIMARY KEY (" + CHILD_ALLERGY_COLUMN_KID_ID + ", " + CHILD_ALLERGY_COLUMN_ALLERGY_NAME + "))";
            db.execSQL(CREATE_CHILD_ALLERGY_TABLE);

            // SQL statement to create trackHours table
            String CREATE_TRACKHOUR_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRACKHOUR_NAME + " ( "
                    + TRACKHOUR_COLUMN_DATE + " DATE, "
                    + TRACKHOUR_COLUMN_TEACHER_ID + " INTEGER, "
                    + TRACKHOUR_COLUMN_START_TIME + " TIME, "
                    + TRACKHOUR_COLUMN_END_TIME + " TIME, "
                    + "PRIMARY KEY (" + TRACKHOUR_COLUMN_DATE + ", " + TRACKHOUR_COLUMN_TEACHER_ID + "))";
            db.execSQL(CREATE_TRACKHOUR_TABLE);

            // SQL statement to create parent table
            String CREATE_PARENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PARENT_NAME + " ( "
                    + PARENT_COLUMN_ID + " INTEGER PRIMARY KEY, "
                    + PARENT_COLUMN_FULLNAME + " TEXT)";
            db.execSQL(CREATE_PARENT_TABLE);

        /////////////>>>>>>>>>>>start
            // Remove PAYMENT_COLUMN_AMOUNT from the SQL statement
            String CREATE_PAYMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYMENT_NAME + " ( "
                    + PAYMENT_COLUMN_KIDNAME + " TEXT, "
                    + PAYMENT_COLUMN_YEAR + " TEXT, "
                    + PAYMENT_COLUMN_FIRST_PAYMENT + " INTEGER, "
                    + PAYMENT_COLUMN_SECOND_PAYMENT + " INTEGER, "
                    + PAYMENT_COLUMN_THIRD_PAYMENT + " INTEGER, "
                    + "PRIMARY KEY (" + PAYMENT_COLUMN_KIDNAME + ", " + PAYMENT_COLUMN_YEAR + "))";
            db.execSQL(CREATE_PAYMENT_TABLE);
//////////////>>>>>>>>>>>end


            // SQL statement to create messages table
            String CREATE_MESSAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGE_NAME + " ( "
                    + MESSAGE_COLUMN_NUM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MESSAGE_COLUMN_DATE + " DATE, "
                    + MESSAGE_COLUMN_CONTENT + " TEXT, "
                    + MESSAGE_COLUMN_SENDERID + " INTEGER, "
                    + MESSAGE_COLUMN_RECEIVERID + " INTEGER)";
            db.execSQL(CREATE_MESSAGES_TABLE);



        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // drop item table if already exists
            //db.execSQL("DROP TABLE IF EXISTS items");
            //db.execSQL("DROP TABLE IF EXISTS folders");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KIDS_NAME);
            onCreate(db);

            // drop childAllergy table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDS_ALLERGY_NAME);
            onCreate(db);
            //////////>>>>>>>>>>start
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_NAME);
            onCreate(db);
            ///////////>>>>>>>end
        } catch (Throwable t) {
            t.printStackTrace();
        }
        //onCreate(db);
    }



}

