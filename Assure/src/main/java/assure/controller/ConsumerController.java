package assure.controller;

import assure.dto.ConsumerDto;
import assure.model.ConsumerData;
import assure.model.ConsumerForm;
import assure.spring.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ConsumerController {
    @Autowired
    private ConsumerDto consumerDto;
//TODO get similar names for client-customer online synonym
    @ApiOperation(value = "Get Consumer")  //TODO change name swagger api naming convention
    @RequestMapping(path = "/consumers", method = RequestMethod.GET) //TODO 3 modules assure, commons, channels
    public List<ConsumerData> getConsumers(@RequestParam(name = "pageNumber") Integer pageNumber) {
        return consumerDto.select(pageNumber);
    }
    //TODO check if url correct

    @ApiOperation(value = "Get Consumer by id")
    @RequestMapping(path = "/consumers/{id}", method = RequestMethod.GET)
    public ConsumerData getConsumersById(@PathVariable Long id) {
        return consumerDto.selectById(id);
    }

    @ApiOperation(value = "Add Consumers")
    @RequestMapping(path = "/consumers", method = RequestMethod.POST)
    public Integer addConsumers(@RequestBody List<ConsumerForm> clientForm) throws ApiException {
        return consumerDto.add(clientForm);
    }


}
