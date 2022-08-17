package assure.dto;

import assure.model.ConsumerData;
import assure.model.ConsumerForm;
import assure.service.ConsumerService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.*;

@Service
public class ConsumerDto {

    @Autowired
    private ConsumerService consumerService;
    public Integer add(List<ConsumerForm> consumerFormList) throws ApiException {
        consumerService.add(convertListClientFormToPojo(consumerFormList));
        return consumerFormList.size();
    }
    public List<ConsumerData> select(Integer pageNumber){
        Integer pageSize = 10;
        return convertListClientPojoToData(consumerService.select(pageNumber,pageSize));
    }

    public ConsumerData selectById(Long id){
        return convertClientPojoToData(consumerService.selectById(id));
    }


}
