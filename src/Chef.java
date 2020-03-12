/**
 * Chef class simulating the chef described in the assignment handout that 
 * repreatedly takes ingredients from table and combine them with the supply it 
 * has to make a sandwich and then eat it
 * 
 * @author Frank Xu 101050120
 */
public class Chef extends Thread {
    private String supply;
    private Table table;
    
    /**
     * @param supply the supply the chef has, all other ingredients are needed 
     *               to make a sandwich
     * @param table the table both chefs and agents will be working on
     */
    public Chef(String supply, Table table) {
        this.supply = supply;
        this.table = table;
    }
    
    /**
     * The chef repeatedly takes ingredients from table and make sandwiches 
     * with them and the supply it has and then ate it. Making and eating 
     * sandwiches are presented by the console output.
     */
    @Override
    public void run() {
        while(true) {
            // Because we are only "pretending" making a sandwich so we don't 
            // actually need the return value, just let the ingredients on table 
            // cleared.
            table.takeIngredientsForSupply(supply);
            System.out.println(String.format(
                "Chef with %s made a sandwich and ate it!", supply));
        }
    }
    
}
