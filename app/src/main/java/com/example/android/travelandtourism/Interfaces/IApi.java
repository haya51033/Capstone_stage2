package com.example.android.travelandtourism.Interfaces;


import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.example.android.travelandtourism.Activities.ResponseValue;
import com.example.android.travelandtourism.Models.Message;

public interface IApi {

    @FormUrlEncoded
    @POST("Account/Login")
    Call<ResponseValue> authenticate(@Field("username") String username, @Field("password") String password);


    @FormUrlEncoded
    @POST("Account/Register")
    Call<ResponseValue> registration(@Field("username") String username,
                                 @Field("password") String password,
                                 @Field("confirmPassword") String confirmPassword,
                                 @Field("firstName") String firstName,
                                 @Field("lastName") String lastName,
                                 @Field("gender") String gender,
                                 @Field("email") String email,
                                 @Field("phoneNumber") String phone,
                                 @Field("country") String country,
                                 @Field("city") String city);

    @FormUrlEncoded
    @POST("hotel/ReserveHotel")
    Call<ResponseValue> bookHotel(@Field("RoomId") int roomId, @Field("Guest") String guestId,
                                  @Field("Check_In_Date") String check_In_Date,
                                  @Field("Check_Out_Date")String check_Out_Date);

    @FormUrlEncoded
    @POST("hotel/Reservations")
    Call<ResponseValue> MyHotelReservations(@Field("Guest") String guestId);

    @FormUrlEncoded
    @POST("flights")
    Call<ResponseValue> SearchFlight(@Field("SourceCity") int sourceCity,
                                     @Field("DestinationCity") int destinationCity,
                                     @Field("DisplayDate") String displayDate);
    @FormUrlEncoded
    @POST("flights/ReserveFlight")
    Call<ResponseValue> BookFlight(@Field("Seats") int seats,
                                            @Field("FlightClass") String flightClass,
                                            @Field("Customer") String customerId,
                                            @Field("FlightID") int FlightID);
    @FormUrlEncoded
    @POST("flights/Reservations")
    Call<ResponseValue> getMyFlightReservations(@Field("Customer") String customer);


    @FormUrlEncoded
    @POST("offers/ReserveOffer")
    Call<ResponseValue> BookOffer(@Field("Id") int offerId, @Field("Customer") String customerId);

    @FormUrlEncoded
    @POST("offers/CncelReserveOffer")
    Call<Message> cancelOffer(@Field("Id") int offerId, @Field("Customer") String customerId);

    @FormUrlEncoded
    @POST("flights/CancelFlightReservation")
    Call<Message> cancelFlight(@Field("Id") int flightId, @Field("Customer") String customerId);


    @FormUrlEncoded
    @POST("hotel/CancelHotelReservation")
    Call<Message> CancelHotelReservation(@Field("Id") int hotelResId, @Field("Guest") String customerId);

    @FormUrlEncoded
    @POST("Message")
    Call<Message> SendMessage(@Field("Email") String email, @Field("Subject") String subject, @Field("Text") String text);

    @FormUrlEncoded
    @POST("Account/ForgetPassword")
    Call<Message> forgetPassword(@Field("Email") String email);

    @FormUrlEncoded
    @POST("Account/ChangePassword")
    Call<Message> ChangePassword(@Field("userId") String userId,@Field("CurrentPassword") String currentPassword,
                                 @Field("NewPassword") String newPassword);
    @FormUrlEncoded
    @POST("Hotel/HotelRate")
    Call<ResponseValue> RateHotel(@Field("User") String User, @Field("HotelId") int HotelId,@Field("Rate") String rate);

    @FormUrlEncoded
    @POST("Account/ChargeCredit")
    Call<ResponseValue> ChargeCredit(@Field("UserId") String userId , @Field("NewCredit") int newCredit);

    @FormUrlEncoded
    @POST("Account/Update")
    Call<Message> UpdateUserInfo(@Field("userId") String userId,
                                    @Field("firstName") String firstName,
                                    @Field("lastName") String lastName,
                                    @Field("gender") String gender,
                                    @Field("email") String email,
                                    @Field("phoneNumber") String phoneNumber,
                                    @Field("country") String country,
                                    @Field("city") String city);

    @GET("countries")
    Call <ResponseValue> getCountries();

    @GET("countries/{id}")
    Call <ResponseValue> getCountry(@Path("id") int countryId);

    @GET("cities")
    Call<ResponseValue> getCities();

    @GET("cities/{id}")
    Call <ResponseValue> getCity(@Path("id") int cityId);

    @GET("hotel/{countryId}/{cityId}")
    Call <ResponseValue> getCityHotel(@Path("countryId") int countryId, @Path("cityId") int cityId);

    @GET("hotel/{hotelId}")
    Call <ResponseValue> getHotel(@Path("hotelId") int hotelId);


    @GET("flights/{sourceCity}/{destinationCity}")
    Call<ResponseValue> getFlightSchedule (@Path("sourceCity") int sourceCityId, @Path("destinationCity") int destinationCityId);


    @GET("offers")
    Call <ResponseValue> getOffers();

    @GET ("offers/{id}")
    Call<ResponseValue>getOfferById(@Path("id") int offerId);

    @GET("offers/{sourceCity}/{destinationCity}")
    Call<ResponseValue> getOffersOptions(@Path("sourceCity") int sourceCityId, @Path("destinationCity") int destinationCityId);

    @GET("offers/MyReservedOffers/{userId}")
    Call<ResponseValue> getMyOfferReservations(@Path("userId") String userId);

    @GET("offers/MyOfferReservation/{reservationId}")
    Call<ResponseValue> getMyOfferReservation(@Path("reservationId") int reservationId);


}
