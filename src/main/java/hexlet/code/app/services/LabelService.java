package hexlet.code.app.services;

import hexlet.code.app.dto.LabelRequestDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
