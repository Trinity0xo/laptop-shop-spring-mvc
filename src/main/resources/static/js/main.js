// dropdown
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

// show modal
$(".show-modal-button").click(function () {
  const modalName = $(this).data("show");
  $(`#${modalName}`).addClass("show");
  $(".overlay").addClass("show");
});

// hide modal
$(document).on("click", ".hide-modal-button", function () {
  const modalName = $(this).data("hide");
  $(`#${modalName}`).removeClass("show");
  $(".overlay").removeClass("show");
});

// overlay click
$(".overlay").click(function () {
  $(".modal").removeClass("show");
  $(this).removeClass("show");
});

// price input format
const formatCurrency = new Intl.NumberFormat("vi-VN", {
  style: "currency",
  currency: "VND",
  minimumFractionDigits: 0,
});

function unFormatCurrency(currencyValue) {
  const numericString = currencyValue.replace(/[^\d]/g, "");
  return Number(numericString);
}

function setTypeNumberAndUnFormatCurrency(jqueryElement) {
  const unFormattedValue = unFormatCurrency(jqueryElement.val());
  jqueryElement.attr("type", "number");
  jqueryElement.val(unFormattedValue);
}

function setTypeTextAndFormatCurrency(jqueryElement) {
  const formattedValue = formatCurrency.format(jqueryElement.val());
  jqueryElement.attr("type", "text");
  jqueryElement.val(formattedValue);
}

const priceInputs = $(".price-input");

priceInputs.each(function () {
  const input = $(this);
  input.val(formatCurrency.format(input.val()));

  input.on("focus", function () {
    setTypeNumberAndUnFormatCurrency(input);
  });

  input.on("blur", function () {
    setTypeTextAndFormatCurrency(input);
  });
});

const controlPanelForm = $(".control-panel-form");

controlPanelForm.on("submit", function () {
  priceInputs.each(function () {
    setTypeNumberAndUnFormatCurrency($(this));
  });
});

// single image upload
const imageUploadInput = $(".image-upload-input");
const imageUploadPreview = $(".image-upload-preview");
const imageUploadControl = $(".image-upload-control");
const imagePreview = $(".image-preview");
const allowedExtensions = ["jpg", "jpeg", "png", "gif", "webp"];
const deleteImageName = $("#deleteImageName");

if (imagePreview.length > 0) {
  imageUploadControl.addClass("hide");
} else {
  imageUploadPreview.addClass("hide");
}

function checkFileType(file, allowedExtensions) {
  let isValid = true;

  const fileExtension = file.name.split(".").pop().toLowerCase();
  if (!allowedExtensions.includes(fileExtension)) {
    isValid = false;
  }

  return isValid;
}

imageUploadInput.change(function () {
  const file = $(this)[0].files[0];

  if (checkFileType(file, allowedExtensions)) {
    const reader = new FileReader();
    reader.onload = function (e) {
      const imagePreview = $(`
                <div class="image-preview">
                </div>
            `);

      const imagePreviewEditButton = $(
        `<label for="image" class="image-preview-edit-button">
                    <i class="fa-solid fa-pen-to-square"></i>
                </label>`
      );

      const img = $("<img>");
      img.attr("src", e.target.result);
      img.attr("class", "image-preview-image");

      imagePreview.append(imagePreviewEditButton);
      imagePreview.append(img);
      imageUploadPreview.empty();
      imageUploadPreview.append(imagePreview);
    };

    reader.readAsDataURL(file);

    imageUploadControl.addClass("hide");
    imageUploadPreview.removeClass("hide");
  }
});

// show message
function showMessage() {
  const successMessage = $(".success-message");
  const errorMessage = $(".error-message");

  let showMessage = false;

  if (successMessage.length > 0) {
    const successMessageText = successMessage.text();
    $.toast({
      heading: "Success",
      text: successMessageText,
      showHideTransition: "slide",
      icon: "success",
      hideAfter: 5000,
      position: "top-right",
    });

    showMessage = true;
  }

  if (errorMessage.length > 0) {
    const errorMessageText = errorMessage.text();

    $.toast({
      heading: "Error",
      text: errorMessageText,
      showHideTransition: "slide",
      icon: "error",
      hideAfter: 5000,
      position: "top-right",
    });

    showMessage = true;
  }

  if (showMessage) {
    successMessage.text("");
    errorMessage.text("");
  }
}

showMessage();
