# Movie-Ticket-Reservation-System

### Program Details

The program serves as a backend of a ticket reservation system. Users are able to reserve seats in three auditoriums. Once an auditorium is selected, the program displays the current seating arrangement and allows the user to select seats. A report is generated at the end to specify, for each individual auditorium and overall for all auditoriums, how many seats have been sold/unsold and how much money was earned.

The customer is able to log in to the system and access their orders. The customer has the ability to add orders, change orders or cancel orders, as well as the ability to see receipts for their orders. The system also has an administrative interface.

The user account information is stored in a HashMap. The pre-built Java HashMap object is used. Each entry in the HashMap contains the customer username, password and all of his/her orders. An order consists of the auditorium, seats and the number of tickets per ticket type (senior, adult, child). The username is used as the key value to the HashMap.

The user enters individual seats for reservation and can reserve seats that are not adjacent to the initial seat selected. If all the desired seats are not available, the user is offered the best available consecutive seats in the entire auditorium equal to the number of tickets they wish to purchase. The best available seats are the seats closest to the middle of the auditorium. If the user declines the best available seats, the program returns to the main menu. If the user accepts the best available seats, then the seats are reserved. A confirmation message is displayed on the screen after seats are reserved.

Ticekts are only reserved on the day of the screening of the movie and all screenings are assumed to occur at the same time.

Tickets are priced as follows:
  * Adult: $10
  * Senior: $7.50
  * Child: $5.25

**Required file structure for the auditoriums:**

The seating arrangement for each auditorium must be stored in separate files. The files should be named A1.txt, A2.txt and A3.txt for auditoriums 1, 2 and 3 respectively. Each line in the file should represent a row in the auditorium and the number of rows maybe random. The number of seats in each row of a *single* auditorium should be the same. For example, if the first line in the file has 15 seats, then every subsequent row in the theater should also have 15 seats. However, each auditorium is not required to have the same number of seats in each row. Empty seats are represented by pound signs (#) and reserved seats are represented by periods (.).

**Filling the HashMap:**

All user data must be saved in a file named userdb.dat. The program reads the file and fills in the hashmap before starting the user interface. The format of the user data file should be as follows: \<username\>\<space\>\<password\>\<newline\>. The last line should not contain a newline character at the end of the line.

**Customer User Interface:**
  
  * **Starting Point:** Before the user can use the system, a login is required. The starting point of the interface asks the user for a username. After the username is entered, the user is prompted to type in the password. The password is then verified. If the password is not valid, the user is prompted to enter the password again. If the password is entered incorrectly for a total of 3 times, the program returns to the starting point of the interface. Once the user successfully logs in, the main menu is displayed:

  * **Main Menu:**
  
   1. Reserve Seats
   2. View Orders
   3. Update Order
   4. Display Receipt
   5. Log Out

  * **Reserve Seats:** When the user reserves seats, the auditorium submenu is displayed:
   1. Auditorium 1
   2. Auditorium 2
   3. Auditorium 3
  
   The selected auditorium is then displayed in the following manner:
  
   12345678901234
  1..#...######.#
  2#########....#
  3......#..#...#
  
   The seats are numbered sequentially from left to right and only the ones digit is displayed above each column to make it easier to display the chart. The second set of digits from 1 - 0 are for the numbers 11 - 20 and so on.
  
   After displaying the selected auditorium, the user is asked for the number of tickets in the following categories:
 
   * Adult
   * Senior
   * Child
  
   For each ticket, the user is prompted to enter the row and seat numbers. The type of ticket (senior, adult or child) for each seat is tracked so that if a seat is removed from an order, the receipt and report totals are accurate.
  
   The availability of each ticket is then checked. The seats are not reserved unless all seats are available. If all the desired seats are not available, the best available consecutive seats (as described above) are searched. If the best seats are located, the user is prompted to enter **Y** to reserve them or **N** to refuse them. Once the selection is processed, the program returns to the main menu.
  
  * **View Orders:** Displays all orders of the user who is logged in. It displays the auditorium, seats and the number of tickets per ticket type. Once the orders are displayed, the program returns to the main menu.
  
  * **Update Order:** Displays the orders in a numerical menu system, so that the user can enter the number of the order that should be updated. Each order is listed on a separate line.
  
   After the order menu has been displayed and the user has selected an order, the menu below is displayed and the user is prompted for an update action:
  
   1. Add tickets to order
   2. Delete tickets from order
   3. Cancel order
  
   If the user wishes to add tickets to the order, the user is stepped through the reservation process to add seats to the current order. When the user finishes adding seats, the program returns to the main menu.
  
   If the user wishes to delete tickets from an order, a numerical menu listing each individual seat reserved for that order is displayed. Along with the row and seat numbers, the ticket type is also displayed. The last entry in the menu is "Exit". The user selects a seat from the menu. That seat is removed from the order the seat is marked as open instead of reserved in the auditorium. If there are no tickets left in the order, the order is removed from the user's account and the program returns to the main menu. The program loops back to the individual seat menu until the user decides to exit the process and return the main menu.
  
   If the user wishes to cancel the order, all the seats in the order are marked as available and the order is removed from the user's account. After the order has been cancelled, the program returns to the main menu.
  
  * **Display Receipt:** Creates a formatted receipt for the user. Each order is displayed in detail (auditorium, seats and the number of tickets per ticket type), the amount for each order and the overall amount of all orders. Once the receipt is displayed, the program returns to the main menu.
  
  * **Log Out:** Returns to the Starting Point.
  
**Admin Main Menu:**

  * **Starting Point:** A log in is required for the admin. Both the customer and admin begin at the login prompt. The admin username is *admin*. After the username is entered, the user is prompted for the password and the password is verified. If the password is not valid, the user is prompted to enter the password again. If the password is entered incorrectly a total of 3 times, the program returns to the Starting Point. Once the user successfully logs in, the Admin main menu is displayed.
  
  * **Admin Main Menu:**
    1. View Auditorium
    2. Print Receipt
    3. Exit
   
  * **View Auditorium:** Displays the auditorium submenu:
  
     1. Auditorium 1
     2. Auditorium 2
     3. Auditorium 3
    
   The user is prompted for the auditorium number and then the current state of the auditorium is displayed. After the auditorium is displayed, the program returns to the Admin Main Menu:
  
  * **Print Report:** Displays a formatted report to the console. The report consists of 7 columns:
    * Column 1 - labels
      * Auditorium 1
      * Auditorium 2
      * Auditorium 3
      * Total
    * Column 2 - Open seats (the number of open seats for each label in column 1)
    * Column 3 - Total reserved seats (the number of all reserved seats for each label in column 1)
    * Column 4 - Adult seats (the number of all adult seats reserved during this session for each label in column 1)
    * Column 5 - Senior seats (the number of all senior seats reserved during this session for each label in column 1)
    * Column 6 - Child seats (the number of all child seats reserved during this session for each label in column 1)
    * Column 7 - Ticket sales (The total amount of ticket sales during this session for each label in column 1)
    
  * **Exit:** The audtorium seating arrangements are stored back to their respective files and the program is ended.
  
  
  
*All user input is checked for validity*
