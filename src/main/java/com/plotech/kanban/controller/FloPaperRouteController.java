package com.plotech.kanban.controller;

import com.plotech.kanban.service.FloPaperRouteService;
import com.plotech.kanban.pojo.vo.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class FloPaperRouteController {
    private final FloPaperRouteService floPaperRouteService;

    @GetMapping("/approval/{paperNo}")
    public R getNonConfirm(@PathVariable String paperNo) {
        HashMap<String, String> permitEmp = floPaperRouteService.getPermitEmp(paperNo);
        return R.ok(permitEmp);
    }
}
