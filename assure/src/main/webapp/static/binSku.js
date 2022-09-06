var pageNumber = 0
function loadBinSku(){
    // Instantiate an new XHR Object
            const xhr = new XMLHttpRequest();

            // Open an obejct (GET/POST, PATH,
            // ASYN-TRUE/FALSE)
            xhr.open("GET",`http://localhost:9000/assure/bin-skus?pageNumber=${pageNumber}`, true);
            // When response is ready
            xhr.onload = function () {
                if (this.status === 200) {
                    // Changing string data into JSON Object
                    obj = JSON.parse(this.responseText);
                    console.log(obj,"inLoadfuntion");
                    // Getting the ul element
                    let body = document.getElementById("binSkuTbody");
                
                    str = ""
                    for (var i=0;i<obj.length;i++) {
                        str += `<tr>
                                      <th scope="row">${obj[i]['binId']}</th>
                                      <td>${obj[i]['quantity']}</td>
                                      <td>${obj[i]['globalSkuId']}</td>
                                      <td>${obj[i]['clientSkuId']}</td>
                                      <td><button type='button' class='btn btn-primary' onclick=editBinSKuModal(${obj[i]['id']},"${obj[i]['quantity']}")>Edit</button></td>
                                    </tr>`;
                    }
                    body.innerHTML = str;
                    checkNextPageNotExist()
                }
                else {
                    console.log("cannot fetch binSku");
                }
            }
    
            // At last send the request
            xhr.send();
    }

function nextPage(){
 pageNumber += 1;
 console.log(document.getElementById("page"))
 document.getElementById("page").innerText = pageNumber+1;
 loadBinSku()
 
}

function prevPage(){
    pageNumber -= 1;
    document.getElementById("page").innerText = pageNumber+1;
    loadBinSku();
    if(pageNumber==0){
        document.getElementById("prevLi").className = "page-item disabled";
        document.getElementById("nextLi").className = "page-item";
    }
}

function checkNextPageNotExist(){
    // Instantiate an new XHR Object
            const xhr = new XMLHttpRequest();

            // Open an obejct (GET/POST, PATH,
            // ASYN-TRUE/FALSE)
            xhr.open("GET",`http://localhost:9000/assure/bin-skus?pageNumber=${pageNumber+1}`, true);
            // When response is ready
            xhr.onload = function () {
                if (this.status === 200) {
                    // Changing string data into JSON Object
                    obj = JSON.parse(this.responseText);
                    console.log(obj,obj.length===0);
                   if(obj.length===0){
                        document.getElementById("prevLi").className = "page-item";
                        document.getElementById("nextLi").className = "page-item disabled";
                        console.log("in next check")
                    }else{
                        document.getElementById("prevLi").className = "page-item";
                    }
                    
                }
                else {
                    console.log("cannot fetch binSku");
                }
            }
    
            // At last send the request
            xhr.send();
    }


function generateBins(){
    $('#binSkuEditModal').modal('show');
    $('#binSkuModalbody').html(getBinModalBody);

}

function generateBinsCall(){
    let n = $('#numberOfBins').val();
        $.ajax({
            type: "POST",
            url: `http://localhost:9000/assure/bins?numberOfBins=${n}`,

            success: function (result) {
               console.log(result, "bins generated")
               writeFileData(result,"Bins")
               $.notify("Bins generated", "success");
               $('#binSkuEditModal').modal('hide');
            },
            error: function (xhr, status, error) {
            console.log(status,error,xhr)
                $.notify(xhr['responseJSON']['description']);
            }
        });
}

function uploadBinSkuFile(){
    let f = document.getElementById('formFile');
if(f.files && f.files[0]){
    console.log(f.files[0]['name'],f.files[0])
    var reader = new FileReader();
    reader.addEventListener('load', function (e) {
    let csvdata = e.target.result;
    uploadBinSkuUtil(csvdata);

    });
   reader.readAsBinaryString(f.files[0]);
}
}

function uploadBinSkus(){
$('#uploadModal').modal('show');
$('#uploadModalBody').html(getUploadModalBody());

}
function uploadBinSkuUtil(data){
    let parsedata = [];
    let newLinebrk = data.split("\n");
    console.log(newLinebrk);
    head = newLinebrk[0].split(",");
    console.log(head)
    if(head[0]!= "binId" || head[1]!="quantity" || head[2]!="clientSkuId"){
        $.notify("Invalid format of csv");
        return ;
    }
    if(newLinebrk.length<2){
        $.notify("Empty file");
        return ;
    }
    if(newLinebrk.length>101){
        $.notify("Number of rows should be less than 100");
        return ;
    }
    for(let i = 1; i < newLinebrk.length; i++) {
        row = newLinebrk[i].split(",");
        console.log(row);
        if(row.length!=3){
            $.notify("Invalid format of csv");
            return ;
        }

        if( row.includes('')){
            $.notify("empty field in csv");
            return ;
        }

        let jsonData = {};
        jsonData[head[0]] = row[0].toLowerCase().trim();
        jsonData[head[1]] = row[1].toLowerCase().trim();
        jsonData[head[2]] = row[2].toLowerCase().trim();
        parsedata.push(jsonData);
            }
    console.table(parsedata);
    binSkuUploadCall(parsedata);

}

function binSkuUploadCall(parsedata){
    let clientId = $("#clinetId").val();
    if(isNaN(parseInt(clientId)) || Number.isInteger(clientId) || parseInt(clientId)<0){
        $.notify("Enter valid clientId");
        return;
    }
    console.log({
        clientId : parseInt(clientId),
        binSkuItemFormList : parsedata
    })
   
    $.ajax({
        type: "POST",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/bin-skus`,
        data: JSON.stringify({
            clientId : parseInt(clientId),
            binSkuItemFormList : parsedata
        }),
        processData: false,
        dataType: 'json',
        success: function (result) {
           console.log(result, "uploaded binSku")
           $.notify("Bin SKUs uploaded", "success");
           $('#uploadModal').modal('hide');
        },
        error: function (xhr, status, error) {
            console.log(status,error,xhr)

            if(xhr['responseJSON']['errorType']===0){
                $.notify(xhr['responseJSON']['description']);
                return;
            }
            $.notify("Error occurred download error list file");
            $('#errorCsv').click(function(){
                writeFileData(xhr['responseJSON']['description'],"error");
            });
        }
    });
}

function getBinModalBody(){
    return `<form id="editBinSkuForm" >
    <div id="modalFormDataDiv">
      <div class="form-group">
        <label for="brandInput">Number of Bins</label>
        <input type="number" class="form-control" id="numberOfBins" name="brand" aria-describedby="text" placeholder="Enter number of Bins" autocomplete="off" minlength="1">
      </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
      <button type="button" class="btn btn-primary" onclick="generateBinsCall()">Generate</button>
    </div>
  </form>`
}

function getUploadModalBody(){
    return `<form>
    <div>
    <div class="form-group">
    <label for="clinetId" class="form-label">Client Id</label>
    <input class="form-control" type="number" id="clinetId" >
  </div>
      <div class="form-group">
        <label for="formFile" class="form-label">Select csv file for upload</label>
        <input class="form-control" type="file" id="formFile" accept=".csv">
      </div>
      <div class="form-group">
        <a class="" href="/assure/static/binSkuTemplate.csv" download>Download Template</a>
        <a class="" id="errorCsv" href="#" style="float: right;">Download Errors</a>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="uploadModalBtn" onclick="uploadBinSkuFile()">Upload</button>
      </div>
    </div>

  </form>`
}
function writeFileData(arr,fname){
    console.log(fname," in write file data")
        var config = {
            quoteChar: '',
            escapeChar: '',
            delimiter: ","
        };
    
        var data = Papa.unparse(arr, config);
        var blob = new Blob([data], {type: 'text/csv;charset=utf-8;'});
        var fileUrl =  null;
    
        if (navigator.msSaveBlob) {
            fileUrl = navigator.msSaveBlob(blob,  `${fname}.csv`);
        } else {
            fileUrl = window.URL.createObjectURL(blob);
        }
        var tempLink = document.createElement('a');
        tempLink.href = fileUrl;
        tempLink.setAttribute('download', `${fname}.csv`);
        tempLink.click();
    }

function writeFileData(arr,fname){
    console.log(fname," in write file data")
        var config = {
            quoteChar: '',
            escapeChar: '',
            delimiter: ","
        };
    
        var data = Papa.unparse(arr, config);
        var blob = new Blob([data], {type: 'text/csv;charset=utf-8;'});
        var fileUrl =  null;
    
        if (navigator.msSaveBlob) {
            fileUrl = navigator.msSaveBlob(blob,  `${fname}.csv`);
        } else {
            fileUrl = window.URL.createObjectURL(blob);
        }
        var tempLink = document.createElement('a');
        tempLink.href = fileUrl;
        tempLink.setAttribute('download', `${fname}.csv`);
        tempLink.click();
    }

