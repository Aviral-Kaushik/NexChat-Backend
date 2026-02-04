package com.aviral.nexchat.utils;

public class EmailTemplates {

    public static String passwordResetEmail(String username, String resetLink) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px;">
                <h2>Hello %s 👋</h2>
                <p>You requested to reset your NexChat password.</p>
                <p>
                    <a href="%s"
                       style="background:#4CAF50;color:white;padding:10px 20px;
                              text-decoration:none;border-radius:5px;">
                        Reset Password
                    </a>
                </p>
                <p>This link will expire in 15 minutes.</p>
                <p>If you didn’t request this, please ignore.</p>
                <br/>
                <p>— NexChat Team</p>
            </div>
        """.formatted(username, resetLink);
    }
}
