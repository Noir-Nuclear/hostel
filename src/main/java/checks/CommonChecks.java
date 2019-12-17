package checks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CommonChecks {
    public static boolean isAvailableDeleting(String columnName, Integer rowId, String tableName, Statement statement) {
        ResultSet set;
        try {
            set = statement.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_name =  \'" + tableName.split("\\.")[1] + "\'");
            boolean existed = false;
            while (set.next()) {
                if(set.isFirst() && columnName.equals(set.getString(1))) {
                    return true;
                }
                if (columnName.equals(set.getString(1))) {
                    existed = true;
                    break;
                }

            }
            if(!existed) {
                return true;
            }
            set = statement.executeQuery( "SELECT COUNT(*) AS Counter FROM " + tableName + " WHERE " + columnName + " = " + rowId);
            set.next();
            int res = set.getInt("Counter");
            return res == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean isExistedId(String rowId, String tableName, Statement statement) {
        String columnName;
        ResultSet set;
        try {
            set = statement.executeQuery("SELECT COLUMN_NAME from information_schema.KEY_COLUMN_USAGE " +
                    "WHERE TABLE_NAME = \'" + (tableName.split("\\."))[1] + "\'");
            set.next();
            columnName = set.getString(1);
            set = statement.executeQuery("SELECT " + columnName + " FROM " + tableName +
                    " WHERE " + columnName + " = \'" + rowId + "\'");
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
}
