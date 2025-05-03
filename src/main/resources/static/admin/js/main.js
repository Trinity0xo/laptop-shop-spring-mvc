const sideBar = $(".sidebar");
const btnSideBarToggle = $("#btnSidebarToggle");
const mainContent = $(".main-content");
const overlay = $(".overlay");
const mainWrapper = $(".main-wrapper");
const popoverButton = $(".popover-button");
const pointerWidth = $(".popover-menu .popover-pointer").outerWidth();
const buttonWidth = popoverButton.outerWidth();
const btnFilterToggle = $("#btnFilterToggle");
const filterMenu = $(".filter-menu");
const body = $("body");
const minWidth = 992;
const closeFilterButton = $(".close-filter-button");

// layout
$(window).resize(function () {
    if ($(window).outerWidth() >= minWidth) {
        mainWrapper.removeClass("show-on-mobile");
        filterMenu.removeClass("show");
        overlay.css("display", "none");
    } else {
        mainWrapper.removeClass("hide-on-desktop");
    }
});

// sidebar toggle
btnSideBarToggle.click(function () {
    if ($(window).outerWidth() >= minWidth) {
        mainWrapper.removeClass("show-on-mobile");
        mainWrapper.toggleClass("hide-on-desktop");
    } else {
        mainWrapper.removeClass("hide-on-desktop");
        mainWrapper.toggleClass("show-on-mobile");
        overlay.css("display", "block");
        body.css("overflow", "hidden");
    }
});

// center popover pointer base on button
function centerPopoverPointer() {
    const leftPosition = (buttonWidth - pointerWidth) / 2;
    $(".popover-menu .popover-pointer").css("left", `${leftPosition}px`);
}

centerPopoverPointer();

// popover toggle
$(btnFilterToggle).click(function () {
    filterMenu.toggleClass("show");
    overlay.css("display", "block");
    body.css("overflow", "hidden");
});

// overlay
overlay.click(function () {
    $(this).css("display", "none");
    mainWrapper.removeClass("show-on-mobile");
    filterMenu.removeClass("show");
    body.css("overflow", "unset");
});

// close filter
closeFilterButton.click(function () {
    overlay.css("display", "none");
    mainWrapper.removeClass("show-on-mobile");
    filterMenu.removeClass("show");
    body.css("overflow", "unset");
});

// show input group for order status edit
const orderStatusSelect = $("#orderStatus");
const cancelledReasonInput = $(".cancelled-reason-input");

function showCancelledReasonInput(){
   const selectedValue = orderStatusSelect.find(":selected").val();
   if(selectedValue == "CANCELLED"){
       cancelledReasonInput.css("display", "flex");
   }else{
       cancelledReasonInput.css("display", "none");
   }
}

showCancelledReasonInput();

orderStatusSelect.change(function(){
    if($(this).val() == "CANCELLED"){
        cancelledReasonInput.css("display", "flex");
    }else{
        cancelledReasonInput.css("display", "none");
    }
})

// show message
function showMessage(){
    const urlParams = new URLSearchParams(window.location.search);
    let urlChanged  = false;

    if(urlParams.has("successMessage")){
        const successMessage = urlParams.get("successMessage");
        $.toast({
            heading: 'Success',
            text: successMessage,
            showHideTransition: 'slide',
            icon: 'success',
            hideAfter: 5000,
            position: 'top-right',
        })
        urlParams.delete("successMessage");
        urlChanged = true;
    }

    if(urlParams.has("failureMessage")){
        const failureMessage = urlParams.get("failureMessage");
        $.toast({
            heading: 'Error',
            text: failureMessage,
            showHideTransition: 'slide',
            icon: 'Error',
            hideAfter: 5000,
            position: 'top-right',
        })
        urlParams.delete("failureMessage");
        urlChanged = true;
    }

    if (urlChanged) {
        const queryString = urlParams.toString();
        const newUrl = queryString
            ? `${window.location.pathname}?${queryString}`
            : window.location.pathname;
        history.replaceState(null, '', newUrl);
    }
}

showMessage();
