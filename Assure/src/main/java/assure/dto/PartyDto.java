package assure.dto;

import assure.model.PartyData;
import assure.model.PartyForm;
import assure.service.PartyService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.*;

@Service
public class PartyDto {

    @Autowired
    private PartyService partyService;
    public Integer add(List<PartyForm> partyFormList) throws ApiException {
        partyService.add(convertListPartyFormToPojo(partyFormList));
        return partyFormList.size();
    }
    public List<PartyData> select(Integer pageNumber){
        Integer pageSize = 10;
        return convertListPartyPojoToData(partyService.select(pageNumber,pageSize));
    }

    public PartyData selectById(Long id){
        return convertPartyPojoToData(partyService.selectById(id));
    }


}
