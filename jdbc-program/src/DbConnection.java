
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/** 
 * <h1> Java Database Connector object</h1>
 * Abstract DbConnection class, which contains functionality and properties
 * of database connection.
 * <p>
 * Harjoitustyö, Olio-ohjelmointi, kevät 2019.
 * <p>
 * @author Ville Juuti
 * Tietojenkäsittelytiede, UEF Joensuu
 */

public class DbConnection {
	
	/** Variable initialization */
	private Connection conn;
	Scanner scan = new Scanner(System.in);
	String dbName;
	
	/** Constructor */
	public DbConnection() {
	}
	
	/** This method is used to establish a connection to database.
	 *  @param username This is user's DBMS login user name
	 *  @param password This is user's DBMS login password
	 *  @return Connection This returns connection if no errors occur. */
	public Connection getConnection(String username, String password) {
		try {								
			// Database URL syntax = jdbc:mysql://host:port/database?propertyName1=propertyValue1&propertyName2=propertyValue2...
			String url = "jdbc:mysql://localhost:3306/kurssisuoritukset?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			
			// Trying to connect with login credentials.
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connection to database established.");
			return conn;
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
	}
	/**
	 *  Method for populating database tables (e.g. adding new data).
	 *  @exception SQLException On SQL error.
	 *  @see SQLException 
	 */
	public void populateTable() throws SQLException {
		// An object that represents a precompiled SQL statement.
		PreparedStatement stmt = null;
		// Asking for table to populate.
		System.out.println("Table to add data (students / enrollments / courses): ");
		String tableName = scan.nextLine();
		// Checks given input and acts accordingly.
		switch (tableName) {
		// students -table
		case "students":
			System.out.println("First name: ");
			String firstName = scan.nextLine();
			System.out.println("Last name: ");
			String lastName = scan.nextLine();
			
			try {
				// Saving prepared SQL statement to stmt variable.
				stmt = conn.prepareStatement("INSERT INTO kurssisuoritukset." + tableName + " (FirstName, LastName) " +
											 "values ('" + firstName + "', '" + lastName + "')");
				// Runs the SQL statement using established database connection.
				stmt.executeUpdate();
			// Simple SQLException catch printing the SQL error.
			} catch (SQLException e) {
				System.out.println(e);
			} finally {
				// Frees up memory related to PreparedStatement.
				if (stmt != null) {
					stmt.close();
				}
				System.out.println(firstName + " " + lastName + " added to database.");
			}
			break;
		// enrollments -table
		case "enrollments":
			System.out.println("Student ID: ");
			int studentID = scan.nextInt();
			System.out.println("Course ID: ");
			int courseID = scan.nextInt();
			System.out.println("Year of completion (4 digits): ");
			int y = scan.nextInt();
			System.out.println("Month of completion (2 digits): ");
			int m = scan.nextInt();
			System.out.println("Day of completion (2 digits): ");
			int d = scan.nextInt();
			
			
			System.out.println("Grade (0-5): ");
			int grade = scan.nextInt();
			
			try {
				// Sends prepared SQL query to the database
				stmt = conn.prepareStatement("INSERT INTO kurssisuoritukset." + tableName + " (StudentID, CourseID, Course_completion, Grade) " +
											 "values (" + studentID + ", " + courseID + ", '" + y + "-" + m + "-" + d + "', " + grade + ")");
				stmt.executeUpdate();		
			} catch (SQLException e) {
				System.out.println(e);
			} finally {
				if (stmt != null) {
					stmt.close();
				}
			}
			break;
		// courses -table	
		case "courses":		
			System.out.println("Course name: ");
			String courseTitle = scan.nextLine();
			System.out.println("Credits awarded upon completion: ");
			int creditsAwarded = scan.nextInt();
	
			try {
				// Sends prepared SQL query to the database
				stmt = conn.prepareStatement("INSERT INTO kurssisuoritukset." + tableName + " (Course_title, credits) " +
											 "values ('" + courseTitle + "', " + creditsAwarded + ")");
				stmt.executeUpdate();		
			} catch (SQLException e) {
				System.out.println(e);
			} finally {
				if (stmt != null) {
					stmt.close();
				}
			}
			break;
		}
	}
	/** 
	 * Method for editing existing data in the database table.
	 * User can choose the table and object to edit.
	 * @exception SQLException On SQL Error.
	 */
	public void editTable() throws SQLException {
		// Represents SQL statement, see line 60
		PreparedStatement stmt = null;
		System.out.println("Which table do you want to edit?");
		System.out.println("Students - PRESS 1 + ENTER");
		System.out.println("Enrollments - PRESS 2 + ENTER");
		System.out.println("Courses - PRESS 3 + ENTER");
		System.out.println("Action: ");
		int tableToEdit = scan.nextInt();
		// Chooses the right option depending on user input
		switch (tableToEdit) {
		// students -table
		case 1:
			System.out.println("Which student's details you want to edit?");
			System.out.println("Enter student's ID number: ");
			int studentChosen = scan.nextInt();
			
			System.out.println("Enter student's new first name: ");
			String newFirstName = scan.nextLine();
			
			System.out.println("Enter student's new last name: ");
			String newLastName = scan.nextLine();
			
				try {
					stmt = conn.prepareStatement("UPDATE student SET FirstName='" + newFirstName + "', LastName='" + newLastName + "' "
							+ "WHERE StudentID=" + studentChosen + ";");
					stmt.executeUpdate();		
				} catch (SQLException e) {
					System.out.println(e);
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
				break;
		// enrollments -table		
		case 2:
			System.out.println("Which enrollment you want to edit?");
			System.out.println("Enter enrollment ID (number): ");
			int enrollmentID = scan.nextInt();
			
			System.out.println("Which data you want to edit?");
			System.out.println("Course completion date - PRESS 1 AND ENTER");
			System.out.println("Student's grade - PRESS 2 AND ENTER");
			System.out.println("Action: ");
			int fieldToEdit = scan.nextInt();
			// course completion date
			if (fieldToEdit == 1) {
				System.out.println("Enter a new date. ");
				System.out.println("Year (4 digits): ");
				int newYear = scan.nextInt();
				System.out.println("Month (2 digits): ");
				int newMonth = scan.nextInt();
				System.out.println("Day (2 digits): ");
				int newDay = scan.nextInt();
				
				try {
					stmt = conn.prepareStatement("UPDATE kurssisuoritukset.enrollments SET Course_completion='" + 
				newYear + "-" + newMonth + "-" + newDay + "' WHERE EnrollmentID=" + enrollmentID);
					stmt.executeUpdate();
				} catch (SQLException ex) {
					System.out.println(ex);
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			}
			// student's grade
			if (fieldToEdit == 2) {
				System.out.println("Enter a new grade for the student: ");
				int newGrade = scan.nextInt();
				
				try {
					stmt = conn.prepareStatement("UPDATE kurssisuoritukset.enrollments SET grade=" + newGrade + " WHERE EnrollmentID=" + enrollmentID);
				} catch (SQLException ex) {
					System.out.println(ex);
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			}
			break;
		// courses -table	
		case 3:
			System.out.println("Which course you want to edit?");
			System.out.println("Enter Course ID: ");
			int courseToEdit = scan.nextInt();
			
			System.out.println("Which data you want to change?");
			System.out.println("Course title - PRESS 1 and ENTER");
			System.out.println("Credits awarded - PRESS 2 and ENTER");
			int fieldToEdit2 = scan.nextInt();
			// course title
			if (fieldToEdit2 == 1) {
				System.out.println("Write a new title for the chosen course: ");
				String newCourseTitle = scan.nextLine();
				
				try {
					stmt = conn.prepareStatement("UPDATE kurssisuoritukset.courses SET Course_title=" + newCourseTitle + " WHERE CourseID=" + courseToEdit);
				} catch (SQLException ex) {
					System.out.println(ex);
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			}
			// credits awarded upon course completion
			if (fieldToEdit2 == 2) {
				System.out.println("Enter a new amount of credits for the course (number): ");
				int creditAmount = scan.nextInt();
				
				try {
					stmt = conn.prepareStatement("UPDATE kurssisuoritukset.courses SET credits=" + creditAmount + ";");
				} catch (SQLException ex) {
					System.out.println(ex);
				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}
			}
			break;
		}
		
	}
	/**
	 * This method deletes existing data from the database table.
	 * User can choose which data will be deleted. Method executeQuery returns one ResultSet object.
	 * @exception SQLException On SQL error.
	 * @see SQLException
	 * @see PreparedStatement
	 */
	public void deleteData() throws SQLException {
		// Represents SQL statement, see line 60
		PreparedStatement stmt = null;
		System.out.println("From what table you want to delete data? ");
		System.out.println("Students - PRESS 1 + ENTER");
		System.out.println("Enrollments - PRESS 2 + ENTER");
		System.out.println("Courses - PRESS 3 + ENTER");
		System.out.println("Action: ");
		int entityToDelete = scan.nextInt();
		
		switch (entityToDelete) {
		// students -table
		case 1:
			System.out.println("-- Students table --");
			System.out.println("Enter the ID of the student you want to delete (number): ");
			int studToDelete = scan.nextInt();
			
			try {
				stmt = conn.prepareStatement("DELETE FROM kurssisuoritukset.students WHERE StudentID=" + studToDelete + ";");
				stmt.executeUpdate();
			} catch (SQLException ex) {
				System.out.println(ex);
			} finally {
				if (stmt != null) {
					stmt.close();
				}
			}
			System.out.println("Student deleted.");
			break;
		// enrollments -table
		case 2:
			System.out.println("-- Enrollments table --");
			System.out.println("Enter the ID of the enrollment which will be deleted (number): ");
			int enrToDelete = scan.nextInt();
			
			try {
				// Prepares a SQL DELETE -statement
				stmt = conn.prepareStatement("DELETE FROM kurssisuoritukset.enrollments WHERE EnrollmentID=" + enrToDelete + ";");
				stmt.executeUpdate();
			} catch (SQLException ex) {
				System.out.println(ex);
			} finally {
				if (stmt != null) {
					stmt.close();
				}
			}
			System.out.println("Enrollment deleted.");
			break;
		// courses -table
		case 3:
			System.out.println("-- Course table --");
			System.out.println("Enter the ID of the course which will be deleted: ");
			int courToDelete = scan.nextInt();
			
			try {
				// Prepares a SQL DELETE -statement
				stmt = conn.prepareStatement("DELETE FROM kurssisuoritukset.courses WHERE CourseID=" + courToDelete + ";");
				stmt.executeUpdate();
			} catch (SQLException ex) {
				System.out.println(ex);
			} finally {
				if (stmt != null) {
					stmt.close();
				}
			}
			System.out.println("Course deleted.");
			break;
		}
		
	}
	
	/**
	 * This method prints out a result set (list) of either specific student's course enrollments
	 * or specific course's enrollments. Executing Statement objects will generate ResultSet objects,
	 * which is a table of data representing a database result set.
	 * @exception SQLException On SQL error.
	 * @see SQLException
	 */
	public void listData() throws SQLException {
		System.out.println("Which data you want to list?");
		System.out.println("Student's enrollments - PRESS 1 AND ENTER");
		System.out.println("Enrollments of a course - PRESS 2 AND ENTER");
		int wantedList = scan.nextInt();
		// student's enrollments
		if (wantedList == 1) {
			System.out.println("Enter student's ID (number): ");
			int studentID = scan.nextInt();
			
			try {
				/**
				 *  A Statement is an interface that represents a SQL statement.
				 *  You need a Connection object to create a Statement object.
				 */
				Statement selectStmt = conn.createStatement();
				String strSelect1 = "SELECT Enrollments.EnrollmentID, Courses.CourseID, Courses.Course_title, Enrollments.Course_completion, Enrollments.Grade FROM Enrollments " +
						"INNER JOIN Courses ON Enrollments.CourseID=Courses.CourseID WHERE StudentID=" + studentID +";";
				// ResultSet contains a table of data and executeQuery returns it
				ResultSet set1 = selectStmt.executeQuery(strSelect1);
				
				System.out.println("Found enrollments: ");
				// Iterating through tables to list all chosen data
				int rows = 0;
				while (set1.next()) {
					int foundCourseID = set1.getInt("CourseID");
					String foundName = set1.getString("Course_title");
					Date compDate = set1.getDate("Course_completion");
					int foundGrade = set1.getInt("grade");
					System.out.println("Course ID: " + foundCourseID + " | Course name: " + foundName + " | Completion date: " + compDate + " | Course grade: " + foundGrade);
					rows++;
				}
				System.out.println("Total matches: " + rows);
			} catch (SQLException ex) {
				System.out.println(ex);
			}
		}
		// One course's all enrollments
		if (wantedList == 2) {
			System.out.println("Enter course's ID (number): ");
			int courseID = scan.nextInt();
			
			try {
				Statement selectStmt = conn.createStatement();
				String strSelect2 = "SELECT Students.StudentID, Students.firstName, Students.lastName, Enrollments.EnrollmentID, Enrollments.Grade, Courses.CourseID" +
						" FROM Students JOIN Enrollments ON Students.StudentID=Enrollments.StudentID JOIN Courses ON Enrollments.CourseID=Courses.CourseID" +
						" WHERE Courses.CourseID=" + courseID + ";";
				ResultSet set2 = selectStmt.executeQuery(strSelect2);
				
				System.out.println("Found enrollments: ");
				// Iterating through tables to list all chosen data
				int rows = 0;
				while (set2.next()) {
					int foundStudentID = set2.getInt("StudentID");
					String foundFName = set2.getString("firstName");
					String foundLName = set2.getString("lastName");
					int foundEnrollmentID = set2.getInt("EnrollmentID");
					int foundGrade= set2.getInt("grade");
		            System.out.println("Student ID: " + foundStudentID + " | " + foundFName + " " + foundLName +
		            		" | Enrollment ID: " + foundEnrollmentID + " | Grade: " + foundGrade);
		            rows++;
				}
				System.out.println("Total matches: " + rows);
				
			} catch (SQLException e){
				System.out.println(e);
			}
		}
		
	}

}
