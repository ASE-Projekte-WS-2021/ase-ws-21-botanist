package com.example.urbotanist.ui.map;

import com.google.android.gms.maps.model.PolygonOptions;
import java.util.ArrayList;

public class PolygonInfo {

  private final String areaName;
  private final ArrayList<PolygonOptions> polyOpList;


  public PolygonInfo(String areaName, ArrayList<PolygonOptions> polyOpList) {
    this.areaName = areaName;
    this.polyOpList = polyOpList;
  }

  public String getAreaName() {
    return this.areaName;
  }

  public ArrayList<PolygonOptions> getPolyOpList() {
    return polyOpList;
  }
}

