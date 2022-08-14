package knubisoft.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.util.Map;
import knubisoft.dto.Table;
import knubisoft.strategy.Strategy;
import knubisoft.util.TableBuilderUtil;
import lombok.SneakyThrows;

public class StrategyXml implements Strategy {
    private final String pattern = "\\<\\?xml.+\\>\\<\\w+s\\>.*\\<\\/\\w+s\\>";

    @Override
    public boolean isApplyable(String content) {
        return content.matches(pattern);
    }

    @Override
    @SneakyThrows
    public Table reader(File file) {
        JsonNode nodes = getNodes(file);
        TableBuilderUtil builder = TableBuilderUtil.getBuilder();
        Map<Integer, String> mapping = builder.buildMapping(nodes.get(0));
        return builder.buildTable(nodes, mapping);
    }

    @SneakyThrows
    private JsonNode getNodes(File file) {
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode jsonNodeTree = mapper.readTree(file);
        String name = jsonNodeTree.fieldNames().next();
        return jsonNodeTree.findValue(name);
    }
}
