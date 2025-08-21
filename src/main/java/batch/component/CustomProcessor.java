package batch.component;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("customProcessor")
//@Qualifier("CustomProcessor")
public class CustomProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) {
        return item.toUpperCase(); // Exemple de transformation
    }
}
