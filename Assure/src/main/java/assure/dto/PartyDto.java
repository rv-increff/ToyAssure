package assure.dto;

import assure.model.PartyData;
import assure.model.PartyForm;
import assure.service.PartyService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.ConversionUtil.*;

@Service
public class PartyDto {

    private static final Integer PAGE_SIZE = 10;
    @Autowired
    private PartyService partyService;

    public Integer add(List<PartyForm> partyFormList) throws ApiException {
        partyService.add(convertListPartyFormToPojo(partyFormList));
        return partyFormList.size();
    }

    public List<PartyData> select(Integer pageNumber) {
        return convertListPartyPojoToData(partyService.select(pageNumber, PAGE_SIZE));
    }

    public PartyData selectById(Long id) {
        return convertPartyPojoToData(partyService.selectById(id));
    }

}
