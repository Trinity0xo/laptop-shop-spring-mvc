$(".thumbnails").owlCarousel({
    dots: false,
    nav: false,
    loop: false,
    margin: 10,
    responsive: {
        0: {
            items: 3,
        },

        320: {
            items: 4,
        },

        420: {
            items: 5,
        },

        520: {
            items: 6,
        },
    },
});

$(document).on("click", ".owl-item", function () {
    const imageUrl = $(this).find("img").attr("src");
    $(".image-wrapper .image").attr("src", imageUrl);

    const owlIndex = $(this).index();
    $(".owl-stage-outer").trigger("to.owl.carousel", owlIndex);

    $(".owl-item").find(".thumbnails-item").removeClass("active");

    $(this).find(".thumbnails-item").addClass("active");
});

const productQuantityInput = $(".product-quantity-input");
const productQuantityPlus = $(".product-quantity-plus");
const productQuantityMinus = $(".product-quantity-minus");
const quantityLimit = 100;

productQuantityMinus.click(function () {
    if (productQuantityInput.val() > 1) {
        productQuantityInput.val(parseInt(productQuantityInput.val()) - 1);
    }
});

productQuantityPlus.click(function () {
    if (productQuantityInput.val() < quantityLimit) {
        productQuantityInput.val(parseInt(productQuantityInput.val()) + 1);
    }
});
