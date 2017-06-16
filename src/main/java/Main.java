import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {

        try {
            Requests req = new Requests();
            req.go();

        } catch (SQLException e) {
            e.printStackTrace();
        }catch(NullPointerException n){
            n.printStackTrace();
        }
    }
}
