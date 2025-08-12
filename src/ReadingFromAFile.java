import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReadingFromAFile {
    private final List<String> inputFiles;

    public ReadingFromAFile(List<String> inputFiles) {
        this.inputFiles = Objects.requireNonNull(inputFiles,
                "Список входных файлов не может быть null");
    }

    public List<String> readingFile() {
        List<String> lines = new ArrayList<>();
        boolean anyFileRead = false;

        for (String fileName : inputFiles) {
            File file = new File(fileName.trim());

            if (!file.exists()) {
                System.err.println("Файл не существует: " + file.getAbsolutePath());
                continue;
            }

            if (!file.isFile()) {
                System.err.println("Указанный путь не является файлом: " + file.getAbsolutePath());
                continue;
            }

            if (!file.canRead()) {
                System.err.println("Нет прав на чтение файла: " + file.getAbsolutePath());
                continue;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String trimmedLine = line.trim();
                    if (!trimmedLine.isEmpty()) {
                        lines.add(trimmedLine);
                    }
                }
                anyFileRead = true;
            } catch (IOException e) {
                System.err.printf("Ошибка при чтении файла %s: %s%n",
                        file.getAbsolutePath(), e.getMessage());
            }
        }

        if (!anyFileRead) {
            System.err.println("Ни один из файлов не был успешно прочитан.");
        } else if (lines.isEmpty()) {
            System.err.println("Файлы были прочитаны, но не содержали данных.");
        }

        return lines;
    }
}