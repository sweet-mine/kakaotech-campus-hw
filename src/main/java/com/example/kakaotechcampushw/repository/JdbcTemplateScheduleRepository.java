package com.example.kakaotechcampushw.repository;

import com.example.kakaotechcampushw.dto.ScheduleResponseDto;
import com.example.kakaotechcampushw.entity.Author;
import com.example.kakaotechcampushw.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto saveSchedule(Author author, Schedule schedule) {
        // 작성자 조회
        Optional<Author> existingAuthor = findAuthorByNameAndEmail(author.getName(), author.getEmail());

        Long author_id;

        if (existingAuthor.isEmpty()) {
            // 작성자 insert
            SimpleJdbcInsert authorInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("author")
                    .usingGeneratedKeyColumns("id")
                    .usingColumns("name", "email");

            Map<String, Object> authorParams = new HashMap<>();
            authorParams.put("name", author.getName());
            authorParams.put("email", author.getEmail());

            Number authorKey = authorInsert.executeAndReturnKey(new MapSqlParameterSource(authorParams));
            author_id = authorKey.longValue();
        } else {
            // 기존 작성자 id 재사용
            author_id = existingAuthor.get().getId();  // ← 여기서 Author의 ID를 가져와야 함
        }

        // 3. 일정 저장
        SimpleJdbcInsert scheduleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id")
                .usingColumns("task", "password", "author_id");

        Map<String, Object> scheduleParams = new HashMap<>();
        scheduleParams.put("task", schedule.getTask());
        scheduleParams.put("password", schedule.getPassword());
        scheduleParams.put("author_id", author_id);

        Number scheduleKey = scheduleInsert.executeAndReturnKey(new MapSqlParameterSource(scheduleParams));

        return new ScheduleResponseDto(
                author_id,
                scheduleKey.longValue(),
                schedule.getTask()
        );
    }


    @Override
    public List<ScheduleResponseDto> findAllSchedules() {
        return jdbcTemplate.query("select * from schedule", scheduleRowMapper());
    }

    @Override
    public Optional<Schedule> findScheduleById(Long id) {
        // 작성자 ID로 조회
        List<Schedule> result = jdbcTemplate.query("select * from schedule where id = ?", scheduleRowMapperV2(), id);

        return result.stream().findAny();
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("select * from schedule where id = ?", scheduleRowMapperV2(), id);

        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    // 같은 이름, 이메일 있는지 체크 후 반환
    @Override
    public Optional<Author> findAuthorByNameAndEmail(String name, String email) {
        List<Author> result = jdbcTemplate.query("SELECT * from author where name = ? and email = ?", authorRowMapperV2(), name, email);
        return result.stream().findAny();
    }

    @Override
    public int updateSchedule(Long id, String task, String password) {
        Optional<Schedule> schedule = findScheduleById(id);
        if(schedule.get().getPassword().equals(password)) {
            // 쿼리의 영향을 받은 row 수를 int로 반환한다.
            return jdbcTemplate.update("update schedule set task = ? where id = ?", task, id);
        }
        else return 0;
    }

    @Override
    public int updateName(Long id, String name) {
        // 쿼리의 영향을 받은 row 수를 int로 반환한다.
        return jdbcTemplate.update("update author set name = ? where id = ?", name, id);
    }

    @Override
    public int deleteSchedule(Long id, String password) {
        Optional<Schedule> schedule = findScheduleById(id);
        if(schedule.isEmpty()) return 0;
        if(schedule.get().getPassword().equals(password)) {
            return jdbcTemplate.update("delete from schedule where id = ?", id);
        }
        else return 0;
    }

    private RowMapper<ScheduleResponseDto> scheduleRowMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("author_id"),
                        rs.getLong("id"),
                        rs.getString("task")
                );
            }
        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("author_id"),
                        rs.getLong("id"),
                        rs.getString("task"),
                        rs.getString("password")
                );
            }
        };
    }

    private RowMapper<Author> authorRowMapperV2() {
        return new RowMapper<Author>() {
            @Override
            public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Author(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        };
    }
}
