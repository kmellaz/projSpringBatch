package batch.rowMapper;

import batch.dto.ClientDto;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientMappeur implements RowMapper<ClientDto> {

    @Override
    public ClientDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ClientDto(
                rs.getLong("id"),
                rs.getString("nom"),
                rs.getString("email")
        );
    }
}

