package age.club.administrator.age_101;

import java.text.DateFormat;
import java.util.Date;

public class QRCodeGenerator {

    private String cardNumber;
    private String timeStamp;

    public QRCodeGenerator(){
        User myUser = User.getInstance();
        this.cardNumber = myUser.getCardId();
        timeStamp = DateFormat.getDateTimeInstance().format(new Date());
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getCardNumber() {
        return cardNumber;
    }
}
