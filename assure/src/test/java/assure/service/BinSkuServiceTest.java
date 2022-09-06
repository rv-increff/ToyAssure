package assure.service;

import assure.config.QaConfig;
import assure.util.AbstractTest;
import assure.pojo.BinSkuPojo;
import assure.spring.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class BinSkuServiceTest extends AbstractTest {

    @Autowired
    private BinSkuService binSkuService;

    @Test
    public void add() throws ApiException {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        binSkuPojoList.add(binSkuAdd());
        for (int i = 0; i < 5; i++) {
            binSkuPojoList.add(getBinSku());
        }
        binSkuService.add(binSkuPojoList);
    }

    @Test
    public void addDuplicateError() {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            binSkuPojoList.add(getBinSku());
        }
        binSkuPojoList.add(binSkuPojoList.get(0));
        try {
            binSkuService.add(binSkuPojoList);
            fail("error not thrown");
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "duplicate values of globalSkuId-binId pair");
        }
    }

    @Test
    public void updateTest() throws ApiException {
        BinSkuPojo binSkuPojo = binSkuAdd();
        BinSkuPojo updateBinSkuPojo = getBinSku();
        updateBinSkuPojo.setId(binSkuPojo.getId());
        binSkuService.update(updateBinSkuPojo);
    }

    @Test
    public void updateError() {
        BinSkuPojo binSkuPojo = getBinSku();
        binSkuPojo.setId(1L);
        try {
            binSkuService.update(binSkuPojo);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), "id doesn't exist, id : " + binSkuPojo.getId());
        }
    }

    @Test
    public void select() {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            binSkuPojoList.add(binSkuAdd());
        }
        Assert.assertEquals(binSkuPojoList, binSkuService.select(0, 5));
    }

    @Test
    public void allocateQtyTest() {
        BinSkuPojo binSkuPojo = binSkuAdd();

        binSkuService.allocateQty(binSkuPojo.getQuantity(), binSkuPojo.getGlobalSkuId());
        Assert.assertEquals((long) binSkuPojo.getQuantity(), 0L);
    }


}
