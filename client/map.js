function initMap() {

    // declaring inputs for the locations of the trip and events
    const tripLocInput = document.getElementById("trip-loc-input");
    const event1LocInput = document.getElementById("event1-loc-input");
    const event2LocInput = document.getElementById("event2-loc-input");
    const event3LocInput = document.getElementById("event3-loc-input");

    // options applying to all inputs
    const options = {
      fields: ["formatted_address", "geometry", "name"],
      strictBounds: false,
      types: ["establishment"],
    };
    
    // constructing 4 separate autocomplete objects for the trip + events
    const autocompleteTrip = new google.maps.places.Autocomplete(tripLocInput, options);
    const autocompleteEvent1 = new google.maps.places.Autocomplete(event1LocInput, options);
    const autocompleteEvent2 = new google.maps.places.Autocomplete(event2LocInput, options);
    const autocompleteEvent3 = new google.maps.places.Autocomplete(event3LocInput, options);
    
    autocompleteTrip.setTypes(["(cities)"]);
    autocompleteEvent1.setTypes(["(cities)"]);
    autocompleteEvent2.setTypes(["(cities)"]);
    autocompleteEvent3.setTypes(["(cities)"]);

    const infowindow = new google.maps.InfoWindow();
    const infowindowContent = document.getElementById("infowindow-content");
    infowindow.setContent(infowindowContent);
  
    // LISTENERS FOR TRIP AND EVENTS:

    // LISTENER FOR THE LOCATION OF TRIP OVERALL
    autocompleteTrip.addListener("place_changed", () => {
      infowindow.close();

      const place = autocompleteTrip.getPlace();

      // updating the global tripLatitude and tripLongitude variables in addtrip.html
      tripLatitude = place.geometry.location.lat();
      tripLongitude = place.geometry.location.lng();
      
      console.log("tripLatitude: " + tripLatitude);
      console.log("tripLongitude: " + tripLongitude);
  
      if (!place.geometry || !place.geometry.location) {
        // User entered the name of a Place that was not suggested and
        // pressed the Enter key, or the Place Details request failed.
        window.alert("No details available for input: '" + place.name + "'");
        return;
      }
  
    });

    // LISTENER FOR THE LOCATION OF EVENT #1
    autocompleteEvent1.addListener("place_changed", () => {
      infowindow.close();

      const place = autocompleteEvent1.getPlace();

      // updating the global tripLatitude and tripLongitude variables in addtrip.html
      event1Latitude = place.geometry.location.lat();
      event1Longitude = place.geometry.location.lng();
      
      console.log("event1Latitude: " + event1Latitude);
      console.log("event1Longitude: " + event1Longitude);
  
      if (!place.geometry || !place.geometry.location) {
        // User entered the name of a Place that was not suggested and
        // pressed the Enter key, or the Place Details request failed.
        window.alert("No details available for input: '" + place.name + "'");
        return;
      }
  
    });



    // LISTENER FOR THE LOCATION OF EVENT #2
    autocompleteEvent2.addListener("place_changed", () => {
      infowindow.close();

      const place = autocompleteEvent2.getPlace();

      // updating the global tripLatitude and tripLongitude variables in addtrip.html
      event2Latitude = place.geometry.location.lat();
      event2Longitude = place.geometry.location.lng();
      
      console.log("event2Latitude: " + event2Latitude);
      console.log("event2Longitude: " + event2Longitude);
  
      if (!place.geometry || !place.geometry.location) {
        // User entered the name of a Place that was not suggested and
        // pressed the Enter key, or the Place Details request failed.
        window.alert("No details available for input: '" + place.name + "'");
        return;
      }
  
    });

   
    // LISTENER FOR THE LOCATION OF EVENT #3
    autocompleteEvent3.addListener("place_changed", () => {
      infowindow.close();

      const place = autocompleteEvent2.getPlace();

      // updating the global tripLatitude and tripLongitude variables in addtrip.html
      event3Latitude = place.geometry.location.lat();
      event3Longitude = place.geometry.location.lng();
      
      console.log("event3Latitude: " + event3Latitude);
      console.log("event3Longitude: " + event3Longitude);
  
      if (!place.geometry || !place.geometry.location) {
        // User entered the name of a Place that was not suggested and
        // pressed the Enter key, or the Place Details request failed.
        window.alert("No details available for input: '" + place.name + "'");
        return;
      }
  
    });



  }
  