package com.musicseque.artist.artist_models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistModel {
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("ProfileTypeId")
    @Expose
    private Integer profileTypeId;
    @SerializedName("ProfileTypeName")
    @Expose
    private String profileTypeName;
    @SerializedName("CountryCodeId")
    @Expose
    private String countryCodeId;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("Otp")
    @Expose
    private String otp;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("OTPType")
    @Expose
    private String oTPType;
    @SerializedName("PostCode")
    @Expose
    private String postCode;
    @SerializedName("Bio")
    @Expose
    private String bio;
    @SerializedName("Website")
    @Expose
    private String website;
    @SerializedName("Genre")
    @Expose
    private Integer genre;
    @SerializedName("Talent")
    @Expose
    private String talent;
    @SerializedName("ProfilePic")
    @Expose
    private String profilePic;
    @SerializedName("ProfileImagePath")
    @Expose
    private String profileImagePath;
    @SerializedName("ExperienceId")
    @Expose
    private Integer experienceId;
    @SerializedName("ExperienceYear")
    @Expose
    private String experienceYear;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("CountryId")
    @Expose
    private Integer countryId;
    @SerializedName("ArtistType")
    @Expose
    private String artistType;
    @SerializedName("Skills")
    @Expose
    private String skills;
    @SerializedName("CountryName")
    @Expose
    private String countryName;
    @SerializedName("GenreTypeName")
    @Expose
    private String genreTypeName;
    @SerializedName("ArtistTypeId")
    @Expose
    private Integer artistTypeId;
    @SerializedName("SocialId")
    @Expose
    private String socialId;
    @SerializedName("SocialType")
    @Expose
    private String socialType;
    @SerializedName("SocialImageUrl")
    @Expose
    private String socialImageUrl;
    @SerializedName("IsEmailVerified")
    @Expose
    private String isEmailVerified;
    @SerializedName("Certifications")
    @Expose
    private String certifications;
    @SerializedName("Grade")
    @Expose
    private String grade;
    @SerializedName("Expertise")
    @Expose
    private String expertise;
    @SerializedName("Followers")
    @Expose
    private Integer followers;
    @SerializedName("Reviews")
    @Expose
    private Integer reviews;
    @SerializedName("Rating")
    @Expose
    private String rating;
    @SerializedName("UniqueCode")
    @Expose
    private String uniqueCode;
    @SerializedName("Tag")
    @Expose
    private String tag;
    @SerializedName("NewStatus")
    @Expose
    private String newStatus;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ScreenShot")
    @Expose
    private String screenShot;
    @SerializedName("SearchTypeValue")
    @Expose
    private String searchTypeValue;
    @SerializedName("Serverpath")
    @Expose
    private String serverpath;
    @SerializedName("LocationId")
    @Expose
    private Integer locationId;
    @SerializedName("UserLatitude")
    @Expose
    private String userLatitude;
    @SerializedName("UserLongitude")
    @Expose
    private String userLongitude;
    @SerializedName("LoginUserLatitude")
    @Expose
    private String loginUserLatitude;
    @SerializedName("LoginUserLongitude")
    @Expose
    private String loginUserLongitude;

    @SerializedName("DistanceFromLoggedInUserInMiles")
    @Expose
    private String DistanceFromLoggedInUserInMiles;

    public String getDistanceFromLoggedInUserInMiles() {
        return DistanceFromLoggedInUserInMiles;
    }

    public void setDistanceFromLoggedInUserInMiles(String distanceFromLoggedInUserInMiles) {
        DistanceFromLoggedInUserInMiles = distanceFromLoggedInUserInMiles;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getProfileTypeId() {
        return profileTypeId;
    }

    public void setProfileTypeId(Integer profileTypeId) {
        this.profileTypeId = profileTypeId;
    }

    public String getProfileTypeName() {
        return profileTypeName;
    }

    public void setProfileTypeName(String profileTypeName) {
        this.profileTypeName = profileTypeName;
    }

    public String getCountryCodeId() {
        return countryCodeId;
    }

    public void setCountryCodeId(String countryCodeId) {
        this.countryCodeId = countryCodeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOTPType() {
        return oTPType;
    }

    public void setOTPType(String oTPType) {
        this.oTPType = oTPType;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getGenre() {
        return genre;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

    public String getTalent() {
        return talent;
    }

    public void setTalent(String talent) {
        this.talent = talent;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public Integer getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Integer experienceId) {
        this.experienceId = experienceId;
    }

    public String getExperienceYear() {
        return experienceYear;
    }

    public void setExperienceYear(String experienceYear) {
        this.experienceYear = experienceYear;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getArtistType() {
        return artistType;
    }

    public void setArtistType(String artistType) {
        this.artistType = artistType;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getGenreTypeName() {
        return genreTypeName;
    }

    public void setGenreTypeName(String genreTypeName) {
        this.genreTypeName = genreTypeName;
    }

    public Integer getArtistTypeId() {
        return artistTypeId;
    }

    public void setArtistTypeId(Integer artistTypeId) {
        this.artistTypeId = artistTypeId;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public String getSocialImageUrl() {
        return socialImageUrl;
    }

    public void setSocialImageUrl(String socialImageUrl) {
        this.socialImageUrl = socialImageUrl;
    }

    public String getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(String isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public String getCertifications() {
        return certifications;
    }

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(String screenShot) {
        this.screenShot = screenShot;
    }

    public String getSearchTypeValue() {
        return searchTypeValue;
    }

    public void setSearchTypeValue(String searchTypeValue) {
        this.searchTypeValue = searchTypeValue;
    }

    public String getServerpath() {
        return serverpath;
    }

    public void setServerpath(String serverpath) {
        this.serverpath = serverpath;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(String userLatitude) {
        this.userLatitude = userLatitude;
    }

    public String getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(String userLongitude) {
        this.userLongitude = userLongitude;
    }

    public String getLoginUserLatitude() {
        return loginUserLatitude;
    }

    public void setLoginUserLatitude(String loginUserLatitude) {
        this.loginUserLatitude = loginUserLatitude;
    }

    public String getLoginUserLongitude() {
        return loginUserLongitude;
    }

    public void setLoginUserLongitude(String loginUserLongitude) {
        this.loginUserLongitude = loginUserLongitude;
    }



}
