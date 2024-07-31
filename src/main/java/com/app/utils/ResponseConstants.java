package com.app.utils;


public class ResponseConstants {
    public static final String ILLEGAL_PASSWORD_MESSAGE = "illegal password";
    public static final String RECOVER_LETTER = "recover letter send in ur email";
    public static final String REDIRECT_TO_POST_REQUEST = "send this request using the post method";
    public static final String SUCCESSFULLY_CHANGED = "successfully changed";
    public static final String INVALID_MAIN_BILL_ID = "invalid main bill id";
    public static final String MONEY_TRANSACTION_NOT_FOUND = "transaction with with this id not found";
    public static final String INVALID_USER_ID = "invalid user id";
    public static final String MAIN_BILL_NOT_FOUND = "main bill with with this id not found";
    public static final String GO_ACTIVATE_RESPONSE = "the letter with your account activation link is send in your email\t";
    public static final String INVALID_TOKEN = "token is invalid";
    public static final String WEAK_PASSWORD = "password is weak";
    public static final String DUPLICATE_EMAIL = "this email already exist";
    public static final String BLANK_SURNAME = "surname must be fill in";
    public static final String BLANK_NAME = "name must be fill in";
    public static final String EMAIL_NOT_FOUND = "email does not found";
    public static final String TOKEN_IS_COMPROMISED = "The token has been compromised, please log into your account again using your credentials.";
    public static final String INVALID_FINGER_PRINT = "invalid finger print";
    public static final String SUCCESS_LOGOUT = "logout successfully";
    public static final String INVALID_REFRESH_TOKEN = "need well formatted refresh token to do refresh";
    public static final String SAVING_BILL_NOT_FOUND = "saving bill with with this id not found";
    public static final String SUCCESSFULLY_DELETED = "successfully deleted";
    public static final String NOT_ENOUGH_BALANCE = "not enough balance to make this operation";
    public static final String SUCCESSFULLY_FROZEN = "successfully frozen";
    public static final String FROZEN_BILL_EX_MSG = "this bill is frozen, can't perform operation";
    public static final String SUCCESSFULLY_UNFROZEN = "successfully unfrozen";
    public static final String CANT_DELETE_BILL = "can't delete bill with non zero balance";
    public static final String TRANSFER_FROM_SOMEONE_MAIN_BILL = "you can't transfer money from someone else main bill";
    public static final String USER_ID_MUST_NOT_NULL = "user id must not be null";
    public static final String BILL_ID_MUST_NOT_NULL = "bill id must not be null";
    public static final String REQUIRE_DESCRIPTION = "require description of operation";
    public static final String VALID_AMOUNT = "require positive amount";
    public static final String NEED_NOT_BLANK_PASSWORD = "need not blank password";
    public static final String NEED_EMAIL_TOKEN = "need email token";
    public static final String NEED_TARGET_AMOUNT = "need target amount";
    public static final String NEED_INITIAL_BALANCE = "need initial balance";
    public static final String NEED_AMOUNT = "need amount of transaction";
    public static final String NEED_NOT_BLANK_NEW_PASSWORD = "need not blank new password";
    public static final String NEED_NOT_BLANK_EMAIL = "need not blank email";
    public static final String USER_CONFIRM_LOOKUP_FAILURE = "the user you want to confirm has been deleted for some reason, please register again";
    public static final String ID_MUST_NOT_BE_NULL = "id must not be null";
    public static final String NO_ACCESS = "you have no access to this resource";
    public static final String NEED_FROM_DATE = "need a start date for selecting operations\n";
    public static final String NEED_TO_DATE = "need an end date for the selection of operations";
    public static final String NEED_OPERATION_TYPE = "need operation type";

    private ResponseConstants() {

    }
}
