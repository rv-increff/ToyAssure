package assure.dto;

import assure.config.QaConfig;
import assure.util.AbstractTest;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class PartyDtoTest extends AbstractTest {

    @Autowired
    private PartyDto partyDto;

    @Test
    public void addTest() throws ApiException {
        int n = 5;
        List<PartyForm> partyFormList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            partyFormList.add(getPartyForm());
        }
        partyDto.add(partyFormList);
    }

    @Test
    public void selectTest(){
        int n = 5;
        for (int i = 0; i < n; i++) {
            partyAdd();
        }
        Assert.assertEquals(n,partyDto.select(0).size());
    }

    @Test
    public void selectByIdTest() throws ApiException {
        PartyPojo partyPojo = partyAdd();
        Assert.assertNotNull(partyDto.selectById(partyPojo.getId()));
    }
}
