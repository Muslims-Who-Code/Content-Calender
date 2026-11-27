package dev.idris.content_calendar.repository;

import dev.idris.content_calendar.model.Content;
import dev.idris.content_calendar.model.Status;
import dev.idris.content_calendar.model.Type;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ContentJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    ContentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Content mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Content(rs.getInt("id"),
                rs.getString("title"),
                rs.getString("desc"),
                rs.getObject("status", Status.class),
                rs.getObject("content_type", Type.class),
                rs.getTimestamp("date_created").toLocalDateTime(),
                rs.getTimestamp("date_updated").toLocalDateTime(),
                rs.getString("url")
        );
    }

    public List<Content> getAllContent() {
        String sqlQuery = "SELECT * FROM Content";

        return jdbcTemplate.query(sqlQuery, ContentJdbcTemplateRepository::mapRow);
    }

    public void insertContent(
        String title,
        String desc,
        String status,
        String content_type,
        String url
    ) {
        String sqlQuery = "INSERT INTO Content (title, desc, status, content_type, url";
        jdbcTemplate.update(sqlQuery, title, desc, status, content_type, url);
    }

    public void updateContent(
        int id,
        String title,
        String desc,
        String status,
        String content_type,
        String url
    ) {
        String sqlQuery = "UPDATE Content SET title=?, desc=?, status=?, content_type=?, url=?, id=?";
        jdbcTemplate.update(sqlQuery, title, desc, status, content_type, url, id);
    }

    public void deleteContent(int id) {
        String sqlQuery = "DELETE FROM Content WHERE id=?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public Content getContent(int id) {
        String sqlQuery = "SELECT * FROM Content WHERE id=?";

        // The method that used Object[]{id} is depreciated!
        return  jdbcTemplate.queryForObject(sqlQuery, ContentJdbcTemplateRepository::mapRow, id);
    }
}
