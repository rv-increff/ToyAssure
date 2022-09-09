var pageNumber = 0

localStorage.setItem("orderItems", JSON.stringify([]));

function loadOrder() {
    const xhr = new XMLHttpRequest();

    xhr.open("GET", `http://localhost:9000/assure/orders?InvoiceType=CHANNEL&pageNumber=${pageNumber}`, true);

    xhr.onload = function () {
        if (this.status === 200) {

            obj = JSON.parse(this.responseText);
            console.log(obj, "inLoadfuntion");

            let body = document.getElementById("orderTbody");

            str = ""
            for (var i = 0; i < obj.length; i++) {
                str += `<tr>
                                      <td>${obj[i]['id']}</td>
                                      <td>${obj[i]['clientId']}</td>
                                      <td>${obj[i]['customerId']}</td>
                                      <td>${obj[i]['channelId']}</td>
                                      <td>${obj[i]['channelOrderId']}</td>
                                      <td>${obj[i]['status']}</td>
                                      <td><button type='button' class='btn btn-info' onclick=viewOrder(${obj[i]['id']})>VIEW</button></td></tr>`;
            }
            body.innerHTML = str;
            checkNextPageNotExist()
        }
        else {
            console.log("cannot fetch order");
        }
    }

    xhr.send();
}

function nextPage() {
    pageNumber += 1;
    console.log(document.getElementById("page"))
    document.getElementById("page").innerText = pageNumber + 1;
    loadOrder()

}

function prevPage() {
    pageNumber -= 1;
    document.getElementById("page").innerText = pageNumber + 1;
    loadOrder();
    if (pageNumber == 0) {
        document.getElementById("prevLi").className = "page-item disabled";
        document.getElementById("nextLi").className = "page-item";
    }
}

function checkNextPageNotExist() {
    // Instantiate an new XHR Object
    const xhr = new XMLHttpRequest();

    xhr.open("GET", `http://localhost:9000/assure/orders?pageNumber=${pageNumber + 1}`, true);
    // When response is ready
    xhr.onload = function () {
        if (this.status === 200) {
            // Changing string data into JSON Object
            obj = JSON.parse(this.responseText);
            console.log(obj, obj.length === 0);
            if (obj.length === 0) {
                document.getElementById("prevLi").className = "page-item";
                document.getElementById("nextLi").className = "page-item disabled";
                console.log("in next check")
            } else {
                document.getElementById("prevLi").className = "page-item";
            }

        }
        else {
            console.log("cannot fetch order");
        }
    }
    xhr.send();
}

async function addOrder() {
    let obj = JSON.parse(localStorage.getItem("orderItems"));
    console.log(obj);
    console.log(obj, "inLoadfuntion");
    $('#orderItemModal').modal('show');
    $('#modalTitle').text(`Add order`);
    let clientDropDown = await getClientDropDown();
    let channelDropDown = await getChannelDropDown();
    let customerDropDown = await getCustomerDropDown();
    $('#orderDetails').html(getOrderDetailModal(channelDropDown,customerDropDown,clientDropDown));
    $('#orderItemModal-body').html(getAddOrderModalBody());
    loadOrderItemCart()
}

function showOrderModal(){
    loadOrderItemCart();
    $('#orderItemModal').modal('show');
}

function loadOrderItemCart(){
    let obj = JSON.parse(localStorage.getItem("orderItems"));
    let body = document.getElementById("orderItemTbody");

    str = ""
    for (var i = 0; i < obj.length; i++) {
        str += `<tr>
                                  <td>${obj[i]['channelSkuId']}</td>
                                  <td>${obj[i]['quantity']}</td>
                                  <td>${obj[i]['sellingPricePerUnit']}</td>
                                  <td><button type="button" class="btn btn-primary" onclick="editOrderItem(${i},'${obj[i]['channelSkuId']}',${obj[i]['quantity']},${obj[i]['sellingPricePerUnit']})">Edit</button</td>
                                  <td><button type="button" class="btn btn-primary" onclick="deleteOrderItem(${i})">Delete</button</td>
                                  </tr>`;
    }
    str += ` <div style="padding-top:1rem;padding-bottom:1rem;">
    <button type="button" class="btn btn-primary" onclick="addOrderItem()">Add Items</button>`;
    str += ` 
    <button type="button" class="btn btn-primary" onclick="placeOrder()">Submit</button> 
    <a class="" id="errorCsv" href="#" style="float: right;">Download Errors</a>
  <div>`;
    body.innerHTML = str;
}
function placeOrder(){
    let channelOrderId = $('#channelOrderId').val();
    let clientId = $('#clientId').val();
    let customerId = $('#customerId').val();
    let channelId = $('#channelId').val();
    let orderItemFormList = JSON.parse(localStorage.getItem("orderItems"));
    //validation 
    if ( !Number.isInteger(parseFloat(clientId)) || parseInt(clientId) < 0) {
        $.notify("Enter valid client ID");
        return;
    }
    if ( !Number.isInteger(parseFloat(customerId)) || parseInt(customerId) < 0) {
        $.notify("Enter valid customer ID");
        return;
    }

    if ( !Number.isInteger(parseFloat(channelId)) || parseInt(channelId) < 0) {
        $.notify("Enter valid hannel ID");
        return;
    }



    if(channelOrderId.trim().length==0){
        $.notify("Channel order ID cannot be blank ");
        return;
    }
    if(orderItemFormList.length==0){
        $.notify("No orders in cart");
        return;
    }

//ajax call;
$.ajax({
    type: "POST",
    contentType: 'application/json',
    url: `http://localhost:9001/channel/orders`,
    data: JSON.stringify({
        clientId : clientId,
        channelOrderId : channelOrderId,
        customerId : customerId,
        channelId : channelId,
        orderItemFormChannelList : orderItemFormList
    }),
    processData: false,
    dataType: 'json',
    success: function (result) {
        console.log(result, "order placed");
        let obj = result;
        console.log(obj)
        if(parseInt(orderItemFormList) === orderItemFormList.length){
            $.notify(`Order Placed`, "success");
            $('#orderItemModal').modal('hide');
            loadOrder();
            return;
        }
        
        if(obj.code === 200){
            $.notify(`Order Placed`, "success");
            $('#orderItemModal').modal('hide');
            loadOrder();
        }else {
            if (obj['errorType'] === 0) {
                $.notify(obj['description']);
                return;
            }
            $.notify("Error occurred download error list file");
            $('#errorCsv').click(function () {
                writeFileData(obj['description'], "error");
            });

        }
    },
    error: function (xhr, status, error) {
        console.log(status, error, xhr)
        if (xhr['responseJSON']['errorType'] === 0) {
            $.notify(xhr['responseJSON']['description']);
            return;
        }
        $.notify("Error occurred download error list file");
        $('#errorCsv').click(function () {
            writeFileData(xhr['responseJSON']['description'], "error");
        });
    }
});

}

function addOrderItem(){
    $('#orderItemModal').modal('hide');
    $('#orderItemAddModal').modal('show');
    $('#orderItemModalbody').html(getAddOrderItem());
}

function editOrderItem(id, channelSkuId,qty,price){
    $('#orderItemModal').modal('hide');
    $('#orderItemAddModal').modal('show');
    $('#orderItemModalbody').html(getAddOrderItemEdit(id,channelSkuId,qty,price));
    
}
function saveAddEdit(id){
    //validations
      
    obj = JSON.parse(localStorage.getItem("orderItems"));
    channelSkuId = $('#channelSkuId').val();
    qty = $('#qty').val();
    price = $('#price').val();

    if ( !Number.isInteger(parseFloat(qty)) || parseInt(qty) < 0) {
        $.notify("Enter valid quantity");
        return;
    }

    
    if (isNaN(parseFloat(price)) || parseFloat(price) < 0) {
        $.notify("Enter valid Price per unit ");
        return;
    }

    if(channelSkuId.trim().length==0){
        $.notify("Channel SKU ID cannot be blank ");
        return;
    }

    $('#orderItemModal').modal('show');
    $('#orderItemAddModal').modal('hide');

    obj[id] = {
        channelSkuId : channelSkuId,
        quantity : qty,
        sellingPricePerUnit : price
    }
    localStorage.setItem("orderItems", JSON.stringify(obj));
    showOrderModal();

}

function deleteOrderItem(id){
    obj = JSON.parse(localStorage.getItem("orderItems"));
    obj.splice(id,1);
    localStorage.setItem("orderItems", JSON.stringify(obj));
    showOrderModal();

}

function cancelAdd(){
    $('#orderItemModal').modal('show');
    $('#orderItemAddModal').modal('hide');

}

function saveAdd(){

    //validations
    obj = JSON.parse(localStorage.getItem("orderItems"));
    channelSkuId = $('#channelSkuId').val();
    qty = $('#qty').val();
    price = $('#price').val();

    if (!Number.isInteger(parseFloat(qty)) || parseInt(qty) < 0) {
        $.notify("Enter valid quantity");
        return;
    }
    
    if (isNaN(parseFloat(price)) || parseFloat(price) < 0) {
        $.notify("Enter valid Price per unit ");
        return;
    }
    if(channelSkuId.trim().length==0){
        $.notify("Channel SKU ID cannot be blank ");
        return;
    }
    $('#orderItemModal').modal('show');
    $('#orderItemAddModal').modal('hide');

    obj.push({
        channelSkuId : channelSkuId,
        quantity : qty,
        sellingPricePerUnit : price
    });
    localStorage.setItem("orderItems", JSON.stringify(obj));
    showOrderModal();

}

function getAddOrderItem(){
    return ` <form >
    <div class="form-group ">
    <label for="channelSkuId">Channel SKU</label>
    <input type="text" class="form-control" id="channelSkuId" name="channelSkuId" aria-describedby="text" placeholder="Enter Channel SKU ID" autocomplete="off" minlength="1" maxlength="20" >
  </div> 
  <div class="form-group ">
  <label for="qty">Quantity</label>
  <input type="number" class="form-control" id="qty" name="qty" aria-describedby="text" placeholder="Enter Quantity" autocomplete="off" minlength="1" maxlength="20" >
</div>
 <div class="form-group">
<label for="price">Unit Price</label>
<input type="number" class="form-control" id="price" name="price" aria-describedby="text" placeholder="Enter Unit Price" autocomplete="off" minlength="1" maxlength="20" >
</div>
<div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick= "saveAdd()">Save</button>
        <button type="button" class="btn btn-secondary" onclick="cancelAdd()" >Close</button>
      </div>
</form>`
}
function getAddOrderItemEdit(id,channelSkuId,qty,price){
    return ` <form >
    <div class="form-group ">
    <label for="channelSkuId">Channel SKU ID</label>
    <input type="text" class="form-control" id="channelSkuId" name="channelSkuId" aria-describedby="text" placeholder="Enter Channel SKU ID" autocomplete="off" minlength="1" maxlength="20" value="${channelSkuId}">
  </div> 
  <div class="form-group ">
  <label for="qty">Quantity</label>
  <input type="number" class="form-control" id="qty" name="qty" aria-describedby="text" placeholder="Enter Channel ID" autocomplete="off" minlength="1" maxlength="20" value=${qty}>
</div>
 <div class="form-group">
<label for="price">Price Per Unit</label>
<input type="number" class="form-control" id="price" name="price" aria-describedby="text" placeholder="Enter Customer ID" autocomplete="off" minlength="1" maxlength="20" value="${price}">
</div>
<div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick= "saveAddEdit(${id})">Save</button>
        <button type="button" class="btn btn-secondary" onclick="cancelAdd()" >Close</button>
      </div>
</form>`
}

function getAddOrderModalBody() {
    return `
    
    <div class="jumbotron" id="orderItemView">
    <table class="table">
      <thead class="thead-dark">
        <tr>
          <th scope="col">Channel SKU ID</th>
          <th scope="col">Quantity</th>
          <th scope="col">Unit Price</th>
          <th scope="col">Edit</th>
          <th scope="col">Delete</th>
        </tr>
    </thead>

    <tbody id="orderItemTbody">
    </tbody>
</table>
</div>`
}

function getOrderDetailModal(channelDropDown,customerDropDown,clientDropDown){
    return `
    <form >
    <div class="row">
        <div class="form-group col-12">
            <label for="channelOrderId">Channel Order ID</label>
            <input type="text" class="form-control" id="channelOrderId" name="channelOrderId" aria-describedby="text" placeholder="Enter Channel Order ID" autocomplete="off" minlength="1" maxlength="20" >
        </div> 
        <div class="form-group col-12">
            <label for="channelId">Channel Name</label>
            ${channelDropDown}
        </div>
        <div class="form-group col-12">
            <label for="customerId">Customer Name</label>
            ${customerDropDown}
        </div>
        <div class="form-group col-12">
            <label for="clientId">Client Name</label>
            ${clientDropDown}
        </div>
</form>
`
}

function viewOrder(orderId) {
    console.log("in view order")
    const xhr = new XMLHttpRequest();

    xhr.open("GET", `http://localhost:9000/assure/orders/${orderId}/order-items`, true);

    xhr.onload = function () {
        if (this.status === 200) {

            obj = JSON.parse(this.responseText);
            console.log(obj);
            console.log(obj, "inLoadfuntion");
            $('#orderItemModal').modal('show');
            $('#orderDetails').html('')
            $('#modalTitle').text(`Order Id - ${orderId}`);
            $('#orderItemModal-body').html(getOrderItemModalBody());
            let body = document.getElementById("orderItemTbody");

            str = ""
            for (var i = 0; i < obj.length; i++) {
                str += `<tr>
                                          <td>${obj[i]['id']}</td>
                                          <td>${obj[i]['clientSkuId']}</td>
                                          <td>${obj[i]['orderedQuantity']}</td>
                                          <td>${obj[i]['allocatedQuantity']}</td>
                                          <td>${obj[i]['fulfilledQuantity']}</td>
                                          <td>${obj[i]['sellingPricePerUnit']}</td>
                                          </tr>`;
            }
        
            body.innerHTML = str;
        }
        else {
            console.log("cannot fetch orderItem");
            console.log(status, error, xhr)

            if (xhr['responseJSON']['errorType'] === 0) {
                $.notify(xhr['responseJSON']['description']);
                return;
            }
            $.notify("Error occurred download error list file");
            $('#errorCsv').click(function () {
                writeFileData(xhr['responseJSON']['description'], "error");
            });
        }
    }

    xhr.send();

}
function getOrderItemModalBody() {
    return `<div class="jumbotron" id="orderItemView">
    <table class="table">
      <thead class="thead-dark">
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Channel SKU ID</th>
          <th scope="col">Ordered Quantity</th>
          <th scope="col">Allocated Quantity</th>
          <th scope="col">Fulfilled Quantity</th>
          <th scope="col">Unit Price</th>
        </tr>
    </thead>

    <tbody id="orderItemTbody">
    </tbody>
</table>
</div>`
}

function getOrderUpdateModal(globalSkuId, channelSkuId, name, brandId, mrp, description) {
    return `<form id="editOrderForm" >
    <div id="modalFormDataDiv">
      <div class="form-group">
        <label for="">Channel SKU ID</label>
        <input type="text" class="form-control" id="channelSkuId" name="channelSkuId" aria-describedby="text" placeholder="Enter ChannelSkuId" autocomplete="off" minlength="1" maxlength="20" value="${channelSkuId}">
      </div>
      <div class="form-group">
        <label for="">Name</label>
        <input type="text" class="form-control" id="name" name="name" aria-describedby="text" placeholder="Enter name" autocomplete="off" minlength="1" maxlength="20" value="${name}">
      </div>
      <div class="form-group">
        <label for="brandId">Brand ID</label>
        <input type="text" class="form-control" id="brandId" name="brandId" aria-describedby="text" placeholder="Enter brand ID" autocomplete="off" minlength="1" maxlength="20" value="${brandId}">
      </div>
      <div class="form-group">
        <label for="mrp">MRP</label>
        <input type="number" class="form-control" id="mrp" name="mrp" aria-describedby="text" placeholder="Enter MRP" autocomplete="off" minlength="1" value="${mrp}" max="1000000">
        </div>
      <div class="form-group">
        <label for="description">Description</label>
        <input type="text" class="form-control" id="description" name="description" aria-describedby="text" placeholder="Enter description" autocomplete="off" minlength="1" maxlength="40" value="${description}">
      </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
      <button type="button" class="btn btn-primary" onclick="editOrderModalCall(${globalSkuId})">Save</button>
    </div>
  </form>`
}

function getUploadModalBody() {
    return `<form>
   
    <div class="form-group">
    <label for="partyId" class="form-label">Party Id</label>
    <input class="form-control" type="number" id="partyId" >
    </div>
  </div>
      <div class="form-group">
        <label for="formFile" class="form-label">Select csv file for upload</label>
        <input class="form-control" type="file" id="formFile" accept=".csv">
      </div>
      <div class="form-group">
        <a class="" href="/assure/static/orderTemplate.csv" download>Download Template</a>
        <a class="" id="errorCsv" href="#" style="float: right;">Download Errors</a>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="uploadModalBtn" onclick="uploadOrderFile()">Upload</button>
      </div>
    </div>

  </form>`
}
function writeFileData(arr, fname) {
    console.log(fname, " in write file data")
    var config = {
        quoteChar: '',
        escapeChar: '',
        delimiter: ","
    };

    var data = Papa.unparse(arr, config);
    var blob = new Blob([data], { type: 'text/csv;charset=utf-8;' });
    var fileUrl = null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, `${fname}.csv`);
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', `${fname}.csv`);
    tempLink.click();
}

function writeFileData(arr, fname) {
    console.log(fname, " in write file data")
    var config = {
        quoteChar: '',
        escapeChar: '',
        delimiter: ","
    };

    var data = Papa.unparse(arr, config);
    var blob = new Blob([data], { type: 'text/csv;charset=utf-8;' });
    var fileUrl = null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, `${fname}.csv`);
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', `${fname}.csv`);
    tempLink.click();
}


function getClientDropDown(){
    
    return new Promise(function(resolve,reject){
        const xhr = new XMLHttpRequest();
        xhr.open("GET", `http://localhost:9000/assure/parties/partyType/CLIENT`, true);

    xhr.onload = function () {
        if (this.status === 200) {

            let obj  = JSON.parse(this.responseText);
            console.log(obj,"result drop down")
                let rows = ``
                for(var i = 0; i < obj.length; i++){
                    rows += `<option value="${obj[i]['id']}">${obj[i]['name']}</option>`
                }
                console.log(rows)
                let drop =  `<select class="custom-select col-8 float-right" size="1" aria-label="Client ID" id="clientId">${rows}</select>`
                
                resolve(drop);
        }
        else {
            reject();
        }
    }

    xhr.send();
    })
   
}

function getChannelDropDown(){
    return new Promise(function(resolve,reject){
    const xhr = new XMLHttpRequest();
        
        xhr.open("GET", `http://localhost:9000/assure/channels`, true);

    xhr.onload = function () {
        if (this.status === 200) {

            let obj  = JSON.parse(this.responseText);
            console.log(obj,"result drop down")
               
                let rows = ``
                for(var i = 0; i < obj.length; i++){
                    if(obj[i]['invoiceTypes']!=="SELF")
                        rows += `<option value="${obj[i]['id']}">${obj[i]['name']}</option>`
                }
                // console.log(rows)
                let drop =  `<select class="custom-select col-8 float-right" size="1" aria-label="Channel ID" id="channelId">${rows}</select>`
                
                resolve(drop);
        }
        else {
            reject();
        }
    }

    xhr.send();
    })
    
}

function getCustomerDropDown(){
    return new Promise(function(resolve,reject){
    const xhr = new XMLHttpRequest();

        xhr.open("GET", `http://localhost:9000/assure/parties/partyType/CUSTOMER`, true);

    xhr.onload = function () {
        if (this.status === 200) {

            let obj  = JSON.parse(this.responseText);
            console.log(obj,"result drop down")
      
                let rows = ``
                for(var i = 0; i < obj.length; i++){
                    rows += `<option value="${obj[i]['id']}">${obj[i]['name']}</option>`
                }
                // console.log(rows)
                let drop =  `<select class="custom-select col-8 float-right" size="1" aria-label="Customer ID" id="customerId">${rows}</select>`
                
                resolve(drop);
        }
        else {
            reject();
        }
    }

    xhr.send();
    })
}

function getClientSkuIdDropDown(){
    return new Promise(function(resolve,reject){
        const xhr = new XMLHttpRequest();
        let clientId = $('#clientId').val();
        xhr.open("GET", `http://localhost:9000/assure/products/client-id/${clientId}`, true);
    
        xhr.onload = function () {
            if (this.status === 200) {
    
                let obj  = JSON.parse(this.responseText);
                console.log(obj,"clientSkuId drop down")
                obj  = result
                let rows = ``
                for(var i = 0; i < obj.length; i++){
                    rows += `<option value="${obj[i]['clientSkuId']}">${obj[i]['clientSkuId']}</option>`
                }
                console.log(rows)
                let drop =  `<select class="custom-select col-8 float-right" size="1" aria-label="Client ID" id="clientSkuId">${rows}</select>`
                
                resolve(drop);
            }
            else {
                reject();
            }
        }
        xhr.send();
        })
}

function getChannelSkuIdDropDown(){
    return new Promise(function(resolve,reject){
        let clientId = $('#clientId').val();
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: `http://localhost:9000/assure/products/client-id/${clientId}`,
            processData: false,
            dataType: 'json',
            success: function (result) {
                console.log(result,"clientSkuId drop down")
                obj  = result
                let rows = ``
                for(var i = 0; i < obj.length; i++){
                    rows += `<option value="${obj[i]['clientSkuId']}">${obj[i]['clientSkuId']}</option>`
                }
                console.log(rows)
                let drop =  `<select class="custom-select col-8 float-right" size="1" aria-label="Client ID" id="clientSkuId">${rows}</select>`
                
                resolve(drop);
            },
            error: function (xhr, status, error) {
                console.log(status, error, xhr)
    
                if (xhr['responseJSON']['errorType'] === 0) {
                    $.notify(xhr['responseJSON']['description']);
                    reject();
                }
                $.notify("Error occurred download error list file");
                $('#errorCsv').click(function () {
                    writeFileData(xhr['responseJSON']['description'], "error");
                });
                reject();
            }
        });
    })
    
}