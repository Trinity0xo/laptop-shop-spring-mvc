// show message
function showMessage(){
    const messageWrapper = $(".message-wrapper");

    if(messageWrapper.length > 0){
        const successMessage = messageWrapper.find(".success-message");
        const errorMessage = messageWrapper.find(".error-message");
        const successMessageText = successMessage.text();
        const errorMessageText = errorMessage.text();

        let showMessage = false;

        if(successMessageText){
            $.toast({
                heading: 'Success',
                text: successMessageText,
                showHideTransition: 'slide',
                icon: 'success',
                hideAfter: 5000,
                position: 'top-right',
            })
            showMessage = true;
        }

        if(errorMessageText){
            $.toast({
                heading: 'Error',
                text: errorMessageText,
                showHideTransition: 'slide',
                icon: 'Error',
                hideAfter: 5000,
                position: 'top-right',
            })
            showMessage = true;

        }

        if(showMessage){
            successMessage.text("");
            errorMessage.text("");
        }
    }
}

showMessage();

const dropDownToggle = $(".dropdown-toggle");
const dropDownMenu = $(".dropdown-menu");

dropDownToggle.click(function () {
    dropDownMenu.not($(this).next()).hide();
    $(this).next().toggle();
});

$(document).on("click", function (e) {
    if (
        !$(e.target).closest(".dropdown-menu").length &&
        !$(e.target).closest(".dropdown-toggle").length
    ) {
        dropDownMenu.hide();
    }
});

const sideBar = $("#sidebar");
const body = $("body");
const sideBarToggle = $("#sideBarToggle");
const btnCloseSideBar = $("#btnCloseSideBar");
const overlay = $(".overlay");

sideBarToggle.click(function () {
    sideBar.addClass("show");
    body.css("overflow", "hidden");
    overlay.toggleClass("show");
});

btnCloseSideBar.click(function () {
    sideBar.removeClass("show");
    body.css("overflow", "auto");
    overlay.removeClass("show");
});

const btnOpenSearch = $("#btnOpenSearch");
const searchModal = $(".search-modal");

btnOpenSearch.click(function () {
    searchModal.addClass("show");
    body.css("overflow", "hidden");
    overlay.toggleClass("show");
});

const popoverToggle = $(".popover-toggle");
const popoverButtonWidth = popoverToggle.outerWidth();
const pointerWidth = $(".popover-menu .popover-pointer").outerWidth();
const popoverMenu = $(".popover-menu");
const closeFilterButton = $(".close-filter-button");

function centerPopoverPointer() {
    const leftPosition = (popoverButtonWidth - pointerWidth) / 2;
    $(".popover-menu .popover-pointer").css("left", `${leftPosition}px`);
}

centerPopoverPointer();

popoverToggle.click(function () {
    $(this).next().toggleClass("show");
    body.css("overflow", "hidden");
    overlay.addClass("show");
});

closeFilterButton.click(function () {
    popoverMenu.removeClass("show");
    body.css("overflow", "auto");
    overlay.removeClass("show");
});

overlay.click(function () {
    $(this).removeClass("show");
    sideBar.removeClass("show");
    searchModal.removeClass("show");
    popoverMenu.removeClass("show");
    body.css("overflow", "auto");
});
