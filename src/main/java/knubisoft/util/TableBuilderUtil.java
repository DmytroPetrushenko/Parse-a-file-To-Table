package knubisoft.util;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import knubisoft.dto.Table;

public class TableBuilderUtil {
    private static TableBuilderUtil builder;

    public static TableBuilderUtil getBuilder() {
        if (builder == null) {
            builder = new TableBuilderUtil();
        }
        return builder;
    }

    public Table buildTable(JsonNode jsonNodeTree, Map<Integer, String> mapping) {
        Map<Integer, Map<String, String>> map = IntStream.range(0, jsonNodeTree.size())
                .boxed()
                .collect(Collectors.toMap(num -> num,
                        num -> buildRow(mapping, jsonNodeTree.get(num))));
        return new Table(map);
    }

    private Map<String, String> buildRow(Map<Integer, String> mapping, JsonNode jsonNode) {
        return IntStream.range(0, mapping.size())
                .boxed()
                .map(mapping::get)
                .collect(Collectors.toMap(name -> name,
                        name -> jsonNode.get(name).asText()));
    }

    public Map<Integer, String> buildMapping(JsonNode jsonNode) {
        List<String> list = new ArrayList<>();
        Iterator<String> stringIterator = jsonNode.fieldNames();
        while (stringIterator.hasNext()) {
            list.add(stringIterator.next());
        }
        return IntStream.range(0, list.size())
                .boxed()
                .collect(Collectors.toMap(num -> num, list::get));
    }
}
