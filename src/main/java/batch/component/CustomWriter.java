package batch.component;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("customWriter")
public class CustomWriter implements ItemWriter<String> {
    @Override
    public void write(Chunk<? extends String> items) throws Exception {
        System.out.println("Écriture du chunk : " + items);
    }

    /* @Override
    public void write(String item) {
        System.out.println("Écriture du chunk : " + item);
    }*/
}

