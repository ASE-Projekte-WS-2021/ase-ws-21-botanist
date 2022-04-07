package com.example.urbotanist.mainfragments.map;

import com.example.urbotanist.drawerfragments.area.Area;

public interface MarkerInfoClickListener {

  /**
   * @param area contains area, in which a marker info was clicked
   */
  void onMarkerInfoClicked(Area area);
}
