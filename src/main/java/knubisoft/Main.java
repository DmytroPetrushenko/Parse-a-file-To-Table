package knubisoft;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import knubisoft.model.Person;
import lombok.SneakyThrows;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        FileMapperOrm fileMapperOrm = new FileMapperOrm();

        URL urlCsv = FileMapperOrm.class.getClassLoader().getResource("test.csv");
        File fileCsv = new File(Objects.requireNonNull(urlCsv).toURI());
        List<Person> personListCsv = fileMapperOrm.transform(fileCsv, Person.class);
        personListCsv.forEach(System.out::println);

        URL urlJson = FileMapperOrm.class.getClassLoader().getResource("file.json");
        File fileJSon = new File(Objects.requireNonNull(urlJson).toURI());
        List<Person> personListJson = fileMapperOrm.transform(fileJSon, Person.class);
        personListJson.forEach(System.out::println);

        URL urlXml = FileMapperOrm.class.getClassLoader().getResource("fileXML.xml");
        File fileXml = new File(Objects.requireNonNull(urlXml).toURI());
        List<Person> personListXml = fileMapperOrm.transform(fileXml, Person.class);
        personListXml.forEach(System.out::println);
    }
}
