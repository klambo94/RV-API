function handleCheckBox(checkbox, reqId) {
    var status = checkbox.checked;


    $.post("/moveLocations/editCompleteStatus", JSON.stringify( {id : reqId,
        isCompleted : status}), "json");

}