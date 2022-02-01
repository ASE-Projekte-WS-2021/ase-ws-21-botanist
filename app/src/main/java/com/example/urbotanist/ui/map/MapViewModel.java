package com.example.urbotanist.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.urbotanist.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class MapViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private static final int COLOR_AREA_A = 0x80FDAD02;
    private static final int COLOR_AREA_B = 0x80FFFFBF;
    private static final int COLOR_AREA_E = 0x80CDAA66;
    private static final int COLOR_AREA_M = 0x8038A700;
    private static final int COLOR_AREA_N = 0x8C38A700;
    private static final int COLOR_AREA_S = 0x80C8D79E;
    private static final int COLOR_AREA_H = 0x9938A700;
    private static final int COLOR_AREA_G = 0x80FF7F7E;

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
    private PolygonOptions polyOpTOne;
    private PolygonOptions polyOpTTwo;
    private PolygonOptions polyOpUOne;
    private PolygonOptions polyOpUTwo;
    private PolygonOptions polyOpUThree;
    private PolygonOptions polyOpV;
    private PolygonOptions polyOpX;
    private PolygonOptions polyOpY;
    private PolygonOptions polyOpZ;

    public MapViewModel() {
        initPolygons();
    }

   public void initData (GoogleMap map) {
       /**set marker
       LatLng botanic_garden = new LatLng(48.993161, 12.090753);

       Polygon area = map.addPolygon(polyOpA);
       area.setFillColor(0x80C8D79E);**/

       LatLng botanic_garden = new LatLng(48.993161, 12.090753);

       map.addMarker(new MarkerOptions()
               .position(botanic_garden)
               .title("Marker"));

       map.moveCamera(CameraUpdateFactory.newLatLngZoom(botanic_garden, 18));

       initPolygons();
       PolygonOptions polygon = getPolygons();
       Polygon area = map.addPolygon(polyOpA);
       area.setFillColor(COLOR_AREA_S);

       Polygon areaB = map.addPolygon(polyOpB);
       Polygon areaC = map.addPolygon(polyOpC);
      map.addPolygon(polyOpD);
       map.addPolygon(polyOpE);
       map.addPolygon(polyOpF);
       map.addPolygon(polyOpG);
       map.addPolygon(polyOpH);
       map.addPolygon(polyOpI);
       map.addPolygon(polyOpJ);
       map.addPolygon(polyOpK);
       map.addPolygon(polyOpL);
       map.addPolygon(polyOpM);
       map.addPolygon(polyOpN);
       map.addPolygon(polyOpP);
       map.addPolygon(polyOpR);
       map.addPolygon(polyOpTOne);
       map.addPolygon(polyOpTTwo);
       map.addPolygon(polyOpUThree);
       map.addPolygon(polyOpUOne);
       map.addPolygon(polyOpUTwo);
       map.addPolygon(polyOpV);
       map.addPolygon(polyOpX);
       map.addPolygon(polyOpY);
       map.addPolygon(polyOpZ);



    }

    private void initPolygons() {
        polyOpA = new PolygonOptions()
                .add(new LatLng(48.994040,12.090085),
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
                        new LatLng(48.992778, 12.091637),
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
                .add(new LatLng(48.993552, 12.091610),
                        new LatLng(48.993500, 12.091680),
                        new LatLng(48.993287, 12.091650),
                        new LatLng(48.993071, 12.091634),
                        new LatLng(48.993074,12.091570));

        polyOpG = new PolygonOptions()
                .add(new LatLng(48.992734, 12.089562),
                        new LatLng(48.992841,12.089948),
                        new LatLng(48.992753, 12.090184),
                        new LatLng(48.992744, 12.090663),
                        new LatLng(48.992953, 12.091486),
                        new LatLng(48.992764, 12.091495),
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
            .add(new LatLng(48.993377, 12.092537),
                    new LatLng(48.993378, 12.092633),
                    new LatLng(48.993366, 12.092644),
                    new LatLng(48.993359, 12.092530));

        polyOpK = new PolygonOptions()
                .add(new LatLng(48.993499, 12.092276),
                        new LatLng(48.993501, 12.092528),
                        new LatLng(48.993359, 12.092530),
                        new LatLng(48.993314, 12.092268));

        polyOpL = new PolygonOptions()
                .add(new LatLng(48.9930249, 12.092239),
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

       polyOpTOne = new PolygonOptions()
               .add(new LatLng(48.993485, 12.092853),
                       new LatLng(48.993480, 12.093473),
                       new LatLng(48.993319, 12.093489),
                       new LatLng(48.993325, 12.092834));

       polyOpTTwo = new PolygonOptions()
               .add(new LatLng(48.993483, 12.093614),
                       new LatLng(48.993487, 12.093818),
                       new LatLng(48.993415, 12.093828),
                       new LatLng(48.993414, 12.093615));

       polyOpUOne = new PolygonOptions()
               .add(new LatLng (48.993074, 12.091570),
                       new LatLng(48.993071, 12.091634),
                       new LatLng(48.992865, 12.091704),
                       new LatLng(48.992887, 12.091584));

       polyOpUTwo = new PolygonOptions()
                .add(new LatLng(48.993552, 12.091610),
                        new LatLng(48.993523, 12.092626),
                        new LatLng(48.993484, 12.092638),
                        new LatLng(48.993501, 12.092528),
                        new LatLng(48.993499, 12.092276),
                        new LatLng(48.993500, 12.091680));

       polyOpUThree = new PolygonOptions()
               .add(new LatLng(48.992882, 12.091875),
                       new LatLng(48.992922, 12.091971),
                       new LatLng(48.993029, 12.092239),
                       new LatLng(48.992979, 12.092280),
                       new LatLng(48.992837, 12.091892));

       polyOpV = new PolygonOptions()
               .add(new LatLng(48.993287, 12.091650),
                       new LatLng(48.993284, 12.092059),
                       new LatLng(48.993029, 12.092239),
                       new LatLng(48.992922, 12.091971),
                       new LatLng(48.993094, 12.091871),
                       new LatLng(48.993071, 12.091634));

       polyOpX = new PolygonOptions()
                .add(new LatLng(48.993501, 12.092528),
                        new LatLng(48.993484, 12.092638),
                        new LatLng(48.993378, 12.092633),
                        new LatLng(48.993377, 12.092537));

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
    }



    public PolygonOptions getPolygons() {
        PolygonOptions polygonS = new PolygonOptions()
                .add(new LatLng(48.992985, 12.091516),
                        new LatLng(48.993540, 12.091541),
                        new LatLng(48.993592, 12.091425),
                        new LatLng(48.993516, 12.091324),
                        new LatLng(48.993612, 12.089953),
                        new LatLng(48.993113, 12.089794),
                        new LatLng(48.992837, 12.089988),
                        new LatLng(48.992735, 12.090326));
        return polygonS;
    }
}