import java.io.*;

public class Merge {
    public static void main(String[] args) {

        String inputFile1 = "C:\\Users\\admin\\IdeaProjects\\Input_Output\\src\\input1.txt";
        String inputFile2 = "C:\\Users\\admin\\IdeaProjects\\Input_Output\\src\\input2.txt";
        String mergedFile = "merge.txt";
        String commonFile = "common.txt";

        try {

            // Read the numbers from the first file.
            int[] nums = readIntegersFromFile(inputFile1);

            // Read the numbers from the second file.
            int[] nums2 = readIntegersFromFile(inputFile2);

            // Merge the numbers together and place them in a new array.
            int[] mergedNums = mergeArrays(nums, nums2);

            // Write the merged numbers into a new file.
            writeIntegersToFile(mergedNums, mergedFile);

            // Get the common numbers from the two files.
            int[] commonNums = findCommonIntegers(nums, nums2);

            // Write the common numbers to a new file.
            writeIntegersToFile(commonNums, commonFile);

            System.out.println("Successfully merged!");

        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing files: " + e.getMessage());

        } catch (NumberFormatException e) {
            System.out.println("Invalid integer format in the input files: " + e.getMessage());

        }
    }

    private static int[] readIntegersFromFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int[] integers = new int[0];

            while ((line = reader.readLine()) != null) {
                // Convert the line to an integer and add it to the array.
                int number = Integer.parseInt(line);
                integers = appendToArray(integers, number);
            }

            return integers;
        }
    }

    private static int[] mergeArrays(int[] array1, int[] array2) {
        int[] mergedArray = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, mergedArray, 0, array1.length);
        System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
        return mergedArray;
    }

    private static int[] findCommonIntegers(int[] array1, int[] array2) {
        int[] commonArray = new int[Math.min(array1.length, array2.length)];
        int index = 0;

        for (int num1 : array1) {
            for (int num2 : array2) {
                if (num1 == num2) {
                    commonArray[index] = num1;
                    index++;
                    // When a common integer is found, move to the next number in array1.
                    break;
                }
            }
        }

        // Trims the commonArray to remove unused elements.
        return trimArray(commonArray, index);
    }

    private static void writeIntegersToFile(int[] integers, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int number : integers) {
                writer.write(String.valueOf(number));
                writer.newLine();
            }
        }
    }

    private static int[] appendToArray(int[] array, int number) {
        int[] newArray = new int[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = number;
        return newArray;
    }

    private static int[] trimArray(int[] array, int length) {
        int[] trimmedArray = new int[length];
        System.arraycopy(array, 0, trimmedArray, 0, length);
        return trimmedArray;
    }
}
