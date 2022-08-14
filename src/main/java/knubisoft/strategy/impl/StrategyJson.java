package knubisoft.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Map;
import knubisoft.dto.Table;
import knubisoft.strategy.Strategy;
import knubisoft.util.TableBuilderUtil;
import lombok.SneakyThrows;

public class StrategyJson implements Strategy {
    private final String pattern = "\\[.*\\{.+\\}+.*\\]";

    @Override
    public boolean isApplyable(String content) {
        return content.matches(pattern);
    }

    @Override
    @SneakyThrows
    public Table reader(File file) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNodeTree = mapper.readTree(file);
        TableBuilderUtil builder = TableBuilderUtil.getBuilder();
        Map<Integer, String> mapping = builder.buildMapping(jsonNodeTree.get(0));
        return builder.buildTable(jsonNodeTree, mapping);
    }
}
