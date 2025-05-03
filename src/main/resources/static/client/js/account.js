const imageUpload = $(".image-upload");
const imagePreview = $(".image-preview");
const imageUploadControl = $(".image-upload-control");
const imageUploadWrapper = $(".image-upload-wrapper");
const imageError = $("#imageError");
const imagePreviewItem = $(".image-preview-item");
const allowedExtensions = ["jpg", "jpeg", "png", "gif", "webp"];

let deleteImageName = "";

if (imagePreviewItem.length > 0) {
    imageUploadControl.addClass("hide");
}

function checkFileType(file, allowedExtensions) {
    let isValid = true;

    const fileExtension = file.name.split(".").pop().toLowerCase();
    if (!allowedExtensions.includes(fileExtension)) {
        isValid = false;
    }

    return isValid;
}

imageUpload.change(function () {
    const file = $(this)[0].files[0];

    if (checkFileType(file, allowedExtensions)) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const img = $("<img>");
            img.attr("src", e.target.result);

            const previewItem = $(`
      <div class="image-preview-item">
      </div>
    `);

            const imageEdit = $(
                `<label for="image" class="image-edit-button">
        <i class="fa-solid fa-pen-to-square"></i>
      </label>`
            );

            previewItem.append(img);
            previewItem.append(imageEdit);
            imagePreview.empty();
            imagePreview.append(previewItem);
        };

        reader.readAsDataURL(file);

        imageUploadControl.addClass("hide");
    }
});

$(document).on("click", function (e) {
    const imageEdit = e.target.closest(".image-edit-button");

    if (imageEdit) {
        const currentImage = $(imageEdit).next("img");
        const currentImageUrl = currentImage.attr("src");
        if (currentImageUrl) {
            const currentImageUrlArray = currentImageUrl.split("/");
            const currentImageName =
                currentImageUrlArray[currentImageUrlArray.length - 1];
            deleteImageName = currentImageName;
        }
    }
});
