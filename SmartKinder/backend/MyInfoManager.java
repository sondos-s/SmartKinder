package androidArmy.SmartKinder.backend;

import static androidArmy.SmartKinder.backend.DataBaseHelper.ALLERGY_COLUMN_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.ATTENDANCE_COLUMN_ATTEND;
import static androidArmy.SmartKinder.backend.DataBaseHelper.ATTENDANCE_COLUMN_DATE;
import static androidArmy.SmartKinder.backend.DataBaseHelper.ATTENDANCE_COLUMN_KIDID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.CHILDMEAL_COLUMN_KIDNAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.CHILDMEAL_COLUMN_MEALDATE;
import static androidArmy.SmartKinder.backend.DataBaseHelper.CHILDMEAL_COLUMN_MEALID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.CHILD_ALLERGY_COLUMN_ALLERGY_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.CHILD_ALLERGY_COLUMN_KID_ID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.KID_COLUMN_BIRTHDATE;
import static androidArmy.SmartKinder.backend.DataBaseHelper.KID_COLUMN_FATHER;
import static androidArmy.SmartKinder.backend.DataBaseHelper.KID_COLUMN_ID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.KID_COLUMN_MOTHER;
import static androidArmy.SmartKinder.backend.DataBaseHelper.KID_COLUMN_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.KID_COLUMN_NEEDS;
import static androidArmy.SmartKinder.backend.DataBaseHelper.MEAL_COLUMN_ID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.MEAL_COLUMN_MEALNAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.MEAL_COLUMN_TYPE;
import static androidArmy.SmartKinder.backend.DataBaseHelper.MESSAGE_COLUMN_CONTENT;
import static androidArmy.SmartKinder.backend.DataBaseHelper.MESSAGE_COLUMN_DATE;
import static androidArmy.SmartKinder.backend.DataBaseHelper.MESSAGE_COLUMN_RECEIVERID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.MESSAGE_COLUMN_SENDERID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PARENT_COLUMN_FULLNAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PARENT_COLUMN_ID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PAYMENT_COLUMN_FIRST_PAYMENT;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PAYMENT_COLUMN_KIDNAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PAYMENT_COLUMN_SECOND_PAYMENT;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PAYMENT_COLUMN_THIRD_PAYMENT;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PAYMENT_COLUMN_YEAR;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PROGRESS_COLUMN_KIDNAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PROGRESS_COLUMN_PROGRESS;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PROGRESS_COLUMN_PROGRESSDATE;
import static androidArmy.SmartKinder.backend.DataBaseHelper.PROGRESS_COLUMN_TEACHERID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_ALLERGY_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_ATTENDANCE_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_CHILDMEAL_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_CHILDS_ALLERGY_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_KIDS_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_MEAL_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_MESSAGE_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_PARENT_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_PAYMENT_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_PROGRESS_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_TEACHER_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TABLE_TRACKHOUR_NAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TEACHER_COLUMN_FIRSTNAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TEACHER_COLUMN_ID;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TEACHER_COLUMN_LASTNAME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TRACKHOUR_COLUMN_DATE;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TRACKHOUR_COLUMN_END_TIME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TRACKHOUR_COLUMN_START_TIME;
import static androidArmy.SmartKinder.backend.DataBaseHelper.TRACKHOUR_COLUMN_TEACHER_ID;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyInfoManager {

    private static User user;
    private static MyInfoManager instance = null;
    private Context context = null;
    private DataBaseHelper databaseHelper = null;

    private MyInfoManager(Context context) {
        databaseHelper = DataBaseHelper.getInstance(context);
    }

    public static synchronized MyInfoManager getInstance(Context context) {
        if (instance == null) {
            instance = new MyInfoManager(context.getApplicationContext());
        }
        return instance;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }



    public androidArmy.SmartKinder.backend.User getUser() {
        return user;
    }

    public void addInfoKid(androidArmy.SmartKinder.backend.InfoKid infoKid) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KID_COLUMN_ID, infoKid.getId());
        values.put(KID_COLUMN_NAME, infoKid.getName());
        values.put(DataBaseHelper.KID_COLUMN_MOTHER, infoKid.getMotherId());
        values.put(DataBaseHelper.KID_COLUMN_FATHER, infoKid.getFatherId());
        values.put(DataBaseHelper.KID_COLUMN_BIRTHDATE, infoKid.getBirthdate().getTime());
        values.put(DataBaseHelper.KID_COLUMN_NEEDS, infoKid.getNeeds());

        db.insert(TABLE_KIDS_NAME, null, values);
        db.close();
    }

    public List<String> getAllKidsNames() {
        List<String> kidsNames = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("kids", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            kidsNames.add(name);
        }
        cursor.close();
        db.close();
        return kidsNames;
    }

    ////////////////get children that admin insearted
    public List<InfoKid> getAllKids() {
        List<InfoKid> kids = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_KIDS_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(KID_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(KID_COLUMN_NAME));
            String motherId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.KID_COLUMN_MOTHER));
            String fatherId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.KID_COLUMN_FATHER));
            long birthdateTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DataBaseHelper.KID_COLUMN_BIRTHDATE));
            String needs = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.KID_COLUMN_NEEDS));

//            // Parse the birthdate string to a Date object
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            Date birthdate;
            // Convert the birthdate timestamp to a Date object
            Date birthdate = new Date(birthdateTimestamp);

            // Format the birthdate as a string
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String birthdateStr = sdf.format(birthdate);
            try {
                birthdate = sdf.parse(birthdateStr);
            } catch (ParseException e) {
                birthdate = new Date(); // Default to current date if parsing fails
            }

            InfoKid infoKid = new InfoKid(id, name, motherId, fatherId, birthdate, needs);
            kids.add(infoKid);
        }
        cursor.close();
        db.close();
        return kids;
    }

    public List<Meal> getAllMeals() {
        List<Meal> meals = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEAL_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MEAL_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MEAL_COLUMN_MEALNAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(MEAL_COLUMN_TYPE));


            Meal meal = new Meal(id, name, type);
            meals.add(meal);
        }
        cursor.close();
        db.close();
        return meals;
    }
/////>>>>>>>>end

    public boolean addTeacher(androidArmy.SmartKinder.backend.Teacher teacher) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TEACHER_COLUMN_ID, teacher.getTeacherId());
        values.put(DataBaseHelper.TEACHER_COLUMN_FIRSTNAME, teacher.getFirstName());
        values.put(DataBaseHelper.TEACHER_COLUMN_LASTNAME, teacher.getLastName());

        long row = db.insert(DataBaseHelper.TABLE_TEACHER_NAME, null, values);
        db.close();
        if (row == -1)
            return false;
        return true;
    }

    public boolean addProgress(String kidName, int teacherId, String kidProgress, String progressDate) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROGRESS_COLUMN_KIDNAME, kidName);
        values.put(PROGRESS_COLUMN_TEACHERID, teacherId);
        values.put(PROGRESS_COLUMN_PROGRESS, kidProgress);
        values.put(PROGRESS_COLUMN_PROGRESSDATE, progressDate);

        long row = db.insert(TABLE_PROGRESS_NAME, null, values);
        db.close();
        if(row == -1)
            return false;
        return true;
    }

    public boolean addMeal(String mealName, String type) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.MEAL_COLUMN_MEALNAME, mealName);
        values.put(DataBaseHelper.MEAL_COLUMN_TYPE, type);

        long row = db.insert(DataBaseHelper.TABLE_MEAL_NAME, null, values);
        db.close();

        if (row == -1) {
            return false;
        }
        return true;
    }

    public void addChildMeal(String kidName, int mealId, String mealDate) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CHILDMEAL_COLUMN_KIDNAME, kidName);
        values.put(CHILDMEAL_COLUMN_MEALID, mealId);
        values.put(CHILDMEAL_COLUMN_MEALDATE, mealDate);

        db.insert(TABLE_CHILDMEAL_NAME, null, values);
        db.close();
    }

    public boolean addAllergy(String allergyName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.ALLERGY_COLUMN_NAME, allergyName);

        long row = db.insert(DataBaseHelper.TABLE_ALLERGY_NAME, null, values);
        db.close();
        if (row == -1)
            return false;
        return true;
    }

    public List<Allergies> getAllAllergies() {
        List<Allergies> allergies = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ALLERGY_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndexOrThrow(ALLERGY_COLUMN_NAME));


            Allergies allergie = new Allergies(name);
            allergies.add(allergie);
        }
        cursor.close();
        db.close();
        return allergies;
    }

    public void deleteAllergies() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String tableName = DataBaseHelper.TABLE_ALLERGY_NAME;

        db.delete(tableName, null, null);
        db.close();
    }



    public boolean insertAttendance(Date date, String kidName, boolean isAttend) {
        // Get the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(date);

        // Fetch the kid's ID from your data source based on the name (replace with your implementation)
        int kidId = fetchKidIdByName(kidName);

        if (kidId == 0) {
            Log.d("Attendance", "Failed to find kid id");
            return false;
        } else {
            // Insert the attendance record using your data source (replace with your implementation)
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ATTENDANCE_COLUMN_DATE, currentDate);
            values.put(ATTENDANCE_COLUMN_KIDID, kidId);
            values.put(ATTENDANCE_COLUMN_ATTEND, isAttend);

            long rowId = db.insert(TABLE_ATTENDANCE_NAME, null, values);

            if (rowId == -1) {
                // Insertion failed, try updating the existing record
                String whereClause = ATTENDANCE_COLUMN_KIDID + " = ? AND " + ATTENDANCE_COLUMN_DATE + " = ?";
                String[] whereArgs = new String[]{String.valueOf(kidId), currentDate};

                int updatedRows = db.update(TABLE_ATTENDANCE_NAME, values, whereClause, whereArgs);
                db.close();

                if (updatedRows > 0) {
                    // Existing record updated successfully
                    return true;
                } else {
                    // Existing record update failed as well
                    return false;
                }
            } else {
                // New record inserted successfully
                db.close();
                return true;
            }
        }
    }


    private int fetchKidIdByName(String kidName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("kids", null, null, null, null, null, null);

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

                if (name.equals(kidName)) {
                    return id;
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        return 0; // Return 0 if the kid's ID is not found
    }

    public void deleteAllAttendance() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String tableName = "attendance";

        db.delete(tableName, null, null);
        db.close();

    }

    public void deleteAllKids() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String tableName = "kids";

        db.delete(tableName, null, null);
        db.close();

    }


    public boolean insertTrackHour(Date date, int teacherId, Time startTime, Time endTime) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRACKHOUR_COLUMN_DATE, formatDateToString(date));
        values.put(TRACKHOUR_COLUMN_TEACHER_ID, teacherId);
        values.put(TRACKHOUR_COLUMN_START_TIME, formatTimeToString(startTime));
        values.put(TRACKHOUR_COLUMN_END_TIME, formatTimeToString(endTime));


        long rowId = db.insert(TABLE_TRACKHOUR_NAME, null, values);
        db.close();

        return rowId != -1;
    }

    // Helper method to convert Date to string
    private String formatDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    // Helper method to convert Time to string
    private String formatTimeToString(Time time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return timeFormat.format(time);
    }

    public boolean insertParent(Integer id, String fullName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PARENT_COLUMN_FULLNAME, fullName);
        values.put(PARENT_COLUMN_ID, id);

        long rowId = db.insert(TABLE_PARENT_NAME, null, values);
        db.close();

        return rowId != -1;
    }


    public boolean insertMessages(String date, String content, int senderId, int receiverId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MESSAGE_COLUMN_DATE, date);
        values.put(MESSAGE_COLUMN_CONTENT, content);
        values.put(MESSAGE_COLUMN_SENDERID, senderId);
        values.put(MESSAGE_COLUMN_RECEIVERID, receiverId);

        long rowId = db.insert(TABLE_MESSAGE_NAME, null, values);
        db.close();

        return rowId != -1;
    }

    public ArrayList<String> getPresentKidsNames(Date selectedDate) {
        ArrayList<String> presentKidsNames = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate);

        String query = "SELECT kids." + KID_COLUMN_NAME +
                " FROM " + TABLE_KIDS_NAME + " AS kids" +
                " INNER JOIN " + TABLE_ATTENDANCE_NAME + " AS attendance" +
                " ON kids." + KID_COLUMN_ID + " = attendance." + ATTENDANCE_COLUMN_KIDID +
                " WHERE attendance." + ATTENDANCE_COLUMN_DATE + " = ?" +
                " AND attendance." + ATTENDANCE_COLUMN_ATTEND + " = 1";

        Cursor cursor = db.rawQuery(query, new String[]{formattedDate});

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(KID_COLUMN_NAME));
            presentKidsNames.add(name);
        }

        cursor.close();
        db.close();

        return presentKidsNames;
    }

    public ArrayList<String> getAbsentKidsNames(Date selectedDate) {
        ArrayList<String> absentKidsNames = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate);

        String query = "SELECT kids." + KID_COLUMN_NAME +
                " FROM " + TABLE_KIDS_NAME + " AS kids" +
                " INNER JOIN " + TABLE_ATTENDANCE_NAME + " AS attendance" +
                " ON kids." + KID_COLUMN_ID + " = attendance." + ATTENDANCE_COLUMN_KIDID +
                " WHERE attendance." + ATTENDANCE_COLUMN_DATE + " = ?" +
                " AND attendance." + ATTENDANCE_COLUMN_ATTEND + " = 0";

        Cursor cursor = db.rawQuery(query, new String[]{formattedDate});

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(KID_COLUMN_NAME));
            absentKidsNames.add(name);
        }

        cursor.close();
        db.close();

        return absentKidsNames;
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TEACHER_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TEACHER_COLUMN_ID));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(TEACHER_COLUMN_FIRSTNAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(TEACHER_COLUMN_LASTNAME));


            Teacher teacher = new Teacher(id, firstName, lastName);
            teachers.add(teacher);
        }
        cursor.close();
        db.close();
        return teachers;
    }

    ////////>>>>>>>>>>>>>>>>start
    //// add payment
    public void insertPayment(String kidName, String year, int firstPayment, int secondPayment, int thirdPayment) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PAYMENT_COLUMN_KIDNAME, kidName);
        values.put(PAYMENT_COLUMN_YEAR, year);
        values.put(PAYMENT_COLUMN_FIRST_PAYMENT, firstPayment);
        values.put(PAYMENT_COLUMN_SECOND_PAYMENT, secondPayment);
        values.put(PAYMENT_COLUMN_THIRD_PAYMENT, thirdPayment);

        db.insert(TABLE_PAYMENT_NAME, null, values);
        db.close();
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_PAYMENT_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String kidName = cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_KIDNAME));
                @SuppressLint("Range") String year = cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_YEAR));
                @SuppressLint("Range") int firstPayment = cursor.getInt(cursor.getColumnIndex(PAYMENT_COLUMN_FIRST_PAYMENT));
                @SuppressLint("Range") int secondPayment = cursor.getInt(cursor.getColumnIndex(PAYMENT_COLUMN_SECOND_PAYMENT));
                @SuppressLint("Range") int thirdPayment = cursor.getInt(cursor.getColumnIndex(PAYMENT_COLUMN_THIRD_PAYMENT));

                Payment payment = new Payment(kidName, year, firstPayment, secondPayment, thirdPayment);
                payments.add(payment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return payments;
    }
///////////>>>>>>>>>>>>End

    public Boolean deleteTeacher(Teacher teacher) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Define the WHERE clause and arguments for the deletion
        String whereClause = "teacherid = ?";
        String[] whereArgs = new String[]{String.valueOf(teacher.getTeacherId())};

        // Perform the deletion
        int deletedRows = db.delete(TABLE_TEACHER_NAME, whereClause, whereArgs);
        db.close();

        if (deletedRows > 0) {
            // Deletion successful
            Log.d("InfoManager", "Teacher deleted successfully");
            return true;
        } else {
            // Deletion failed
            Log.d("InfoManager", "Failed to delete teacher");
            return false;
        }
    }

    public Boolean deleteMeal(Meal meal) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Define the WHERE clause and arguments for the deletion
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(meal.getId())};

        // Perform the deletion
        int deletedRows = db.delete(TABLE_MEAL_NAME, whereClause, whereArgs);
        db.close();

        if (deletedRows > 0) {
            // Deletion successful
            Log.d("InfoManager", "Meal deleted successfully");
            return true;
        } else {
            // Deletion failed
            Log.d("InfoManager", "Failed to delete meal");
            return false;
        }
    }

    public Boolean deleteChild(InfoKid kid) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Define the WHERE clause and arguments for the deletion
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(kid.getId())};

        // Perform the deletion
        int deletedRows = db.delete(TABLE_KIDS_NAME, whereClause, whereArgs);
        db.close();

        if (deletedRows > 0) {
            // Deletion successful
            Log.d("InfoManager", "Kid deleted successfully");
            return true;
        } else {
            // Deletion failed
            Log.d("InfoManager", "Failed to delete kid");
            return false;
        }
    }

    public Boolean deleteAllergy(Allergies allergy) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Define the WHERE clause and arguments for the deletion
        String whereClause = "allergyname = ?";
        String[] whereArgs = new String[]{String.valueOf(allergy.getAllergyName())};

        // Perform the deletion
        int deletedRows = db.delete(TABLE_ALLERGY_NAME, whereClause, whereArgs);
        db.close();

        if (deletedRows > 0) {
            // Deletion successful
            Log.d("InfoManager", "Allergy deleted successfully");
            return true;
        } else {
            // Deletion failed
            Log.d("InfoManager", "Failed to delete Allergy");
            return false;
        }
    }

    public boolean editMeal(Meal meal, String name, String type) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create ContentValues object with the updated values
        ContentValues values = new ContentValues();
        values.put(MEAL_COLUMN_MEALNAME, name);
        values.put(MEAL_COLUMN_TYPE, type);
        // Add more columns as needed

        // Define the WHERE clause and arguments for the update
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(meal.getId())};

        // Perform the update
        int updatedRows = db.update(TABLE_MEAL_NAME, values, whereClause, whereArgs);
        db.close();

        if (updatedRows > 0) {
            // Update successful
            Log.d("InfoManager", "Meal updated successfully");
            return true;
        } else {
            // Update failed
            Log.d("InfoManager", "Failed to update meal");
            return false;
        }
    }

    public boolean editKid(InfoKid kid) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create ContentValues object with the updated values
        ContentValues values = new ContentValues();
        values.put(KID_COLUMN_NAME, kid.getName());
        values.put(KID_COLUMN_MOTHER, kid.getMotherId());
        values.put(KID_COLUMN_FATHER, kid.getFatherId());
        values.put(KID_COLUMN_BIRTHDATE, kid.getBirthdate().getTime());
        values.put(KID_COLUMN_NEEDS, kid.getNeeds());
        // Add more columns as needed

        // Define the WHERE clause and arguments for the update
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(kid.getId())};

        // Perform the update
        int updatedRows = db.update(TABLE_KIDS_NAME, values, whereClause, whereArgs);
        db.close();

        if (updatedRows > 0) {
            // Update successful
            Log.d("InfoManager", "Kid updated successfully");
            return true;
        } else {
            // Update failed
            Log.d("InfoManager", "Failed to update kid");
            return false;
        }
    }

    public boolean editTeacher(Teacher teacher) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create ContentValues object with the updated values
        ContentValues values = new ContentValues();
        values.put(TEACHER_COLUMN_FIRSTNAME, teacher.getFirstName());
        values.put(TEACHER_COLUMN_LASTNAME, teacher.getLastName());
        // Add more columns as needed

        // Define the WHERE clause and arguments for the update
        String whereClause = "teacherid = ?";
        String[] whereArgs = new String[]{String.valueOf(teacher.getTeacherId())};

        // Perform the update
        int updatedRows = db.update(TABLE_TEACHER_NAME, values, whereClause, whereArgs);
        db.close();

        if (updatedRows > 0) {
            // Update successful
            Log.d("InfoManager", "Teacher updated successfully");
            return true;
        } else {
            // Update failed
            Log.d("InfoManager", "Failed to update Teacher");
            return false;
        }
    }

    public boolean editAllergy(Allergies allergy, String allergyName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create ContentValues object with the updated values
        ContentValues values = new ContentValues();
        values.put(ALLERGY_COLUMN_NAME, allergyName);
        // Add more columns as needed

        // Define the WHERE clause and arguments for the update
        String whereClause = "allergyname = ?";
        String[] whereArgs = new String[]{String.valueOf(allergy.getAllergyName())};

        // Perform the update
        int updatedRows = db.update(TABLE_ALLERGY_NAME, values, whereClause, whereArgs);
        db.close();

        if (updatedRows > 0) {
            // Update successful
            Log.d("InfoManager", "Allergy updated successfully");
            return true;
        } else {
            // Update failed
            Log.d("InfoManager", "Failed to update allergy");
            return false;
        }
    }

    //>>>>>>>>>>>>start
    //>>>>>>>>>>>>start
    public List<Meal> getLunchMeals() {
        List<Meal> lunchMeals = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = MEAL_COLUMN_TYPE + " = ?";
        String[] selectionArgs = {"Lunch"};
        Cursor cursor = db.query(TABLE_MEAL_NAME, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MEAL_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MEAL_COLUMN_MEALNAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(MEAL_COLUMN_TYPE));

            Meal meal = new Meal(id, name, type);
            lunchMeals.add(meal);
        }
        cursor.close();
        db.close();
        return lunchMeals;

    }

    public List<Meal> getBreakfastMeals() {
        List<Meal> breakfastMeals = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = MEAL_COLUMN_TYPE + " = ?";
        String[] selectionArgs = {"Breakfast"};
        Cursor cursor = db.query(TABLE_MEAL_NAME, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MEAL_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MEAL_COLUMN_MEALNAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(MEAL_COLUMN_TYPE));

            Meal meal = new Meal(id, name, type);
            breakfastMeals.add(meal);
        }
        cursor.close();
        db.close();
        return breakfastMeals;
    }

    //getAllergies names
    public List<String> getAllAllergiesNames() {
        List<String> allergiesNames = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ALLERGY_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ALLERGY_COLUMN_NAME));
            allergiesNames.add(name);
        }
        cursor.close();
        db.close();
        return allergiesNames;
    }

    public boolean updateKidNeed(Integer kidId, String needs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create ContentValues object with the updated values
        ContentValues values = new ContentValues();
        values.put(KID_COLUMN_NEEDS, needs);
        // Add more columns as needed

        // Define the WHERE clause and arguments for the update
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(kidId)};

        // Perform the update
        int updatedRows = db.update(TABLE_KIDS_NAME, values, whereClause, whereArgs);
        db.close();

        if (updatedRows > 0) {
            // Update successful
            Log.d("InfoManager", "Needs if the kid updated successfully");
            return true;
        } else {
            // Update failed
            Log.d("InfoManager", "Failed to update kid's needs");
            return false;
        }
    }

    public boolean updateKidAllergies(Integer kidId, List<String> needs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Delete all records for the given kidId
        String deleteQuery = "DELETE FROM childsAllergy WHERE kidId = ?";
        db.execSQL(deleteQuery, new String[]{String.valueOf(kidId)});

        // Define the WHERE clause and arguments for the update
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(kidId)};

        // Insert new records for each allergy
        ContentValues values = new ContentValues();
        values.put(CHILD_ALLERGY_COLUMN_KID_ID, kidId);
        for (String allergy : needs) {
            values.put(CHILD_ALLERGY_COLUMN_ALLERGY_NAME, allergy);
            db.insert(TABLE_CHILDS_ALLERGY_NAME, null, values);
        }

        db.close();

        // Return true if the update is successful
        return true;
    }

    public List<String> getKidAllergies(Integer kidId) {
        List<String> allergiesNames = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {CHILD_ALLERGY_COLUMN_ALLERGY_NAME};
        String selection = "kidId = ?";
        String[] selectionArgs = {String.valueOf(kidId)};

        Cursor cursor = db.query(TABLE_CHILDS_ALLERGY_NAME, projection, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CHILD_ALLERGY_COLUMN_ALLERGY_NAME));
            allergiesNames.add(name);
        }

        cursor.close();
        db.close();

        return allergiesNames;
    }

    public String getKidNeeds(Integer kidId){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {KID_COLUMN_NEEDS};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(kidId)};

        Cursor cursor = db.query(TABLE_KIDS_NAME, projection, selection, selectionArgs, null, null, null);

        String kidNeeds = null;

        if (cursor.moveToFirst()) {
            kidNeeds = cursor.getString(cursor.getColumnIndexOrThrow(KID_COLUMN_NEEDS));
        }

        cursor.close();
        db.close();

        return kidNeeds;
    }

    public List<Progress> getProgressesByKid(String name) {
        List<Progress> progresses = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                PROGRESS_COLUMN_KIDNAME,
                PROGRESS_COLUMN_TEACHERID,
                PROGRESS_COLUMN_PROGRESS,
                PROGRESS_COLUMN_PROGRESSDATE
        };

        String selection = PROGRESS_COLUMN_KIDNAME + " = ?";
        String[] selectionArgs = {name};

        Cursor cursor = db.query(
                TABLE_PROGRESS_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String kidName = cursor.getString(cursor.getColumnIndexOrThrow(PROGRESS_COLUMN_KIDNAME));
            int teacherId = cursor.getInt(cursor.getColumnIndexOrThrow(PROGRESS_COLUMN_TEACHERID));
            String progress = cursor.getString(cursor.getColumnIndexOrThrow(PROGRESS_COLUMN_PROGRESS));
            String progressDate = cursor.getString(cursor.getColumnIndexOrThrow(PROGRESS_COLUMN_PROGRESSDATE));

            Progress progressItem = new Progress(kidName, teacherId, progress, progressDate);
            progresses.add(progressItem);
        }

        cursor.close();
        db.close();

        return progresses;
    }

    public void deleteProgress(String kidName, int teacherId, String progressDate) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String whereClause = PROGRESS_COLUMN_KIDNAME + " = ? AND " + PROGRESS_COLUMN_TEACHERID + " = ? AND " + PROGRESS_COLUMN_PROGRESSDATE + " = ?";
        String[] whereArgs = {kidName, String.valueOf(teacherId), progressDate};
        db.delete(TABLE_PROGRESS_NAME, whereClause, whereArgs);
        db.close();
    }


    public List<TrackHour> getTrackHoursByTeacherId(int teacherId) {
        List<TrackHour> trackHours = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selection = TRACKHOUR_COLUMN_TEACHER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(teacherId)};

        Cursor cursor = db.query(TABLE_TRACKHOUR_NAME, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            String dateString = cursor.getString(cursor.getColumnIndexOrThrow(TRACKHOUR_COLUMN_DATE));
            String startTimeString = cursor.getString(cursor.getColumnIndexOrThrow(TRACKHOUR_COLUMN_START_TIME));
            String endTimeString = cursor.getString(cursor.getColumnIndexOrThrow(TRACKHOUR_COLUMN_END_TIME));

            // Convert the string values to Date and Time objects
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            Date date;
            Time startTime;
            Time endTime;
            try {
                date = dateFormat.parse(dateString);
                startTime = new Time(timeFormat.parse(startTimeString).getTime());
                endTime = new Time(timeFormat.parse(endTimeString).getTime());
            } catch (ParseException e) {
                // Handle parsing exception
                e.printStackTrace();
                continue; // Skip this record and proceed to the next one
            }

            TrackHour trackHour = new TrackHour(teacherId, date, startTime, endTime);
            trackHours.add(trackHour);
        }

        cursor.close();
        db.close();

        return trackHours;
    }

    public boolean editTrackHour(TrackHour trackHour) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TRACKHOUR_COLUMN_START_TIME, formatTimeToString(trackHour.getStartTime()));
        values.put(TRACKHOUR_COLUMN_END_TIME, formatTimeToString(trackHour.getEndTime()));
        values.put(TRACKHOUR_COLUMN_TEACHER_ID, trackHour.getTeacherId());
        values.put(TRACKHOUR_COLUMN_DATE, formatDateToString(trackHour.getDate()));
        // Convert the date to a string without the time component
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = dateFormat.format(trackHour.getDate());

        String whereClause = TRACKHOUR_COLUMN_DATE + " = ? AND " + TRACKHOUR_COLUMN_TEACHER_ID + " = ?";
        String[] whereArgs = {dateString, String.valueOf(trackHour.getTeacherId())};

        int rowsAffected = db.update(TABLE_TRACKHOUR_NAME, values, whereClause, whereArgs);

        db.close();

        return rowsAffected > 0;
    }

    public boolean deleteTrackHour(TrackHour trackHour) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Convert the date to a string without the time component
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = dateFormat.format(trackHour.getDate());

        String whereClause = TRACKHOUR_COLUMN_DATE + " = ? AND " + TRACKHOUR_COLUMN_TEACHER_ID + " = ?";
        String[] whereArgs = {dateString, String.valueOf(trackHour.getTeacherId())};

        int rowsAffected = db.delete(TABLE_TRACKHOUR_NAME, whereClause, whereArgs);

        db.close();

        return rowsAffected > 0;
    }

    public boolean getFirstPayment(String kidName, String year) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] columns = {PAYMENT_COLUMN_FIRST_PAYMENT};
        String selection = PAYMENT_COLUMN_KIDNAME + " = ? AND " + PAYMENT_COLUMN_YEAR + " = ?";
        String[] selectionArgs = {kidName, year};

        Cursor cursor = db.query(TABLE_PAYMENT_NAME, columns, selection, selectionArgs, null, null, null);

        int firstPayment = 0;

        if (cursor.moveToFirst()) {
            firstPayment = cursor.getInt(cursor.getColumnIndexOrThrow(PAYMENT_COLUMN_FIRST_PAYMENT));
        }

        cursor.close();
        db.close();
         if(firstPayment == 0)
             return false;
        return true;
    }

    public boolean getSecondPayment(String kidName, String year) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] columns = {PAYMENT_COLUMN_SECOND_PAYMENT};
        String selection = PAYMENT_COLUMN_KIDNAME + " = ? AND " + PAYMENT_COLUMN_YEAR + " = ?";
        String[] selectionArgs = {kidName, year};

        Cursor cursor = db.query(TABLE_PAYMENT_NAME, columns, selection, selectionArgs, null, null, null);

        int secondPayment = 0;

        if (cursor.moveToFirst()) {
            secondPayment = cursor.getInt(cursor.getColumnIndexOrThrow(PAYMENT_COLUMN_SECOND_PAYMENT));
        }

        cursor.close();
        db.close();

        if(secondPayment == 0)
            return false;
        return true;
    }

    public boolean getThirdPayment(String kidName, String year) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] columns = {PAYMENT_COLUMN_THIRD_PAYMENT};
        String selection = PAYMENT_COLUMN_KIDNAME + " = ? AND " + PAYMENT_COLUMN_YEAR + " = ?";
        String[] selectionArgs = {kidName, year};

        Cursor cursor = db.query(TABLE_PAYMENT_NAME, columns, selection, selectionArgs, null, null, null);

        int thirdPayment = 0;

        if (cursor.moveToFirst()) {
            thirdPayment = cursor.getInt(cursor.getColumnIndexOrThrow(PAYMENT_COLUMN_THIRD_PAYMENT));
        }

        cursor.close();
        db.close();

        if(thirdPayment == 0)
            return false;

        return true;
    }

    public boolean updateFirstPayment(String childName, String year, boolean firstPayment) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PAYMENT_COLUMN_KIDNAME, childName);
        values.put(PAYMENT_COLUMN_YEAR, year);
        values.put(PAYMENT_COLUMN_FIRST_PAYMENT, firstPayment ? 1 : 0);

        String whereClause = PAYMENT_COLUMN_KIDNAME + " = ? AND " + PAYMENT_COLUMN_YEAR + " = ?";
        String[] whereArgs = {childName, year};

        int rowsAffected = db.update(TABLE_PAYMENT_NAME, values, whereClause, whereArgs);

        if (rowsAffected <= 0) {
            long rowId = db.insert(TABLE_PAYMENT_NAME, null, values);
            db.close();
            return rowId != -1;
        } else {
            db.close();
            return true;
        }
    }

    public boolean updateSecondPayment(String childName, String year, boolean secondPayment) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PAYMENT_COLUMN_KIDNAME, childName);
        values.put(PAYMENT_COLUMN_YEAR, year);
        values.put(PAYMENT_COLUMN_SECOND_PAYMENT, secondPayment ? 1 : 0);

        String whereClause = PAYMENT_COLUMN_KIDNAME + " = ? AND " + PAYMENT_COLUMN_YEAR + " = ?";
        String[] whereArgs = {childName, year};

        int rowsAffected = db.update(TABLE_PAYMENT_NAME, values, whereClause, whereArgs);

        if (rowsAffected <= 0) {
            long rowId = db.insert(TABLE_PAYMENT_NAME, null, values);
            db.close();
            return rowId != -1;
        } else {
            db.close();
            return true;
        }
    }

    public boolean updateThirdPayment(String childName, String year, boolean thirdPayment) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PAYMENT_COLUMN_KIDNAME, childName);
        values.put(PAYMENT_COLUMN_YEAR, year);
        values.put(PAYMENT_COLUMN_THIRD_PAYMENT, thirdPayment ? 1 : 0);

        String whereClause = PAYMENT_COLUMN_KIDNAME + " = ? AND " + PAYMENT_COLUMN_YEAR + " = ?";
        String[] whereArgs = {childName, year};

        int rowsAffected = db.update(TABLE_PAYMENT_NAME, values, whereClause, whereArgs);

        if (rowsAffected <= 0) {
            long rowId = db.insert(TABLE_PAYMENT_NAME, null, values);
            db.close();
            return rowId != -1;
        } else {
            db.close();
            return true;
        }
    }

    public List<Attendance> getAllAttendance(Date date) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Attendance> attendanceList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        String[] columns = {
                ATTENDANCE_COLUMN_DATE,
                ATTENDANCE_COLUMN_KIDID,
                ATTENDANCE_COLUMN_ATTEND
        };

        String selection = ATTENDANCE_COLUMN_DATE + " = ?";
        String[] selectionArgs = {formattedDate};

        Cursor cursor = db.query(TABLE_ATTENDANCE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex(ATTENDANCE_COLUMN_DATE);
            int childIdIndex = cursor.getColumnIndex(ATTENDANCE_COLUMN_KIDID);
            int attendIndex = cursor.getColumnIndex(ATTENDANCE_COLUMN_ATTEND);

            do {
                if (dateIndex >= 0 && childIdIndex >= 0 && attendIndex >= 0) {
                    String attendanceDateString = cursor.getString(dateIndex);
                    String childId = cursor.getString(childIdIndex);
                    int attendValue = cursor.getInt(attendIndex);
                    boolean attendance = (attendValue == 1);

                    Date attendanceDate;
                    try {
                        attendanceDate = dateFormat.parse(attendanceDateString);
                    } catch (ParseException e) {
                        Log.e("Attendance", "Failed to parse attendance date: " + attendanceDateString);
                        attendanceDate = null;
                    }

                    Attendance attendanceObj = new Attendance(attendanceDate, childId, attendance);
                    attendanceList.add(attendanceObj);
                } else {
                    Log.e("Attendance", "Column index out of bounds");
                }
            } while (cursor.moveToNext());
        } else {
            Log.e("Attendance", "No data found");
        }

        cursor.close();
        db.close();

        return attendanceList;
    }



    public Attendance findAttendance(List<Attendance> attendanceRecords, String kidName) {
        for (Attendance record : attendanceRecords) {
            int kidId = fetchKidIdByName(kidName);
            if (record.getChildId().equals(String.valueOf(kidId))) {
                return record;
            }
        }
        return null; // Return null if no attendance record is found for the given kidName
    }


////////////////////////////////////
public List<String> getTeachersNames() {
    List<String> teachersNames = new ArrayList<>();
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = db.query(TABLE_TEACHER_NAME, null, null, null, null, null, null);

    while (cursor.moveToNext()) {
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(TEACHER_COLUMN_FIRSTNAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(TEACHER_COLUMN_LASTNAME));

        String fullName = firstName + " " + lastName;
        teachersNames.add(fullName);
    }

    cursor.close();
    db.close();
    return teachersNames;
}
    public int getTeacherId(String teacherName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = TEACHER_COLUMN_FIRSTNAME + "||' '||" + TEACHER_COLUMN_LASTNAME + "=?";
        String[] selectionArgs = {teacherName};
        Cursor cursor = db.query(TABLE_TEACHER_NAME, new String[]{TEACHER_COLUMN_ID}, selection, selectionArgs, null, null, null);

        int teacherId = -1;
        if (cursor.moveToFirst()) {
            teacherId = cursor.getInt(cursor.getColumnIndexOrThrow(TEACHER_COLUMN_ID));
        }
        cursor.close();
        db.close();

        return teacherId;
    }
    //start
    public int getChildId(String childName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = KID_COLUMN_NAME + "=?";
        String[] selectionArgs = {childName};
        Cursor cursor = db.query(TABLE_KIDS_NAME, new String[]{KID_COLUMN_ID}, selection, selectionArgs, null, null, null);

        int childId = -1;
        if (cursor.moveToFirst()) {
            childId = cursor.getInt(cursor.getColumnIndexOrThrow(KID_COLUMN_ID));
        }
        cursor.close();
        db.close();

        return childId;
    }
















}



//>>>>>>s