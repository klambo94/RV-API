function handleCheckBox(checkbox, reqId) {
    var status = checkbox.checked;


    $.ajax({
        type: "POST",
        contentType: "application/json;",
        url: "/moveLocations/editCompleteStatus",
        method: "POST",
        data:JSON.stringify( {id : reqId,
            isCompleted : status}),
        dataType: 'json',
        cache: false,
        timeout: 600000
    });
}

function AutoRefresh( t ) {
    setTimeout("location.reload(true);", t);
}