package com.example.administrator.age_101;

import java.text.DateFormat;
import java.util.Date;

public class QRCodeGenerator {

    String cardNumber;
    int transactionType;
    String timeStamp;

    public QRCodeGenerator(){
        User myUser = User.getInstance();
        this.cardNumber = myUser.getCardId();
        this.transactionType = myUser.getType();
        timeStamp = DateFormat.getDateTimeInstance().format(new Date());
    }

    public String getTimeStamp() {
        return timeStamp;
    }

}
