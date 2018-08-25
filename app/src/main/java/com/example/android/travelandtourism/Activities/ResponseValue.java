package com.example.android.travelandtourism.Activities;
import com.example.android.travelandtourism.Models.City;
import com.example.android.travelandtourism.Models.Countries;
import com.example.android.travelandtourism.Models.Flight;
import com.example.android.travelandtourism.Models.FlightReservation;
import com.example.android.travelandtourism.Models.Hotel;
import com.example.android.travelandtourism.Models.HotelRate;
import com.example.android.travelandtourism.Models.HotelReservations;
import com.example.android.travelandtourism.Models.Offer;
import com.example.android.travelandtourism.Models.OfferConfermation;
import com.example.android.travelandtourism.Models.UserModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;


public class ResponseValue {

    ////////////////////////////
    private List<Countries> contries;

    public List<Countries> getContries() {
        return contries;
    }

    public void setContries(List<Countries> contries) {
        this.contries = contries;
    }
///////////////////////////

    private Countries country;

    public Countries getCountry() {
        return country;
    }

    public void setCountry(Countries country) {
        this.country = country;
    }
//////////////////////////
    private List<City> cities;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    ///////////////////////////
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
    ///   //////////////////////

    @SerializedName("userModel")
    @Expose
    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
/////////////////////////


    @SerializedName("hotels")
    @Expose
    private List <Hotel> hotels;

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

   //////////////////////////

    @SerializedName("hotel")
    @Expose
    private Hotel hotel;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

/////////

    @SerializedName("confermation")
    @Expose
    private HotelReservations confermation;


    public HotelReservations getConfermation() {
        return confermation;
    }

    public void setConfermation(HotelReservations confermation) {
        this.confermation = confermation;
    }
///////

    @SerializedName("flightConfermation")
    @Expose
    private FlightReservation confermationFly;

    public FlightReservation getFlightConfermation() {
        return confermationFly;
    }

    public void setFlightConfermation(FlightReservation confermation) {
        this.confermationFly = confermation;
    }
    ///////////
    @SerializedName("flightReservations")
    @Expose
    private  List<FlightReservation> flightReservationList=null;
    public List<FlightReservation> getFlightReservationList() {
        return flightReservationList;
    }

    public void setFlightReservationList(List<FlightReservation> flightReservationList) {
        this.flightReservationList = flightReservationList;
    }


    ///////
    @SerializedName("hotelReservations")
    @Expose
    private List <HotelReservations> HotelReservations;

    public List<HotelReservations> getHotelReservations() {
        return HotelReservations;
    }

    public void setHotelReservations(List<HotelReservations> hotelReservations) {
        HotelReservations = hotelReservations;
    }


    ///////////////////
    @SerializedName("flight")
    @Expose
    private List<Flight> flight = null;

    public List<Flight> getFlight() {
        return flight;
    }

    public void setFlight(List<Flight> flight) {
        this.flight = flight;
    }
//////////////////////

    @SerializedName("flights")
    @Expose
    private List<Flight> flightSchedule = null;


    public List<Flight> getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(List<Flight> flightSchedule) {
        this.flightSchedule = flightSchedule;
    }


    /////////////
    @SerializedName("offers")
    @Expose
    private List<Offer> offers = null;

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    /////////



    /////////////

    @SerializedName("offer")
    @Expose
    private Offer offer;

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
/////////////////////

    @SerializedName("offersOptions")
    @Expose
    private List<Offer> offersOptions = null;

    public List<Offer> getOffersOptions() {
        return offersOptions;
    }

    public void setOffersOptions(List<Offer> offersOptions) {
        this.offersOptions = offersOptions;
    }

//////////////////////////////

    @SerializedName("offerConfermation")
    @Expose
    private OfferConfermation offerConfermation=null;

    public OfferConfermation getOfferConfermation() {
        return offerConfermation;
    }

    public void setOfferConfermation(OfferConfermation offerConfermation) {
        this.offerConfermation = offerConfermation;
    }
    /////////////////////


    @SerializedName("myOffers")
    @Expose
    private List<OfferConfermation> offerConfermationList=null;

    public List<OfferConfermation> getOfferConfermationList() {
        return offerConfermationList;
    }

    public void setOfferConfermationList(List<OfferConfermation> offerConfermationList) {
        this.offerConfermationList = offerConfermationList;
    }


////////////////////////

    @SerializedName("yourRate")
    @Expose
    private HotelRate yourRate;

    public HotelRate getYourRate() {
        return yourRate;
    }

    public void setYourRate(HotelRate yourRate) {
        this.yourRate = yourRate;
    }


/////////////

    @SerializedName("userModel2")
    @Expose
    private  UserModel chargeCridet;

    public UserModel getChargeCridet() {
        return chargeCridet;
    }

    public void setChargeCridet(UserModel chargeCridet) {
        this.chargeCridet = chargeCridet;
    }


    @SerializedName("myOffer")
    @Expose
    private OfferConfermation offerReservation=null;

    public OfferConfermation getOfferReservation() {
        return offerReservation;
    }

    public void setOfferReservation(OfferConfermation offerReservation) {
        this.offerReservation = offerReservation;
    }

}
