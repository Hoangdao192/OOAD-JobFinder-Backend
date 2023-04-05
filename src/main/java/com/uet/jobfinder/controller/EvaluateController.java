package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.EvaluateModel;
import com.uet.jobfinder.model.EvaluateStarModel;
import com.uet.jobfinder.service.EvaluateService;
import com.uet.jobfinder.service.EvaluateStarService;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("evaluate")
public class EvaluateController {
    private EvaluateService evaluateService;
    private EvaluateStarService evaluateStarService;

    @GetMapping
    public ResponseEntity<List<EvaluateModel>> findAllEvaluate() {
        List<EvaluateModel> evaluateList = evaluateService.getAllEvaluate();
        return ResponseEntity.ok().body(evaluateList);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<List<EvaluateModel>> findAllEvaluateByCompany(@PathVariable Long companyId) {
        List<EvaluateModel> evaluateList = evaluateService.getAllEvaluateByCompany(companyId);
        return ResponseEntity.ok().body(evaluateList);
    }


    @PostMapping
    public ResponseEntity<Pair<EvaluateModel, EvaluateStarModel>> createEvaluate(
            @RequestBody @Valid EvaluateModel evaluateModel) {
        EvaluateModel evaluate = evaluateService.createEvaluate(evaluateModel);
        EvaluateStarModel evaluateStar = evaluateStarService.updateEvaluateStar(evaluate);

        return ResponseEntity.status(HttpStatus.OK).body(Pair.of(evaluate, evaluateStar));
    }

    @PutMapping
    public ResponseEntity<Pair<EvaluateModel, EvaluateStarModel>> updateEvaluate(
            @RequestBody @Valid EvaluateModel evaluateModel,
            HttpServletRequest httpServletRequest) {
        EvaluateModel evaluate = evaluateService.updateEvaluate(evaluateModel, httpServletRequest);
        EvaluateStarModel evaluateStar = evaluateStarService.updateEvaluateStar(evaluate);

        return ResponseEntity.status(HttpStatus.OK).body(Pair.of(evaluate, evaluateStar));
    }

    @DeleteMapping
    public ResponseEntity<Pair<EvaluateModel, EvaluateStarModel>> deleteEvaluate(
            @RequestBody @Valid EvaluateModel evaluateModel,
            HttpServletRequest httpServletRequest) {
        EvaluateModel evaluate = evaluateService.deleteEvaluate(evaluateModel, httpServletRequest);
        EvaluateStarModel evaluateStar = evaluateStarService.updateEvaluateStar(evaluateModel.getCompanyId());

        return ResponseEntity.status(HttpStatus.OK).body(Pair.of(evaluate, evaluateStar));
    }

    @Autowired
    public void setEvaluateService(EvaluateService evaluateService) {
        this.evaluateService = evaluateService;
    }

    @Autowired
    public void setEvaluateStar(EvaluateStarService evaluateStarService) {
        this.evaluateStarService = evaluateStarService;
    }
}
