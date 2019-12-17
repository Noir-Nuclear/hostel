package entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDate extends Date {

    public CustomDate(long date) {
        super(date);
    }

    public CustomDate(String s) throws ParseException {
        super((new SimpleDateFormat("yyyy-MM-dd")).parse(s).getTime());
    }

    public CustomDate() {
        super();
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this);
    }
}
