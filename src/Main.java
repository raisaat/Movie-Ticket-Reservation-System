// RAISAAT RASHID
// Net ID: rar150430
// CS 2336.003: Project 5

import java.io.*;
import java.util.*;
import Hashmap.*;

public class Main 
{
    // Variables to hold the total no. of open and reserved seats for all the auditoriums
    public static int totalOpen = 0, totalReserved = 0;
    
    public static void main (String[] args) throws IOException
    {
        // Create a HashMap object to store all the users' details
        HashMap<String, HashMapNode> hm = new HashMap<>();
        
        // Scanner object to read from userdb file
        Scanner inFile = new Scanner(new File("userdb.dat"));
        
        while (inFile.hasNext()) // while the file has a next line
        {
            String username = inFile.next(); // Read the next line into a String object
           
            // Insert the username and password into the hashmap
            hm.put(username, new HashMapNode(username, inFile.next()));
        }
        // Scanner to read input from the console
        Scanner input = new Scanner(System.in);
        
        // Array to store the no. of adult, senior and child tickets for each auditorium
        int[][] quantity = new int[3][3];
        
        do // loop while true
        {
            String username = ""; // String object to hold the username
        
            for (int i = 0;; i++)
            {
                // If the user enters incorrect password 3 times or if the user hasn't entered the username yet
                if ((i % 3) == 0)
                {
                    // Get the username
                    System.out.print("\nEnter your username: ");
                    username = input.nextLine();
                }
                
                // Get the password, and if it's correct break from the for loop
                System.out.print("Enter your password: ");
                if (hm.get(username).getPass().equals(input.nextLine()))
                    break;
                // Else display an error message
                else
                    System.out.println("Invalid password entered.\n");
            }
            // Variable to read in the user's menu selection
            int menu;
            
            if (username.equals("admin")) // If the user is the admin
            {
                do // loop while the admin doesn't choose to exit
                {
                    // Get the admin's menu selection and validate it
                    menu = validateInput("\nEnter what you want to do:\n1. View Auditorium\n2. Print Report\n3. Exit\n", 1, 3, input);
                
                    if (menu == 1) // If the admin chooses to view an auditorium
                    {
                        // Get the auditorium no. and validate it
                        int menu2 = validateInput("\nSelect the auditorium to be displayed:\n1. Auditorium\n2. Auditorium 2\n3. Auditorium\n", 1, 3, input);
                    
                        // Initialize a char array with the chosen auditorium's seating arrangement
                        char[][] auditorium = initializeArray(menu2);
                    
                        printArray(auditorium); // Display the auditorium's seating arrangement
                    }
                    
                    else if (menu == 2) // If the admin chooses to print report
                    {
                        // Display the report's column headings
                        System.out.println("\nLabels   Open seats   Total reserved seats   Adult seats   Senior seats   Child seats   Ticket Sales / $");
                    
                        // Display the status of each auditorium
                        for (int i = 0; i < 3; i++)
                            printAuditoriumStatus(i + 1, quantity[i][0], quantity[i][1], quantity[i][2]);
                        
                        // Get the total no. of adult, senior and child tickets for this session
                        int totalAdultNum = quantity[0][0] + quantity[1][0] + quantity[2][0];
                        int totalSeniorNum = quantity[0][1] + quantity[1][1] + quantity[2][1];
                        int totalChildNum = quantity[0][2] + quantity[1][2] + quantity[2][2];
                        
                        // Display the status of all the auditoriums in total
                        System.out.printf("\n%-9s%-13d%-23d", "Total", totalOpen, totalReserved);
                        System.out.printf("%-14d%-15d%-14d%.2f\n", totalAdultNum, totalSeniorNum, totalChildNum, (totalAdultNum * 10 + totalSeniorNum * 7.5 + totalChildNum * 5.25));
                    }
                } while(menu != 3);
                
                break; // break from the do-while loop to end the program
            }
            
            else // else if the user is not the admin
            {
                do // loop while the user doesn't choose to log out
                {
                    // Get the user's menu selection and validate it
                    menu = validateInput("\nEnter what you want to do:\n1. Reserve Seats\n2. View Orders\n3. Update Order\n4. Display Receipt\n5. Log Out\n", 1, 5, input);
                
                    if (menu == 1) // If the user chooses to reserve seats
                    {
                        // Get the auditorium number and validate it
                        menu = validateInput("\nSelect an auditorium:\n1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3\n", 1, 3, input);
                
                        char[][] auditorium = initializeArray(menu); // Read the auditorium's seating arrangement in a char array
                        
                        printArray(auditorium); // display the auditorium's seating arrangement
                        
                        // Get the numbers of adult, senior and child tickets and validate them
                        int adult = validateInput("Enter the no. of adult tickets: ", 0, -1, input);
                        int senior = validateInput("Enter the no. of senior tickets: ", 0, -1, input);
                        int child = validateInput("Enter the no. of child tickets: ", 0, -1, input);
                        
                        if (adult != 0 || senior != 0 || child != 0) // If the quantity of at least one ticket type is not 0
                        {
                            // Create an ArrayList to hold the seats chosen by the user
                            ArrayList<OrdersNode> order = new ArrayList<>();
                        
                            // Add the chosen seats for each ticket type to order
                            addSeats(order, "adult", adult, auditorium.length, auditorium[0].length, input);
                            addSeats(order, "senior", senior, auditorium.length, auditorium[0].length, input);
                            addSeats(order, "child", child, auditorium.length, auditorium[0].length, input);
            
                            // If the chosen seats are not available, get the best available seats
                            if (!checkAvailability(order, auditorium))
                                order = bestAvailable(adult, senior, child, auditorium, input);
            
                            if (order != null) // If order is not empty
                            {
                                // Add the order to user's account
                                hm.get(username).addOrder(new OrderDetails(menu, adult, senior, child, order));
                            
                                // Mark the chosen seats as reserved in the auditorium
                                for (int i = 0; i < order.size(); i++)
                                    auditorium[order.get(i).getRow() - 1][order.get(i).getSeat() - 1] = '.';
                            
                                updateFile(menu, auditorium); // Update the auditorium's file
                            
                                // Increment the no. of each ticket type for the auditorium by the amount entered
                                quantity[menu - 1][0] += adult;
                                quantity[menu - 1][1] += senior;
                                quantity[menu - 1][2] += child;
                            
                                // Display a confirmation message saying that the seats have been reserved
                                System.out.println("\nThe seats have been reserved.");
                            }
                        }
                    }
        
                    else if (menu == 2) // Else if the user chooses to view orders
                    {
                        // If the user currently has no orders, display a message saying so and go back to the main menu
                        if (hm.get(username).getOrderDetails().isEmpty())
                            System.out.println("\nThere are currently no orders.");
                        
                        // Else, display all the orders of the user
                        else
                            viewOrders(hm.get(username).getOrderDetails());
                    }
                    
                    else if (menu == 3) // Else if the user chooses to update an order
                    {
                        // If the user currently has no orders, display a message saying so and go back to the main menu
                        if (hm.get(username).getOrderDetails().isEmpty())
                            System.out.println("\nThere are currently no orders.");
                        
                        else
                        {
                            // Display all the orders of the user
                            viewOrders(hm.get(username).getOrderDetails());
                        
                            // Get the order no. and validate it
                            int orderNum = validateInput("Enter an order number: ", 1, hm.get(username).getOrderDetails().size(), input);
                        
                            // OrderDetails object to store the details of the selected order
                            OrderDetails orderDetails = hm.get(username).getOrderDetails().get(orderNum - 1);
            
                            int auditoriumNum = orderDetails.getAuditoriumNum(); // Store the order's auditorium number
            
                            ArrayList<OrdersNode> order = orderDetails.getOrders(); // Store all the seats of the order in an Arraylist
            
                            // Get the submenu selection from the user and validate it
                            menu = validateInput("\nSelect a menu:\n1. Add tickets to order\n2. Delete tickets from order\n3. Cancel order\n", 1, 3, input);
            
                            if (menu == 1) // If the user chooses to add tickets
                            {
                                char[][] auditorium = initializeArray(auditoriumNum); // Initialize a char array with the auditorium's seating arrangement
                            
                                printArray(auditorium); // Display the auditorium's seating arrangement
                        
                                // Get the numbers of adult, senior and child tickets and validate them
                                int adult = validateInput("Enter the no. of adult tickets: ", 0, -1, input);
                                int senior = validateInput("Enter the no. of adult tickets: ", 0, -1, input);
                                int child = validateInput("Enter the no. of child tickets: ", 0, -1, input);
                                
                                if (adult != 0 || senior != 0 || child != 0) // If the quantity of at least one ticket type is not 0
                                {
                                    ArrayList<OrdersNode> newOrder = new ArrayList<>(); // Create an ArrayList to store the new seats
                                    
                                    // Get the seats for each ticket type and add them to the ArrayList
                                    addSeats(newOrder, "adult", adult, auditorium.length, auditorium[0].length, input);
                                    addSeats(newOrder, "senior", senior, auditorium.length, auditorium[0].length, input);
                                    addSeats(newOrder, "child", child, auditorium.length, auditorium[0].length, input);
                            
                                    // If the selected seats are not available, get the best available seats
                                    if (!checkAvailability(newOrder, auditorium))
                                        newOrder = bestAvailable(adult, senior, child, auditorium, input);
                            
                                    if (newOrder != null) // If the orders' list is not empty
                                    {
                                        ArrayList<OrdersNode> updatedOrder = new ArrayList<>(); // Create a new ArrayList to hold the old and new seats
                                    
                                        for (int i = 0; i < order.size(); i++) // Copy the old seats to updatedOrder
                                            updatedOrder.add(order.get(i));
                                
                                        for (int i = 0; i < newOrder.size(); i++) // Copy the new seats to updatedOrder
                                            updatedOrder.add(newOrder.get(i));
                                
                                        // Add updatedOrder to the user's account
                                        hm.get(username).setOrder(orderNum, new OrderDetails(auditoriumNum, orderDetails.getAdultNum() + adult, orderDetails.getSeniorNum() + senior, orderDetails.getChildNum() + child, updatedOrder));
                                
                                        for (int i = 0; i < newOrder.size(); i++) // Mark the new seats selected as reserved in the auditorium
                                            auditorium[newOrder.get(i).getRow() - 1][newOrder.get(i).getSeat() - 1] = '.';
                            
                                        updateFile(auditoriumNum, auditorium); // Update the auditorium's file
                            
                                        // Increment the number of each ticket type for the auditorium by the amount entered
                                        quantity[auditoriumNum - 1][0] += adult;
                                        quantity[auditoriumNum - 1][1] += senior;
                                        quantity[auditoriumNum - 1][2] += child;
                            
                                        System.out.println("\nThe seats have been reserved."); // Display a confirmation message saying that the seats have been reserved
                                    }
                                }
                            }
            
                            else if (menu == 2) // Else if the user chooses to delete seats
                            {
                                do // loop while the order is not empty
                                {
                                    // Display the seats reserved for the order and the option to exit
                                    System.out.println("\nThe seats selected for order #" + orderNum + " in auditorium " + auditoriumNum + " are:");
                                    int i;
                                    for (i = 0; i < order.size(); i++)
                                        System.out.println((i + 1) + ". row " + order.get(i).getRow() + ", seat " + order.get(i).getSeat() + " (" + order.get(i).getTicketType() + ")");
                                    System.out.println((i + 1) + ". Exit");
                                
                                    // Get the user's selection and validate it
                                    menu = validateInput("\nEnter the seat you want to delete or exit: ", 1, i + 1, input);
                                
                                    if (menu == i + 1) // Break from the loop if the user chooses to exit
                                        break;
                    
                                    char[][] auditorium = initializeArray(auditoriumNum); // Initialize a char array with the the auditorium's seating arrangement
                    
                                    // Mark the deleted seat as open in the auditorium
                                    auditorium[order.get(menu - 1).getRow() - 1][order.get(menu - 1).getSeat() - 1] = '#';
                    
                                    updateFile(auditoriumNum, auditorium); // Update the auditorium's file
                                    
                                    if (order.get(menu - 1).getTicketType().equals("adult")) // If the seat to be deleted is of adult type
                                    {
                                        quantity[auditoriumNum - 1][0]--; // Decrement the no. of adult tickets for the auditorium by 1
                                        orderDetails.setAdultNum(orderDetails.getAdultNum() - 1); // Decrement the no. of adult tickets for the order by 1
                                    }
                                
                                    else if (order.get(menu - 1).getTicketType().equals("senior")) // Else if the seat to be deleted is of senior type
                                    {
                                        quantity[auditoriumNum - 1][1]--; // Decrement the no. of senior tickets for the auditorium by 1
                                        orderDetails.setSeniorNum(orderDetails.getSeniorNum() - 1); // Decrement the no. of senior tickets for the order by 1
                                    }
                                
                                    else // Else if seat to be deleted of child type
                                    {
                                        quantity[auditoriumNum - 1][2]--; // Decrement the no. of child tickets for the auditorium by 1
                                        orderDetails.setChildNum(orderDetails.getChildNum() - 1); // Decrement the no. of child tickets for the order by 1
                                    }
                                
                                    order.remove(menu - 1); // Remove the seat from the order
                    
                                    if (order.isEmpty()) // If the order has no more seats left, delete the order
                                        hm.get(username).getOrderDetails().remove(orderNum - 1);
                                
                                } while (!order.isEmpty());
                            }
            
                            else // Else if the user chooses to cancel order
                            {
                                char[][] auditorium = initializeArray(auditoriumNum); // Initialize an array with the auditorium's seating arrangement
                
                                for (int i = 0; i < order.size(); i++)
                                {
                                    // Mark each seat of the order as open in the auditorium
                                    auditorium[order.get(i).getRow() - 1][order.get(i).getSeat() - 1] = '#';
                                
                                    if (order.get(i).getTicketType().equals("adult")) // If the seat is of adult type
                                        quantity[auditoriumNum - 1][0]--; // Decrement the no. of adult tickets for the auditorium by 1
                                
                                    else if (order.get(i).getTicketType().equals("senior")) // Else if the seat is of senior type
                                        quantity[auditoriumNum - 1][1]--; // Decrement the no. of senior tickets for the auditorium by 1
                                
                                    else // Else if the seat is of child type
                                        quantity[auditoriumNum - 1][2]--; // Decrement the no. of child tickets for the auditorium by 1
                                }
                
                                updateFile(auditoriumNum, auditorium); // Update the auditorium's file
                
                                hm.get(username).getOrderDetails().remove(orderNum - 1); // Remove the order from the user's account
                            }
                        }
                    }
        
                    else if (menu == 4) // If the user chooses to display the receipt
                    {
                        System.out.println(); // Go to the next line
                        for (int i = 0; i < 143; i++) // Print out dashes to mark the start of the receipt
                            System.out.print("-");
                        
                        // Display the column headings
                        System.out.println("\nReceipt for " + username + ":\n");
                        System.out.print("Order No.   Auditorium   No. of Adult Tickets   No. of Senior Tickets   No. of Child Tickets   ");
                        System.out.println("Total Price for the Order / $   Seats Selected\n");
                        
                        ArrayList<OrderDetails> order = hm.get(username).getOrderDetails(); // ArrayList to hold all the orders of the user
            
                        int orderSize = order.size(); // Get the user's total no. of orders
            
                        double totalPrice = 0; // variable to hold the user's total bill
            
                        for (int i = 0; i < orderSize; i++)
                        {
                            // Print the order no. and auditorium no. of each order
                            System.out.printf("%-12d%-13s", (i + 1), "A" + order.get(i).getAuditoriumNum());
                
                            // Get the number of each ticket type for each order
                            int adultNum = order.get(i).getAdultNum();
                            int seniorNum = order.get(i).getSeniorNum();
                            int childNum = order.get(i).getChildNum();
                
                            totalPrice += adultNum * 10 + seniorNum * 7.5 + childNum * 5.25; // calculate the total bill for each order
                
                            // Print the no. of each ticket type and the total bill for each order
                            System.out.printf("%-23d%-24d%-23d%-32.2f", adultNum, seniorNum, childNum, (adultNum * 10 + seniorNum * 7.5 + childNum * 5.25));
                
                            // Display the seats chosen for each order
                            for (int j = 0; j < adultNum + seniorNum + childNum; j++)
                            {
                                if (j == 0)
                                    System.out.print("row " + order.get(i).getOrders().get(j).getRow() + ", seat " + order.get(i).getOrders().get(j).getSeat() + "\n");
                                else
                                {
                                    System.out.printf("%131s", "row ");
                                    System.out.print(order.get(i).getOrders().get(j).getRow() + ", seat " + order.get(i).getOrders().get(j).getSeat() + "\n");
                                } 
                            }
                            System.out.print("\n"); // Go to the next line
                        }   
                        System.out.println("\nTotal price of all orders: $" + totalPrice); // Print the user's total bill
                        
                        for (int i = 0; i < 143; i++) // Print dashes to mark the end of the receipt
                            System.out.print("-");
                        System.out.println(); // Go to the next line
                    }
                } while (menu != 5);
            }
        } while (true);
    }
    
    // function initializeArray returns the current seating arrangement of an auditorium as a 2D char array
    public static char[][] initializeArray (int file) throws IOException
    {
        // Create a file object
        File inFile = new File("A" + file + ".txt");
            
        // Create a Scanner to read the file's data
        Scanner readFile = new Scanner(inFile);
            
        // Find the total number of lines in the file
        LineNumberReader reader = new LineNumberReader(new FileReader(inFile));
        while ((reader.readLine()) != null);
        
        // Find the number of rows in the auditorium
        int rowSize = reader.getLineNumber();
          
        // Read the first line of the file into a String object
        String s = readFile.nextLine();
        
        // Find the column size
        int colSize = s.length();
            
        // Create an array with the auditorium's row size and column size
        char [][]auditorium = new char[rowSize][colSize];
        
        // Assign the first row of the array with the characters of the first line read from the file
        for (int j = 0; j < colSize; j++)
                auditorium[0][j] = s.charAt(j);
        
        // Assign the array with the auditorium's seating arrangement
        for (int i = 1; readFile.hasNext(); i++)
        {
            // Read a line from the file into the String object
            s = readFile.nextLine();
            
            // Assign the status of each seat to an element of the array
            for (int j = 0; j < colSize; j++)
                auditorium[i][j] = s.charAt(j);
        }
        // Close the file
        readFile.close();
        
        // Return the array
        return auditorium;
    }
    
    // Function printArray prints the current seating arrangement of an auditorium
    public static void printArray (char[][] arr)
    {
        // Find the auditorium's row and column sizes
        int rowSize = arr.length; 
        int colSize = arr[0].length;
        
        // Go to the next line
        System.out.print("\n");
        
        // Find the number of spaces that should precede the column heading numbers
        int numOfSpaces = (Integer.toString(rowSize)).length() + 1;
        
        // Print spaces before writing the column heading numbers
        for (int i = 0; i < numOfSpaces; i++)
            System.out.print(" ");
            
        // Write the column heading numbers
        for (int i = 1; i <= colSize; i++)
            System.out.print(i % 10);
            
        // Print the rows
        for (int i = 0; i < rowSize; i++)
        {
            // Print the row number
            System.out.print("\n" + (i + 1));
                
            // Find and print the number of spaces to come after the row number
            for (int j = 0; j < numOfSpaces - (Integer.toString(i)).length(); j++)
                System.out.print(" ");
                
            // Print the status of each seat
            for (int j = 0; j < colSize; j++)
                System.out.print(arr[i][j]);
            }
        // Skip a line
        System.out.print("\n\n");
    }
    
    // function isInteger determines whether a string consists entirely of digits
    public static boolean isInteger (String s)
    {
        for (int i = 0; i < s.length(); i++)
        {
            // if the character in the string is not a digit, return false
            if (s.charAt(i) < '0' || s.charAt(i) > '9')
                return false;
        }
        // return true
        return true;
    }
    
    // Function checkAvailability determines whether the seats chosen by the user are available
    public static boolean checkAvailability (ArrayList<OrdersNode> order, char[][] auditorium)
    {
        for (int i = 0; i < order.size(); i++) // Loop until the end of the order is reached
        {
            // return false if the chosen seat is already reserved 
            if (auditorium[order.get(i).getRow() - 1][order.get(i).getSeat() - 1] == '.')
                return false;
        }
        return true; // return true
    }
    
    // function bestAvailable finds the best available seats in an auditorium
    public static ArrayList<OrdersNode> bestAvailable (int adult, int senior, int child, char[][] arr, Scanner input)
    {
        // variables to hold the total numbers of rows and columns, the best seat, the best row and the total no. of seats to be reserved in the auditorium
        int rowSize = arr.length, colSize = arr[0].length, bestSeat = -1, bestRow = -1, total = adult + senior + child;
        
        // Variable to hold the minimum distance between a seat and the middle seat of the auditorium
        double minDistance = -1;
        
         // Variable to hold the middle seat and the middle row of the auditorium
        int midSeat = (int) Math.ceil(colSize / 2.0) - 1, midRow = (int) Math.ceil(rowSize / 2.0) - 1;
       
        // Find the best available seat
        for (int i = 0; i < rowSize; i++)
        {
            for (int j = 0; j < colSize; j++)
            {
                if (checkForConsecutiveSeats(j, i, total, arr)) // If consecutive seats starting from this seat are available
                {
                    // Get the distance between the middle seat of the group of adjacent seats and the middle seat of the auditorium
                    double distance = Math.sqrt(Math.pow(midSeat - ((int) Math.ceil(total / 2.0) - 1) - j, 2) + Math.pow(midRow - i, 2));
                    
                    if (minDistance == -1) // If minDistance is equal to -1, i.e. if the first available group of adjacent seats is found
                    {
                        bestSeat = j; // Assign the seat to bestSeat
                        bestRow = i; // Assign the row to bestRow
                        minDistance = distance; // Assign the distance to minDistance
                    }
                    // Else if the distance is less than the minimum distance or if it is equal to the minimum distance but its row is closer to the middle row than the best row
                    else if (distance < minDistance || (distance == minDistance && (Math.abs(midRow - i) < Math.abs(midRow - bestRow))))
                    {
                        bestSeat = j; // Assign the seat to bestSeat
                        bestRow = i; // Assign the row to bestRow
                        minDistance = distance; // Assign the distance to minDistance
                    }
                }
            }
        }
        
        if (bestSeat == -1) // If bestSeat is equal to -1, i.e. if no group of consecutive seats is found
        {
            System.out.println("\nThe seats are not available."); // Display a message saying that the chosen seats are not available
            return null; // return null
        }
        
        // Print the best available seats and ask the user if the user wants to reserve them
        System.out.print("\nSeat(s) ");
        for (int i = bestSeat; i < bestSeat + total; i++)
            System.out.print((i + 1) + " ");
        System.out.print("in row " + (bestRow + 1) + " are available. Do you want to reserve them? ");
        
        do // loop while true
        {
            // Get the user's answer
            System.out.print("Enter Y or N: ");
            String str = input.nextLine();
            
            if (str.equals("Y")) // If the user chooses to reserve them
            {
                ArrayList<OrdersNode> order = new ArrayList<>(); // Create an ArrayList to store the seats
                
                // Add the adult seats to the order
                for (int i = bestSeat; i < bestSeat + adult; i++)
                    order.add(new OrdersNode(bestRow + 1, i + 1, "adult"));
                
                // Add the senior seats to the order
                for (int i = bestSeat + adult; i < bestSeat + adult + senior; i++)
                    order.add(new OrdersNode(bestRow + 1, i + 1, "senior"));
                
                // Add the child seats to the order
                for (int i = bestSeat + adult + senior; i < bestSeat + total; i++)
                    order.add(new OrdersNode(bestRow + 1, i + 1, "child"));
                
                return order; // return the order
            }
            // Else if the user chooses not to reserve the best available seats, return null
            else if (str.equals("N"))
                return null;
            
            System.out.println("Invalid answer entered."); // Display an error message for invalid input
        
        } while(true);
    }
    
    // Function checkForConsecutiveSeats checks whether a no. of seats are available as a group
    public static boolean checkForConsecutiveSeats (int startSeat, int row, int quantity, char[][] arr)
    {
        // Check if the seats are available consecutively
        for (int i = startSeat; i < startSeat + quantity; i++)
        {
            // If a seat is out of the array or is already reserved, return false
            if (i >= arr[0].length || arr[row][i] == '.')
                return false;
        }
        return true; // return true
    }
    
    // Function viewOrders displays all the orders of a user
    public static void viewOrders (ArrayList<OrderDetails> order)
    {
        // Display the column headings
        System.out.println("\nOrder No.   Auditorium   No. of Adult Tickets   No. of Senior Tickets   No. of Child Tickets   Seats Selected\n");
            
        int orderSize = order.size(); // get the user's total no. of orders
            
        for (int i = 0; i < orderSize; i++)
        {
            // Display the order no. and the auditorium no. of each order
            System.out.printf("%-12d%-13s", (i + 1), "A" + order.get(i).getAuditoriumNum());
                
            // Get the no. of each ticket type for each order
            int adultNum = order.get(i).getAdultNum();
            int seniorNum = order.get(i).getSeniorNum();
            int childNum = order.get(i).getChildNum();
                
            // Display the no. of each ticket type for each order
            System.out.printf("%-23d%-24d%-23d", adultNum, seniorNum, childNum);
            
            // Display the seats chosen for each order
            for (int j = 0; j < adultNum + seniorNum + childNum; j++)
            {
                if (j == 0)
                    System.out.print("row " + order.get(i).getOrders().get(j).getRow() + ", seat " + order.get(i).getOrders().get(j).getSeat() + "\n");
                else
                {
                    System.out.printf("%99s", "row ");
                    System.out.print(order.get(i).getOrders().get(j).getRow() + ", seat " + order.get(i).getOrders().get(j).getSeat() + "\n");
                } 
            }
            System.out.print("\n");
        }
    }
    
    // Function validateInput determines whether the user's input is valid
    public static int validateInput (String s, int min, int max, Scanner input)
    {
        String menu; // Object to hold the user's input
        int value; // Variable to hold the number entered by the user if it is one
        
        do // loop while true
        {
            // Get the user's input
            System.out.print(s);
            menu = input.nextLine();
                
            if (isInteger(menu)) // If the input is an integer
            {
                value = Integer.parseInt(menu); // Convert the string of digits to an int
                
                // If the integer is within the accepted range, return true
                if (value >= min && (max == -1 || value <= max))
                    return value;
            }
            System.out.println("Invalid input.\n"); // Display an error message
                
        } while(true);
    }
    
    // Function updateFile writes the current seating arrangement of an auditorium to the auditorium's file
    public static void updateFile (int file, char[][] auditorium) throws IOException
    {
        // Create a PrintWriter object to write the auditorium's current seating arrangement back to the file
        PrintWriter output = new PrintWriter("A" + file + ".txt");
            
        /// Print the rows
        for (int i = 0; i < auditorium.length; i++)
        {
            // Print the status of each seat
            for (int j = 0; j < auditorium[0].length; j++)
                output.print(auditorium[i][j]);
            output.print("\n");
        }
        // Close the file
        output.close();
    }
    
    // function printAuditoriumStatus displays the status of each auditorium to the console
    public static void printAuditoriumStatus(int auditNum, int adultNum, int seniorNum, int childNum) throws IOException
    {
        char[][] arr = initializeArray(auditNum); // Initialize a char array with the auditorium's seating arrangement
        
        // Variables to hold the no. of reserved seats and the no. of open seats in the auditorium
        int reserved = 0, open;
        
        // Traverse the array to find the no. of reserved seats in the auditorium
        for (int row = 0; row < arr.length; row++)
        {
            for(int col = 0; col < arr[0].length; col++)
            {
                // If a seat is reserved, increment reserved
                if (arr[row][col] == '.')
                    reserved++;
            }
        }
        // Compute the no. of open seats in the auditorium
        open = arr.length * arr[0].length - reserved;
        
        // Increment totalOpen and totalReserved
        totalOpen += open;
        totalReserved += reserved;
        
        // Print the no. of reserved seats, the no. of open seats, the no. of each ticket type and the total sales of the auditorium
        System.out.printf("\n%s%-8d%-13d%-23d", "A", auditNum, open, reserved);
        System.out.printf("%-14d%-15d%-14d%.2f", adultNum, seniorNum, childNum, (adultNum * 10 + seniorNum * 7.5 + childNum * 5.25));
    }
    
    // function addSeats adds the seats selected for a ticket type to an order
    public static void addSeats (ArrayList<OrdersNode> order, String type, int quantity, int rowMax, int seatMax, Scanner input)
    {
        for (int i = 0; i < quantity; i++)
        {
            // Display the type of ticket the user has to choose a seat for
            System.out.println("\nFor " + type + " ticket #" + (i + 1) + ",");
            
            // Get the desired row and desired seat from the user and validate them
            int desiredRow = validateInput("Enter a row number: ", 1, rowMax, input);
            int seat = validateInput("Enter a seat number: ", 1, seatMax, input);
            
            order.add(new OrdersNode(desiredRow, seat, type)); // Add the seat chosen to the order
        }
    }
}