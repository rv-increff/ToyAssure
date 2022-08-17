package assure.dto;

import assure.model.BinData;
import assure.service.BinService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.convertListBinPojoToData;

@Service
public class BinDto {
    private static final Long MAX_BIN_LIMIT = 100L;
    private static final Integer PAGE_SIZE = 10;
    @Autowired
    private BinService binService;

    public List<BinData> add(Integer numberOfBins) throws ApiException {
        if (numberOfBins > MAX_BIN_LIMIT) {
            throw new ApiException("number of bins greater than limit , limit : " + MAX_BIN_LIMIT);
        }
        return convertListBinPojoToData(binService.add(numberOfBins));
    }

    public List<BinData> select(Integer pageNumber) {
        return convertListBinPojoToData(binService.select(pageNumber, PAGE_SIZE));
    }

}
