package assure.dto;

import assure.model.ClientForm;
import assure.model.ProductForm;
import assure.services.ClientServices;
import assure.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static assure.util.Helper.convertListProductFormToPojo;

@Service
public class ProductDto {

    @Autowired
    private ProductServices productServices;
    @Autowired
    private ClientServices clientServices;
    public Integer add(List<ProductForm> productFormList, Long clientId){



        productServices.add(convertListProductFormToPojo(productFormList,clientId));
        return productFormList.size();
    }

}
