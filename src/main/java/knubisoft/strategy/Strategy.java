package knubisoft.strategy;

import java.io.File;
import knubisoft.dto.Table;

public interface Strategy {

    boolean isApplyable(String content);

    Table reader(File file);
}
