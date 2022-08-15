package knubisoft;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import knubisoft.dto.Table;
import knubisoft.strategy.Strategy;
import knubisoft.util.TypeFieldsUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;

public class FileMapperOrm {
    public static final String PACKAGE_NAME = "knubisoft";

    @SneakyThrows
    public <T> List<T> transform(File file, Class<T> clazz) {
        String fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        Strategy strategy = findStrategyStream(fileContent);
        Table table = strategy.read(file);
        return getEntitiesList(table, clazz);
    }

    private <T> List<T> getEntitiesList(Table table, Class<T> clazz) {
        return IntStream.range(0, table.size())
                .boxed()
                .map(v -> parseTableToEntity(table.getRowByIndex(v), clazz))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private <T> T parseTableToEntity(Map<String, String> row, Class<T> clazz) {
        TypeFieldsUtil typeFieldsUtil = TypeFieldsUtil.getObject();
        T instance = (T) getObjectByReflection(clazz);
        Field[] fields = clazz.getDeclaredFields();
        Map<Field, Object> map = Arrays.stream(fields)
                .collect(Collectors.toMap(field -> field, field -> typeFieldsUtil
                        .transformStringToTypeField(row.get(field.getName()), field)));
        map.forEach((key, value) -> setInstance(instance, key, value));
        return instance;
    }

    private Strategy findStrategyStream(String fileContent) {
        String modifiedContent = fileContent.replaceAll("[\\r\\n]", "");
        Reflections reflections = new Reflections(PACKAGE_NAME);
        List<Class<? extends Strategy>> classList = List
                .copyOf(reflections.getSubTypesOf(Strategy.class));
        return classList.stream()
                .map(clazz -> (Strategy) getObjectByReflection(clazz))
                .filter(object -> object.isApplyable(modifiedContent))
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("a strategy is existed for this content: "
                            + fileContent + " !");
                });
    }

    @SneakyThrows
    private Object getObjectByReflection(Class<?> clazz) {
        return clazz.getConstructor().newInstance();
    }

    @SneakyThrows
    private <T> void setInstance(T instance, Field field, Object value) {
        field.setAccessible(true);
        field.set(instance, value);
    }
}
