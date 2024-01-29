import java.util.*;

/**
 * The InterlockingImpl class implements the Interlocking interface, which represents a railway interlocking system.
 * It manages train movement between track sections, ensuring that constraints and priorities are observed.
 */
public class InterlockingImpl implements Interlocking {
    private final Map<Integer, TrackSection> sections;
    private final Map<String, Train> trains;
    private final Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> constraints;
    private final Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> priority;
    private final Set<Pair<Integer, Integer>> prioritySet;

    /**
     * Constructor to initialize the Interlocking system.
     */
    public InterlockingImpl() {
        sections = new HashMap<>();
        for (int i = 1; i < 12; i++) {
            sections.put(i, new TrackSection(i));
        }
        trains = new HashMap<>();

        constraints = new HashMap<>();
        constraints.put(Pair.of(4, 3), Set.of(Pair.of(3, 4)));
        constraints.put(Pair.of(3, 4), Set.of(Pair.of(4, 3)));
        constraints.put(Pair.of(3, 11), Set.of(Pair.of(11, 3), Pair.of(7, 3)));
        constraints.put(Pair.of(11, 3), Set.of(Pair.of(3, 11), Pair.of(7, 11)));
        constraints.put(Pair.of(1, 9), Set.of(Pair.of(9, 2)));
        constraints.put(Pair.of(9, 2), Set.of(Pair.of(1, 9), Pair.of(5, 9)));

        priority = new HashMap<>();
        priority.put(Pair.of(3, 4), Set.of(
            Pair.of(1, 5),
            Pair.of(6, 2)
        ));
        priority.put(Pair.of(4, 3), Set.of(
            Pair.of(1, 5),
            Pair.of(6, 2)
        ));
        priority.put(Pair.of(9, 6), Set.of(
            Pair.of(5, 8),
            Pair.of(10, 6)
        ));
        prioritySet = new HashSet<>();
        for (Set<Pair<Integer, Integer>> item : priority.values()) {
            prioritySet.addAll(item);
        }
    }

    /**
     * Checks the priority of a given section transition according to the interlocking system's priority rules.
     *
     * This method examines whether a specific section transition has higher priority, based on the priority rules defined
     * for the interlocking system. It checks if there is a higher-priority train that is currently occupying the target
     * section of the transition. If such a train exists and is moving to the same destination as the current train, it
     * indicates that the current train should wait to ensure safety and compliance with priority rules.
     *
     * @param key The section transition represented as a pair of section IDs (source and target).
     * @return True if there is a higher-priority train moving to the target section; false otherwise.
     */
    private boolean checkPriority(Pair<Integer, Integer> key) {
        Set<Pair<Integer, Integer>> priorityTarget = priority.get(key);
        if (priorityTarget == null) {
            return false;
        }
        for (Pair<Integer, Integer> target : priorityTarget) {
            TrackSection section = sections.get(target.first);
            int targetDestination = target.second;
            if (!section.isOccupied()) {
                continue;
            }
            if (section.train.getNextSection() == targetDestination) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if a train can be moved based on constraints and priorities.
     *
     * @param train The train to check for movability.
     * @return True if the train can be moved, false otherwise.
     */
    private boolean isMovable(Train train) {
        TrackSection nextSection = sections.get(train.getNextSection());
        Pair<Integer, Integer> key = Pair.of(train.getSection(), train.getNextSection());
        if (nextSection == null) {
            return true;
        }
        if (nextSection.isOccupied() || checkPriority(key)) {
            return false;
        }
        return true;
    }

    /**
     * Moves a train to the next track section if it is movable.
     *
     * @param train The train to be moved.
     * @return True if the train was successfully moved, false if it couldn't be moved.
     */
    private boolean moveTrain(Train train) {
        if (isMovable(train)) {
            TrackSection currentSection = sections.get(train.getSection());
            TrackSection nextSection = sections.get(train.getNextSection());
            currentSection.moveTrain();
            if (nextSection != null) {
                nextSection.addTrain(train);
            }
            return true;
        }
        return false;
    }

    /**
     * Adds a new train to the interlocking system, checking for constraints and priorities.
     *
     * @param trainName              The name of the train.
     * @param entryTrackSection      The entry track section for the train.
     * @param destinationTrackSection The destination track section for the train.
     * @throws IllegalArgumentException If the train cannot be added due to constraints or invalid path.
     * @throws IllegalStateException    If the train cannot be added due to full sections.
     */
    @Override
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection)
            throws IllegalArgumentException, IllegalStateException {
        //Check if any constraint is violated
        Pair<Integer, Integer> key = Pair.of(entryTrackSection, destinationTrackSection);
        Set<Pair<Integer, Integer>> value = constraints.get(key);
        if (value != null) {
            for (Pair<Integer, Integer> c : value) {
                TrackSection constraintSection = sections.get(c.first);
                int targetSection = c.second;
                if (constraintSection.isOccupied()) {
                    if (constraintSection.train.getDestination() == targetSection) {
                        throw new IllegalStateException("Constraint not met: trying to add a train heading for " +
                                destinationTrackSection + " from " + entryTrackSection + ", but a train heading for "
                                + targetSection + " is on " + c.first);
                    }
                }
            }
        }

        Train newTrain = new Train(trainName, entryTrackSection, destinationTrackSection);
        sections.get(entryTrackSection).addTrain(newTrain);
        trains.put(trainName, newTrain);
    }

    /**
     * Moves a list of trains, prioritizing trains with higher priority, while checking for constraints.
     *
     * @param trainNames An array of train names to be moved.
     * @return The number of trains successfully moved.
     * @throws IllegalArgumentException If one of the trains is not in service.
     */
    @Override
    public int moveTrains(String[] trainNames) throws IllegalArgumentException {
        int count = 0;
        List<String> priorityNames = new ArrayList<>();
        List<String> nonPriorityNames = new ArrayList<>();
        //First pass - check illegal exception and prioritise priority sets.
        for (String name : trainNames) {
            //Check if train is in service
            if (!Train.allTrains.contains(name)) {
                throw new IllegalArgumentException("Train " + name + " is not in service.");
            }
            Train train = trains.get(name);
            Pair<Integer, Integer> key = Pair.of(train.getSection(), train.getNextSection());
            if (prioritySet.contains(key)) {
                priorityNames.add(name);
                continue;
            }
            nonPriorityNames.add(name);
        }
        priorityNames.addAll(nonPriorityNames);
        //Second pass - move trains
        for (String name : priorityNames) {
            Train train = trains.get(name);
            boolean isMoved = moveTrain(train);
            if (isMoved) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Gets the name of the train occupying a specific track section.
     *
     * @param trackSection The track section to query.
     * @return The name of the train occupying the section or null if the section is unoccupied.
     * @throws IllegalArgumentException If the specified track section does not exist.
     */
    @Override
    public String getSection(int trackSection) throws IllegalArgumentException {
        TrackSection section = sections.get(trackSection);

        if (section == null) {
            throw new IllegalArgumentException("Track section does not exist");
        }

        return section.getTrain();
    }

    /**
     * Gets the current track section of a train by its name.
     *
     * @param trainName The name of the train.
     * @return The track section where the train is located.
     * @throws IllegalArgumentException If the specified train name does not exist.
     */
    @Override
    public int getTrain(String trainName) throws IllegalArgumentException {
        Train train = trains.get(trainName);

        if (train == null) {
            throw new IllegalArgumentException("Train name does not exist");
        }

        return train.getSection();
    }

    public static void removeTrain(String trainName) {
        Train.removeTrain(trainName);
    }
}