package com.plotech.kanban.controller;

import com.plotech.kanban.pojo.dto.RunFlowRequestBody;
import com.plotech.kanban.pojo.vo.R;
import com.plotech.kanban.service.FloPaperRouteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/approval")
public class FloPaperRouteController {
    private final FloPaperRouteService floPaperRouteService;

    @GetMapping("/{paperNo}")
    public R getNonConfirm(@PathVariable String paperNo) {
        ArrayList<String> permitEmp = floPaperRouteService.getPermitEmp(paperNo);
        return R.ok(permitEmp);
    }

    @PostMapping("/runFlow")
    public R runFlow(@RequestBody RunFlowRequestBody requestBody) {
        floPaperRouteService.runFlow(requestBody.getTop(), requestBody.getPaperNo());
        return R.ok();
    }
}
