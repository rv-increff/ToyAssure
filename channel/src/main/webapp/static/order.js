var pageNumber = 0

localStorage.setItem("orderItems", JSON.stringify([]));

function loadOrder() {
    const xhr = new XMLHttpRequest();

    xhr.open("GET", `http://localhost:9001/channel/orders?pageNumber=${pageNumber}`, true);

    xhr.onload = function () {
        if (this.status === 200) {

            obj = JSON.parse(this.responseText);
            console.log(obj, "inLoadfuntion");

            let body = document.getElementById("orderTbody");

            str = ""
            for (var i = 0; i < obj.length; i++) {
                str += `<tr>
                                      <td>${obj[i]['clientName']}</td>
                                      <td>${obj[i]['customerName']}</td>
                                      <td>${obj[i]['channelName']}</td>
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
    document.getElementById("prevLi").className = "page-item";
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

    xhr.open("GET", `http://localhost:9001/channel/orders?pageNumber=${pageNumber + 1}`, true);
    // When response is ready
    xhr.onload = function () {
        if (this.status === 200) {
            // Changing string data into JSON Object
            obj = JSON.parse(this.responseText);
            console.log(obj, obj.length === 0);
            if (obj.length === 0) {
                document.getElementById("nextLi").className = "page-item disabled";
                console.log("in next check")
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
    $('#modalTitle').text(`Add order`);
    let clientDropDown = await getClientDropDown();
    let channelDropDown = await getChannelDropDown();
    let customerDropDown = await getCustomerDropDown();
    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#orderDetails').html(getOrderDetailModal(channelDropDown,customerDropDown,clientDropDown));
    $('#orderItemModal-body').html(getAddOrderModalBody());
    loadOrderItemCart()
}

function showOrderModal(){
    loadOrderItemCart();
    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
}

function loadOrderItemCart(){
    let obj = JSON.parse(localStorage.getItem("orderItems"));
    let body = document.getElementById("orderItemTbody");

    str = ""
    for (var i = 0; i < obj.length; i++) {
        str += `<tr>
                                  <td>${obj[i]['channelSkuId']}</td>
                                  <td>${obj[i]['quantity']}</td>
                                  <td>${parseFloat(obj[i]['sellingPricePerUnit']).toFixed(2)}</td>
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
    
    if(channelOrderId.indexOf(' ')>=0){
        $.notify("Channel order ID cannot have empty space ");
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
        $.notify(`Order Placed`, "success");
        $('#orderItemModal').modal('hide');
        localStorage.setItem("orderItems", JSON.stringify([]));
        loadOrder();
       
    },
    error: function (xhr, status, error) {
        console.log(status, error, xhr)

        if (xhr['responseJSON']['errorType'] === 0) {
            $.notify(xhr['responseJSON']['description']);
            return
        }
        $.notify("Error occurred download error list file");
        $('#errorCsv').click(function () {
            writeFileData(xhr['responseJSON']['description'], "error");
        });
        
    }
});

}

async function addOrderItem(){
    let channelSkuIdDropDown = await getChannelSkuIdDropDown();
    $('#orderItemModal').modal('hide');
    $('#orderItemAddModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#orderItemTitle').text('Add Order Item');
    $('#orderItemModalbody').html(getAddOrderItem(channelSkuIdDropDown));
}

function editOrderItem(id, channelSkuId,qty,price){
    $('#orderItemModal').modal('hide');
    $('#orderItemAddModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#orderItemTitle').text('Add Order Item');
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

    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
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
    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
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

  
    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#orderItemAddModal').modal('hide');

    obj.push({
        channelSkuId : channelSkuId,
        quantity : qty,
        sellingPricePerUnit : price
    });
    localStorage.setItem("orderItems", JSON.stringify(obj));
    showOrderModal();

}

function getAddOrderItem(channelSkuIdDropDown){
    return ` <form >
    <div class="form-group ">
    <label for="channelSkuId" class="required">Channel SKU</label>
    ${channelSkuIdDropDown}
  </div> 
  <div class="form-group ">
  <label for="qty" class="required">Quantity</label>
  <input type="number" class="form-control" id="qty" name="qty" aria-describedby="text" placeholder="Enter Quantity" autocomplete="off" minlength="1" maxlength="20" >
</div>
 <div class="form-group">
<label for="price" class="required">Unit Price</label>
<input type="number" class="form-control" id="price" name="price" aria-describedby="text" placeholder="Enter Unit Price" autocomplete="off" minlength="1" maxlength="20" >
</div>
<div style="float:right; padding-top:8px">
        <button type="button" class="btn btn-primary" onclick= "saveAdd()">Save</button>
        <button type="button" class="btn btn-secondary" onclick="cancelAdd()" >Close</button>
      </div>
</form>`
}
function getAddOrderItemEdit(id,channelSkuId,qty,price){
    return ` <form >
    <div class="form-group " class="required">
    <label for="channelSkuId" class="required">Channel SKU</label>
    <input type="text" class="form-control" id="channelSkuId" name="channelSkuId" aria-describedby="text" placeholder="Enter Channel SKU ID" autocomplete="off" minlength="1" maxlength="20" value="${channelSkuId}">
  </div> 
  <div class="form-group " class="required">
  <label for="qty" class="required">Quantity</label>
  <input type="number" class="form-control" id="qty" name="qty" aria-describedby="text" placeholder="Enter Channel ID" autocomplete="off" minlength="1" maxlength="20" value=${qty}>
</div>
 <div class="form-group" class="required">
<label for="price" class="required">Price Per Unit</label>
<input type="number" class="form-control" id="price" name="price" aria-describedby="text" placeholder="Enter Customer ID" autocomplete="off" minlength="1" maxlength="20" value="${price}">
</div>
<div style="float:right; padding-top:8px">
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
            <label for="channelOrderId" class="required">Channel Order ID</label>
            <input type="text" class="form-control" id="channelOrderId" name="channelOrderId" aria-describedby="text" placeholder="Enter Channel Order ID" autocomplete="off" minlength="1" maxlength="20" >
        </div> 
        <div class="form-group col-12">
            <label for="channelId" class="required">Channel Name</label>
            ${channelDropDown}
        </div>
        <div class="form-group col-12">
            <label for="customerId" class="required">Customer Name</label>
            ${customerDropDown}
        </div>
        <div class="form-group col-12">
            <label for="clientId" class="required">Client Name</label>
            ${clientDropDown}
        </div>
</form>
`
}

function viewOrder(orderId) {
    console.log("in view order")
    const xhr = new XMLHttpRequest();

    xhr.open("GET", `http://localhost:9001/channel/orders/${orderId}/order-items`, true);

    xhr.onload = function () {
        if (this.status === 200) {

            obj = JSON.parse(this.responseText);
            console.log(obj);
            console.log(obj, "inLoadfuntion");
            $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
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
                                          <td>${obj[i]['sellingPricePerUnit'].toFixed(2)}</td>
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
        $.ajax({
            type: "POST",
            contentType: 'application/json',
            url: `http://localhost:9001/channel/parties/search`,
            processData: false,
            data: JSON.stringify({
                type: "CLIENT",
            }),
            dataType: 'json',
            success: function (result) {
                console.log(result,"result drop down")
                obj  = result
                let rows = ``
                for(var i = 0; i < obj.length; i++){
                    rows += `<option value="${obj[i]['id']}">${obj[i]['name']}</option>`
                }
                console.log(rows)
                let drop =  `<select class="custom-select col-8 float-right" size="1" aria-label="Client ID" id="clientId">${rows}</select>`
                
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

function getChannelDropDown(){
    return new Promise(function(resolve,reject){
    const xhr = new XMLHttpRequest();
        
        xhr.open("GET", `http://localhost:9001/channel/channels`, true);

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
        $.ajax({
            type: "POST",
            contentType: 'application/json',
            url: `http://localhost:9001/channel/parties/search`,
            processData: false,
            dataType: 'json',
            data: JSON.stringify({
                type: "CUSTOMER",
            }),
            success: function (result) {
                console.log(result,"result drop down")
                obj  = result
                let rows = ``
                for(var i = 0; i < obj.length; i++){
                    rows += `<option value="${obj[i]['id']}">${obj[i]['name']}</option>`
                }
                // console.log(rows)
                let drop =  `<select class="custom-select col-8 float-right" size="1" aria-label="Customer ID" id="customerId">${rows}</select>`
                
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

function getChannelSkuIdDropDown(){
    return new Promise(function(resolve,reject){
        let clientId = $('#clientId').val();
        let channelId = $('#channelId').val();
        const xhr = new XMLHttpRequest();

        xhr.open("GET", `http://localhost:9001/channel/channel-listings?channelId=${channelId}&clientId=${clientId}`, true);

    xhr.onload = function () {
        if (this.status === 200) {

            let obj  = JSON.parse(this.responseText);
            console.log(obj,"channel-listing drop down")
            let rows = ``
            for(var i = 0; i < obj.length; i++){
                rows += `<option value="${obj[i]['channelSkuId']}">${obj[i]['channelSkuId']}</option>`
            }
            console.log(rows)
            let drop =  `<select class="custom-select col-8 float-right" size="1" aria-label="Channel SKU ID" id="channelSkuId">${rows}</select>`
            
            resolve(drop);
        }
        else {
            reject();
        }
    }

    xhr.send();
    })    
}