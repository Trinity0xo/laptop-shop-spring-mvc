// $(document).ready(function () {
//     $(".banners").owlCarousel({
//         items: 1,
//         loop: true,
//         autoplay: true,
//         autoplayTimeout: 5000,
//         autoplayHoverPause: true,
//         nav: false,
//         dots: false,
//     });
// });

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
