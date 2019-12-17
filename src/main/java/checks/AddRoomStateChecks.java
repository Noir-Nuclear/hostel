package checks;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddRoomStateChecks {
    public static boolean isStateValid(String state) {
        try {
            return Integer.parseInt(state) == 0 || Integer.parseInt(state) == 1;
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
    }
    public static boolean isDateIntervalCorrect(Date begin, Date end) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return begin.before(end) && (begin.after(c.getTime()) || c.getTime().equals(begin));
    }

    public static boolean isDateIntervalAvailable(Date begin, Date end, String roomId, Statement statement) {
        ResultSet set;
        try {
            String query = "SELECT s.room_state_id FROM public.room_states s " +
                    "WHERE s.room_id = " + roomId + " AND s.begin <= \'" + end + "\' AND s.end >= \'" + begin + "\'";
            set = statement.executeQuery(query);
            return !set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
