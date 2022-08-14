package knubisoft.strategy.impl;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import knubisoft.dto.Table;
import knubisoft.strategy.Strategy;
import lombok.SneakyThrows;

public class StrategyCsv implements Strategy {
    private final String pattern = "(([\\w\\d\\-]+[,])+[\\w\\d\\-]+\\b)";

    @Override
    public boolean isApplyable(String content) {
        return content.matches(pattern);
    }

    @Override
    @SneakyThrows
    public Table reader(File file) {
        CSVReader readerCsv = new CSVReader(new FileReader(file));
        Map<Integer, String> mapping = mapping(readerCsv.readNext());
        return buildTable(mapping, readerCsv);
    }

    @SneakyThrows
    private Table buildTable(Map<Integer, String> mapping, CSVReader readerCsv) {
        List<String[]> list = readerCsv.readAll();
        Map<Integer, Map<String, String>> collect = IntStream.range(0, list.size())
                .boxed()
                .collect(Collectors.toMap(v -> v, v -> buildRaw(list.get(v), mapping)));
        return new Table(collect);
    }

    private Map<String, String> buildRaw(String[] entities, Map<Integer, String> mapping) {
        return mapping.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, entry -> entities[entry.getKey()]));
    }

    private Map<Integer, String> mapping(String[] array) {
        return IntStream.range(0, array.length)
                .boxed()
                .collect(Collectors.toMap(e -> e, e -> array[e]));
    }

}
