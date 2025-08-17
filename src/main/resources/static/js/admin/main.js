const overlay = $(".overlay");

// sidebar
const sideBar = $(".sidebar");
const btnToggleSidebar = $("#btnToggleSideBar");
const layout = $(".layout");

const MIN_WIDTH = 1200;

// toggle sidebar
btnToggleSidebar.click(function (e) {
  e.stopPropagation();
  if ($(window).outerWidth() >= MIN_WIDTH) {
    layout.removeClass("mobile-show");
    layout.toggleClass("desktop-hide");
  } else {
    layout.removeClass("desktop-hide");
    layout.toggleClass("mobile-show");
  }
});

// hide sidebar on click outside (only for mobile)
$(document).click(function (e) {
  if (
    $(window).outerWidth() < MIN_WIDTH &&
    !sideBar.is(e.target) &&
    sideBar.has(e.target).length === 0 &&
    !btnToggleSidebar.is(e.target)
  ) {
    layout.removeClass("mobile-show");
  }
});

// show modal
$(".show-modal-button").click(function () {
  const modalName = $(this).data("show");
  $(`#${modalName}`).addClass("show");
  overlay.addClass("show");
});

// hide modal
$(document).on("click", ".hide-modal-button", function () {
  const modalName = $(this).data("hide");
  $(`#${modalName}`).removeClass("show");
  overlay.removeClass("show");
});

// overlay click
overlay.click(function () {
  $(".modal").removeClass("show");
  $(this).removeClass("show");
});

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

// show message
function showMessage() {
  const successMessage = $(".message-success");
  const errorMessage = $(".message-error");

  const localStorageSuccessMessage = localStorage.getItem("successMessage");
  const localStorageErrorMessage = localStorage.getItem("errorMessage");

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

  if (localStorageSuccessMessage) {
    $.toast({
      heading: "Success",
      text: localStorageSuccessMessage,
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
      icon: "Error",
      hideAfter: 5000,
      position: "top-right",
    });

    showMessage = true;
  }

  if (localStorageErrorMessage) {
    $.toast({
      heading: "Error",
      text: localStorageErrorMessage,
      showHideTransition: "slide",
      icon: "Error",
      hideAfter: 5000,
      position: "top-right",
    });

    showMessage = true;
  }

  if (showMessage) {
    successMessage.text("");
    errorMessage.text("");
    localStorage.removeItem("successMessage");
    localStorage.removeItem("errorMessage");
  }
}

showMessage();

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

// discount input format
const MIN_DISCOUNT = 0;
const MAX_DISCOUNT = 100;

function unFormatDiscount(discountValue) {
  const numericString = discountValue.replace(/[^\d.]/g, "");
  let result = parseFloat(numericString);
  if (isNaN(result)) return 0;
  return Math.min(Math.max(result, MIN_DISCOUNT), MAX_DISCOUNT);
}

function formatDiscount(discountValue) {
  const number = parseFloat(discountValue);
  if (isNaN(number)) return "0 %";
  const bounded = Math.min(Math.max(number, MIN_DISCOUNT), MAX_DISCOUNT);
  return `${bounded} %`;
}

function setTypeNumberAndUnFormatDiscount(jqueryElement) {
  const unFormattedValue = unFormatDiscount(jqueryElement.val());
  jqueryElement.attr("type", "number");
  jqueryElement.val(unFormattedValue);
}

function setTypeTextAndFormatDiscount(jqueryElement) {
  const formattedValue = formatDiscount(jqueryElement.val());
  jqueryElement.attr("type", "text");
  jqueryElement.val(formattedValue);
}

const discountInputs = $(".discount-input");

discountInputs.each(function () {
  const input = $(this);

  input.val(formatDiscount(input.val()));

  input.on("focus", function () {
    setTypeNumberAndUnFormatDiscount(input);
  });

  input.on("blur", function () {
    setTypeTextAndFormatDiscount(input);
  });
});

// format on submit
const productForm = $(".product-form");

productForm.on("submit", function () {
  discountInputs.each(function () {
    setTypeNumberAndUnFormatDiscount($(this));
  });
  priceInputs.each(function () {
    setTypeNumberAndUnFormatCurrency($(this));
  });
});

const controlPanelForm = $(".control-panel-form");

controlPanelForm.on("submit", function () {
  priceInputs.each(function () {
    setTypeNumberAndUnFormatCurrency($(this));
  });
});

// allowed file types
const ALLOWED_EXTENSIONS = ["jpg", "jpeg", "png", "gif", "webp"];

// check file type
function checkFileType(file, ALLOWED_EXTENSIONS) {
  let isValid = true;

  const fileExtension = file.name.split(".").pop().toLowerCase();
  if (!ALLOWED_EXTENSIONS.includes(fileExtension)) {
    isValid = false;
  }

  return isValid;
}

// single image upload
const imageUploadInput = $(".image-upload-input");
const imageUploadPreview = $(".image-upload-preview");
const imageUploadControl = $(".image-upload-control");
const imagePreview = $(".image-preview");
const deleteImageName = $("#deleteImageName");

if (imagePreview.length > 0) {
  imageUploadControl.addClass("hide");
} else {
  imageUploadPreview.addClass("hide");
}

imageUploadInput.change(function () {
  const file = $(this)[0].files[0];
  if (!checkFileType(file, ALLOWED_EXTENSIONS)) {
    return;
  }

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
});

// multiple images upload
const imagesUploadInput = $(".images-upload-input");
const imagesUploadPreview = $(".images-upload-preview");
const imagesUploadControl = $(".images-upload-control");

let newImages = [];
let deleteImageNames = [];

imagesUploadInput.change(function () {
  const files = $(this)[0].files;
  for (let i = 0; i < files.length; i++) {
    if (checkFileType(files[i], ALLOWED_EXTENSIONS)) {
      const index = newImages.length;
      newImages.push(files[i]);

      const reader = new FileReader();
      reader.onload = function (e) {
        const img = $("<img alt='product image'>");
        img.attr("src", e.target.result);

        const previewImage = $(`
          <div class="preview-image"></div>
        `);

        const imageRemoveButton = $(
          `<button type="button" class="image-remove-button" data-index="${index}">
              <i class="fa-solid fa-xmark"></i>
            </button>`
        );

        previewImage.append(img);
        previewImage.append(imageRemoveButton);
        imagesUploadPreview.append(previewImage);
      };

      reader.readAsDataURL(files[i]);
    }
  }
  $(this).val("");
});

$(document).on("click", function (e) {
  const imageRemoveButton = e.target.closest(".image-remove-button");
  if (imageRemoveButton) {
    const currentImage = $(imageRemoveButton).next("img");
    const currentImageUrl = currentImage.attr("src");
    if (currentImageUrl) {
      const currentImageUrlArray = currentImageUrl.split("/");
      const currentImageName =
        currentImageUrlArray[currentImageUrlArray.length - 1];
      deleteImageNames.push(currentImageName);
    }

    const imageIndex = $(imageRemoveButton).data("index");
    if (imageIndex) {
      newImages.splice(imageIndex, 1);
    }

    imagesUploadInput.val("");

    const imagesPreviewItem = $(imageRemoveButton).closest(".preview-image");
    imagesPreviewItem.remove();
  }
});

// ajax product request url
const REQUEST_URL = "/dashboard/product";

// create product
$("#createProductForm").submit(function (e) {
  e.preventDefault();

  const formData = new FormData(this);
  formData.delete("newImages");
  newImages.forEach((image) => {
    formData.append("newImages", image);
  });

  const token = $("meta[name='_csrf']").attr("content");
  const header = $("meta[name='_csrf_header']").attr("content");

  $.ajax({
    type: "post",
    url: `${REQUEST_URL}/create`,
    beforeSend: function (request) {
      request.setRequestHeader(header, token);
    },
    data: formData,
    contentType: false,
    processData: false,
    success: function (response, status, xhr) {
      if (xhr.status === 201) {
        newImages.length = 0;
        window.location.href = `${REQUEST_URL}`;
        localStorage.setItem("successMessage", `${xhr.responseJSON.message}`);
      }
    },
    error: function (xhr) {
      if (xhr.status === 400) {
        const errors = xhr.responseJSON.data;

        $(".input-error-message").text("");
        $("input").removeClass("error");

        for (const field in errors) {
          if (errors.hasOwnProperty(field)) {
            $("#" + field + "Error").text(errors[field]);
            $("#" + field).addClass("error");
          }
          if (field === "newImages") {
            // $(".images-upload-group").addClass("error");
            $("#" + field + "Error").text(errors[field]);
          }
        }

        $(window).scrollTop(0);
      } else {
        document.open();
        document.write(xhr.responseText);
        document.close();
      }
    },
  });
});

// update product
$("#updateProductForm").submit(function (e) {
  e.preventDefault();

  $("#deleteImageNames").val(deleteImageNames);
  const formData = new FormData(this);
  formData.delete("newImages");
  newImages.forEach((image) => {
    formData.append("newImages", image);
  });

  const token = $("meta[name='_csrf']").attr("content");
  const header = $("meta[name='_csrf_header']").attr("content");

  $.ajax({
    type: "post",
    url: `${REQUEST_URL}/update`,
    beforeSend: function (request) {
      request.setRequestHeader(header, token);
    },
    data: formData,
    contentType: false,
    processData: false,
    success: function (response, status, xhr) {
      if (xhr.status === 200) {
        newImages.length = 0;
        deleteImageNames.length = 0;
        window.location.href = `${REQUEST_URL}`;
        localStorage.setItem("successMessage", xhr.responseJSON.message);
      }
    },
    error: function (xhr) {
      if (xhr.status === 400) {
        const errors = xhr.responseJSON.data;

        $(".input-error-message").text("");
        $("input").removeClass("error");

        for (const field in errors) {
          if (errors.hasOwnProperty(field)) {
            $("#" + field + "Error").text(errors[field]);
            $("#" + field).addClass("error");
          }
          if (field === "newImages") {
            // $(".images-upload-group").addClass("error");
            $("#" + field + "Error").text(errors[field]);
          }
        }

        $(window).scrollTop(0);
      } else {
        document.open();
        document.write(xhr.responseText);
        document.close();
      }
    },
  });
});
