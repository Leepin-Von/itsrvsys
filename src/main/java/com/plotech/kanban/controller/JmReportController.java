package com.plotech.kanban.controller;

import com.plotech.kanban.service.FaAddPaperService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jm_report")
@AllArgsConstructor
public class JmReportController {

    private final FaAddPaperService faAddPaperService;

    @GetMapping("/execFaAddPaper")
    public String execFaAddPaper(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "1") Integer pageSize,
                                 @RequestParam(name = "paperNo", required = true, defaultValue = "AD2111000005") String paperNo) {
        return faAddPaperService.execFaAddPaper(paperNo, pageNo, pageSize);
    }
}
