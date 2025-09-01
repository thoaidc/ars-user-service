package com.ars.userservice.constants;

import com.dct.model.dto.response.BaseResponseDTO;

/**
 * Message in api response with internationalization (I18n) here <p>
 * Use when you want to create a detailed response message for the client in {@link BaseResponseDTO} <p>
 * The constant content corresponds to the message key in the resources bundle files in directories such as:
 * <ul>
 *   <li><a href="">resources/i18n/messages</a></li>
 * </ul>
 *
 * @author thoaidc
 */
public interface ResultConstants {

    // Authenticate account result messages
    String LOGIN_SUCCESS = "result.auth.login.success";
    String REGISTER_SUCCESS = "result.auth.register.success";
    String GET_USER_SUCCESS = "result.user.info.success";
}
