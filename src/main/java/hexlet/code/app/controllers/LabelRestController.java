package hexlet.code.app.controllers;

import hexlet.code.app.services.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/labels")
public class LabelRestController {

    @Autowired
    private LabelService labelService;
}
