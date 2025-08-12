import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WritingToAFile {
    private final CommandLineArgs cli;
    private int intCount = 0;
    private int floatCount = 0;
    private int stringCount = 0;
    private Integer intMin = null;
    private Integer intMax = null;
    private Double floatMin = null;
    private Double floatMax = null;
    private Integer shortestStr = null;
    private Integer longestStr = null;
    private long intSum = 0;
    private double floatSum = 0;

    public WritingToAFile(CommandLineArgs cli) {
        this.cli = Objects.requireNonNull(cli, "CommandLineArgs не может быть null");
    }

    public void writingFile(List<String> content) throws IOException {
        Objects.requireNonNull(content, "Содержимое файла не может быть null");

        List<Integer> integers = new ArrayList<>();
        List<Double> floats = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        for (String item : content) {
            item = item.trim();
            if (item.isEmpty()) continue;

            if (isInteger(item)) {
                processInteger(item, integers);
            } else if (isFloat(item)) {
                processFloat(item, floats);
            } else {
                processString(item, strings);
            }
        }

        writeDataToFile(integers, "integers.txt");
        writeDataToFile(floats, "floats.txt");
        writeDataToFile(strings, "strings.txt");
    }

    private void processInteger(String item, List<Integer> integers) {
        int num = Integer.parseInt(item);
        integers.add(num);
        intCount++;
        intSum += num;
        intMin = intMin == null ? num : Math.min(intMin, num);
        intMax = intMax == null ? num : Math.max(intMax, num);
    }

    private void processFloat(String item, List<Double> floats) {
        double num = Double.parseDouble(item);
        floats.add(num);
        floatCount++;
        floatSum += num;
        floatMin = floatMin == null ? num : Math.min(floatMin, num);
        floatMax = floatMax == null ? num : Math.max(floatMax, num);
    }

    private void processString(String item, List<String> strings) {
        strings.add(item);
        stringCount++;
        int length = item.length();
        shortestStr = shortestStr == null ? length : Math.min(shortestStr, length);
        longestStr = longestStr == null ? length : Math.max(longestStr, length);
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isFloat(String s) {
        try {
            Double.parseDouble(s);
            return !s.contains("f") && !s.contains("d"); // Исключаем float/double литералы
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private <T> void writeDataToFile(List<T> data, String defaultFilename) throws IOException {
        if (data.isEmpty()) return;

        String path = getOutputPath(defaultFilename);
        ensureOutputDirectoryExists();

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(path, cli.isAppendMode()))) {
            for (T item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }

    private String getOutputPath(String filename) {
        return cli.getOutputPath() + File.separator + cli.getPrefix() + filename;
    }

    private void ensureOutputDirectoryExists() throws IOException {
        File outputDir = new File(cli.getOutputPath());
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IOException("Не удалось создать директорию: " + cli.getOutputPath());
        }
    }

    public void printStatistics(boolean full) {
        if (!cli.showStatistics()) return;

        System.out.println("\nСтатистика:");
        printIntStatistics(full);
        printFloatStatistics(full);
        printStringStatistics(full);
    }

    private void printIntStatistics(boolean full) {
        System.out.println("Целые числа: " + intCount);
        if (full && intCount > 0) {
            System.out.printf("  Min: %d, Max: %d, Sum: %d, Avg: %.2f%n",
                    intMin, intMax, intSum, (double) intSum / intCount);
        }
    }

    private void printFloatStatistics(boolean full) {
        System.out.println("Вещественные числа: " + floatCount);
        if (full && floatCount > 0) {
            System.out.printf("  Min: %.2f, Max: %.2f, Sum: %.2f, Avg: %.2f%n",
                    floatMin, floatMax, floatSum, floatSum / floatCount);
        }
    }

    private void printStringStatistics(boolean full) {
        System.out.println("Строки: " + stringCount);
        if (full && stringCount > 0) {
            System.out.println("  Самая короткая: " + shortestStr +
                    ", самая длинная: " + longestStr);
        }
    }
}