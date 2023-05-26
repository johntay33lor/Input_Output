package IO.Project;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class fileManager {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String directoryPath = getDirectoryPath(scanner);

        try {
            displayDirectoryContents(directoryPath);
            copyMoveDeleteFile(scanner, directoryPath);
            createDeleteDirectory(scanner, directoryPath);
            searchFiles(scanner, directoryPath);
        } catch (IOException e) {
            System.err.println("An error occurred while accessing the directory: " + e.getMessage());
        }
    }

    // Get the directory path from the user
    private static String getDirectoryPath(Scanner scanner) {
        System.out.print("Enter the directory path: ");
        return scanner.nextLine();
    }

    // Display the contents of the specified directory
    private static void displayDirectoryContents(String directoryPath) throws IOException {
        System.out.println("Directory Contents:");
        Files.walkFileTree(Paths.get(directoryPath), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String fileName = file.getFileName().toString();
                long fileSize = Files.size(file);
                LocalDateTime lastModified = LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneOffset.UTC);

                System.out.println("Name: " + fileName);
                System.out.println("Size: " + fileSize + " bytes");
                System.out.println("Last Modified: " + lastModified.format(formatter));
                System.out.println();

                return FileVisitResult.CONTINUE;
            }
        });
    }

    // Copy, move, or delete files within the specified directory
    private static void copyMoveDeleteFile(Scanner scanner, String directoryPath) throws IOException {
        System.out.print("Enter the file name to copy/move/delete (including the extension): ");
        String fileName = scanner.nextLine();

        Path sourceFile = Paths.get(directoryPath, fileName);
        if (!Files.exists(sourceFile)) {
            System.out.println("File not found.");
            return;
        }

        System.out.print("Enter the destination directory path: ");
        String destinationDirectory = scanner.nextLine();

        Path destinationFile = Paths.get(destinationDirectory, fileName);

        System.out.println("Select an option:");
        System.out.println("1. Copy");
        System.out.println("2. Move");
        System.out.println("3. Delete");
        System.out.print("Enter your choice:");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied successfully.");
                break;
            case 2:
                Files.move(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File moved successfully.");
                break;
            case 3:
                Files.delete(sourceFile);
                System.out.println("File deleted successfully.");
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    // Create or delete directories within the specified directory
    private static void createDeleteDirectory(Scanner scanner, String directoryPath) {
        System.out.println("Select an option:");
        System.out.println("1. Create Directory");
        System.out.println("2. Delete Directory");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the directory name: ");
        String directoryName = scanner.nextLine();

        Path directory = Paths.get(directoryPath, directoryName);

        switch (choice) {
            case 1:
                try {
                    Files.createDirectory(directory);
                    System.out.println("Directory created successfully.");
                } catch (IOException e) {
                    System.err.println("Failed to create directory: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    Files.delete(directory);
                    System.out.println("Directory deleted successfully.");
                } catch (IOException e) {
                    System.err.println("Failed to delete directory: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    // Search for files within the specified directory based on file name or extension
    private static void searchFiles(Scanner scanner, String directoryPath) throws IOException {
        System.out.println("Search Directory: " + directoryPath); // Display the search directory path

        System.out.print("Enter the search keyword: ");
        String keyword = scanner.nextLine();

        System.out.println("Search Results:");
        final boolean[] matchFound = {false}; // Array to hold the matchFound flag

        Files.walkFileTree(Paths.get(directoryPath), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String fileName = file.getFileName().toString();
                if (fileName.contains(keyword)) {
                    System.out.println("Name: " + fileName);
                    System.out.println("Path: " + file.getParent());
                    System.out.println();
                    matchFound[0] = true; // Update the value in the array
                }
                return FileVisitResult.CONTINUE;
            }
        });

        if (!matchFound[0]) {
            System.out.println("No matching files found.");
        }
    }
}
