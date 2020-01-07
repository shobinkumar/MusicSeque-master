package com.musicseque.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Constants {


    public static String ROOT_URL = "http://musicapi.htistelecom.in/api/";

// public static String ROOT_URL2 = "http://musicapi.htistelecom.in/";


    public static final String API_LOGIN = "account/login";
    public static final String API_SIGN_UP = "account/Register";
    public static final String ACCOUNT_EXISTS_API = "Account/UserSocialLogin";
    public static final String LOGIN_SOCIAL_API = "Account/UpdateUserSocial";
    public static final String EMAIL_VERYFIY = "account/SendOtp";
    public static final String RESET_PASSWORD_API = "account/ResetPassword";
    public static final String ACCOUNT_VERIFY_API = "account/EmailVerification";
    public static final String RESEND_OTP_API = "Account/ResendOtpEmailVerification";

    public static final String RETRIEVE_USER_PROFILE_API = "Service/User_Displayinfo";
    public static final String RETRIEVE_ARTIST_UPLOADED_IMAGES_API = "Service/GetUserImage";
    public static final String UPLOAD_ARTIST_IMAGES_API = "Service/UserImageUpload";


    public static final String UPDATE_USER_INFO_API = "Account/ProfileUpdate";
    public static final String UPLOAD_ARTIST_IMAGE_API = "Service/profileImageUpload";
    public static final String UPLOAD_ARTIST_BACKGROUND_IMAGE_API = "Service/BackgroundImageUpload";

    public static final String GENRE_LIST_API = "Service/GenreTypeList";
    public static final String COUNTRY_LIST_API = "Service/CountryCodes";
    public static final String STATE_LIST_API ="Service/StateList";
    public static final String CITY_LIST_API ="Service/CityList";


    public static final String GET_UPLOADED_MEDIA_API = "Service/GetUploadedFiles";
    public static final String UPLOAD_MEDIA_API = "Service/UploadFile";
    public static final String CHANGE_PASSWORD_API = "Account/ChangePassword";
    public static final String UPDATE_STATUS_API = "Service/UpdateUserStatus";


    public static final String REPORT_ISSUE_API = "Service/ReportProblem";
    public static final String DELETE_ARTIST_IMAGES_API = "Service/DeleteUserImages";
    public static final String DELETE_ARTIST_MEDIA_FILES_API = "Service/DeleteUserUploadedFiles";
    public static final String DELETE_ACCOUNT_API = "Service/DeleteUser";
    public static final String GET_ARTIST_LIST_API = "Service/GetUserList";
    public static final String SEARCH_ARTIST_LIST_API = "Service/ProfileTypeSearch";

    public static final String UPLOAD_LAT_LNG_API = "Service/UserLatLongAdd";
    public static final String FOLLOW_UNFOLLOW_ARTIST_API = "Service/ArtistFollowerAdd";
    public static final String OTHER_PROFILE_API = "Service/User_Displayinfo_Follower";


    //Band APIs
    public static final String GET_BAND_LIST_API = "Service/BandProfilesListDisplay";
    public static final String GET_BAND_DETAIL_API = "Service/BandProfileDisplayViaId";
    public static final String UPDATE_BAND_PROFILE_API = "Service/BandProfileAddUpdate";
    public static final String UPLOAD_BAND_PROFILE_IMAGE_API = "Service/BandProfileImageUpload";
    public static final String UPLOAD_BAND_BACKGROUND_IMAGE_API = "Service/BandProfileBackgroundImageUpload";
    public static final String UPDATE_BAND_AVAILABILITY_STATUS_API = "Service/BandProfileAvailabilityStatus";
    public static final String DELETE_BAND_API = "Service/BandProfileDelete";
    public static final String SEARCH_BAND_MEMBER_API = "Service/BandMembersSearch";
    public static final String BAND_MEMBER_STATUS_API = "Service/BandMembersList";
    public static final String BAND_MEMBER_REMOVE_API = "Service/BandMemberRemove";
    public static final String BAND_MEMBER_ADD_API = "Service/BandMemberAdd";
    public static final String FOLLOW_UNFOLLOW_BAND_API = "Service/BandFollowersCount";
    public static final String NOTIFICATION_API = "Service/UserNotifications";
    public static final String ACCEPT_REJECT_BAND_REQUEST_API = "Service/ArtistRequestsStatus";
    public static final String OTHER_BAND_LIST_API = "Service/ArtistsBandListDisplay";
    public static final String SEARCH_BAND_LIST_API = "Service/BandListSearch";

    //Event Manager

    public static final String EVENT_TYPE_LIST_API = "Service/EventTypeList";
    public static final String CURRENCY_LIST_API = "Service/CurrencyTypeList";
    public static final String EVENT_DETAIL_API = "Service/EventDetailsViaEventId";
    public static final String EVENT_SAVE_UPDATE_DETAIL_API = "Service/EventBookingSaveUpdate";
    public static final String GET_EVENTS_LIST_API = "Service/EventsListDisplay";
    public static final String UPLOAD_EVENT_IMAGE_API = "Service/EventPromotionImageUpload";
    public static final String DELETE_EVENT_API = "Service/EventDelete";

    //Venue
    public static final String VENUE_LIST_API = "Service/VenueListSearch";
    public static final String VENUE_BOOK_API = "Service/VenueBooking";
    public static final String GET_VENUE_TIMMINGS_API = "Service/VenueBookingsDisplayViaVId";
    public static final String VENUE_TO_TIMMINGS_API = "Service/VenueBookingsDisplayAvailability";
    public static final String VENUE_FROM_TIMMINGS_API = "Service/VenueBookingsUnAvailabilityDisplay";
    public static final String VENUE_SUBMIT_TIMMINGS_API = "Service/VenueUnAvailabilityAdd";
    public static final String VENUE_BOOKED_PENDING_TIMMINGS_API = "Service/VenuePendingBookingsDisplay";
    public static final String VENUE_ACCEPT_REJECT_API = "Service/VenueBookingAccept";
    public static final String EVENT_STATUS_API = "Service/VenueBookingsList_ViaBookingStatus";



    //Artist
    public static final String GIGS_ARTIST_API ="Service/EventDetailsViaLoggedInUserId";
    public static final String GIGS_VENUE_API ="Service/EventDetailsViaLoggedInUserId";

    public static final String LOGOUT_API = "Service/UserLogOut";


    public static final String  TEST_API="Service/SalarySlip";

    // public static final String SEND_VENUE_TIMMINGS_API="Service/VenueUnAvailabilityAdd";

    // public static final String VENUE_PROFILE_API="Service/VenueProfileDisplayViaId";
    public static final String VENUE_ADD_UPDATE_PROFILE_API = "Service/VenueProfileSaveUpdate";
    public static final String USER_ID = "USER_ID";
    public static final String PROFILE_ID = "PROFILE_ID";
    public static final String PROFILE_TYPE = "PROFILE_TYPE";
    public static final String EMAIL_ID = "EMAIL_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String LOGIN_TYPE = "LOGIN_TYPE";
    public static final String PROFILE_IMAGE = "PROFILE_IMAGE";
    public static final String COVER_IMAGE = "COVER_IMAGE";
    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    public static final String COUNTRY_CODE = "COUNTRY_CODE";
    public static final String COUNTRY_NAME = "COUNTRY_NAME";
    public static final String COUNTRY_ID = "COUNTRY_ID";
    public static final String VISIBILITY_STATUS = "VISIBILITY_STATUS";
    public static final String IS_FIRST_LOGIN = "IS_FIRST_LOGIN";
    public static final String UNIQUE_CODE = "UNIQUE_CODE";
    public static final String PASSWORD = "PASSWORD";
    public static final String IS_REMEMBER = "IS_REMEMBER";

    public static final String FOR_FACEBOOK = "F";
    public static final String FOR_GOOGLE = "G";
    public static final int FOR_LOGIN = 101;
    public static final int FOR_FORGOT_PASSWORD = 102;
    public static final int FOR_SIGNUP = 103;
    public static final int FOR_ACCOUNT_EXISTS = 104;
    public static final int FOR_SOCIAL_LOGIN = 105;
    public static final int FOR_RESET_PASSWORD = 106;
    public static final int FOR_VERIFY_ACCOUNT = 107;
    public static final int FOR_RESEND_OTP = 108;
    public static final int FOR_UPDATE_PROFILE = 109;
    public static final int FOR_UPLOAD_PROFILE_IMAGE = 110;
    public static final int FOR_GENRE_LIST = 111;
    public static final int FOR_COUNTRIES_LIST = 112;
    public static final int FOR_USER_PROFILE = 113;
    public static final int FOR_OTHER_PROFILE = 114;
    public static final int FOR_SONGS_LIST = 115;
    public static final int FOR_ARTIST_UPLOADED_IMAGES = 116;
    public static final int FOR_UPLOAD_ARTIST_IMAGE = 117;
    public static final int FOR_UPLOADED_AUDIO = 118;
    public static final int FOR_UPLOAD_AUDIO = 119;
    public static final int FOR_UPLOADED_VIDEO = 120;
    public static final int FOR_UPLOAD_VIDEO = 121;
    public static final int FOR_UPLOAD_COVER_PIC = 122;
    public static final int FOR_CHANGE_PASSWORD = 123;
    public static final int FOR_UPDATE_STATUS = 124;
    public static final int FOR_REPORT_PROBLEM = 125;
    public static final int FOR_UPLOAD_ARTIST_AUDIO_VIDEO = 126;
    public static final int FOR_DELETE_ARTIST_IMAGES = 127;
    public static final int FOR_DELETE_ARTIST_MEDIA = 128;
    public static final int FOR_DELETE_ACCOUNT = 129;
    public static final int GET_ARTIST_LIST = 130;
    public static final int SEARCH_ARTIST = 131;
    public static final int FOR_LAT_LNG = 132;
    public static final int FOR_FOLLOW_UNFOLLOW_ARTIST = 133;
    public static final int FOR_BAND_PROFILE = 134;
    public static final int FOR_UPDATE_BAND_PROFILE = 135;
    public static final int FOR_UPLOAD_BAND_PROFILE_IMAGE = 136;
    public static final int FOR_UPLOAD_BAND_BACKGROUND_IMAGE = 137;
    public static final int FOR_BAND_VISIBILITY_STATUS = 138;
    public static final int FOR_BAND_LIST = 139;
    public static final int FOR_DELETE_BAND = 140;
    public static final int FOR_BAND_MEMBER_STATUS = 141;
    public static final int FOR_SEARCH_BAND_MEMBER = 142;
    public static final int FOR_REMOVE_BAND_MEMBER = 143;
    public static final int FOR_ADD_BAND_MEMBER = 144;
    public static final int FOR_NOTIFICATION = 145;
    public static final int FOR_ACCEPT_BAND_REQUEST = 146;
    public static final int FOR_REJECT_BAND_REQUEST = 147;
    public static final int FOR_OTHER_BAND_LIST = 148;
    public static final int FOR_EVENT_MANAGER_DETAIL = 149;
    public static final int FOR_EVENT_TYPE_LIST = 150;
    public static final int FOR_CURRENCY_LIST = 151;
    public static final int FOR_EVENT_DETAIL = 152;
    public static final int FOR_SAVE_UPDATE_EVENT_DETAIL = 153;
    public static final int FOR_SEARCH_BAND_LIST = 154;
    public static final int FOR_SEARCH_VENUE_LIST = 155;
    public static final int FOR_VENUE_PROFILE = 156;
    public static final int FOR_CREATE_UPDATE_VENUE_PROFILE = 157;

    public static final int FOR_SHOW_EVENTS_LIST = 158;
    public static final int FOR_VENUE_TIMMINGS = 159;
    public static final int FOR_VENUE_BOOK = 160;
    public static final int FOR_VENUE_TO_TIMMINGS = 161;
    public static final int FOR_VENUE_FROM_TIMMINGS = 162;
    public static final int FOR_SUBMIT_TIMMINGS = 163;
    public static final int FOR_VENUE_SHOW_BOOKED_PENDING_TIMMINGS = 164;

    public static final int FOR_ACCEPTED_REQ = 165;
    public static final int FOR_PENDING_REQ = 166;
    public static final int FOR_REJECTED_REQ = 167;
    // public static final int EVENT_DETAIL_VENUE_MANAGER=168;


    public static final int FOR_ACCEPT_EVENT_REQ = 169;
    public static final int FOR_REJECT_EVENT_REQ = 170;


    public static final int FOR_UPLOAD_EVENT_PROFILE_IMAGE = 201;
    public static final int FOR_DELETE_EVENT = 202;


    public static final int FOR_LOGOUT = 1001;
    public static final int FOR_STATE_LIST=1002;
    public static final int FOR_CITY_LIST=1003;


    public static final int FOR_TEST=2001;



    //For Venues
    public static final int FOR_VENUE_GIGS=3001;

    //FOr Artist
    public static final int FOR_ARTIST_GIGS=4001;

}