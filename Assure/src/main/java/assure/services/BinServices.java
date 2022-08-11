package assure.services;

import assure.dao.BinDao;
import assure.pojo.BinPojo;
import assure.spring.ApiException;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinServices {

    @Autowired
    private BinDao binDao;

    public List<BinPojo> add(Integer numberOfBins) {
        Integer num = numberOfBins;
        while (num > 0) {
            BinPojo binPojo = new BinPojo();
            binDao.add(binPojo);
            num--;
        }
        return binDao.selectLatestCreatedBins(numberOfBins);
    }

    public List<BinPojo> select(Integer pageNumber, Integer pageSize) {
        return binDao.select(pageNumber, pageSize);
    }

    public List<BinPojo> selectByIdList(List<Long> idList){
        return binDao.selectByIdList(idList);
    }
}
