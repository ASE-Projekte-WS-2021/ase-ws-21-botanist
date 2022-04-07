package com.example.urbotanist.mainfragments.map;

// Google Maps by Google, https://developers.google.com/maps
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import java.util.ArrayList;
import java.util.Arrays;

public class PolygonMaker {

  private PolygonOptions polyOpA;
  private PolygonOptions polyOpB;
  private PolygonOptions polyOpC;
  private PolygonOptions polyOpD;
  private PolygonOptions polyOpE;
  private PolygonOptions polyOpF;
  private PolygonOptions polyOpG;
  private PolygonOptions polyOpH;
  private PolygonOptions polyOpI;
  private PolygonOptions polyOpJ;
  private PolygonOptions polyOpK;
  private PolygonOptions polyOpL;
  private PolygonOptions polyOpM;
  private PolygonOptions polyOpN;
  private PolygonOptions polyOpP;
  private PolygonOptions polyOpR;
  private PolygonOptions polyOpS;
  private PolygonOptions polyOpT1;
  private PolygonOptions polyOpT2;
  private PolygonOptions polyOpU1;
  private PolygonOptions polyOpU2;
  private PolygonOptions polyOpV;
  private PolygonOptions polyOpX;
  private PolygonOptions polyOpY;
  private PolygonOptions polyOpZ;

  private ArrayList<PolygonInfo> polyInfoList = new ArrayList<>();

  public PolygonMaker() {
    initPolygonOptions();
    fillArrayList();
  }

  /**
   * PolygonInfo contains area name and a List of all polygons included in this area
   */
  private void fillArrayList() {
    polyInfoList.add(new PolygonInfo("A", new ArrayList<>(Arrays.asList(polyOpA))));
    polyInfoList.add(new PolygonInfo("B", new ArrayList(Arrays.asList(polyOpB))));
    polyInfoList.add(new PolygonInfo("C", new ArrayList(Arrays.asList(polyOpC))));
    polyInfoList.add(new PolygonInfo("D", new ArrayList(Arrays.asList(polyOpD))));
    polyInfoList.add(new PolygonInfo("E", new ArrayList(Arrays.asList(polyOpE))));
    polyInfoList.add(new PolygonInfo("F", new ArrayList(Arrays.asList(polyOpF))));
    polyInfoList.add(new PolygonInfo("G", new ArrayList(Arrays.asList(polyOpG))));
    polyInfoList.add(new PolygonInfo("H", new ArrayList(Arrays.asList(polyOpH))));
    polyInfoList.add(new PolygonInfo("I", new ArrayList(Arrays.asList(polyOpI))));
    polyInfoList.add(new PolygonInfo("J", new ArrayList(Arrays.asList(polyOpJ))));
    polyInfoList.add(new PolygonInfo("K", new ArrayList(Arrays.asList(polyOpK))));
    polyInfoList.add(new PolygonInfo("L", new ArrayList(Arrays.asList(polyOpL))));
    polyInfoList.add(new PolygonInfo("M", new ArrayList(Arrays.asList(polyOpM))));
    polyInfoList.add(new PolygonInfo("N", new ArrayList(Arrays.asList(polyOpN))));
    polyInfoList.add(new PolygonInfo("P", new ArrayList(Arrays.asList(polyOpP))));
    polyInfoList.add(new PolygonInfo("R", new ArrayList(Arrays.asList(polyOpR))));
    polyInfoList.add(new PolygonInfo("S", new ArrayList(Arrays.asList(polyOpS))));
    polyInfoList.add(new PolygonInfo("T", new ArrayList(Arrays.asList(polyOpT1, polyOpT2))));
    polyInfoList.add(new PolygonInfo("U", new ArrayList(Arrays.asList(
        polyOpU1, polyOpU2))));
    polyInfoList.add(new PolygonInfo("V", new ArrayList(Arrays.asList(polyOpV))));
    polyInfoList.add(new PolygonInfo("X", new ArrayList(Arrays.asList(polyOpX))));
    polyInfoList.add(new PolygonInfo("Y", new ArrayList(Arrays.asList(polyOpY))));
    polyInfoList.add(new PolygonInfo("Z", new ArrayList(Arrays.asList(polyOpZ))));
  }

  /**
   * @return all PolygonInfo as ArrayList
   */
  public ArrayList<PolygonInfo> getPolyInfoList() {
    return polyInfoList;
  }

  /**
   *  inits all Polygons with LatLng points as PolygonOptions
   */
  private void initPolygonOptions() {
    polyOpA = new PolygonOptions()
        .add(new LatLng(48.994040, 12.090085),
            new LatLng(48.993783, 12.091022),
            new LatLng(48.993608, 12.091456),
            new LatLng(48.993465, 12.091358),
            new LatLng(48.993536, 12.090970),
            new LatLng(48.993492, 12.090836),
            new LatLng(48.993560, 12.090528),
            new LatLng(48.993622, 12.089916));

    polyOpB = new PolygonOptions()
        .add(new LatLng(48.993293, 12.089466),
            new LatLng(48.993260, 12.089792),
            new LatLng(48.993013, 12.089745),
            new LatLng(48.992841, 12.089948),
            new LatLng(48.992734, 12.089562));

    polyOpC = new PolygonOptions()
        .add(new LatLng(48.993359, 12.092530),
            new LatLng(48.993366, 12.092644),
            new LatLng(48.993314, 12.092646),
            new LatLng(48.993315, 12.092529));

    polyOpD = new PolygonOptions()
        .add(new LatLng(48.993071, 12.091634),
            new LatLng(48.993094, 12.091871),
            new LatLng(48.992922, 12.091971),
            new LatLng(48.992882, 12.091875),
            new LatLng(48.992837, 12.091892),
            new LatLng(48.992774, 12.091546),
            new LatLng(48.992822, 12.091543),
            new LatLng(48.992865, 12.091704));

    polyOpE = new PolygonOptions()
        .add(new LatLng(48.993795, 12.089464),
            new LatLng(48.993902, 12.089781),
            new LatLng(48.994040, 12.089815),
            new LatLng(48.994040, 12.090085),
            new LatLng(48.993622, 12.089916),
            new LatLng(48.993260, 12.089792),
            new LatLng(48.993293, 12.089466));

    polyOpF = new PolygonOptions()
        .add(new LatLng(48.993553, 12.091583),
            new LatLng(48.993500, 12.091680),
            new LatLng(48.993287, 12.091650),
            new LatLng(48.993071, 12.091634),
            new LatLng(48.992865, 12.091704),
            new LatLng(48.992822, 12.091543),
            new LatLng(48.992979, 12.091551),
            new LatLng(48.993340, 12.091544));

    polyOpG = new PolygonOptions()
        .add(new LatLng(48.992734, 12.089562),
            new LatLng(48.992841, 12.089948),
            new LatLng(48.992753, 12.090184),
            new LatLng(48.992744, 12.090663),
            new LatLng(48.992979, 12.091551),
            new LatLng(48.992822, 12.091543),
            new LatLng(48.992774, 12.091546),
            new LatLng(48.992590, 12.090897),
            new LatLng(48.992462, 12.089775));

    polyOpH = new PolygonOptions()
        .add(new LatLng(48.995001, 12.091307),
            new LatLng(48.994992, 12.091640),
            new LatLng(48.994833, 12.091699),
            new LatLng(48.994650, 12.091440),
            new LatLng(48.994631, 12.091213),
            new LatLng(48.994776, 12.090776));

    polyOpI = new PolygonOptions()
        .add(new LatLng(48.993315, 12.092529),
            new LatLng(48.993314, 12.092646),
            new LatLng(48.993184, 12.092620),
            new LatLng(48.993195, 12.092526));

    polyOpJ = new PolygonOptions()
        .add(new LatLng(48.993378, 12.092528),
            new LatLng(48.993378, 12.092633),
            new LatLng(48.993366, 12.092644),
            new LatLng(48.993359, 12.092530));

    polyOpK = new PolygonOptions()
        .add(new LatLng(48.993499, 12.092276),
            new LatLng(48.993501, 12.092528),
            new LatLng(48.993378, 12.092528),
            new LatLng(48.993359, 12.092530),
            new LatLng(48.993314, 12.092268));

    polyOpL = new PolygonOptions()
        .add(new LatLng(48.993029, 12.092239),
            new LatLng(48.993133, 12.092520),
            new LatLng(48.993195, 12.092526),
            new LatLng(48.993184, 12.092620),
            new LatLng(48.993056, 12.092604),
            new LatLng(48.992979, 12.092280));

    polyOpM = new PolygonOptions()
        .add(new LatLng(48.994620, 12.090378),
            new LatLng(48.994395, 12.090807),
            new LatLng(48.994208, 12.091105),
            new LatLng(48.993900, 12.091157),
            new LatLng(48.993783, 12.091022),
            new LatLng(48.994040, 12.090085),
            new LatLng(48.994491, 12.090131));

    polyOpN = new PolygonOptions()
        .add(new LatLng(48.994776, 12.090776),
            new LatLng(48.994631, 12.091213),
            new LatLng(48.994495, 12.091344),
            new LatLng(48.994296, 12.091235),
            new LatLng(48.994208, 12.091105),
            new LatLng(48.994395, 12.090807),
            new LatLng(48.994620, 12.090378));

    polyOpP = new PolygonOptions()
        .add(new LatLng(48.993500, 12.091680),
            new LatLng(48.993499, 12.092276),
            new LatLng(48.993314, 12.092268),
            new LatLng(48.993325, 12.092056),
            new LatLng(48.993284, 12.092059),
            new LatLng(48.993287, 12.091650));

    polyOpR = new PolygonOptions()
        .add(new LatLng(48.993537, 12.092755),
            new LatLng(48.993535, 12.093830),
            new LatLng(48.993487, 12.093818),
            new LatLng(48.993484, 12.092758));

    polyOpT1 = new PolygonOptions()
        .add(new LatLng(48.993485, 12.092853),
            new LatLng(48.993480, 12.093473),
            new LatLng(48.993319, 12.093489),
            new LatLng(48.993325, 12.092834));

    polyOpT2 = new PolygonOptions()
        .add(new LatLng(48.993483, 12.093614),
            new LatLng(48.993487, 12.093818),
            new LatLng(48.993415, 12.093828),
            new LatLng(48.993414, 12.093615));

    polyOpU1 = new PolygonOptions()
        .add(new LatLng(48.993553, 12.091583),
            new LatLng(48.993523, 12.092626),
            new LatLng(48.993484, 12.092638),
            new LatLng(48.993501, 12.092528),
            new LatLng(48.993499, 12.092276),
            new LatLng(48.993500, 12.091680));

    polyOpU2 = new PolygonOptions()
        .add(new LatLng(48.992882, 12.091875),
            new LatLng(48.992922, 12.091971),
            new LatLng(48.993029, 12.092239),
            new LatLng(48.992979, 12.092280),
            new LatLng(48.992837, 12.091892));

    polyOpV = new PolygonOptions()
        .add(new LatLng(48.993287, 12.091650),
            new LatLng(48.993284, 12.092059),
            new LatLng(48.993325, 12.092056),
            new LatLng(48.993314, 12.092268),
            new LatLng(48.993359, 12.092530),
            new LatLng(48.993315, 12.092529),
            new LatLng(48.993195, 12.092526),
            new LatLng(48.993195, 12.092526),
            new LatLng(48.993133, 12.092520),
            new LatLng(48.993029, 12.092239),
            new LatLng(48.992922, 12.091971),
            new LatLng(48.993094, 12.091871),
            new LatLng(48.993071, 12.091634));

    polyOpX = new PolygonOptions()
        .add(new LatLng(48.993501, 12.092528),
            new LatLng(48.993484, 12.092638),
            new LatLng(48.993378, 12.092633),
            new LatLng(48.993378, 12.092528));

    polyOpY = new PolygonOptions()
        .add(new LatLng(48.993456, 12.092695),
            new LatLng(48.993454, 12.092754),
            new LatLng(48.993108, 12.092768),
            new LatLng(48.993101, 12.092715));

    polyOpZ = new PolygonOptions()
        .add(new LatLng(48.994772, 12.090087),
            new LatLng(48.994787, 12.090612),
            new LatLng(48.994720, 12.090619),
            new LatLng(48.994620, 12.090378),
            new LatLng(48.994491, 12.090131),
            new LatLng(48.994502, 12.090012),
            new LatLng(48.994671, 12.089905),
            new LatLng(48.994670, 12.090063));

    polyOpS = new PolygonOptions()
        .add(new LatLng(48.993622, 12.089916),
            new LatLng(48.993560, 12.090528),
            new LatLng(48.993492, 12.090836),
            new LatLng(48.993536, 12.090970),
            new LatLng(48.993465, 12.091358),
            new LatLng(48.993608, 12.091456),
            new LatLng(48.993553, 12.091583),
            new LatLng(48.993340, 12.091544),
            new LatLng(48.992979, 12.091551),
            new LatLng(48.992744, 12.090663),
            new LatLng(48.992753, 12.090184),
            new LatLng(48.992841, 12.089948),
            new LatLng(48.993013, 12.089745),
            new LatLng(48.993260, 12.089792));

  }
}
