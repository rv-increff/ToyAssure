package assure.dto;

import assure.config.QaConfig;
import assure.util.BaseTest;
import assure.util.TestData;
import assure.model.PartyForm;
import assure.pojo.PartyPojo;
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


public class PartyDtoTest extends BaseTest {

    @Autowired
    private PartyDto partyDto;
    @Autowired
    private TestData testData;

    @Test
    public void addTest() throws ApiException {
        int n = 5;
        List<PartyForm> partyFormList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            partyFormList.add(testData.getPartyForm());
        }
        partyDto.add(partyFormList);
    }

    @Test
    public void selectTest(){
        int n = 5;
        for (int i = 0; i < n; i++) {
            testData.partyAdd();
        }
        Assert.assertEquals(n,partyDto.select(0).size());
    }

    @Test
    public void selectByIdTest() throws ApiException {
        PartyPojo partyPojo = testData.partyAdd();
        Assert.assertNotNull(partyDto.selectById(partyPojo.getId()));
    }
}
