var pageNumber = 0
function loadParty() {
    let data = {
        pageNumber: pageNumber
    }
    let type = $('#type').val();
    if(type !== "all"){
        data["type"] = type;
    }
    
    $.ajax({
        type: "POST",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/parties/search`,
        data: JSON.stringify(data),
        processData: false,
        dataType: 'json',
        success: function (result) {
            
            obj = result
            console.log(obj, "inLoadfuntion");
           
            let body = document.getElementById("partyTbody");

            str = ""
            
            for (var i = 0; i < obj.length; i++) {
                str += `<tr>
                                      <td>${obj[i]['name']}</td>
                                      <td>${obj[i]['type']}</td>
                                    </tr>`;}
            body.innerHTML = str;
            checkNextPageNotExist()
        },
        error: function (xhr, status, error) {
            console.log("cannot fetch party");
        }
    });
}

function onChangeType(){

    pageNumber = 1 ;
    prevPage();
    
    loadParty();
    let type = $('#type').val();
    if(type == "all"){
        if (pageNumber == 0) {
            document.getElementById("prevLi").className = "page-item disabled";
            document.getElementById("nextLi").className = "page-item";
        }
    }
}

function nextPage() {
    pageNumber += 1;
    console.log(document.getElementById("page"))
    document.getElementById("page").innerText = pageNumber + 1;
    document.getElementById("prevLi").className = "page-item";
    loadParty()

}

function prevPage() {
    pageNumber -= 1;
    document.getElementById("page").innerText = pageNumber + 1;
    loadParty();
    if (pageNumber == 0) {
        document.getElementById("prevLi").className = "page-item disabled";
        document.getElementById("nextLi").className = "page-item";
    }
    return pageNumber;
}

function checkNextPageNotExist() {
    let data = {
        pageNumber: pageNumber+1
    }
    let type = $('#type').val();
    if(type !== "all"){
        data["type"] = type;
    }
    $.ajax({
        type: "POST",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/parties/search`,
        data: JSON.stringify(data),
        processData: false,
        dataType: 'json',
        success: function (result) {
            
            obj = result;
            console.log(obj, obj.length === 0);
            if (obj.length === 0) {
                document.getElementById("nextLi").className = "page-item disabled";
                console.log("in next check")
            } 
        },
        error: function (xhr, status, error) {
            console.log("cannot fetch party");
        }
    });
}

function addParty(){
    $('#partyEditModal').modal({backdrop: 'static', keyboard: false}, 'show');
    $('#modalTitle').text('Add Party');
    $('.close').css('visibility', 'hidden');
    $('#partyModalbody').html(getPartyModal());
}

function addPartyCall(){
    let name = $('#name').val();
    let type = $('#partyTypeInput').val();

    $.ajax({
        type: "POST",
        contentType: 'application/json',
        url: `http://localhost:9000/assure/parties`,
        data: JSON.stringify([{
            name: name,
            type : type
        }]),
        processData: false,
        dataType: 'json',
        success: function (result) {
            console.log(result, "party added")
            $.notify("Success", "success");
            $('#partyEditModal').modal('hide');
            loadParty() ;
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

function getPartyModal(){
    return `<form>
                <div class="row">
                    <div class="form-group col-12">
                        <label for="name" class="form-label required">Name</label>
                        <input class="form-control" type="text" id="name" >
                    </div>
                    <div class="form-group col-12">
                        <label for="formFile" class="form-label required">Party Type</label>
                        <select class="custom-select float-right col-8" name="type" id="partyTypeInput">
                        <option value="CLIENT" >CLIENT</option>
                        <option value="CUSTOMER" >CUSTOMER</option>
                        </select>
                    </div>
                 
                </div>
                <div style="float:right; padding-top:8px">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" id="uploadModalBtn" onclick="addPartyCall()">Save</button>
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