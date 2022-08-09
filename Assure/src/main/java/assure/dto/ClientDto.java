package assure.dto;

import assure.model.ClientData;
import assure.model.ClientForm;
import assure.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.*;

@Service
public class ClientDto {

    @Autowired
    private ClientServices clientServices;
    public Integer add(List<ClientForm> clientFormList){
        clientServices.add(convertListClientFormToPojo(clientFormList));
        return clientFormList.size();
    }
    public List<ClientData> select(Integer pageNumber){
        Integer pageSize = 10;
        return convertListClientPojoToData(clientServices.select(pageNumber,pageSize));
    }

    public ClientData selectById(Long id){
        return convertClientPojoToData(clientServices.selectById(id));
    }


}
