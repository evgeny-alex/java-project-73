package hexlet.code.app.controllers;

import com.rollbar.notifier.Rollbar;
import hexlet.code.app.dto.LabelRequestDto;
import hexlet.code.app.dto.LabelResponseDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.services.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelRestController {

    @Autowired
    private LabelService labelService;

    @Autowired
    private Rollbar rollbar;

    @Operation(summary = "Операция создания метки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Метка успешно создана"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при создании метки")
    })
    @PostMapping
    public ResponseEntity<LabelResponseDto> createLabel(@RequestBody LabelRequestDto labelDto) {
        Label label = labelService.createLabel(labelDto);
        if (label != null) {
            LabelResponseDto labelResponseDto = labelService.entityToResponseDto(label);
            return ResponseEntity.ok(labelResponseDto);
        }
        rollbar.error("Произошла ошибка при создании метки.");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Получение метки по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Метка возвращена"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при поиске метки")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LabelResponseDto> getLabel(@PathVariable("id") String id) {
        Label label = labelService.getLabelById(Long.parseLong(id));
        if (label != null) {
            LabelResponseDto labelResponseDto = labelService.entityToResponseDto(label);
            return ResponseEntity.ok(labelResponseDto);
        }
        rollbar.error("Произошла ошибка при получении метки по ID.");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Получение списка всех меток")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список меток возвращен"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при поиске всех меток")
    })
    @GetMapping
    public ResponseEntity<List<LabelResponseDto>> getAllLabel() {
        List<Label> labelList = labelService.getAllLabelList();
        List<LabelResponseDto> labelResponseDtoList = labelList.stream().map(label ->
                labelService.entityToResponseDto(label)
        ).toList();
        return ResponseEntity.ok(labelResponseDtoList);
    }

    @Operation(summary = "Обновление метки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Метка успешно обновлена"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при обновлении метки")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LabelResponseDto> updateLabel(@RequestBody LabelRequestDto labelDto, @PathVariable("id") String id) {
        Label label = labelService.updateLabel(labelDto, Long.parseLong(id));
        if (label != null) {
            LabelResponseDto labelResponseDto = labelService.entityToResponseDto(label);
            return ResponseEntity.ok(labelResponseDto);
        }
        rollbar.error("Произошла ошибка при обновлении метки с ID = " + id);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Удаление метки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Метка успешно удалена"),
            @ApiResponse(responseCode = "500", description = "Произошла ошибка при удалении метки")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLabel(@PathVariable("id") String id) {
        labelService.deleteLabel(Long.parseLong(id));
        return ResponseEntity.ok("Label successfully deleted");
    }
}
