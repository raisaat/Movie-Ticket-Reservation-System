// RAISAAT RASHID
// Net ID: rar150430
// CS 2336.003: Project 5

package Hashmap;

import java.util.*;

public class HashMapNode 
{
    // Private data fields
    private String username, password; // Variable to hold the username and password
    private ArrayList<OrderDetails> orderDetails = new ArrayList<>(); // ArrayList to hold all the orders
    
    // Default constructor
    public HashMapNode() {}
    
    // Overloaded constructor
    public HashMapNode (String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    // Accessors
    public String getPass () { return password; }
    public String getUsername () { return username; }
    public ArrayList<OrderDetails> getOrderDetails () { return orderDetails; }
    
    // Mutators
    public void setPass (String pass) { password = pass; }
    public void setUsername (String username) { this.username = username; }
    public void setOrderDetails (ArrayList<OrderDetails> orderDetails) { this.orderDetails = orderDetails; }
    
    // Function setOrder changes an existing order to the order that is passed in
    public void setOrder (int orderNum, OrderDetails order) { orderDetails.set(orderNum - 1, order); }

    // Function addOrder adds an order to the list of existing orders
    public void addOrder(OrderDetails order) { orderDetails.add(order); }
}