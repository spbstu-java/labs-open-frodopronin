package lab_4;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Тест метода вычисления среднего
        System.out.println("========================================");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("Список: " + numbers);
        System.out.println("Среднее значение: " + calculateMean(numbers).orElse(0.0));

        // Тест метода преобразования строк
        System.out.println("========================================");
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        System.out.println("Список: " + strings);
        System.out.println("Модифицированные строки: " + modifyStrings(strings));

        // Тест метода уникальных квадратов
        System.out.println("========================================");
        List<Integer> duplicateNumbers = Arrays.asList(1, 2, 2, 3, 4, 4, 5);
        System.out.println("Список: " + duplicateNumbers);
        System.out.println("Уникальные квадраты: " + getDistinctSquares(duplicateNumbers));

        // Тест метода получения последнего элемента
        System.out.println("========================================");
        List<String> moreStrings = Arrays.asList("first", "second", "third");
        System.out.println("Список: " + moreStrings);
        System.out.println("Финальный элемент: " + fetchLastItem(moreStrings));
        System.out.println("----------------------------------------");
        List<String> moreStringsEmpty = List.of();
        System.out.println("Список: " + moreStringsEmpty);
        try {
            System.out.println("Финальный элемент: " + fetchLastItem(moreStringsEmpty));
        } catch (Exception e) {
            System.out.println("Возникла ошибка: " + e.getMessage());
        }

        // Тест метода суммы четных чисел
        System.out.println("========================================");
        int[] numArray = {1, 2, 3, 4, 5, 6};
        System.out.println("Массив: " + Arrays.toString(numArray));
        System.out.println("Сумма четных: " + calculateEvenSum(numArray));
        System.out.println("----------------------------------------");
        int[] numArrayWithoutEven = {1, 3, 5, 7, 9, 11};
        System.out.println("Массив: " + Arrays.toString(numArrayWithoutEven));
        System.out.println("Сумма четных: " + calculateEvenSum(numArrayWithoutEven));

        // Тест метода конвертации в карту
        System.out.println("========================================");
        List<String> stringList = Arrays.asList("apple", "banana", "cherry");
        System.out.println("Список: " + stringList);
        System.out.println("Результирующая карта: " + createCharacterMap(stringList));

        System.out.println("========================================");
    }

    // Альтернативная реализация вычисления среднего
    public static OptionalDouble calculateMean(List<Integer> numbers) {
        return numbers.stream()
                .mapToDouble(Integer::doubleValue)
                .average();
    }

    // Другой способ преобразования строк
    public static List<String> modifyStrings(List<String> strings) {
        return strings.stream()
                .map(String::toUpperCase)
                .map(s -> "_new_" + s)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Уникальные квадраты через Set
    public static List<Integer> getDistinctSquares(List<Integer> numbers) {
        return numbers.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() == 1)
                .map(e -> e.getKey() * e.getKey())
                .collect(Collectors.toList());
    }

    // Получение последнего элемента через итератор
    public static <T> T fetchLastItem(Collection<T> collection) {
        if (collection.isEmpty()) {
            throw new NoSuchElementException("Коллекция пуста");
        }

        Iterator<T> iterator = collection.iterator();
        T last = iterator.next();

        while (iterator.hasNext()) {
            last = iterator.next();
        }
        return last;
    }

    // Сумма четных чисел с использованием IntStream
    public static int calculateEvenSum(int[] numbers) {
        return Arrays.stream(numbers)
                .filter(n -> n % 2 == 0)
                .reduce(0, Integer::sum);
    }

    // Конвертация в карту с проверкой дубликатов
    public static Map<Character, String> createCharacterMap(List<String> strings) {
        return strings.stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toMap(
                        s -> s.charAt(0),
                        s -> s.substring(1),
                        (existing, replacement) -> existing
                ));
    }
}