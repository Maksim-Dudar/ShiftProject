import java.util.ArrayList;
import java.util.List;

public class CommandLineArgs {
    private String outputPath = ".";
    private String prefix = "";
    private boolean appendMode = false;
    private boolean showStats = false;
    private boolean fullStats = false;
    private final List<String> inputFiles = new ArrayList<>();

    public CommandLineArgs(String[] args) throws IllegalArgumentException {
        if (args == null) {
            throw new IllegalArgumentException("Аргументы не могут быть null");
        }

        try {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i].trim();

                switch (arg) {
                    case "-o":
                        validateNextArg(args, i, "Отсутствует путь после -o");
                        outputPath = args[++i].trim();
                        break;
                    case "-p":
                        validateNextArg(args, i, "Отсутствует префикс после -p");
                        prefix = args[++i].trim();
                        break;
                    case "-a":
                        appendMode = true;
                        break;
                    case "-s":
                        showStats = true;
                        break;
                    case "-f":
                        showStats = true;
                        fullStats = true;
                        break;
                    default:
                        if (!arg.startsWith("-")) {
                            inputFiles.add(arg);
                        } else {
                            throw new IllegalArgumentException("Неизвестный аргумент: " + arg);
                        }
                }
            }

            if (inputFiles.isEmpty()) {
                throw new IllegalArgumentException("Не указаны входные файлы");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Некорректные аргументы командной строки");
        }
    }

    private void validateNextArg(String[] args, int currentIndex, String errorMessage) {
        if (currentIndex + 1 >= args.length || args[currentIndex + 1].startsWith("-")) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // Геттеры
    public List<String> getInputFiles() { return inputFiles; }
    public String getOutputPath() { return outputPath; }
    public String getPrefix() { return prefix; }
    public boolean isAppendMode() { return appendMode; }
    public boolean showStatistics() { return showStats; }
    public boolean showFullStats() { return fullStats; }
}