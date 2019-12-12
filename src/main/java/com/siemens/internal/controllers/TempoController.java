package com.siemens.internal.controllers;

import com.siemens.internal.models.TempoRequest;
import com.siemens.internal.models.TmsInput;
import com.siemens.internal.models.WorklogDetails;
import com.siemens.internal.service.ExcelService;
import com.siemens.internal.service.TmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TempoController {

    @Autowired
    private TmsService tmsService;

    @Autowired
    private ExcelService excelService;

    @PostMapping(value = "/tempo")
    public List<WorklogDetails> getTempoTimesheet(@RequestHeader (name = "Authorization") String token,
                                                  @RequestBody TempoRequest tempoRequest) {
        List<WorklogDetails> result = tmsService.getTempoFormat(token, tempoRequest);
        return result;
    }

    @GetMapping(value = "/tms")
    public List<TmsInput> getFromTmsDatabase() {
        List<TmsInput> result = tmsService.getAllFromDb();
        return result;
    }

    @PostMapping(value = "/tms")
    public List<TmsInput> saveToTms(@RequestHeader (name = "Authorization") String token,
                                      @RequestBody TempoRequest tempoRequest) {
        List<TmsInput> result = tmsService.getTmsFormat(token, tempoRequest);
        return result;
    }

    @GetMapping(value = "/excel")
    public void readFromExcel() {
        excelService.readFromExcel();
    }


    @GetMapping(value = "/team")
    public void readFromExcel(@RequestHeader(name = "Authorization") String token) {
        tmsService.getTeamDetails(token);
    }
}
