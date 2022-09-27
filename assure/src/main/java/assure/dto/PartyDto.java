package assure.dto;

import commons.model.PartyData;
import assure.model.PartyForm;
import commons.model.PartySearchForm;
import assure.service.PartyService;
import assure.spring.ApiException;
import commons.util.PartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static assure.util.ConversionUtil.*;
import static assure.util.ValidationUtil.validateFormList;
import static java.util.Objects.isNull;

@Service
public class PartyDto {

    private static final Integer PAGE_SIZE = 5;
    @Autowired
    private PartyService partyService;

    public Integer add(List<PartyForm> partyFormList) throws ApiException {
        validateFormList(partyFormList);
        partyService.add(convertListPartyFormToPojo(partyFormList));
        return partyFormList.size();
    }

    public List<PartyData> partySearch(PartySearchForm partySearchForm){
        if(!isNull(partySearchForm.getPageNumber()) && isNull(partySearchForm.getType())  )
            return select(partySearchForm.getPageNumber());

        if(!isNull(partySearchForm.getType()) && isNull(partySearchForm.getPageNumber()))
            return selectByPartyType(partySearchForm.getType());

        return selectByPartyType(partySearchForm.getType(), partySearchForm.getPageNumber());
    }

    public List<PartyData> select(Integer pageNumber) {
        return convertListPartyPojoToData(partyService.select(pageNumber, PAGE_SIZE));
    }

    public PartyData selectById(Long id) throws ApiException {
        return convertPartyPojoToData(partyService.selectById(id));
    }

    public List<PartyData> selectByPartyType(PartyType partyType){
        return convertListPartyPojoToData(partyService.selectByPartyType(partyType));
    }
    public List<PartyData> selectByPartyType(PartyType partyType, Integer pageNumber){
        return convertListPartyPojoToData(partyService.selectByPartyType(partyType, pageNumber, PAGE_SIZE));
    }

}
