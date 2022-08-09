package assure.util;

import assure.model.ClientData;
import assure.model.ClientForm;
import assure.model.ProductForm;
import assure.pojo.ClientPojo;
import assure.pojo.ProductPojo;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static ClientPojo convertClientFormToPojo(ClientForm clientForm){
        ClientPojo clientPojo = new ClientPojo();
        clientPojo.setName(clientForm.getName());
        clientPojo.setTypes(clientForm.getTypes());

        return clientPojo;
    }


    public static ClientData convertClientPojoToData(ClientPojo clientPojo){
        ClientData clientData = new ClientData();
        clientData.setName(clientData.getName());
        clientData.setTypes(clientData.getTypes());
        clientData.setId(clientData.getId());
        return clientData;
    }
    public static List<ClientData> convertListClientPojoToData(List<ClientPojo> clientPojoList){
        List<ClientData> clientDataList = new ArrayList<>();
        for(ClientPojo clientPojo : clientPojoList){
            clientDataList.add(convertClientPojoToData(clientPojo));
        }

        return clientDataList;
    }

    public static List<ClientPojo> convertListClientFormToPojo(List<ClientForm> clientFormList){
        List<ClientPojo> clientPojoList = new ArrayList<>();
        for(ClientForm clientForm : clientFormList){
            clientPojoList.add(convertClientFormToPojo(clientForm));
        }

        return clientPojoList;
    }

    public static ProductPojo convertProductFormToPojo(ProductForm productForm, Long clientId){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientId(clientId);
        productPojo.setBrandId(productForm.getBrandId());
        productPojo.setDescription(productForm.getDescription());
        productPojo.setMrp(productPojo.getMrp());
        productPojo.setName(productForm.getName());
        productPojo.setClientSkuId(productForm.getClientSkuId());
        return productPojo;
    }

    public static List<ProductPojo> convertListProductFormToPojo(List<ProductForm> productFormList,Long clientId){
        List<ProductPojo> productPojoList = new ArrayList<>();
        for(ProductForm productForm : productFormList){
            productPojoList.add(convertProductFormToPojo(productForm,clientId));
        }
        return productPojoList;
    }
}
