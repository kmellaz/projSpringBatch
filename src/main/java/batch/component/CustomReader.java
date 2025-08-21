package batch.component;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;

@Component("customReader")

public class CustomReader implements ItemReader<String> {

    private final Iterator<String> data = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eva").iterator();

    @Override
    public String read() {
        return data.hasNext() ? data.next() : null;
    }
}

