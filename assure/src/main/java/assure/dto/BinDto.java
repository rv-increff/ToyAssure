package assure.dto;

import assure.model.BinData;
import assure.service.BinService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.ConversionUtil.convertBinPojoListToData;

@Service
public class BinDto {
    private static final Long MAX_BIN_LIMIT = 100L;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private BinService binService;

    public List<BinData> add(Integer numberOfBins) throws ApiException {
        if (numberOfBins <= 0 || numberOfBins > MAX_BIN_LIMIT)
            throw new ApiException("Number of bins should be between 1 and " + MAX_BIN_LIMIT); //TODO combine these two if in one

        return convertBinPojoListToData(binService.add(numberOfBins)); //TODO add CollectsUtils.isEmpty() in all conversion
    }

    public List<BinData> select(Integer pageNumber) {
        return convertBinPojoListToData(binService.select(pageNumber, PAGE_SIZE));
    }

}
