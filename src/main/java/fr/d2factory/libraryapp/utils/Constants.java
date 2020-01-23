package fr.d2factory.libraryapp.utils;

/**
 * All Constants gathered here.
 */
public class Constants {


    /**
     * number of max days a student can borrow a book before is he considered late
     */
    public static final int STUDENT_DAYS_LIMIT = 30;

    /**
     * number of free days for new students
     */
    public static final int STUDENT_DAYS_FREE = 15;

    /**
     * number of max days a resident can borrow a book before he is considered late
     */
    public static final int RESIDENT_DAYS_LIMIT = 60;

    /**
     * the standard fee for a day borrowing a book
     */
    public static final float REGULAR_CHARGE = 0.10f;

    /**
     * the extra fee for a day borrowing a book more than the limit
     */
    public static final float LATE_CHARGE = 0.20f;
}
