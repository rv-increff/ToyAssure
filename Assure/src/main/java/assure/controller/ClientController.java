package assure.controller;

import assure.dto.ClientDto;
import assure.model.ClientData;
import assure.model.ClientForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ClientController {
    @Autowired
    private ClientDto clientDto;

    @ApiOperation(value = "Gives clients data")
    @RequestMapping(path = "/clients/{pageNumber}", method = RequestMethod.GET)//TODO 3 modules assure, commons, channels
    public List<ClientData> getClient(@PathVariable Integer pageNumber) {
        return clientDto.select(pageNumber);
    }

    @ApiOperation(value = "Gives clients data by id")
    @RequestMapping(path = "/clients/{id}", method = RequestMethod.GET)
    public ClientData getClientById(@PathVariable Long id) {
        return clientDto.selectById(id);
    }

    @ApiOperation(value = "add clients")
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public Integer addClient(@RequestBody List<ClientForm> clientForm) {
        return clientDto.add(clientForm);
    }


}
