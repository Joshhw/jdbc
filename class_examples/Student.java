import java.io.*;
import java.sql.*;
import java.util.Scanner;

class Q2{
  // the host name of the server and the server instance name/id
  public static final String oracleServer = "dbs2.cs.umb.edu";
  public static final String oracleServerSid = "dbs2";

  public static void main(String args[]) {
    Connection conn = null;
    try {
      conn = getConnection();
      if (conn != null) {
            conn.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    // preparedstatements for building the query.
    // ResultSet for executing the query.
    // oracle sequences for creating new id
    // always close ResultSet and preparedstatements
		Scanner input = new Scanner(System.in);
