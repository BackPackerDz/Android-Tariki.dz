package com.squalala.tariki.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.squalala.tariki.R;

import org.osmdroid.bonuspack.clustering.MarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * Copyright 2016 Fayçal KADDOURI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
public class RadiusMarkerClusterer extends MarkerClusterer {

    protected int mMaxClusteringZoomLevel = 17;
    protected int mRadiusInPixels = 100;
    protected double mRadiusInMeters;
    protected Paint mTextPaint;
    private ArrayList<Marker> mClonedMarkers;

    private Context context;

    private int [] drawableSources = {R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5};

    /** cluster icon anchor */
    public float mAnchorU = Marker.ANCHOR_CENTER, mAnchorV = Marker.ANCHOR_CENTER;
    /** anchor point to draw the number of markers inside the cluster icon */
    public float mTextAnchorU = Marker.ANCHOR_CENTER, mTextAnchorV = Marker.ANCHOR_CENTER;

    public RadiusMarkerClusterer(Context ctx) {
        super(ctx);
        this.context = ctx;

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(15 * ctx.getResources().getDisplayMetrics().density);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
     /*   Drawable clusterIconD = ctx.getResources().getDrawable(org.osmdroid.bonuspack.R.drawable.marker_cluster);
        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
        setIcon(clusterIcon); */
    }

    /** If you want to change the default text paint (color, size, font) */
    public Paint getTextPaint(){
        return mTextPaint;
    }

    /** Set the radius of clustering in pixels. Default is 100px. */
    public void setRadius(int radius){
        mRadiusInPixels = radius;
    }

    /** Set max zoom level with clustering. When zoom is higher or equal to this level, clustering is disabled.
     * You can put a high value to disable this feature. */
    public void setMaxClusteringZoomLevel(int zoom){
        mMaxClusteringZoomLevel = zoom;
    }

    /** Radius-Based clustering algorithm */
    @Override public ArrayList<StaticCluster> clusterer(MapView mapView) {

        ArrayList<StaticCluster> clusters = new ArrayList<StaticCluster>();
        convertRadiusToMeters(mapView);

        mClonedMarkers = new ArrayList<Marker>(mItems); //shallow copy
        while (!mClonedMarkers.isEmpty()) {
            Marker m = mClonedMarkers.get(0);
            StaticCluster cluster = createCluster(m, mapView);
            clusters.add(cluster);
        }
        return clusters;
    }

    private StaticCluster createCluster(Marker m, MapView mapView) {
        GeoPoint clusterPosition = m.getPosition();

        StaticCluster cluster = new StaticCluster(clusterPosition);
        cluster.add(m);

        mClonedMarkers.remove(m);

        if (mapView.getZoomLevel() > mMaxClusteringZoomLevel) {
            //above max level => block clustering:
            return cluster;
        }

        Iterator<Marker> it = mClonedMarkers.iterator();
        while (it.hasNext()) {
            Marker neighbour = it.next();
            int distance = clusterPosition.distanceTo(neighbour.getPosition());
            if (distance <= mRadiusInMeters) {
                cluster.add(neighbour);
                it.remove();
            }
        }

        return cluster;
    }

    @Override public Marker buildClusterMarker(StaticCluster cluster, MapView mapView) {
        Marker m = new Marker(mapView);
        m.setPosition(cluster.getPosition());
        m.setInfoWindow(null);
        m.setAnchor(mAnchorU, mAnchorV);

        if (cluster.getSize() >= 500 )
        {
            setIcon(drawableSources[4]);
        }
        else if (cluster.getSize() >= 250 && cluster.getSize() < 500)
        {
            setIcon(drawableSources[3]);
        }
        else if (cluster.getSize() >= 100 && cluster.getSize() < 250)
        {
            setIcon(drawableSources[3]);
        }
        else if (cluster.getSize() >= 50 && cluster.getSize() < 100)
        {
            setIcon(drawableSources[2]);
        }
        else if (cluster.getSize() >= 10 && cluster.getSize() < 50)
        {
            setIcon(drawableSources[1]);
        }
        else if (cluster.getSize() < 10)
        {
            setIcon(drawableSources[0]);
        }

        Bitmap finalIcon = Bitmap.createBitmap(mClusterIcon.getWidth(), mClusterIcon.getHeight(), mClusterIcon.getConfig());
        Canvas iconCanvas = new Canvas(finalIcon);
        iconCanvas.drawBitmap(mClusterIcon, 0, 0, null);
        String text = "" + cluster.getSize();

        int textHeight = (int) (mTextPaint.descent() + mTextPaint.ascent());
        iconCanvas.drawText(text,
                mTextAnchorU * finalIcon.getWidth(),
                mTextAnchorV * finalIcon.getHeight() - textHeight / 2,
                mTextPaint);
        m.setIcon(new BitmapDrawable(mapView.getContext().getResources(), finalIcon));

        return m;
    }

    private void setIcon(int source) {
        Drawable clusterIconD = ContextCompat.getDrawable(context, source);
        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
        setIcon(clusterIcon);
    }

    @Override public void renderer(ArrayList<StaticCluster> clusters, Canvas canvas, MapView mapView) {
        for (StaticCluster cluster : clusters) {
            if (cluster.getSize() == 1) {
                //cluster has only 1 marker => use it as it is:
                cluster.setMarker(cluster.getItem(0));
            } else {
                //only draw 1 Marker at Cluster center, displaying number of Markers contained
                Marker m = buildClusterMarker(cluster, mapView);
                cluster.setMarker(m);
            }
        }
    }

    private void convertRadiusToMeters(MapView mapView) {

        Rect mScreenRect = mapView.getIntrinsicScreenRect(null);

        int screenWidth = mScreenRect.right - mScreenRect.left;
        int screenHeight = mScreenRect.bottom - mScreenRect.top;

        BoundingBoxE6 bb = mapView.getBoundingBox();

        double diagonalInMeters = bb.getDiagonalLengthInMeters();
        double diagonalInPixels = Math.sqrt(screenWidth * screenWidth + screenHeight * screenHeight);
        double metersInPixel = diagonalInMeters / diagonalInPixels;

        mRadiusInMeters = mRadiusInPixels * metersInPixel;
    }

}
