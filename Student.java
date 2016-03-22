import java.io.*;
import java.sql.*;
import java.util.Scanner;

class Student{
	// the host name of the server and the server instance name/id
	public static final String oracleServer = "dbs2.cs.umb.edu";
	public static final String oracleServerSid = "dbs2";
	public static void main(String args[]) {
    Scanner input = new Scanner(System.in);
		int studentId = 0;
		Connection conn = null;
		try {
			conn = getConnection();
			if (conn == null) {
			      conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		login(conn);
	}

  public static void list(Connection conn){

		try{
			String sql = "Select * FROM Courses";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			  while (rs.next()) {
			      for (int i = 1; i <= columnsNumber; i++) {
	            if (i > 1) System.out.print(",  ");
	            String columnValue = rs.getString(i);
	            System.out.print(columnValue + " " + rsmd.getColumnName(i));
	        }
	        System.out.println("");
	    }
			System.out.println();
		} catch(SQLException es) {
			System.out.println("SQL ERROR IN list");
			login(conn);
		} catch(Exception f) {
			System.out.println("wrong type of input in list");
			login(conn);
		}
  }

	public static void enrolled(Connection conn, int studentId){
		int num;
		String courseCheck = "SELECT Count(*) FROM Courses C WHERE C.cid = ?";
		String inCourse = "SELECT E.sid FROM Enrolled E WHERE E.cid = ?" +
													" AND E.sid = ?";

		Scanner input = new Scanner(System.in);
		System.out.print("Please enter course number");
		int cid = input.nextInt();

	try{
		PreparedStatement check = conn.prepareStatement(inCourse);
		check.clearParameters();
		check.setInt(1, cid);
		check.setInt(2, studentId);
		ResultSet rs = check.executeQuery();
		if(rs.next()){
			System.out.println("Student already in course");
			menu(conn, studentId);
		}
		check = conn.prepareStatement(courseCheck);
		check.clearParameters();
		check.setInt(1, cid);
		rs = check.executeQuery();
		if(!rs.next()){
			System.out.println("No course available");
			menu(conn, studentId);
		}

		String sql = "INSERT INTO ENROLLED E VALUES(?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.clearParameters();
		pstmt.setInt(1, studentId);
		pstmt.setInt(2, cid);
		num = pstmt.executeUpdate();
		if(num == 1){
			System.out.println("you are enrolled in course number:" + cid);
			menu(conn, studentId);
		}

	} catch(SQLException es) {
		System.out.println("SQL ERROR IN enrolled");
	} catch(Exception f) {
		System.out.println("wrong type of input in enrolled");
	}

	}

	public static void withdraw(Connection conn, int studentId) {
		int num;
		String inCourse = "SELECT E.sid FROM Enrolled E WHERE E.cid = ?" +
													" AND E.sid = ?";
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter course number:");
		int cid = input.nextInt();

	try{
		PreparedStatement check = conn.prepareStatement(inCourse);
		check.clearParameters();
		check.setInt(1, cid);
		check.setInt(2, studentId);
		ResultSet rs = check.executeQuery();
		if(!rs.next()){
			System.out.println("Student isn't enrolled in course.");
			menu(conn, studentId);
		}

		String sql = "DELETE FROM ENROLLED E WHERE E.sid = ? AND E.cid = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.clearParameters();
		pstmt.setInt(1, studentId);
		pstmt.setInt(2, cid);
		num = pstmt.executeUpdate();
		if(num == 1){
			System.out.println("you are withdrawn from course number:" + cid);
			menu(conn, studentId);
		}

	} catch(SQLException es) {
		System.out.println("SQL ERROR IN withdraw");
	} catch(Exception f) {
		System.out.println("wrong type of input in withdraw");
	}

	}


	public static void search(Connection conn, int studentId) {
		Scanner input = new Scanner(System.in);
		String name;
		String sql = "SELECT * FROM COURSES C WHERE LOWER(C.cname) LIKE ?";
		System.out.println("Please enter the course name:");
		name = input.nextLine();

		try{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			pstmt.setString(1, "%" + name.toLowerCase() + "%");
			ResultSet rs = pstmt.executeQuery();
			if(!rs.next()){
				System.out.println("No results found");
				menu(conn, studentId);
			}
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			rs = pstmt.executeQuery();
				while (rs.next()) {
						for (int i = 1; i <= columnsNumber; i++) {
							if (i > 1) System.out.print(",  ");
							String columnValue = rs.getString(i);
							System.out.print(columnValue + " " + rsmd.getColumnName(i));
					}
					System.out.println("");
			}
			System.out.println();
		} catch(SQLException es) {
			System.out.println("SQL ERROR IN search");
		} catch(Exception f) {
			System.out.println("wrong type of input in search");
		}

	}

	public static void myClasses(Connection conn, int studentId) {
		int num;
		String myCourse = "SELECT cname FROM Enrolled E, Courses C"
		+ " WHERE E.cid = C.cid AND E.sid = ?";
		Scanner input = new Scanner(System.in);
	try {
		PreparedStatement check = conn.prepareStatement(myCourse);
		check.clearParameters();
		check.setInt(1, studentId);
		ResultSet rs = check.executeQuery();
		if(!rs.next()){
			System.out.println("You have no classes.");
			menu(conn, studentId);
		}
		rs = check.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
					for (int i = 1; i <= columnsNumber; i++) {
						if (i > 1) System.out.print(",  ");
						String columnValue = rs.getString(i);
						System.out.print(columnValue + " ");
				}
				System.out.println("");
		}
		System.out.println();
	} catch(SQLException es) {
		System.out.println("SQL ERROR IN myClasses");
	} catch(Exception f) {
		System.out.println("wrong type of input in myClasses");
	}



	}

	public static void createStudent(Connection conn){
		String test = "SELECT sid, sname FROM Students S WHERE S.sid = ?";
		ResultSet rs;
		String sname;
		int num;
		int studentId;
		Scanner input = new Scanner(System.in);
		System.out.print("Enter your name:");
		sname = input.nextLine();

		System.out.print("Please enter preferred ID number:");
		studentId = input.nextInt();

		try{
			PreparedStatement check = conn.prepareStatement(test);
			check.clearParameters();
			check.setInt(1, studentId);
			rs = check.executeQuery();
			if(rs.next()){
				System.out.println("ID already used");
				createStudent(conn);
			} else {
				String sql = "INSERT INTO Students VALUES(?,?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.clearParameters();
				pstmt.setInt(1, studentId);
				pstmt.setString(2, sname);
				num = pstmt.executeUpdate();
				if(num == 1){
					System.out.println("Thank you.");
					menu(conn, studentId);
				}
			}
		} catch(SQLException es) {
			System.out.println("SQL ERROR IN createStudent");
			login(conn);
		} catch(Exception f) {
			System.out.println("wrong type of input in createStudent");
			login(conn);
		}

	}

	public static void login(Connection conn){
		int studentId = 0;
		String sql = "SELECT sid, sname FROM Students S WHERE S.sid = ?";
		PreparedStatement pstmt;
		ResultSet rs;
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter your student ID "
																+	"(if new student enter -1): ");
		if(!input.hasNextInt()){
			System.out.print("Please enter valid input:");
			login(conn);
		}
		studentId = input.nextInt();
		if(studentId == -1){
			createStudent(conn);
			return;
		}
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.clearParameters();
			pstmt.setInt(1, studentId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				menu(conn, studentId);
			} else {
				System.out.println("No records found.");
				login(conn);
			}

			return;
		} catch(SQLException es) {
			System.out.println("SQL ERROR");
			login(conn);
		} catch(Exception f) {
			System.out.println("wrong type of input");
			login(conn);
		}
	}

  public static void menu(Connection conn, int studentId){


		Scanner input = new Scanner(System.in);
		String selection = "q";
		while(selection.charAt(0) != 'x'){

	    System.out.println("Select from a list of available options:");
	    System.out.println();
	    System.out.println("L - List: lists all records in the course table.");
	    System.out.println();
	    System.out.println("E - Enroll: enroll in a course of your choice.");
	    System.out.println();
	    System.out.println("W - Withdraw: withdraw from a course of your choice.");
	    System.out.println();
	    System.out.println("S - Search: search for a course.");
	    System.out.println();
	    System.out.println("M - My Classes: list all your classes.");
	    System.out.println();
	    System.out.println("X - Exit: exit application.");
	    System.out.println();
	    System.out.print("Make your selection:");


			selection = input.nextLine();
			selection = selection.toLowerCase();

			switch(selection.charAt(0)) {
				case 'l':
					list(conn);
					continue;
				case 'e':
					enrolled(conn, studentId);
					continue;
				case 'w':
					withdraw(conn, studentId);
					continue;
				case 's':
					search(conn, studentId);
					continue;
				case 'm':
					myClasses(conn, studentId);
					continue;
				case 'x':
					System.exit(0);
				default:
					System.out.println("invalid selection, please try again");
					continue;
				}




  	}
	}

	public static Connection getConnection(){

		// first we need to load the driver
		String jdbcDriver = "oracle.jdbc.OracleDriver";
		try {
			Class.forName(jdbcDriver);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get username and password
		Scanner input = new Scanner(System.in);
		System.out.print("Username:");
		String username = input.nextLine();
		System.out.print("Password:");
		//the following is used to mask the password
		Console console = System.console();
		String password = new String(console.readPassword());
		String connString = "jdbc:oracle:thin:@" + oracleServer + ":1521:"
				+ oracleServerSid;

		System.out.println("Connecting to the database...");

		Connection conn;
		// Connect to the database
		try{
			conn = DriverManager.getConnection(connString, username, password);
			System.out.println("Connection Successful");
		}
		catch(SQLException e){
			System.out.println("Connection ERROR");
			e.printStackTrace();
			return null;
		}

		return conn;
	}


}
