package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.exception.InsufficientFundsException;
import fr.d2factory.libraryapp.utils.Constants;

public class ResidentMember extends Member {

    public ResidentMember(float initialWallet){
        super(initialWallet);
    }

    @Override
    public void payBook(int numberOfDays) {
        if(numberOfDays > 0) {
            float fee;
            if (numberOfDays > Constants.RESIDENT_DAYS_LIMIT) {
                fee =  Constants.RESIDENT_DAYS_LIMIT * Constants.REGULAR_CHARGE + (numberOfDays - Constants.RESIDENT_DAYS_LIMIT) * Constants.LATE_CHARGE;
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
        return Constants.RESIDENT_DAYS_LIMIT;
    }
}
