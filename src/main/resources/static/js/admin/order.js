const cancelledReasonInput = $(".cancelled-reason-input");
const orderStatus = $("#orderStatus");

function showCancelledReasonInput(statusValue){
    if (statusValue === "CANCELLED") {
        cancelledReasonInput.addClass("show");
    } else {
        cancelledReasonInput.removeClass("show");
    }
}

$(document).ready(function(){
    showCancelledReasonInput(orderStatus.val());
});

orderStatus.change(function () {
    showCancelledReasonInput($(this).val());
});