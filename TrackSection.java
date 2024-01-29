/**
 * Represents a section of a track in a train interlocking system.
 */
public class TrackSection {
    public int sectionID;
    public Train train;
    public boolean occupied;

    /**
     * Initializes a new track section with the given section ID.
     *
     * @param sectionID The unique identifier for the track section.
     */
    public TrackSection(int sectionID) {
        this.sectionID = sectionID;
        this.train = null;
        this.occupied = false;
    }

    /**
     * Checks whether the track section is currently occupied by a train.
     *
     * @return True if the track section is occupied; false otherwise.
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Attempts to add a train to the track section.
     *
     * @param train The train to add to the track section.
     * @throws IllegalStateException If the track section is already occupied by a different train.
     */
    public void addTrain(Train train) throws IllegalStateException{
        if (!isOccupied()) {
            this.train = train;
            this.occupied = true;
        } 
        if(this.train == train){
            return;
        }
            throw new IllegalStateException("Track " + sectionID + " is currently occupied.");

    }

    /**
     * Gets the name of the train currently occupying the track section.
     *
     * @return The name of the occupying train, or null if the section is unoccupied.
     */
    public String getTrain() {
        if(train != null){
            return train.getTrainName();
        }
        return null;
    }

    /**
     * Moves the occupying train out of the track section, making it unoccupied.
     */
    public void moveTrain() {
        if (train != null) {
            this.train.move();
            this.occupied = false;
            this.train = null;
        }
    }
}
