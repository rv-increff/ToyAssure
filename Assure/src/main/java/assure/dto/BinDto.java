package assure.dto;

import assure.model.BinData;
import assure.services.BinServices;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.*;

@Service
public class BinDto {
    @Autowired
    private BinServices binServices;

    public List<BinData> add(Integer numberOfBins) throws ApiException {
        Long maxBinLimit = 100L;
        if(numberOfBins>maxBinLimit){
            throw new ApiException("number of bins greater than limit , limit : "  + maxBinLimit);
        }
        return convertListBinPojoToData(binServices.add(numberOfBins));
    }

    public List<BinData> select(Integer pageNumber){
        Integer pageSize = 10;
        return convertListBinPojoToData(binServices.select(pageNumber,pageSize));
    }

}
