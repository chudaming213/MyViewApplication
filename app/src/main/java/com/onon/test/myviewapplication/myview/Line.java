package com.onon.test.myviewapplication.myview;

import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/21 0021.
 */

public abstract class Line {
    public Point mStartPoint = new Point(0,0);
    public Point mEndPoint = new Point(0,0);
    public ArrayList<Point> points=new ArrayList<>();
}
