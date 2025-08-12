import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            CommandLineArgs cli = new CommandLineArgs(args);
            ReadingFromAFile reader = new ReadingFromAFile(cli.getInputFiles());
            List<String> content = reader.readingFile();

            if (!content.isEmpty()) {
                WritingToAFile writer = new WritingToAFile(cli);
                writer.writingFile(content);
                writer.printStatistics(cli.showFullStats());
            } else {
                System.out.println("Нет данных для обработки.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка аргументов: " + e.getMessage());
            printUsage();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
            System.exit(3);
        }
    }

    private static void printUsage() {
        System.out.println("Использование:");
        System.out.println("java Main [опции] file1.txt file2.txt ...");
        System.out.println("Опции:");
        System.out.println("  -o <путь>    Каталог для выходных файлов (по умолчанию текущий)");
        System.out.println("  -p <префикс> Префикс имен выходных файлов");
        System.out.println("  -a           Добавлять в существующие файлы (по умолчанию перезапись)");
        System.out.println("  -s           Вывести краткую статистику");
        System.out.println("  -f           Вывести полную статистику");
        System.out.println("\nПример:");
        System.out.println("java Main -o output -p result_ -s input1.txt input2.txt");
    }
}