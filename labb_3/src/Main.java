package labb_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

// Основной класс
public class Main {
    // Константа для файла словаря
    private static final String DICTIONARY_FILE_PATH_IN_RESOURCES = "/words.txt";

    public static void main(String[] args) {
        Dictionary translator = null;
        try {
            translator = new Dictionary(DICTIONARY_FILE_PATH_IN_RESOURCES);
        } catch (InvalidFileFormatException | FileReadException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Dictionary successfully loaded!");

        System.out.println("========================================");
        System.out.println("Enter text for translation:");
        System.out.println("========================================");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        System.out.println("========================================");
        System.out.println("Translation:");
        System.out.println("========================================");
        System.out.println(translator.translateText(input));
        System.out.println("========================================");
    }
}

// Класс для перевода текста
class Dictionary {
    private final Map<String, String> dictionary = new HashMap<>();

    public Dictionary(String resourcePath) throws InvalidFileFormatException, FileReadException {
        URL resource = getClass().getResource(resourcePath);
        if (resource == null) {
            throw new FileReadException("Dictionary file not found in resources: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 2) {
                    throw new InvalidFileFormatException("Invalid file format: " + line);
                }
                dictionary.put(parts[0].trim().toLowerCase(), parts[1].trim().toLowerCase());
            }
        } catch (IOException e) {
            throw new FileReadException("Error reading resource file: " + e.getMessage());
        }
    }

    public String translateText(String input) {
        String[] words = input.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int wordId = 0; wordId < words.length; wordId++) {
            String word = words[wordId].toLowerCase();
            String bestMatch = findBestMatch(word);
            if (bestMatch != null) {
                result.append(dictionary.get(bestMatch));
            } else {
                result.append(words[wordId]);
            }
            if (wordId < words.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    private String findBestMatch(String input) {
        return dictionary.keySet().stream()
                .filter(input::startsWith)
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }
}

// Исключение для ошибок чтения файла
class FileReadException extends Exception {
    public FileReadException(String message) {
        super(message);
    }
}

// Исключение для ошибок формата файла
class InvalidFileFormatException extends Exception {
    public InvalidFileFormatException(String message) {
        super(message);
    }
}
