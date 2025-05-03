$(".add-to-cart-form").submit(function (e){
    e.preventDefault();

    const productId = $(this).data("productid");

    const formData = new FormData(this);

    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "post",
        url: `/cart/add/${productId}`,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: formData,
        contentType: false,
        processData: false,
        success: function (response, status, xhr) {
            if (xhr.status === 200) {
                const updatedCartItemCount = xhr.responseJSON.data;

                $(".cart-count-value").text(updatedCartItemCount);

                const successMessage = xhr.responseJSON.message;
                $.toast({
                    heading: 'Success',
                    text: successMessage,
                    showHideTransition: 'slide',
                    icon: 'success',
                    hideAfter: 5000,
                    position: 'top-right',
                })
            }
        },
        error: function (xhr) {
            if (xhr.status === 400) {
                const errors = xhr.responseJSON.data;

                // $(".error-message").text("");
                // $("input").removeClass("error");
                //
                // for (const field in errors) {
                //     if (errors.hasOwnProperty(field)) {
                //         $("#" + field + "Error").text(errors[field]);
                //         $("#" + field).addClass("error");
                //     }
                // }
            }
            if(xhr.status === 401) {
                window.location.href = "/auth/login";
            }
        },
    });
})