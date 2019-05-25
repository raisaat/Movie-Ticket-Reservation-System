// RAISAAT RASHID

package Hashmap;

public class OrdersNode 
{
    // Private data fields
    private int row, seat; // Variables to hold the seat and row numbers
    private String type; // Object to hold the ticket type
    
    // Default constructor
    public OrdersNode () {}
    
    // Overloaded constructor
    public OrdersNode(int row, int seat, String type)
    {
        this.row = row;
        this.seat = seat;
        this.type = type;
    }
    
    // Mutators
    public void setRow (int r) { row = r; }
    public void setSeat (int s) { seat = s; }
    public void setTicketType (String str) { type = str; }
    
    // Accessors
    public int getRow () { return row; }
    public int getSeat () { return seat; }
    public String getTicketType () { return type; }
}
