public abstract class Staff {
    private final String name;
    protected boolean taskDone;

    public Staff(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff name cannot be empty.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean canPerformTask() {
        return !taskDone;
    }

    public void resetTurn() {
        taskDone = false;
    }

    protected void checkAvailable() {
        if (taskDone) {
            throw new IllegalStateException(name + " has already acted this turn.");
        }
    }
}
