package batch.component;

import batch.dto.LigneCommande;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component("ligneCommandeItemWriter")
@StepScope
public class LigneCommandeItemWriter extends FlatFileItemWriter<LigneCommande> {

    public LigneCommandeItemWriter(@Value("#{jobParameters['outputFile']}") String outputFile) {
        setResource(new FileSystemResource(outputFile));
        DelimitedLineAggregator lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] {"client", "email", "produit",
                                                         "quantite", "prixUnitaire",
                                                         "prixTotal" ,"dateCommande"});
        lineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        setLineAggregator(lineAggregator);
    }
}
