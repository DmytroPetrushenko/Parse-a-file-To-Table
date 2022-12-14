package knubisoft.strategy.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.util.Map;
import knubisoft.dto.Table;
import knubisoft.strategy.Strategy;
import knubisoft.util.JacksonTableBuilderUtils;
import lombok.SneakyThrows;

public class StrategyXml implements Strategy {
    private final String pattern = "\\<\\?xml.+\\>\\<\\w+s\\>.*\\<\\/\\w+s\\>";

    @Override
    public boolean isApplyable(String content) {
        return content.matches(pattern);
    }

    @Override
    @SneakyThrows
    public Table read(File file) {
        JsonNode nodes = getChildListNode(file);
        JacksonTableBuilderUtils builder = JacksonTableBuilderUtils.getBuilder();
        Map<Integer, String> mapping = builder.buildMapping(nodes.get(0));
        return builder.buildTable(nodes, mapping);
    }

    @SneakyThrows
    private JsonNode getChildListNode(File file) {
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode jsonNodeTree = mapper.readTree(file);
        String name = jsonNodeTree.fieldNames().next();
        return jsonNodeTree.findValue(name);
    }
}
