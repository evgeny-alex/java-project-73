package hexlet.code.app.services;

import hexlet.code.app.dto.LabelRequestDto;
import hexlet.code.app.dto.LabelResponseDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;

    /**
     * Создание метки
     *
     * @param labelRequestDto - DTO запроса метки
     * @return - ID метки
     */
    public Integer createLabel(LabelRequestDto labelRequestDto) {
        Label label = new Label();

        label.setName(labelRequestDto.getName());

        labelRepository.save(label);

        return label.getId();
    }

    /**
     * Получение метки по ID
     *
     * @param id - ID метки
     * @return - сущность метки
     */
    public Label getLabelById(Integer id) {
        return labelRepository.getById(id);
    }

    /**
     * Получение списка всех меток
     *
     * @return - список всех меток
     */
    public List<Label> getAllLabelList() {
        return labelRepository.findAll();
    }

    /**
     * Обновление метки
     *
     * @param labelRequestDto - DTO метки для обновления
     * @param id - ID метки
     */
    public void updateLabel(LabelRequestDto labelRequestDto, Integer id) {
        Label label = labelRepository.getById(id);

        label.setName(labelRequestDto.getName());

        labelRepository.save(label);
    }

    /**
     * Удаление метки по ID
     *
     * @param id - ID метки
     */
    public void deleteLabel(Integer id) {
        labelRepository.deleteById(id);
    }

    /**
     * Преобразование сущности метки к DTO ответа
     *
     * @param label - сущность метки
     * @return - DTO ответа
     */
    public LabelResponseDto entityToResponseDto(Label label) {
        LabelResponseDto labelResponseDto = new LabelResponseDto();

        labelResponseDto.setId(label.getId());
        labelResponseDto.setName(label.getName());
        labelResponseDto.setCreatedAt(label.getCreatedAt());

        return labelResponseDto;
    }
}
