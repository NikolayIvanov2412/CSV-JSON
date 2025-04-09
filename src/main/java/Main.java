import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    private static final String FILE_NAME = "data.csv";
    private static final String OUTPUT_FILE_NAME = "data.json";

    public static void main(String[] args) throws IOException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        List<Employee> employees = parseCsv(columnMapping, FILE_NAME);
        String json = listToJson(employees);
        writeString(json, OUTPUT_FILE_NAME);
    }

    private static List<Employee> parseCsv(String[] columnMapping, String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder(reader)
                    .withMappingStrategy(strategy)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            System.out.println("Ошибка чтения CSV файла: " + e.getMessage());
            return null;
        }
    }

    public static String listToJson(List<Employee> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Pretty Printing JSON

        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String content, String outputFileName) {
        try (FileWriter writer = new FileWriter(outputFileName)) {
            writer.write(content);
            System.out.println("JSON успешно записан в файл " + outputFileName);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
}