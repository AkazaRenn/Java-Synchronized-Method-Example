import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Table class simulating the table described in the assignment handout where 
 * the ingredients are placed
 * 
 * @author Frank Xu 101050120
 */
public class Table {
    private int ingredientNumber;
    private int sandwichesMade = 0;
    private Set<String> ingredientsOffering = new HashSet<>(ingredientNumber);
    
    /**
     * @param ingredientNumber number of ingredients can be put onto the table
     */
    public Table(int ingredientNumber) {
        // Table accepts ingredients one less than the total ingredients 
        // available
        this.ingredientNumber = ingredientNumber - 1;
    }
    
    public synchronized int getSandwichesMade() {
        return sandwichesMade;
    }
    
    /**
     * Reset the number of sandwiches made to restart counting
     */
    public synchronized void resetSandwichesMade() {
        sandwichesMade = 0;
    }
    
    /**
     * Returns the other ingredients for the given supply to make a sandwich, 
     * the thread will be put onto wait state otherwise.
     * ingredientsOffering will be cleared once the ingredients are taken.
     * 
     * @param supply the supply the chef already have
     * @return a set of ingredients other than the given supply to make a 
     *         sandwich when the ingredients on the table fits, otherwise the 
     *         thread will be put onto wait state
     */
    public synchronized Set<String> takeIngredientsForSupply(String supply) {
        while(ingredientsOffering.isEmpty() || 
            ingredientsOffering.contains(supply)) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
    
        Set<String> ingredientsToReturn = new HashSet<>(ingredientsOffering);
        ingredientsOffering.clear();
        sandwichesMade++;
        notifyAll();
        return ingredientsToReturn;
    }
    
    /**
     * Put a set of ingredients onto the table, the amount of ingredients is one 
     * less than the number of total available ingredients (to allow one of the 
     * chefs to use them to make a sandwich). The thread will be put onto wait 
     * if there are already ingredients on the table.
     * 
     * @param ingredients a set of ingredients to be put onto the table, the 
     *                    size of the set needs to be 1 less than the total 
     *                    amount of ingredients available
     */
    public synchronized void supplyIngredients(Set<String> ingredients) {
        if(ingredients.size() != ingredientNumber) {
            throw new IllegalArgumentException(
                String.format("Please supply %s ingredient(s) at once.", 
                    ingredientNumber));
        }
        
        while(!ingredientsOffering.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        ingredientsOffering.addAll(ingredients);
        System.out.println(String.format("Agent supplied %s and %s!", 
            ingredientsOffering.toArray()));
        notifyAll();
    }
        
    public static void main(String[] args) {
        String[] INGREDIENTS = {"bread", "butter", "jam"};
        int MAX_SANDWICHES_TO_MAKE = 20;
        
        Table table = new Table(INGREDIENTS.length);
        Agent agent = new Agent(table, MAX_SANDWICHES_TO_MAKE, INGREDIENTS);
        agent.start();
        
        List<Chef> chefs = new ArrayList<>();
        for(int i = 0; i < INGREDIENTS.length; i++) {
            Chef chef = new Chef(INGREDIENTS[i], table);
            chefs.add(chef);
            chef.start();
        }
        
        // wait until the agent is stopped (expected number of sandwiches have 
        // been made)
        while(agent.isAlive());
        System.out.println(
            MAX_SANDWICHES_TO_MAKE + " sandwich(es) in total has been made!");
        System.exit(0);
    }
}
