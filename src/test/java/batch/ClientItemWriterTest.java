package batch;

import batch.component.ClientItemWriter;
import batch.dto.ClientDto;
import batch.rowMapper.ClientMappeur;
import org.junit.jupiter.api.*;
import org.springframework.batch.item.Chunk;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientItemWriterTest {

    private static DataSource dataSource;
    private static JdbcTemplate jdbcTemplate;
    private static ClientItemWriter clientItemWriter;

    @BeforeAll
    static void setupDatabase() {
        dataSource = new DriverManagerDataSource("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.execute("CREATE TABLE client (id IDENTITY PRIMARY KEY, nom VARCHAR(100), email VARCHAR(100))");

        clientItemWriter = new ClientItemWriter(dataSource);
    }

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM client");
    }

    @Test
    void testWriteSingleClient() throws Exception {
        ClientDto clientDto = new ClientDto(1L,"Durand", "durand@gmail.com");

        clientItemWriter.write(new Chunk<>(List.of(clientDto)));

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM client", Integer.class);
        assertEquals(1, count);

        String nom = jdbcTemplate.queryForObject("SELECT nom FROM client", String.class);
        assertEquals("Durand", nom);

        ClientDto dto = jdbcTemplate.queryForObject ("SELECT * FROM client  WHERE id = ?", new ClientMappeur(), 1L);
        assertEquals("Durand", dto.getNom());
        assertEquals("durand@gmail.com", dto.getEmail());
        assertEquals(1L, dto.getId());

    }

    @Test
    void testWriteMultipleClient() throws Exception {
        List<ClientDto> persons = List.of(
                new ClientDto(1l,"Dupont", "dupant@gmail.com"),
                new ClientDto(2l,"Martin", "martin@gmail.com")
        );

        clientItemWriter.write(new Chunk<>(persons));

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM client", Integer.class);
        assertEquals(2, count);
    }
}

