/**
 * The main class that demonstrates the functionality of the train interlocking system.
 */
public class Driver {
    public static void main(String[] args) {
        Interlocking network = new InterlockingImpl();

        // Add a new train to the network with a specified path.
        network.addTrain("t", 3, 4);

        // Move the added train in the network. Note that more actions can be uncommented and tested.
        network.moveTrains(new String[]{"t"});
        network.moveTrains(new String[]{"t"});
        //network.moveTrains(new String[]{"t"});

        // Retrieve information about the train's current position or existence.
        System.out.println(network.getTrain("t"));
    }
}
