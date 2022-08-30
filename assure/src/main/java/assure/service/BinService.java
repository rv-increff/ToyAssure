package assure.service;

import assure.dao.BinDao;
import assure.pojo.BinPojo;
import assure.spring.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinService {

    @Autowired
    private BinDao binDao;

    public List<BinPojo> add(Integer numberOfBins) {
        List<BinPojo> binPojoList = new ArrayList<>();

        for(int i = 0 ; i < numberOfBins; i ++)
            binPojoList.add(createBin());

        return binPojoList;
    }


    public List<BinPojo> select(Integer pageNumber, Integer pageSize) {
        return binDao.select(pageNumber, pageSize);
    }

    public BinPojo selectById(Long id) {
        return binDao.selectById(id);
    }

    private BinPojo createBin() {
        BinPojo binPojo = new BinPojo();
        binDao.add(binPojo);
        return binPojo;
    }

}
