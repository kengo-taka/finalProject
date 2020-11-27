import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
//	private static final String ADD_BOOK_QUERY = 
//			"INSERT INTO Booking(courseId,studentId) VALUES(?,?)";
//	private static final String ALL_ST_QUERY = "SELECT * FROM Student";

	public static void main(String[] args) throws SQLException {
//		Connection conn = null;
//		getConnection();
//		book(conn);
//		createTable();
//		post();
//		showAllCourse();
//		showAllCourse();
		start();
	}

	// method to insert data to DB
//	public static void book(Connection conn) throws SQLException{
//		//In this method you can get all the data from user Scanner
//		PreparedStatement prestmt = null;
//		prestmt = conn.prepareStatement(ADD_BOOK_QUERY);
//		prestmt.setInt(1, 3);
//		prestmt.setInt(2, 2);
//		prestmt.executeUpdate();
//	}

	public static void start() {
		Scanner input = new Scanner(System.in);
		System.out.println("Are you a teacher or student?" + "\nPlease enter t(teacher) or s(student).");

		String hello = input.nextLine();
		switch (hello.toLowerCase()) {
		case "t":
			int tea = getYourId();
			helloTeacher(tea);
			getCourseByT(tea);
			printStudent();
			break;
		case "s":
			int stu = getYourId();
			helloStudent(stu);
			studentMenu(stu);
			break;
		default:
			System.out.println("Error");
			start();
			break;
		}
	}

	public static int getYourId() {
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter your ID.");
		int hello = input.nextInt();
		return hello;
	}

//	show all courses
	public static void showAllCourse() {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT courseId,courseName,date FROM Course");
			ResultSet result = statement
					.executeQuery("SELECT c.courseName, c.date , c.courseId, t.firstName, t.lastName \n"
							+ "FROM Teacher t \n" + "INNER JOIN Course c \n" + "ON t.teacherId = c.teacherId");
			System.out.println("All course");
			while (result.next()) {
				System.out.print(result.getString("courseId"));
				System.out.print(" ");
				System.out.print(result.getString("courseName"));
				System.out.print(" ");
				System.out.println(result.getString("date"));
				System.out.println("Teacher : " + result.getString("firstName") + " " + result.getString("lastName"));
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println(e);

		}
	}

	public static void helloTeacher(int teacherId) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Student");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				if (result.getString("studentId").equals(String.valueOf(teacherId))) {
					System.out.println(
							"Hello " + result.getString("firstName") + " " + result.getString("lastName") + " !");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void printStudent() {
		Scanner input = new Scanner(System.in);
		System.out.println("Which course do you want to check? Please enter course ID");
		String hello = input.nextLine();
		System.out.println("Student list");
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Booking");
			ResultSet result = statement.executeQuery(
					"SELECT s.firstName, s.lastName, c.courseName ,s.studentId,b.courseId FROM Student s INNER JOIN Booking b ON b.studentId = s.studentId INNER JOIN Course c ON b.courseId = c.courseId");
			Boolean nothing = false;
			while (result.next()) {
				if (result.getString("courseId").equals(hello)) {
					System.out.println("student ID = " + result.getString("studentId") + " : "
							+ result.getString("firstName") + " " + result.getString("lastName"));
					nothing = true;
				}
			}
			if (nothing == false) {
				System.out.println("This course doesn't have any student.");
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
		}
		printStudent();
	}

	public static void getCourseByT(int teacherId) {
//		Scanner input = new Scanner(System.in);
//		System.out.println("Please enter your ID.");
//		String hello = input.nextLine();
//		helloTeacher(Integer.valueOf(hello));
		System.out.println("Your class list");
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Course");
			ResultSet result = statement.executeQuery();
			Boolean nothing = false;
			while (result.next()) {
				if (result.getString("teacherId").equals(String.valueOf(teacherId))) {

					System.out.println(result.getString("courseId") + " " + result.getString("courseName") + " "
							+ result.getString("date"));
					nothing = true;
				}
			}
			if (nothing == false) {
				System.out.println("You don't have any class.");
			}
		} catch (Exception e) {
			System.out.println(e);

		}
	}
//--------------------------------------------------------

	public static ResultSet getDB(Connection conn, String query) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}

	// --------------------------------------------------------
	public static void book(int studentId) {
		Scanner input = new Scanner(System.in);
		printDate();
		System.out.println("Which date do you want to book? Please enter course ID.");
		int hello = input.nextInt();
		System.out.println(howManyStudent(hello) + " students already booked.");
if (howManyStudent(hello) == 6) {
	System.out.println("This class is full. You can not book.");
} else {
		final int var1 = hello;
		final int var2 = studentId;
		try {
			Connection con = getConnection();
			PreparedStatement posted = con
					.prepareStatement("INSERT INTO Booking(courseId,studentId)VALUES('" + var1 + "','" + var2 + "')");
			posted.executeUpdate();
			System.out.println("Book success!");
		} catch (Exception e) {
			System.out.println(e);
		} 
	}
	}

	public static int howManyStudent(int courseId) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Booking");
			ResultSet result = statement.executeQuery();
		int howmany = 0;
			while (result.next()) {
				if (result.getString("courseId").equals(String.valueOf(courseId))) {
					howmany +=1;
				}
			}
			return howmany;

		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}
	}

	public static void cansel(int studentId) {
		Scanner input = new Scanner(System.in);
		getCourseByS(studentId);
		System.out.println("Which course do you want to cansel? Please enter course ID.");
		int hello = input.nextInt();
		final int var1 = hello;
		final int var2 = studentId;
		try {
			Connection con = getConnection();
			PreparedStatement posted = con.prepareStatement(
					"DELETE FROM Booking Where courseId = '" + var1 + "' AND studentId = '" + var2 + "'");
			posted.executeUpdate();
			System.out.println("Cansel success!");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
		}
	}

	public static void studentMenu(int studentId) {
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter b(book new class) or c(cansel class) or s(see your class) e(exit).");
		String hello = input.nextLine();
		switch (hello) {
		case "b":
			book(studentId);
			studentMenu(studentId);
			break;
		case "c":
			cansel(studentId);

			studentMenu(studentId);
			break;
		case "s":
			getCourseByS(studentId);
			studentMenu(studentId);
			break;
		case "e":
			System.out.println("See you.");
			break;
		default:
			studentMenu(studentId);
			break;
		}
	}

	public static void printDate() {
		Scanner input = new Scanner(System.in);
		System.out.println("Which course do you want to book? Please enter course name.");
		String hello = input.nextLine();
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Course");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				if (result.getString("courseName").equals(hello.toLowerCase())) {
					System.out.println(result.getString("courseId") + " " + result.getString("courseName") + " "
							+ result.getString("date"));
				}
			}
		} catch (

		Exception e) {
			System.out.println(e);
		}
	}

	public static void getCourseByS(int studentId) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Booking");
			ResultSet result = statement.executeQuery(
					"SELECT s.firstName, s.lastName, c.courseName, c.date ,s.studentId,b.courseId FROM Student s INNER JOIN Booking b ON b.studentId = s.studentId INNER JOIN Course c ON b.courseId = c.courseId");
			Boolean nothing = false;

			while (result.next()) {
				if (result.getString("studentId").equals((String.valueOf(studentId)))) {
					System.out.println(result.getString("courseId") + " " + result.getString("courseName") + " "
							+ result.getString("date"));
					nothing = true;
				}
			}
			if (nothing == false) {
				System.out.println("no reservation");
			}

		} catch (Exception e) {
			System.out.println(e);

		} finally {

		}
	}

	public static void helloStudent(int studentId) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Student");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				if (result.getString("studentId").equals(String.valueOf(studentId))) {
					System.out.println(
							"Hello " + result.getString("firstName") + " " + result.getString("lastName") + " !");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// --------------------------------------------------------
	public static Connection getConnection() {
		try {
			String driver = "";
			String url = "";
			String username = ";
			String password = "";
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(url, username, password);
//			System.out.println("Connected");
			return conn;

		} catch (Exception e) {
			System.out.println(e);
			System.out.println("no");
		}
		return null;
	}
}
