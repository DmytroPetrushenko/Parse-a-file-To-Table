package knubisoft.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Table {

    private final Map<Integer, Map<String, String>> table;

    public Integer size() {
        return this.table.size();
    }

    public Map<String, String> getRowByIndex(int index) {
        Map<String, String> row = table.get(index);
        return row == null ? null : new LinkedHashMap<>(row);
    }

    public String getCell(int numberRow, String columnName) {
        Map<String, String> rowByIndex = this.getRowByIndex(numberRow);
        return rowByIndex == null ? null : rowByIndex.get(columnName);
    }
}
