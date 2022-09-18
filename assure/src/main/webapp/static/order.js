var pageNumber = 0

localStorage.setItem("orderItems", JSON.stringify([]));

function loadOrder() {
    const xhr = new XMLHttpRequest();

    xhr.open("GET", `http://localhost:9000/assure/orders?pageNumber=${pageNumber}`, true);

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
                                      <td><button type='button' class='btn btn-info' onclick=viewOrder(${obj[i]['id']})>VIEW</button></td>`;


                if (obj[i]['status'] == "CREATED") {
                    str += `<td><button type='button' class='btn btn-warning action-btn' onclick=allocateOrder(${obj[i]['id']})>ALLOCATE</button></td>
                    </tr>`
                }
                else if (obj[i]['status'] == "ALLOCATED") {
                    str += `<td><button type='button' class='btn btn-success action-btn' onclick=fullfillOrder(${obj[i]['id']})>FULFILL</button></td>
                    </tr>`
                } else {
                    str += `<td><button type='button' class='btn btn-primary action-btn' onclick=getInvoice(${obj[i]['id']})>INVOICE</button></td>
                    </tr>`
                }
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

    // Open an obejct (GET/POST, PATH,
    // ASYN-TRUE/FALSE)
    xhr.open("GET", `http://localhost:9000/assure/orders?pageNumber=${pageNumber + 1}`, true);
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

function allocateOrder(orderId) {
    $.ajax({
        type: "PATCH",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/orders/${orderId}/allocate`,
        processData: false,
        dataType: 'json',
        success: function (result) {
            console.log(result, "order allocated")
            $.notify(`Order ${orderId} allocated`, "success");
            $('#uploadModal').modal('hide');
            loadOrder();
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

function fullfillOrder(orderId) {
    $.ajax({
        type: "PATCH",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/orders/${orderId}/fulfill`,
        processData: false,
        dataType: 'json',
        success: function (result) {
            console.log(result, "order fulfilled")
            $.notify(`Order ${orderId} fulfilled`, "success");
            $('#uploadModal').modal('hide');
            loadOrder();
            getInvoice(orderId);
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

function getInvoice(id) {
    $.ajax({
        contentType: 'application/json', xhr: function () {
            const xhr = new XMLHttpRequest();
            xhr.responseType = 'blob'
            return xhr;
        },
        success: function (data) {
            // data.append("pdf", blob, "invoice.pdf");
            console.log(data)
            var file = new Blob([data], { type: 'application/octet-stream' },);
            file.name = "invoice.pdf"
            var fileURL = URL.createObjectURL(file);
            setTimeout(() => {
                window.open(fileURL, '_blank');
            })
            console.log(fileURL);
        },
        error: function (e) {
            console.log(e)
            return ""

        },
        processData: false,
        type: 'GET',
        url: `http://localhost:9000/assure/orders/${id}/get-invoice`
    });
}

async function addOrder() {
    obj = JSON.parse(localStorage.getItem("orderItems"));
    console.log(obj);
    console.log(obj, "inLoadfuntion");
    $('.close').css('visibility', 'visible');
    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#modalTitle').text(`Add order`);
    let clientDropDown = await getClientDropDown();
    let customerDropDown = await getCustomerDropDown();
    $('#orderDetails').html(getOrderDetailModal(clientDropDown,customerDropDown));
    $('#orderItemModal-body').html(getAddOrderModalBody());
    loadOrderItemCart()
}

function showOrderModal(){
    loadOrderItemCart();
    $('.close').css('visibility', 'visible');
    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
}

function loadOrderItemCart(){
    let body = document.getElementById("orderItemTbody");
    let obj = JSON.parse(localStorage.getItem("orderItems"));
    str = ""
    for (var i = 0; i < obj.length; i++) {
        str += `<tr>
                                  <td>${obj[i]['clientSkuId']}</td>
                                  <td>${obj[i]['quantity']}</td>
                                  <td>${parseFloat(obj[i]['sellingPricePerUnit']).toFixed(2)}</td>
                                  <td><button type="button" class="btn btn-primary" onclick="editOrderItem(${i},${obj[i]['quantity']},${obj[i]['sellingPricePerUnit']})">Edit</button</td>
                                  <td><button type="button" class="btn btn-primary" onclick="deleteOrderItem(${i})">Delete</button</td>
                                  </tr>`;
    }
    str += ` <div style="padding-top:1rem;padding-bottom:1rem;">
    <button type="button" class="btn btn-primary" onclick="addOrderItem()">Add Items</button>`;
    str += ` 
    <button type="button" class="btn btn-primary" onclick="placeOrder()">Submit</button>
    <a class="" id="errorCsv" href="#" style="float: right;">Download Errors</a><div>`;
    body.innerHTML = str;
}
function placeOrder(){

    let channelOrderId = $('#channelOrderId').val().trim();
    let clientId = $('#clientId').val();
    let customerId = $('#customerId').val();
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
    url: `http://localhost:9000/assure/orders`,
    data: JSON.stringify({
        clientId : clientId,
        channelOrderId : channelOrderId,
        customerId : customerId,
        orderItemFormList : orderItemFormList
    }),
    processData: false,
    dataType: 'json',
    success: function (result) {
        console.log(result, "order placed");
        $.notify(`Order Placed`, "success");
        $('#orderItemModal').modal('hide');
        localStorage.setItem("orderItems", JSON.stringify([]));
        loadOrder();
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

async function addOrderItem(){
    $('.close').css('visibility', 'hidden');
    $('#orderItemModal').modal('hide');
    $('#orderItemAddModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#orderItemTitle').text('Add Order Item');
    let clientSkuIdDropDown = await getClientSkuIdDropDown();
    $('#orderItemModalbody').html(getAddOrderItem(clientSkuIdDropDown));
}

async function editOrderItem(id,qty,price){
    $('.close').css('visibility', 'hidden');
    $('#orderItemModal').modal('hide');
    $('#orderItemAddModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#orderItemTitle').text('Add Order Item');
    let clientSkuIdDropDownUpdate = await getClientSkuIdDropDownUpdate();
    $('#orderItemModalbody').html(getAddOrderItemEdit(id,clientSkuIdDropDownUpdate,qty,price));
    
}
function saveAddEdit(id){
    //validations
      
    obj = JSON.parse(localStorage.getItem("orderItems"));
    clientSkuId = $('#clientSkuId').val();
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

    if(clientSkuId.trim().length==0){
        $.notify("Client SKU  cannot be blank ");
        return;
    }

    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#orderItemAddModal').modal('hide');

    obj[id] = {
        clientSkuId : clientSkuId,
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
    $('.close').css('visibility', 'visible');
    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#orderItemAddModal').modal('hide');

}

function saveAdd(){

    //validations
    obj = JSON.parse(localStorage.getItem("orderItems"));
    clientSkuId = $('#clientSkuId').val();
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
    if(clientSkuId.trim().length==0){
        $.notify("Client SKU  cannot be blank ");
        return;
    }
    $('#orderItemModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#orderItemAddModal').modal('hide');

    obj.push({
        clientSkuId : clientSkuId,
        quantity : qty,
        sellingPricePerUnit : price
    });
    localStorage.setItem("orderItems", JSON.stringify(obj));
    showOrderModal();

}

function getAddOrderItem(clientSkuIdDropDown){
    return ` <form >
    <div class="form-group ">
    <label for="clientSkuId" class="required">Client SKU</label>
    ${clientSkuIdDropDown}
  </div> 
  <div class="form-group ">
  <label for="qty" class="required">Quantity</label>
  <input type="number" class="form-control" id="qty" name="qty" aria-describedby="text" placeholder="Enter Quantity" autocomplete="off" minlength="1" maxlength="20" >
</div>
 <div class="form-group">
<label for="price" class="required">Price Per Unit</label>
<input type="number" class="form-control" id="price" name="price" aria-describedby="text" placeholder="Enter Price Per Unit" autocomplete="off" minlength="1" maxlength="20" >
</div>
<div style="float:right; padding-top:8px">
        <button type="button" class="btn btn-primary" onclick= "saveAdd()">Save</button>
        <button type="button" class="btn btn-secondary" onclick="cancelAdd()" >Close</button>
      </div>
</form>`
}
function getAddOrderItemEdit(id,clientSkuIdDropDownUpdate,qty,price){
    return ` <form >
    <div class="form-group ">
    <label for="clientSkuId" class="required">Client SKU </label>
    ${clientSkuIdDropDownUpdate}
  </div> 
  <div class="form-group ">
  <label for="qty" class="required">Quantity</label>
  <input type="number" class="form-control" id="qty" name="qty" aria-describedby="text" placeholder="Enter Quantity" autocomplete="off" minlength="1" maxlength="20" value=${qty}>
</div>
 <div class="form-group">
<label for="price" class="required">Unit price</label>
<input type="number" class="form-control" id="price" name="price" aria-describedby="text" placeholder="Enter Unit Price" autocomplete="off" minlength="1" maxlength="20" value="${price}">
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
          <th scope="col">Client SKU ID</th>
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

function getOrderDetailModal(clientDropDown, customerDropDown){
    return `
    <form >
    <div class="row">
    <div class="form-group col-12">
    <label for="channelOrderId" class="required">Channel Order ID</label>
    <input type="text" class="form-control" id="channelOrderId" name="channelOrderId" aria-describedby="text" placeholder="Enter channel order ID" autocomplete="off" minlength="1" maxlength="20" >
  </div> 
  <div class="form-group col-12">
  <label for="clientId" class="required">Client Name</label>
  ${clientDropDown}
</div>
 <div class="form-group col-12">
<label for="customerId" class="required">Customer Name</label>
${customerDropDown}
</div>
</div>
</form>
`
}
function getClientDropDown(){
    return new Promise(function(resolve,reject){
        $.ajax({
            type: "POST",
            contentType: 'application/json',
            url: `http://localhost:9000/assure/parties/search`,
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
function getClientSkuIdDropDownUpdate(){
    return new Promise(function(resolve,reject){
        let clientId = $('#clientId').val();
        let clientSkuId = $('#clientSkuId').val();
        console.log(clientSkuId, "clientSkuId")
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: `http://localhost:9000/assure/products/client-id?clientId=${clientId}`,
            processData: false,
            dataType: 'json',
            success: function (result) {
                console.log(result,"clientSkuId drop down")
                obj  = result
                let rows = ``
                for(var i = 0; i < obj.length; i++){
                    console.log(String(clientSkuId), obj[i]['clientSkuId'],String(clientSkuId) === String(obj[i]['clientSkuId']) )
                    if(String(clientSkuId) === String(obj[i]['clientSkuId'])){
                        rows +=  `<option value="${obj[i]['clientSkuId']}" selected>${obj[i]['clientSkuId']}</option>`
                    }else{
                        rows += `<option value="${obj[i]['clientSkuId']}">${obj[i]['clientSkuId']}</option>`
                    }
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
function getClientSkuIdDropDown(){
    return new Promise(function(resolve,reject){
        let clientId = $('#clientId').val();
        $.ajax({
            type: "GET",
            contentType: 'application/json',
            url: `http://localhost:9000/assure/products/client-id?clientId=${clientId}`,
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
function getCustomerDropDown(){
    return new Promise(function(resolve,reject){
        $.ajax({
            type: "POST",
            contentType: 'application/json',
            url: `http://localhost:9000/assure/parties/search`,
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

function viewOrder(orderId) {
    $('.close').css('visibility', 'visible');
    console.log("in view order")
    $.ajax({
        type: "GET",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/orders/${orderId}/order-items`,

        processData: false,
        dataType: 'json',
        success: function (result) {
            let obj = result;
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
function getOrderItemModalBody() {
    return `<div class="jumbotron" id="orderItemView">
    <table class="table">
      <thead class="thead-dark">
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Client SKU ID</th>
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

