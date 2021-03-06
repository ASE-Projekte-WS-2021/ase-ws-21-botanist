package com.example.urbotanist.mainfragments.map;

// Google Maps by Google, https://developers.google.com/maps
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public class MapMarkerSetup {


  /**
   * Hardcoded ArrayList for Marker positions and names.
   *
   * @return sets up all Marker as MarkerInfo and returns them in an ArrayList
   */
  public static ArrayList<MarkerInfo> setupMarkerCoordinatesAndNames() {
    ArrayList<MarkerInfo> markerInfoArray = new ArrayList<MarkerInfo>();

    markerInfoArray
            .add(new MarkerInfo("A", new LatLng(48.993714, 12.090622), "Bruch- und Auwälder"));
    markerInfoArray
            .add(new MarkerInfo("B", new LatLng(48.992941, 12.089658), "Submediterraner Bereich"));
    markerInfoArray.add(new MarkerInfo("C", new LatLng(48.993333, 12.092587), "Kalkalpenbeet"));
    markerInfoArray.add(new MarkerInfo("D", new LatLng(48.993010, 12.091769), "Schulgarten"));
    markerInfoArray
            .add(new MarkerInfo("E", new LatLng(48.993669, 12.089771), "Eichen-Birkenwälder"));
    markerInfoArray.add(new MarkerInfo("F", new LatLng(48.993288, 12.091611), "Felsengarten"));
    markerInfoArray
            .add(new MarkerInfo("G3", new LatLng(48.992697, 12.091060), "Amerika"));
    markerInfoArray
            .add(new MarkerInfo("G2", new LatLng(48.992598, 12.090575), "Asien"));
    markerInfoArray
            .add(new MarkerInfo("G1", new LatLng(48.992637, 12.089911), "Europa"));
    markerInfoArray
            .add(new MarkerInfo("H", new LatLng(48.994818, 12.091314),
                    "Alpine Höhenstufengliederung"));
    markerInfoArray
            .add(new MarkerInfo("I", new LatLng(48.993244, 12.092585), "Biologische Gruppen"));
    markerInfoArray.add(new MarkerInfo("J", new LatLng(48.993368, 12.092583), "Silikatalpenbeet"));
    markerInfoArray.add(new MarkerInfo("K", new LatLng(48.993413, 12.092388), "Schattenhallen"));
    markerInfoArray
            .add(new MarkerInfo("L", new LatLng(48.993075, 12.092453),
                    "Gefährdete Pflanzenarten Bayerns"));
    markerInfoArray
            .add(new MarkerInfo("M", new LatLng(48.994167, 12.090566),
                    "Mitteleuropäische Laubwaldzone"));
    markerInfoArray
            .add(new MarkerInfo("N", new LatLng(48.994563, 12.090852), "Nördliche Nadelwaldzone"));
    markerInfoArray
            .add(new MarkerInfo("P", new LatLng(48.993413, 12.091968), "Pharmazeutischer Garten"));
    markerInfoArray.add(new MarkerInfo("R", new LatLng(48.993502, 12.093180), "Schaufensterbeete"));
    markerInfoArray.add(new MarkerInfo("T", new LatLng(48.993397, 12.093149), "Gewächshäuser"));
    markerInfoArray.add(new MarkerInfo("T", new LatLng(48.993461, 12.093716), "Gewächshäuser"));
    markerInfoArray
            .add(new MarkerInfo("U", new LatLng(48.993532, 12.092116), "Rubus- & Sorbus-Sammlung"));
    markerInfoArray
            .add(new MarkerInfo("U", new LatLng(48.992947, 12.092129), "Rubus- & Sorbus-Sammlung"));
    markerInfoArray
            .add(new MarkerInfo("V", new LatLng(48.993163, 12.092096),
                    "Versuchs- & Anzuchtflächen"));
    markerInfoArray.add(new MarkerInfo("X", new LatLng(48.993404, 12.092591), "Vermehrungskästen"));
    markerInfoArray.add(new MarkerInfo("Y", new LatLng(48.993258, 12.092723), "Schaupflanzung"));
    markerInfoArray
            .add(new MarkerInfo("Z", new LatLng(48.994650, 12.090150),
                    "Zergstrauchtundra und Moor"));
    markerInfoArray
            .add(new MarkerInfo("S1", new LatLng(48.993287, 12.091365),
                    "Magnoliidae und Ranunculidae"));
    markerInfoArray
            .add(new MarkerInfo("S2", new LatLng(48.993453, 12.090974),
                    "Alismatidae, Lilidae, Arecidae"));
    markerInfoArray.add(new MarkerInfo("S3", new LatLng(48.993273, 12.090882), "Hamamelididae"));
    markerInfoArray.add(new MarkerInfo("S4", new LatLng(48.993131, 12.090866), "Rosidae"));
    markerInfoArray.add(new MarkerInfo("S5", new LatLng(48.992981, 12.090817), "Dilleniidae"));
    markerInfoArray.add(new MarkerInfo("S6", new LatLng(48.992876, 12.090904), "Cayrophylidae"));
    markerInfoArray
        .add(new MarkerInfo("S7", new LatLng(48.993322, 12.090174), "Lamiidae, Asteridae"));

    return markerInfoArray;
  }



}
