package batch.component;

import batch.dto.LigneCommandeDTO;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomFlatFileReader extends FlatFileItemReader<LigneCommandeDTO> {

    DefaultLineMapper<LigneCommandeDTO> defaultLineMapper = null;
    public DefaultLineMapper<LigneCommandeDTO> getDefaultLineMapper() {
        return defaultLineMapper;
    }

    public CustomFlatFileReader() {
        setLinesToSkip(1); // Pour ignorer l'en-tête

        //lineMapper
        this.defaultLineMapper = new DefaultLineMapper<>();

        //LineTokenizer
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("client", "email", "produit",
                "quantite", "prixUnitaire",
                "prixTotal", "dateCommande");
        delimitedLineTokenizer.setDelimiter(";");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        //FieldSetMapper
        FieldSetMapper<LigneCommandeDTO> fieldSetMapper = new FieldSetMapper<>() {
            @Override
            public LigneCommandeDTO mapFieldSet(FieldSet fieldSet) throws BindException {
                return new LigneCommandeDTO(
                        fieldSet.readString("client"),
                        fieldSet.readString("email"),
                        fieldSet.readString("produit"),
                        fieldSet.readInt("quantite"),
                        fieldSet.readBigDecimal("prixUnitaire"),
                        fieldSet.readBigDecimal("prixTotal"),
                        fieldSet.readString("dateCommande")
                );
            }
        };
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        setLineMapper(defaultLineMapper);

    }
}

