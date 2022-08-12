package assure.dto;

import assure.model.ConsumerData;
import assure.model.ConsumerForm;
import assure.services.ClientServices;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.*;

@Service
public class ConsumerDto {

    @Autowired
    private ClientServices clientServices;
    public Integer add(List<ConsumerForm> consumerFormList) throws ApiException {
        clientServices.add(convertListClientFormToPojo(consumerFormList));
        return consumerFormList.size();
    }
    public List<ConsumerData> select(Integer pageNumber){
        Integer pageSize = 10;
        return convertListClientPojoToData(clientServices.select(pageNumber,pageSize));
    }

    public ConsumerData selectById(Long id){
        return convertClientPojoToData(clientServices.selectById(id));
    }


}
