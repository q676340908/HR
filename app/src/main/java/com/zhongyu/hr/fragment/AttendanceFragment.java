package com.zhongyu.hr.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;


import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.nplatform.comapi.map.MapController;
import com.zhongyu.hr.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by Administrator on 2015/11/30.
 */
public class AttendanceFragment extends Fragment  implements OnGetGeoCoderResultListener {
    //  public static String AKEY = "andg67KOIX3Ot77yObAH9pB8";
    BMapManager bMapManager;
    BaiduMap baiduMap = null;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    BDLocation bdLocation;
    MapController mapController ;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Bind(R.id.bmapView)
    MapView bmapView;
    @Bind(R.id.attendence_bt)
    Button attendenceBt;
    @Bind(R.id.out_bt)
    Button outBt;
    @Bind(R.id.history_bt)
    Button historyBt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //SDKInitializer.initialize(this);
        SDKInitializer.initialize(getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        mLocationClient = new LocationClient(getActivity().getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);


        ButterKnife.bind(this, rootView);

        baiduMap = bmapView.getMap();


       // mapController = new MapController(bmapView);
        //设置缩放控件位置
        Point p = new Point();
        p.set(0,0);
        bmapView.setZoomControlsPosition(p);
        return rootView;

    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


    @Override
    public void onResume() {
        super.onResume();


        if (null != bmapView) {
            bmapView.onResume();
        }
        initLocation();
        mLocationClient.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != bmapView) {
            bmapView.onDestroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
      /*  if (null != bmapView) {
            bmapView.onPause();
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            setBdLocation(location);
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
          //  Log.i("BaiduLocationApiDem", sb.toString());
            Log.i("zhou",sb.toString());

            // 构造定位数据
//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                            // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(100).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();

//
//            baiduMap.setMyLocationEnabled(true);
//            baiduMap.setMyLocationData(locData);
//            baiduMap.setBuildingsEnabled(true);

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());

         // 反Geo搜索
            mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(ll));

          //  MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,20);


         //   baiduMap.animateMapStatus(u);

            mLocationClient.stop();





        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        baiduMap.clear();
        View view  = getActivity().getLayoutInflater().inflate(R.layout.baidu_location_flagview,null);
        TextView locationText = (TextView) view.findViewById(R.id.location_text);
        locationText.setText(getBdLocation().getAddrStr());

        InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), reverseGeoCodeResult
                .getLocation(), -47, null);
        baiduMap.showInfoWindow(mInfoWindow);
        baiduMap.addOverlay(new MarkerOptions().position(reverseGeoCodeResult.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_gcoding)));
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(reverseGeoCodeResult
                .getLocation(),18);
        baiduMap.setMapStatus(u);
    }

    public BDLocation getBdLocation() {
        return bdLocation;
    }

    public void setBdLocation(BDLocation bdLocation) {
        this.bdLocation = bdLocation;
    }
}
