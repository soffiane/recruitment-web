package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.exception.InsufficientFundsException;
import fr.d2factory.libraryapp.utils.Constants;

import java.time.LocalDate;
import java.time.Period;

/**
 * The type Student member.
 */
public class StudentMember extends Member {

    /**
     * entry date of the student
     */
    private LocalDate entryDate;

    public StudentMember(float initialWallet, LocalDate entryDate) {
        super(initialWallet);
        setEntryDate(entryDate);
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public void payBook(int numberOfDays) {
        if (numberOfDays > 0) {
            float fee;
            //the student is in his first year
            if (Period.between(entryDate, LocalDate.now()).getYears() < 1) {
                fee = (numberOfDays - Constants.STUDENT_DAYS_FREE > 0) ? (numberOfDays - Constants.STUDENT_DAYS_FREE) * Constants.REGULAR_CHARGE : 0L;
            } else {
                fee = numberOfDays * Constants.REGULAR_CHARGE;
            }
            if((getWallet() - fee) >= 0){
                setWallet(getWallet() - fee);
            } else {
                throw new InsufficientFundsException("You don't have enough money to pay");
            }
        } else {
            throw new IllegalArgumentException("number of days should be greater than 0");
        }
    }

    @Override
    public long getLimitDaysForBook() {
        return Constants.STUDENT_DAYS_LIMIT;
    }
}
