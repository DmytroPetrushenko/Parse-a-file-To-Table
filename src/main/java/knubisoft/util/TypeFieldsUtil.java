package knubisoft.util;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class TypeFieldsUtil {
    private static TypeFieldsUtil object;
    private final Map<Class<?>, Function<String, Object>> typeValues = new LinkedHashMap<>();

    private TypeFieldsUtil() {
        typeValues.put(String.class, s -> s);
        typeValues.put(Integer.class, Integer::valueOf);
        typeValues.put(Float.class, Float::valueOf);
        typeValues.put(Double.class, Double::valueOf);
        typeValues.put(LocalDate.class, LocalDate::parse);
        typeValues.put(Long.class, Long::valueOf);
    }

    public static TypeFieldsUtil getObject() {
        if (object == null) {
            object = new TypeFieldsUtil();
        }
        return object;
    }

    public Object transformStringToTypeField(String value, Field field) {
        return typeValues.getOrDefault(field.getType(), type -> {
            throw new UnsupportedOperationException("Type is not supported by parser " + type);
        }).apply(value);
    }
}
