export const ALLOWED_EXTENSIONS = ["jpg", "jpeg", "png", "gif", "webp"];

export function checkFileType(file) {
    const fileExtension = file.name.split(".").pop().toLowerCase();
    return ALLOWED_EXTENSIONS.includes(fileExtension);
}

export const MIN_DISCOUNT = 0;
export const MAX_DISCOUNT = 100;

export function unFormatDiscount(discountValue) {
    const numericString = discountValue.replace(/[^\d.]/g, "");
    let result = parseFloat(numericString);
    if (isNaN(result)) return 0;
    return Math.min(Math.max(result, MIN_DISCOUNT), MAX_DISCOUNT);
}

export function formatDiscount(discountValue) {
    const number = parseFloat(discountValue);
    if (isNaN(number)) return "0 %";
    const bounded = Math.min(Math.max(number, MIN_DISCOUNT), MAX_DISCOUNT);
    return `${bounded} %`;
}

// jQuery-specific helpers
export function setTypeNumberAndUnFormatDiscount($input) {
    const unFormattedValue = unFormatDiscount($input.val());
    $input.attr("type", "number");
    $input.val(unFormattedValue);
}

export function setTypeTextAndFormatDiscount($input) {
    const formattedValue = formatDiscount($input.val());
    $input.attr("type", "text");
    $input.val(formattedValue);
}

export const formatCurrency = new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
    minimumFractionDigits: 0,
});

export function unFormatCurrency(currencyValue) {
    const numericString = currencyValue.replace(/[^\d]/g, "");
    return Number(numericString);
}

export function setTypeNumberAndUnFormatCurrency($input) {
    const unFormattedValue = unFormatCurrency($input.val());
    $input.attr("type", "number");
    $input.val(unFormattedValue);
}

export function setTypeTextAndFormatCurrency($input) {
    const formattedValue = formatCurrency.format($input.val());
    $input.attr("type", "text");
    $input.val(formattedValue);
}
