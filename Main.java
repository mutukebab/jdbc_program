import java.sql.SQLException;
import java.util.Scanner;

/**
 * This Java Database Connector main program implements an application
 * that allows user to log in to the database and manipulate database's data.
 * <p>
 * Harjoitustyö, Olio-ohjelmointi, kevät 2019.
 * <p>
 * @author Ville Juuti
 * Tietojenkäsittelytiede, UEF Joensuu
 *
 */
public class Paaohjelma {

	public static void main(String[] args) throws SQLException {
		/* Has a dialog with the user by asking inputs */
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Database connector - Ville Juuti 2019.");
		// Program is running
		boolean running = true;
			// Starts next step or quits the program
			System.out.println("Start connection to database (y/n): ");
			String answer = scan.next();
			// Asks for database username and password
			if (answer.equals("y")) {
				System.out.println("Username: ");
				String user = scan.next();
				System.out.println("Password: ");
				String pw = scan.next();
				
				DbConnection connection = new DbConnection();
				// If getConnection method does not return null (username and password is correct)
				if (connection.getConnection(user, pw) != null) {
					// running variable at line 22
					while (running == true) {
						// asking for an action from the user
						System.out.println(" ");
						System.out.println("Choose an action: ");
						System.out.println("Add entries - PRESS 1 + ENTER");
						System.out.println("Edit entries - PRESS 2 + ENTER");
						System.out.println("Delete entries - PRESS 3 + ENTER");
						System.out.println("List entries - PRESS 4 + ENTER");
						System.out.println("Quit program - PRESS 0 + ENTER");
						System.out.println("");
						System.out.println("Action: ");
						int action = scan.nextInt();
						
						// Chooses appropriate DbConnection method to call.
						switch (action) {
						case 1:
							connection.populateTable();
							break;
						case 2:
							connection.editTable();
							break;
						case 3:
							connection.deleteData();
							break;
						case 4:
							connection.listData();
							break;
						case 0:
							running = false;
							System.out.println("Program closing.");
							break;
						}
					}
				}
		// If user enters 'n' in the start program will stop.
		} else if (answer.equals("n")) {
			running = false;
			scan.close();
			System.out.println("Program closed.");
		}

	}
}

