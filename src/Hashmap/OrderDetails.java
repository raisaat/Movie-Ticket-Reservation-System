// RAISAAT RASHID
// Net ID: rar150430
// CS 2336.003: Project 5

package Hashmap;

import java.util.*;

public class OrderDetails 
{
    // Private data fields
    private int adultNum, seniorNum, childNum; // Variables to hold the no. of each ticket type for the order
    private int auditoriumNum; // Variable to hold the auditorium no. for the order
    private ArrayList<OrdersNode> orders; // ArrayList to hold the seats selected for the order
    
    // default constructor
    public OrderDetails() {}
    
    // Overloaded constructor
    public OrderDetails (int auditNum, int adNum, int seNum, int chNum, ArrayList<OrdersNode> ord)
    {
        auditoriumNum = auditNum;
        adultNum = adNum;
        seniorNum = seNum;
        childNum = chNum;
        orders = ord;
    }
    
    // Mutators
    public void setAuditoriumNum (int auditNum) { auditoriumNum = auditNum; }
    public void setAdultNum (int num) { adultNum = num; }
    public void setSeniorNum (int num) { seniorNum = num; }
    public void setChildNum (int num) { childNum = num; }
    public void setOrders (ArrayList<OrdersNode> ord) { orders = ord; }
    
    // Accessors
    public int getAuditoriumNum () { return auditoriumNum; }
    public int getAdultNum () { return adultNum; }
    public int getSeniorNum () { return seniorNum; }
    public int getChildNum () { return childNum; }
    public ArrayList<OrdersNode> getOrders () { return orders; }
}
