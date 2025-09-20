import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    private static final Map<String, Consumer<Character>> ACTION_MAP = new HashMap<>();

    static {
        ACTION_MAP.put("1", character -> character.setMovementMethod(new MovingStrategyNone()));
        ACTION_MAP.put("2", character -> character.setMovementMethod(new MovingStrategyWalking()));
        ACTION_MAP.put("3", character -> character.setMovementMethod(new MovingStrategySlowStep()));
        ACTION_MAP.put("4", character -> character.setMovementMethod(new MovingStrategyOnHorse()));
        ACTION_MAP.put("5", character -> character.setMovementMethod(new MovingStrategyHelicopter()));
    }

    public static void main(String[] args) {
        Character monarch = new Character("Monarch", new MovingStrategyWalking());
        Character antagonist = new Character("Antagonist", new MovingStrategySlowStep());
        Character hero = new Character("Hero", new MovingStrategyNone());

        printInitialState(monarch, antagonist, hero);
        printAvailableActions();

        Scanner inputReader = new Scanner(System.in);
        processUserInput(inputReader, monarch, antagonist, hero);

        System.out.println("////////////////////////////////////////");
    }

    private static void printInitialState(Character... characters) {
        System.out.println("////////////////////////////////////////");
        System.out.println("Initial positions:");
        System.out.println("////////////////////////////////////////");
        for (Character character : characters) {
            System.out.println(character);
        }
    }

    private static void printAvailableActions() {
        System.out.println("////////////////////////////////////////");
        System.out.println("Available actions:");
        System.out.println("////////////////////////////////////////");
        System.out.println("0 - exit");
        System.out.println("1 - remain stationary");
        System.out.println("2 - proceed by foot");
        System.out.println("3 - advance with slow step");
        System.out.println("4 - travel on horseback");
        System.out.println("5 - transport on helicopter");
        System.out.println("////////////////////////////////////////");
    }

    private static void processUserInput(Scanner inputReader, Character monarch,
                                         Character antagonist, Character hero) {
        while (true) {
            System.out.print("Select action: ");
            String input = inputReader.nextLine();

            if ("0".equals(input)) {
                break;
            }

            Consumer<Character> action = ACTION_MAP.get(input);
            if (action != null) {
                action.accept(hero);
                performMovementCycle(monarch, antagonist, hero);
            } else {
                System.out.println("Invalid action: " + input + ". Please try again.");
            }
        }
    }

    private static void performMovementCycle(Character... characters) {
        System.out.println("////////////////////////////////////////");
        System.out.println("Movement completed:");

        for (Character character : characters) {
            character.performMovement();
            System.out.println(character);
        }
    }
}

class Character {
    private final String title;
    private final Point coordinates;
    private IMovingStrategy movementMethod;

    public Character(String title, IMovingStrategy movementMethod) {
        this.title = title;
        this.coordinates = new Point(0, 0);
        this.movementMethod = movementMethod;
    }

    @Override
    public String toString() {
        String movementName = movementMethod.getClass().getSimpleName()
                .replace("MovingStrategy", "")
                .replace("None", "Stationary");

        return String.format("Character name: %-12s\tPosition: %.1f, %.1f\t\tMovement: %s",
                title, coordinates.getX(), coordinates.getY(), movementName);
    }

    public IMovingStrategy getMovementMethod() {
        return movementMethod;
    }

    public void setMovementMethod(IMovingStrategy movementMethod) {
        this.movementMethod = movementMethod;
    }

    void performMovement() {
        this.movementMethod.move(this.coordinates);
    }
}

interface IMovingStrategy {
    void move(Point position);
}

class MovingStrategyHelicopter implements IMovingStrategy {
    @Override
    public void move(Point position) {
        position.setLocation(position.getX() + 100, position.getY());
    }
}

class MovingStrategyNone implements IMovingStrategy {
    @Override
    public void move(Point position) {
        // Без движения
    }
}

class MovingStrategyOnHorse implements IMovingStrategy {
    @Override
    public void move(Point position) {
        position.setLocation(position.getX() + 30, position.getY());
    }
}

class MovingStrategySlowStep implements IMovingStrategy {
    @Override
    public void move(Point position) {
        position.setLocation(position.getX() + 10, position.getY());
    }
}

class MovingStrategyWalking implements IMovingStrategy {
    @Override
    public void move(Point location) {
        location.setLocation(location.getX() + 20, location.getY());
    }
}