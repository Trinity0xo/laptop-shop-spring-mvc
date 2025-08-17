package com.laptopstore.ecommerce.util.validation.product;

import jakarta.validation.ConstraintValidatorContext;

public abstract class BaseProductValidator<T> {
    protected abstract String getName(T dto);
    protected abstract String getDescription(T dto);
    protected abstract String getShortDescription(T dto);

    protected abstract String getCpu(T dto);
    protected abstract String getGpu(T dto);
    protected abstract String getRam(T dto);
    protected abstract String getStorage(T dto);
    protected abstract String getScreen(T dto);
    protected abstract String getKeyboard(T dto);
    protected abstract String getSdCard(T dto);
    protected abstract String getLan(T dto);
    protected abstract String getWifi(T dto);
    protected abstract String getBluetooth(T dto);
    protected abstract String getWebCam(T dto);
    protected abstract String getOs(T dto);
    protected abstract String getBattery(T dto);
    protected abstract String getWeight(T dto);
    protected abstract String getColor(T dto);
    protected abstract String getSize(T dto);
    protected abstract String getCooling(T dto);
    protected abstract String getMaterial(T dto);
    protected abstract String getIo(T dto);
    protected abstract String getAudio(T dto);

    protected abstract Double getPrice(T dto);
    protected abstract Long getQuantity(T dto);
    protected abstract Float getDiscount(T dto);


    protected boolean validate(T dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        String name = getName(dto);
        String shortDescription = getShortDescription(dto);
        String description = getDescription(dto);

        Double price = getPrice(dto);
        Long quantity = getQuantity(dto);
        Float discount = getDiscount(dto);

        String cpu = getCpu(dto);
        String gpu = getGpu(dto);
        String ram = getRam(dto);
        String storage = getStorage(dto);
        String screen = getScreen(dto);
        String keyboard = getKeyboard(dto);
        String sdCard = getSdCard(dto);
        String lan = getLan(dto);
        String wifi = getWifi(dto);
        String bluetooth = getBluetooth(dto);
        String webCam = getWebCam(dto);
        String os = getOs(dto);
        String battery = getBattery(dto);
        String weight = getWeight(dto);
        String color = getColor(dto);
        String size = getSize(dto);
        String cooling = getCooling(dto);
        String material = getMaterial(dto);
        String io = getIo(dto);
        String audio = getAudio(dto);

        if (name == null || name.length() < 2 || name.length() > 255) {
            context.buildConstraintViolationWithTemplate("Tên phải có độ dài từ 2 đến 255 ký tự")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        if (shortDescription != null && shortDescription.length() > 255) {
            context.buildConstraintViolationWithTemplate("Mô tả ngắn không được vượt quá 255 ký tự")
                    .addPropertyNode("shortDescription")
                    .addConstraintViolation();
            isValid = false;
        }

        if (description != null && description.length() > 5000) {
            context.buildConstraintViolationWithTemplate("Mô tả không được vượt quá 5000 ký tự")
                    .addPropertyNode("description")
                    .addConstraintViolation();
            isValid = false;
        }

        if (price == null || price < 0) {
            context.buildConstraintViolationWithTemplate("Giá phải lớn hơn 0")
                    .addPropertyNode("price")
                    .addConstraintViolation();
            isValid = false;
        }

        if (quantity == null || quantity < 0) {
            context.buildConstraintViolationWithTemplate("Số lượng phải lớn hơn hoặc bằng 0")
                    .addPropertyNode("quantity")
                    .addConstraintViolation();
            isValid = false;
        }

        if (discount == null || discount < 0 || discount > 100) {
            context.buildConstraintViolationWithTemplate("Giảm giá phải nằm trong khoảng từ 0 đến 100")
                    .addPropertyNode("discount")
                    .addConstraintViolation();
            isValid = false;
        }

        if (cpu != null && cpu.length() > 255) {
            context.buildConstraintViolationWithTemplate("CPU không được vượt quá 255 ký tự")
                    .addPropertyNode("cpu")
                    .addConstraintViolation();
            isValid = false;
        }

        if (gpu != null && gpu.length() > 255) {
            context.buildConstraintViolationWithTemplate("GPU không được vượt quá 255 ký tự")
                    .addPropertyNode("gpu")
                    .addConstraintViolation();
            isValid = false;
        }

        if (ram != null && ram.length() > 255) {
            context.buildConstraintViolationWithTemplate("RAM không được vượt quá 255 ký tự")
                    .addPropertyNode("ram")
                    .addConstraintViolation();
            isValid = false;
        }

        if (storage != null && storage.length() > 255) {
            context.buildConstraintViolationWithTemplate("Bộ nhớ không được vượt quá 255 ký tự")
                    .addPropertyNode("storage")
                    .addConstraintViolation();
            isValid = false;
        }

        if (screen != null && screen.length() > 255) {
            context.buildConstraintViolationWithTemplate("Màn hình không được vượt quá 255 ký tự")
                    .addPropertyNode("screen")
                    .addConstraintViolation();
            isValid = false;
        }

        if (keyboard != null && keyboard.length() > 255) {
            context.buildConstraintViolationWithTemplate("Bàn phím không được vượt quá 255 ký tự")
                    .addPropertyNode("keyboard")
                    .addConstraintViolation();
            isValid = false;
        }

        if (sdCard != null && sdCard.length() > 255) {
            context.buildConstraintViolationWithTemplate("Thẻ SD không được vượt quá 255 ký tự")
                    .addPropertyNode("sdCard")
                    .addConstraintViolation();
            isValid = false;
        }

        if (lan != null && lan.length() > 255) {
            context.buildConstraintViolationWithTemplate("LAN không được vượt quá 255 ký tự")
                    .addPropertyNode("lan")
                    .addConstraintViolation();
            isValid = false;
        }

        if (wifi != null && wifi.length() > 255) {
            context.buildConstraintViolationWithTemplate("WiFi không được vượt quá 255 ký tự")
                    .addPropertyNode("wifi")
                    .addConstraintViolation();
            isValid = false;
        }

        if (bluetooth != null && bluetooth.length() > 255) {
            context.buildConstraintViolationWithTemplate("Bluetooth không được vượt quá 255 ký tự")
                    .addPropertyNode("bluetooth")
                    .addConstraintViolation();
            isValid = false;
        }

        if (webCam != null && webCam.length() > 255) {
            context.buildConstraintViolationWithTemplate("WebCam không được vượt quá 255 ký tự")
                    .addPropertyNode("webCam")
                    .addConstraintViolation();
            isValid = false;
        }

        if (os != null && os.length() > 255) {
            context.buildConstraintViolationWithTemplate("Hệ điều hành không được vượt quá 255 ký tự")
                    .addPropertyNode("os")
                    .addConstraintViolation();
            isValid = false;
        }

        if (battery != null && battery.length() > 255) {
            context.buildConstraintViolationWithTemplate("Pin không được vượt quá 255 ký tự")
                    .addPropertyNode("battery")
                    .addConstraintViolation();
            isValid = false;
        }

        if (weight != null && weight.length() > 255) {
            context.buildConstraintViolationWithTemplate("Trọng lượng không được vượt quá 255 ký tự")
                    .addPropertyNode("weight")
                    .addConstraintViolation();
            isValid = false;
        }

        if (color != null && color.length() > 255) {
            context.buildConstraintViolationWithTemplate("Màu sắc không được vượt quá 255 ký tự")
                    .addPropertyNode("color")
                    .addConstraintViolation();
            isValid = false;
        }

        if (size != null && size.length() > 255) {
            context.buildConstraintViolationWithTemplate("Kích thước không được vượt quá 255 ký tự")
                    .addPropertyNode("size")
                    .addConstraintViolation();
            isValid = false;
        }

        if (cooling != null && cooling.length() > 255) {
            context.buildConstraintViolationWithTemplate("Hệ thống làm mát không được vượt quá 255 ký tự")
                    .addPropertyNode("cooling")
                    .addConstraintViolation();
            isValid = false;
        }

        if (material != null && material.length() > 255) {
            context.buildConstraintViolationWithTemplate("Chất liệu không được vượt quá 255 ký tự")
                    .addPropertyNode("material")
                    .addConstraintViolation();
            isValid = false;
        }

        if (io != null && io.length() > 5000) {
            context.buildConstraintViolationWithTemplate("I/O không được vượt quá 5000 ký tự")
                    .addPropertyNode("io")
                    .addConstraintViolation();
            isValid = false;
        }

        if (audio != null && audio.length() > 5000) {
            context.buildConstraintViolationWithTemplate("Âm thanh không được vượt quá 5000 ký tự")
                    .addPropertyNode("audio")
                    .addConstraintViolation();
            isValid = false;
        }


        return isValid;
    }
}
