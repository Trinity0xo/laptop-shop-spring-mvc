const btnSideBarToggle = $("#btnSidebarToggle");
const overlay = $(".overlay");
const mainWrapper = $(".main-wrapper");
const filterMenu = $(".filter-menu");
const body = $("body");
const minWidth = 992;

$(window).resize(function () {
  if ($(window).outerWidth() >= minWidth) {
    mainWrapper.removeClass("show-on-mobile");
    filterMenu.removeClass("show");
    overlay.css("display", "none");
  } else {
    mainWrapper.removeClass("hide-on-desktop");
  }
});

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
  overlay.css("display", "block");
  body.css("overflow", "hidden");
});

closeFilterButton.click(function () {
  popoverMenu.removeClass("show");
  overlay.css("display", "none");
  mainWrapper.removeClass("show-on-mobile");
  body.css("overflow", "unset");
});

overlay.click(function () {
  $(this).css("display", "none");
  mainWrapper.removeClass("show-on-mobile");
  popoverMenu.removeClass("show");
  body.css("overflow", "unset");
});

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

