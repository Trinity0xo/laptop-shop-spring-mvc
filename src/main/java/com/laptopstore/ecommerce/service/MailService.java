package com.laptopstore.ecommerce.service;

public interface MailService {
    void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml);
    void sendEmailFromTemplateSync(String to, String subject, String templateName, String username, Object value);
    void sendResetPasswordLink(String username, String email, String resetPasswordToken);
}
