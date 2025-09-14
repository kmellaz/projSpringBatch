package batch.component;

import batch.dto.ClientDto;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import javax.sql.DataSource;

public class ClientItemWriter extends JdbcBatchItemWriter<ClientDto> {

    public ClientItemWriter(DataSource dataSource){
        setDataSource(dataSource);
        setSql("INSERT INTO client (email, nom) VALUES (:email, :nom)");
        setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        afterPropertiesSet();// obligatoire pour initialiser l'écrivain
    }


}
