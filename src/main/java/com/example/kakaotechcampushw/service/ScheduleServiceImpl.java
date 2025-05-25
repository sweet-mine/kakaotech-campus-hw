package com.example.kakaotechcampushw.service;

import com.example.kakaotechcampushw.dto.ScheduleRequestDto;
import com.example.kakaotechcampushw.dto.ScheduleResponseDto;
import com.example.kakaotechcampushw.entity.Author;
import com.example.kakaotechcampushw.entity.Schedule;
import com.example.kakaotechcampushw.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Annotation @Service는 @Component와 같다, Spring Bean으로 등록한다는 뜻.
 * Spring Bean으로 등록되면 다른 클래스에서 주입하여 사용할 수 있다.
 * 명시적으로 Service Layer 라는것을 나타낸다.
 * 비지니스 로직을 수행한다.
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {

        // 요청받은 데이터로 Schedule 객체 생성
        Schedule schedule = new Schedule(requestDto.getTask(), requestDto.getPassword());
        Author author = new Author(requestDto.getName(), requestDto.getEmail());

        // 저장
        return scheduleRepository.saveSchedule(author, schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules() {

        return scheduleRepository.findAllSchedules();
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String task, String password) {

        // 필수값 검증
        if (task == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The task and password are required values.");
        }

        // schedule 수정
        int updatedRow = scheduleRepository.updateSchedule(id, task, password);
        // 수정된 row가 0개라면
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified or password is incorrect.");
        }

        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);

        // 수정된 메모 조회
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    @Override
    public void updateName(Long id, String name){
        // 필수값 검증
        if (name == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The name is required values.");
        }
        // schedule 수정
        int updatedRow = scheduleRepository.updateName(id, name);
        // 수정된 row가 0개라면
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
        }
    }

    @Override
    public void deleteSchedule(Long id, String password) {

        // 필수값 검증
        if (password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password are required values.");
        }
        // schedule 삭제
        int deletedRow = scheduleRepository.deleteSchedule(id, password);
        // 삭제된 row가 0개 라면
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id + "or password is incorrect. ");
        }
    }
}
