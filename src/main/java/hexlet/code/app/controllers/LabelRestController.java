package hexlet.code.app.controllers;

import hexlet.code.app.dto.LabelRequestDto;
import hexlet.code.app.dto.LabelResponseDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.services.LabelService;
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

    @PostMapping
    public ResponseEntity<LabelResponseDto> createLabel(@RequestBody LabelRequestDto labelDto) {
        Label label = labelService.createLabel(labelDto);
        if (label != null) {
            LabelResponseDto labelResponseDto = labelService.entityToResponseDto(label);
            return ResponseEntity.ok(labelResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabelResponseDto> getLabel(@PathVariable("id") String id) {
        Label label = labelService.getLabelById(Integer.parseInt(id));
        if (label != null) {
            LabelResponseDto labelResponseDto = labelService.entityToResponseDto(label);
            return ResponseEntity.ok(labelResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<LabelResponseDto>> getAllLabel() {
        List<Label> labelList = labelService.getAllLabelList();
        List<LabelResponseDto> labelResponseDtoList = labelList.stream().map(label ->
                labelService.entityToResponseDto(label)
        ).toList();
        return ResponseEntity.ok(labelResponseDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelResponseDto> updateLabel(@RequestBody LabelRequestDto labelDto, @PathVariable("id") String id) {
        Label label = labelService.updateLabel(labelDto, Integer.parseInt(id));
        if (label != null) {
            LabelResponseDto labelResponseDto = labelService.entityToResponseDto(label);
            return ResponseEntity.ok(labelResponseDto);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLabel(@PathVariable("id") String id) {
        labelService.deleteLabel(Integer.parseInt(id));
        return ResponseEntity.ok("Label successfully deleted");
    }
}
