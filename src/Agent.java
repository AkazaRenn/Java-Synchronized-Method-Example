import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Agent class simulating the agent described in the assignment handout which 
 * repeatedly puts ingredients on the table for chefs to take to make sandwiches 
 * 
 * @author Frank Xu 101050120
 */
public class Agent extends Thread {
    private Table table;
    private List<String> ingredients;
    private int maxSupplyNumber;
    private Random rand = new Random();
    
    /**
     * @param table the table where the ingredients are placed
     * @param maxSupplyNumber max number of supplies to provide which is also 
     *                        the amx number of sandwiches can be made
     * @param ingredients ingredients the agent can provide
     */
    public Agent(Table table, int maxSupplyNumber, String...ingredients) {
        this.table = table;
        this.maxSupplyNumber = maxSupplyNumber;
        this.ingredients = Arrays.asList(ingredients);
    }
    
    /**
     * Put all the ingredients but a randomly selected one on the table
     */
    private void supplyIngredients() {
        Set<String> ingredientsToOffer = new HashSet<>(ingredients.size() - 1);
        // randomly select an ingredient not to supply, put all other ingredients 
        int ingredientNotSuppliedIndex = rand.nextInt(ingredients.size());
        ingredientsToOffer.addAll(ingredients.subList(
            0, ingredientNotSuppliedIndex));
        ingredientsToOffer.addAll(ingredients.subList(
            ingredientNotSuppliedIndex + 1, ingredients.size()));
        table.supplyIngredients(ingredientsToOffer);
    }
    
    public List<String> getIngredients() {
        return ingredients;
    }
    
    /**
     * Repeatedly put ingredients onto the table until the max supply number is 
     * reached
     */
    @Override
    public void run() {
        while(maxSupplyNumber-- > 0) {
            supplyIngredients();
        }
    }
    
}
