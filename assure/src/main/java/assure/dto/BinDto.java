package assure.dto;

import assure.model.BinData;
import assure.service.BinService;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static assure.util.ConversionUtil.convertListBinPojoToData;

@Service
public class BinDto {
    private static final Long MAX_BIN_LIMIT = 100L;
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private BinService binService;

    public List<BinData> add(Integer numberOfBins) throws ApiException {
        if(Objects.isNull(numberOfBins) || numberOfBins <= 0)
            throw new ApiException("Null obj");


        if (numberOfBins > MAX_BIN_LIMIT) {
            throw new ApiException("Number of bins to create cannot exceed the limit : " + MAX_BIN_LIMIT);
        }
        return convertListBinPojoToData(binService.add(numberOfBins)); //TODO short name
    }

    public List<BinData> select(Integer pageNumber) {
        return convertListBinPojoToData(binService.select(pageNumber, PAGE_SIZE));
    }

}
