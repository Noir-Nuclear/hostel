package checks;

import entities.CustomDate;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddProvidedServiceChecks {
    public static boolean isHotelEmployeeBusy(String hotelEmployeeId, Date date, Statement statement) {
        try {
            ResultSet set = statement.executeQuery(" SELECT hotel_employee_id FROM public.provided_services " +
                    "WHERE hotel_employee_id = \'" + hotelEmployeeId + "\' AND date = \'" + new CustomDate(date.getTime()) + "\'");
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean isDateCorrect(Date date) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.after(date) || c.getTime().equals(date);
    }
}
