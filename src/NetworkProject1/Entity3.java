package NetworkProject1;

public class Entity3 extends Entity
{    
    // Perform any necessary initialization in the constructor
    public Entity3()
    {
    	//sets all values in array to 999
    	for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
              distanceTable[i][j] = 999;
            }
        }
        //adding initial known values to table
        distanceTable[3][0] = 7;
        distanceTable[3][2] = 2;
        distanceTable[3][3] = 0;

       
        int[] currentShortestPath = new int[4];
        for(int i = 0; i < 4; i++){
        	currentShortestPath[i]=distanceTable[3][i];
            //sets all the value of the distance from a node to itself to 0
            distanceTable[i][i] = 0;
         }
        
        //creates a packet and sends packet to 0
        Packet dtPacket = new Packet(3, 0, currentShortestPath);
        NetworkSimulator.toLayer2(dtPacket);
        
        //creates a packet and sends packet to 2
        dtPacket = new Packet(3, 2, currentShortestPath);
        NetworkSimulator.toLayer2(dtPacket);
        

        
        System.out.println("Entity3 initialized");
        printDT();
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    public void update(Packet p)
    {
    	boolean ready = false;
      	 // creates an array of this Entity's current shortest path. 
      	 // used for ease of access
           int[] currentShortestPath = new int[4];
           for(int i = 0; i < 4; i++){
           	
           	currentShortestPath[i]=distanceTable[p.getDest()][i];
             
           }
           //i is the node that will have the distances to it compared
           for(int i = 0; i<4; i++){
          	 // if yourShortestPath to i + my shortest path to you < my current shortest path to i
             if(p.getMincost(i)+currentShortestPath[p.getSource()] < distanceTable[p.getDest()][i]){
               //then set my current shortest path to i equal to your shortest path to i + my shortest path to you
          	   distanceTable[p.getDest()][i] = p.getMincost(i)+currentShortestPath[p.getSource()];
               //Since there was a change to the entity's list of shortest paths 
               //all other entity's must be sent the update
               ready = true;
               }
            }
           // Adds the minimum cost path from packet to table
           for(int i = 0; i<4; i++){
        		if (distanceTable[p.getSource()][i]>p.getMincost(i)){
            		distanceTable[p.getSource()][i]=p.getMincost(i);
            	}
           }
           
          
           
           if(ready){
               //creates a packet and sends packet to 0
               Packet dtPacket = new Packet(3, 0, currentShortestPath);
               NetworkSimulator.toLayer2(dtPacket);
               
               //creates a packet and sends packet to 2
               dtPacket = new Packet(3, 2, currentShortestPath);
               NetworkSimulator.toLayer2(dtPacket);
               
           }
           System.out.println("Entity3 Update Complete. Distance Table is:");
           printDT();
    }
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }
    
 public void printDT()
       {
           System.out.println();
           System.out.println("           via");
           System.out.println(" D3 |  0   1   2   3");
           System.out.println("----+-----------------");
           for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
           {
               System.out.print("   " + i + "|");
               for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
               {
                   if (distanceTable[i][j] < 10)
                   {
                       System.out.print("   ");
                   }
                   else if (distanceTable[i][j] < 100)
                   {
                       System.out.print("  ");
                   }
                   else
                   {
                       System.out.print(" ");
                   }

                   System.out.print(distanceTable[i][j]);
               }
               System.out.println();
           }
    }
}