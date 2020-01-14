package com.siemens.internal.controllers;

import com.siemens.internal.service.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class TempoController {

    @Autowired
    private RequestHandler requestHandler;


/*
    @PostMapping(value = "/tempo")
    @ResponseBody
    public List<WorklogDetails> getTempoTimesheet(@RequestHeader (name = "Authorization") String token,
                                                  @RequestBody TempoRequest tempoRequest) {
        List<WorklogDetails> result = tmsService.getTempoFormat(token, tempoRequest);
        return result;
    }
*/

/*
    @PostMapping(value = "/tms")
    @ResponseBody
    public List<TmsInput> saveToTms(@RequestHeader (name = "Authorization") String token,
                                      @RequestBody TempoRequest tempoRequest) {
        List<TmsInput> result = tmsService.getTmsFormat(token, tempoRequest);
        return result;
    }

    @GetMapping(value = "/excel")
    @ResponseBody
    public void readFromExcel() {
        excelService.readFromExcel();
    }


    @GetMapping(value = "/team")
    @ResponseBody
    public void readFromExcel(@RequestHeader(name = "Authorization") String token) {
        tmsService.getTeamDetails(token);
    }

    @RequestMapping("/")
    public String welcome() {
        return "index";
    }*/

    @PostMapping(value = "/sync")
    @ResponseBody
    public String syncButtonClicked( @RequestParam Map<String, String> body) {
        return requestHandler.handleSyncRequest(body);
    }

    @GetMapping(value = "/view")
    @ResponseBody
    public String viewButtonClicked() {
        return requestHandler.getAllFromDatabase();
    }
}
