package batch.component;

import batch.dto.LigneCommandeDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component("countingItemWriter")
public class CountingItemWriter implements ItemWriter<LigneCommandeDTO> {
    private static final Logger log = LogManager.getLogger(CountingItemWriter.class);
    private static final AtomicInteger total = new AtomicInteger();

    @Override
    public void write(Chunk<? extends LigneCommandeDTO> chunk) throws Exception {
        int count = chunk.size();
        int cumulative = total.addAndGet(count);
        //System.out.println(">>> Write processed " + count + " items. Total: " + cumulative);
        log.info(">>> " + Thread.currentThread().getName() + " >>> Write processed " + count + " items. Total: " + cumulative);
    }
}
