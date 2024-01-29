import java.util.*;

/**
 * Represents a train with a specified path in a train interlocking system.
 */
public class Train {
    private static final Map<Pair<Integer, Integer>, List<Integer>> allPaths = new HashMap<>();
    public static final Set<String> allTrains = new HashSet<>();

    static {
        allPaths.put(Pair.of(1, 8), List.of(1, 5, 8));
        allPaths.put(Pair.of(1, 9), List.of(1, 5, 9));
        allPaths.put(Pair.of(3, 4), List.of(3, 4));
        allPaths.put(Pair.of(4, 3), List.of(4, 3));
        allPaths.put(Pair.of(9, 2), List.of(9, 6, 2));
        allPaths.put(Pair.of(10, 2), List.of(10, 6, 2));
        allPaths.put(Pair.of(3, 11), List.of(3, 7, 11));
        allPaths.put(Pair.of(11, 3), List.of(11, 7, 3));
    }

    public final String trainName;
    private final int start;
    private final int end;
    private int journeyIndex;

    /**
     * Initializes a new train with a unique name, start section, and end section.
     *
     * @param trainName The unique name of the train.
     * @param start     The starting section for the train's journey.
     * @param end       The destination section for the train's journey.
     * @throws IllegalArgumentException If the train path is invalid or the train name is already in use.
     */
    public Train(String trainName, int start, int end) {
        if (!allPaths.containsKey(Pair.of(start, end)) || allTrains.contains(trainName)) {
            throw new IllegalArgumentException("Invalid train path or train name already in use.");
        }

        this.trainName = trainName;
        this.start = start;
        this.end = end;
        this.journeyIndex = 0;
        allTrains.add(trainName);
    }

    /**
     * Gets the name of the train.
     *
     * @return The name of the train.
     */
    public String getTrainName() {
        return trainName;
    }

    /**
     * Gets the path for the train's journey as a list of section IDs.
     *
     * @return The list of section IDs representing the train's path.
     */
    public List<Integer> getPath() {
        return allPaths.get(Pair.of(this.start, this.end));
    }

    /**
     * Checks if the train is currently in service at the specified index.
     *
     * @param index The index to check in the train's journey.
     * @return True if the train is in service at the given index; false otherwise.
     */
    public boolean isInService(int index) {
        return index < this.getPath().size();
    }

    /**
     * Checks if the train is currently in service at its current position.
     *
     * @return True if the train is in service at its current position; false otherwise.
     */
    public boolean isInService() {
        return isInService(journeyIndex);
    }

    /**
     * Gets the section ID of the train's current position.
     *
     * @return The section ID where the train is currently located, or -1 if not in service.
     */
    public int getSection() {
        return isInService() ? getPath().get(journeyIndex) : -1;
    }

    /**
     * Gets the section ID of the train's next position.
     *
     * @return The section ID of the train's next position, or -1 if not in service at the next index.
     */
    public int getNextSection() {
        return isInService(journeyIndex + 1) ? getPath().get(journeyIndex + 1) : -1;
    }

    /**
     * Gets the destination section ID for the train's journey.
     *
     * @return The section ID of the train's destination.
     */
    public int getDestination() {
        return end;
    }

    /**
     * Moves the train to the next section in its journey.
     *
     * @throws IllegalArgumentException If the train is not in service or has completed its journey.
     */
    public void move() {
        if (isInService()) {
            journeyIndex++;
            if (journeyIndex >= getPath().size()) {
                allTrains.remove(trainName);
            }
        } else {
            throw new IllegalArgumentException("Train is not in service.");
        }
    }

    /**
     * Removes a train with the specified name from the list of all trains.
     *
     * @param trainName The name of the train to remove.
     */
    public static void removeTrain(String trainName) {
        allTrains.remove(trainName);
    }
}
