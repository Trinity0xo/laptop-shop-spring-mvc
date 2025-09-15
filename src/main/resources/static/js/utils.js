export const ALLOWED_EXTENSIONS = ["jpg", "jpeg", "png", "gif", "webp"];

export function checkFileType(file) {
  let isValid = true;

  const fileExtension = file.name.split(".").pop().toLowerCase();
  if (!ALLOWED_EXTENSIONS.includes(fileExtension)) {
    isValid = false;
  }

  return isValid;
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

export function setTypeNumberAndUnFormatDiscount(jqueryElement) {
  const unFormattedValue = unFormatDiscount(jqueryElement.val());
  jqueryElement.attr("type", "number");
  jqueryElement.val(unFormattedValue);
}

export function setTypeTextAndFormatDiscount(jqueryElement) {
  const formattedValue = formatDiscount(jqueryElement.val());
  jqueryElement.attr("type", "text");
  jqueryElement.val(formattedValue);
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

export function setTypeNumberAndUnFormatCurrency(jqueryElement) {
  const unFormattedValue = unFormatCurrency(jqueryElement.val());
  jqueryElement.attr("type", "number");
  jqueryElement.val(unFormattedValue);
}

export function setTypeTextAndFormatCurrency(jqueryElement) {
  const formattedValue = formatCurrency.format(jqueryElement.val());
  jqueryElement.attr("type", "text");
  jqueryElement.val(formattedValue);
}
