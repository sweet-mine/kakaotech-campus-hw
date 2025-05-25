package com.example.kakaotechcampushw.controller;

import com.example.kakaotechcampushw.dto.ScheduleRequestDto;
import com.example.kakaotechcampushw.dto.ScheduleResponseDto;
import com.example.kakaotechcampushw.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Schedule Controller
 */
@RestController // @Controller + @ResponseBody
@RequestMapping("/schedule") // Prefix
public class ScheduleController {

    // 주입된 의존성을 변경할 수 없어 객체의 상태를 안전하게 유지할 수 있다.
    private final ScheduleService scheduleService;

    /**
     * 생성자 주입
     * 클래스가 필요로 하는 의존성을 생성자를 통해 전달하는 방식
     * @param scheduleService @Service로 등록된 ScheduleService 구현체인 Impl
     */
    private ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 메모 생성 API
     * @param : {@link ScheduleRequestDto} 메모 생성 요청 객체
     * @return : {@link ResponseEntity <ScheduleResponseDto>} JSON 응답
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto requestDto) {

        return new ResponseEntity<>(scheduleService.saveSchedule(requestDto), HttpStatus.CREATED);
    }

    /**
     * 메모 전체 조회 API
     * @return : {@link List <ScheduleResponseDto>} JSON 응답
     */
    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules() {

        return scheduleService.findAllSchedules();
    }

    /**
     * 메모 단건 조회 API
     * @param id 식별자
     * @return : {@link ResponseEntity<ScheduleResponseDto>} JSON 응답
     * @exception ResponseStatusException 식별자로 조회된 Schedule가 없는 경우 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {

        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    /**
     * 메모 전체 수정 API
     * @param id 식별자
     * @param : {@link ScheduleRequestDto} 메모 수정 요청 객체
     * @return : {@link ResponseEntity<ScheduleResponseDto>} JSON 응답
     * @exception ResponseStatusException 요청 필수값이 없는 경우 400 Bad Request, 식별자로 조회된 Schedule가 없는 경우 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {

        return new ResponseEntity<>(scheduleService.updateSchedule(id, requestDto.getTask(), requestDto.getPassword()), HttpStatus.OK);
    }

    @PutMapping("/name/{id}")
    public ResponseEntity<ScheduleResponseDto> updateName(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {

        scheduleService.updateName(id, requestDto.getName());

        // 성공한 경우
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 메모 삭제 API
     * @param id 식별자
     * @return {@link ResponseEntity<Void>} 성공시 Data 없이 200OK 상태코드만 응답.
     * @exception ResponseStatusException 식별자로 조회된 Schedule가 없는 경우 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {

        scheduleService.deleteSchedule(id, requestDto.getPassword());

        // 성공한 경우
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
