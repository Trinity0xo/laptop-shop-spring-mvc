import {
  formatCurrency,
  setTypeNumberAndUnFormatCurrency,
  setTypeTextAndFormatCurrency,
  checkFileType,
} from "/js/utils.js";

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

$(".price-input").each(function () {
  const input = $(this);
  input.val(formatCurrency.format(input.val()));

  input.on("focus", function () {
    setTypeNumberAndUnFormatCurrency(input);
  });

  input.on("blur", function () {
    setTypeTextAndFormatCurrency(input);
  });
});

$(".control-panel-form").on("submit", function () {
  $(".price-input").each(function () {
    setTypeNumberAndUnFormatCurrency($(this));
  });
});

// single image upload
const imageUploadInput = $(".image-upload-input");
const imageUploadPreview = $(".image-upload-preview");
const imageUploadControl = $(".image-upload-control");
const imagePreview = $(".image-preview");

if (imagePreview.length > 0) {
  imageUploadControl.addClass("hide");
} else {
  imageUploadPreview.addClass("hide");
}

imageUploadInput.change(function () {
  const file = $(this)[0].files[0];

  if (checkFileType(file)) {
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

function showMessage() {
  const successMessage = $(".success-message");

  let successMessageText = successMessage.text();

  if (!successMessageText) {
    successMessageText = localStorage.getItem("successMessage");
    localStorage.removeItem("successMessage");
  }

  if (successMessageText) {
    $.toast({
      heading: "Success",
      text: successMessageText,
      showHideTransition: "slide",
      icon: "success",
      hideAfter: 5000,
      position: "top-right",
    });
  }

  successMessage.text("");
}

showMessage();
