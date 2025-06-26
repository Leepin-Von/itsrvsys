package com.plotech.itsrvsys.controller;

import com.plotech.itsrvsys.pojo.dto.RunFlowRequestBody;
import com.plotech.itsrvsys.pojo.vo.R;
import com.plotech.itsrvsys.service.FloPaperRouteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
