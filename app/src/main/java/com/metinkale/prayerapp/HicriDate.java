/*
 * Copyright (c) 2013-2017 Metin Kale
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.metinkale.prayerapp;

import android.support.annotation.NonNull;

import com.metinkale.prayerapp.settings.Prefs;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.chrono.IslamicChronology;

import java.util.ArrayList;
import java.util.List;

public class HicriDate {

    public static final int HD = 0;
    public static final int HM = 1;
    public static final int HY = 2;
    public static final int GD = 3;
    public static final int GM = 4;
    public static final int GY = 5;
    public static final int DAY = 6;
    @NonNull
    private static final int[][] mDates;

    @SuppressWarnings("WeakerAccess")
    public final int Year;
    @SuppressWarnings("WeakerAccess")
    public final int Month;
    @SuppressWarnings("WeakerAccess")
    public final int Day;

    public HicriDate(int y, int m, int d) {
        Year = y;
        Month = m;
        Day = d;
    }

    public HicriDate(LocalDate greg) {
        // int[] key = {d, m, y};
        //int[] ret = mCache.get(key);
        //if (ret != null) return ret;
        int hfix = Prefs.getHijriFix();
        if (hfix != 0) {
            greg = greg.plusDays(hfix);
        }
        int d = greg.getDayOfMonth();
        int m = greg.getMonthOfYear();
        int y = greg.getYear();

        int[] last = null;
        for (int[] date : mDates) {
            if (date[GY] < y) {
                last = date;
            } else if ((date[GY] == y) && (date[GM] < m)) {
                last = date;
            } else if ((date[GY] == y) && (date[GM] == m) && (date[GD] <= d)) {
                last = date;
            } else {
                break;
            }
        }
        if (last == null) {
            LocalDate date = greg.toDateTimeAtStartOfDay().withChronology(IslamicChronology.getInstance()).toLocalDate();
            Year = date.getYear();
            Month = date.getMonthOfYear();
            Day = date.getDayOfMonth();
        } else {
            int[] h = {last[HD], last[HM], last[HY]};
            h[0] += Period.fieldDifference(new LocalDate(y, m, d), new LocalDate(last[GY], last[GM], last[GD])).getDays();
            if ((h[0] >= 30) || (h[0] <= 0)) {
                LocalDate date = greg.toDateTimeAtStartOfDay().withChronology(IslamicChronology.getInstance()).toLocalDate();
                Year = date.getYear();
                Month = date.getMonthOfYear();
                Day = date.getDayOfMonth();
            } else {
                Year = h[HY];
                Month = h[HM];
                Day = h[HD];
            }
        }
    }


    @NonNull
    public static List<int[]> getHolydays(int year) {
        List<int[]> dates = new ArrayList<>();
        for (int[] d : mDates) {
            if ((d[GY] == year) && (d[DAY] != 0)) {
                dates.add(d);
            }
        }
        return dates;
    }


    public static final int MIN_YEAR = 2012;
    public static final int MAX_YEAR = 2022;

    static {
        mDates = new int[][]{
                {1, 3, 1433, 24, 1, 2012, 0},
                {11, 3, 1433, 3, 2, 2012, 3},
                {1, 4, 1433, 23, 2, 2012, 0},
                {1, 5, 1433, 23, 3, 2012, 0},
                {1, 6, 1433, 22, 4, 2012, 0},
                {1, 7, 1433, 22, 5, 2012, 4},
                {3, 7, 1433, 24, 5, 2012, 5},
                {26, 7, 1433, 16, 6, 2012, 6},
                {1, 8, 1433, 21, 6, 2012, 0},
                {14, 8, 1433, 4, 7, 2012, 7},
                {1, 9, 1433, 20, 7, 2012, 8},
                {26, 9, 1433, 14, 8, 2012, 9},
                {30, 9, 1433, 18, 8, 2012, 10},
                {1, 10, 1433, 19, 8, 2012, 11},
                {2, 10, 1433, 20, 8, 2012, 12},
                {3, 10, 1433, 21, 8, 2012, 13},
                {1, 11, 1433, 17, 9, 2012, 0},
                {1, 12, 1433, 16, 10, 2012, 0},
                {9, 12, 1433, 24, 10, 2012, 14},
                {10, 12, 1433, 25, 10, 2012, 15},
                {11, 12, 1433, 26, 10, 2012, 16},
                {12, 12, 1433, 27, 10, 2012, 17},
                {13, 12, 1433, 28, 10, 2012, 18},
                {1, 1, 1434, 15, 11, 2012, 1},
                {10, 1, 1434, 24, 11, 2012, 2},
                {1, 2, 1434, 14, 12, 2012, 0},
                {1, 3, 1434, 13, 1, 2013, 0},
                {11, 3, 1434, 23, 1, 2013, 3},
                {1, 4, 1434, 11, 2, 2013, 0},
                {1, 5, 1434, 13, 3, 2013, 0},
                {1, 6, 1434, 11, 4, 2013, 0},
                {1, 7, 1434, 11, 5, 2013, 4},
                {6, 7, 1434, 16, 5, 2013, 5},
                {26, 7, 1434, 6, 6, 2013, 6},
                {1, 8, 1434, 10, 6, 2013, 0},
                {14, 8, 1434, 23, 6, 2013, 7},
                {1, 9, 1434, 9, 7, 2013, 8},
                {26, 9, 1434, 3, 8, 2013, 9},
                {30, 9, 1434, 7, 8, 2013, 10},
                {1, 10, 1434, 8, 8, 2013, 11},
                {2, 10, 1434, 9, 8, 2013, 12},
                {3, 10, 1434, 10, 8, 2013, 13},
                {1, 11, 1434, 7, 9, 2013, 0},
                {1, 12, 1434, 6, 10, 2013, 0},
                {9, 12, 1434, 14, 10, 2013, 14},
                {10, 12, 1434, 15, 10, 2013, 15},
                {11, 12, 1434, 16, 10, 2013, 16},
                {12, 12, 1434, 17, 10, 2013, 17},
                {13, 12, 1434, 18, 10, 2013, 18},
                {1, 1, 1435, 4, 11, 2013, 1},
                {10, 1, 1435, 13, 11, 2013, 2},
                {1, 2, 1435, 4, 12, 2013, 0},
                {1, 3, 1435, 2, 1, 2014, 0},
                {11, 3, 1435, 12, 1, 2014, 3},
                {1, 4, 1435, 1, 2, 2014, 0},
                {1, 5, 1435, 2, 3, 2014, 0},
                {1, 6, 1435, 1, 4, 2014, 0},
                {1, 7, 1435, 30, 4, 2014, 4},
                {2, 7, 1435, 1, 5, 2014, 5},
                {26, 7, 1435, 25, 5, 2014, 6},
                {1, 8, 1435, 30, 5, 2014, 0},
                {14, 8, 1435, 12, 6, 2014, 7},
                {1, 9, 1435, 28, 6, 2014, 8},
                {26, 9, 1435, 23, 7, 2014, 9},
                {30, 9, 1435, 27, 7, 2014, 10},
                {1, 10, 1435, 28, 7, 2014, 11},
                {2, 10, 1435, 29, 7, 2014, 12},
                {3, 10, 1435, 30, 7, 2014, 13},
                {1, 11, 1435, 27, 8, 2014, 0},
                {1, 12, 1435, 25, 9, 2014, 0},
                {9, 12, 1435, 3, 10, 2014, 14},
                {10, 12, 1435, 4, 10, 2014, 15},
                {11, 12, 1435, 5, 10, 2014, 16},
                {12, 12, 1435, 6, 10, 2014, 17},
                {13, 12, 1435, 7, 10, 2014, 18},
                {1, 1, 1436, 25, 10, 2014, 1},
                {10, 1, 1436, 3, 11, 2014, 2},
                {1, 2, 1436, 24, 11, 2014, 0},
                {1, 3, 1436, 23, 12, 2014, 0},
                {11, 3, 1436, 2, 1, 2015, 3},
                {1, 4, 1436, 22, 1, 2015, 0},
                {1, 5, 1436, 20, 2, 2015, 0},
                {1, 6, 1436, 21, 3, 2015, 0},
                {1, 7, 1436, 20, 4, 2015, 4},
                {4, 7, 1436, 23, 4, 2015, 5},
                {26, 7, 1436, 15, 5, 2015, 6},
                {1, 8, 1436, 19, 5, 2015, 0},
                {14, 8, 1436, 1, 6, 2015, 7},
                {1, 9, 1436, 18, 6, 2015, 8},
                {26, 9, 1436, 13, 7, 2015, 9},
                {29, 9, 1436, 16, 7, 2015, 10},
                {1, 10, 1436, 17, 7, 2015, 11},
                {2, 10, 1436, 18, 7, 2015, 12},
                {3, 10, 1436, 19, 7, 2015, 13},
                {1, 11, 1436, 16, 8, 2015, 0},
                {1, 12, 1436, 15, 9, 2015, 0},
                {9, 12, 1436, 23, 9, 2015, 14},
                {10, 12, 1436, 24, 9, 2015, 15},
                {11, 12, 1436, 25, 9, 2015, 16},
                {12, 12, 1436, 26, 9, 2015, 17},
                {13, 12, 1436, 27, 9, 2015, 18},
                {1, 1, 1437, 14, 10, 2015, 1},
                {10, 1, 1437, 23, 10, 2015, 2},
                {1, 2, 1437, 13, 11, 2015, 0},
                {1, 3, 1437, 12, 12, 2015, 0},
                {11, 3, 1437, 22, 12, 2015, 3},
                {1, 4, 1437, 11, 1, 2016, 0},
                {1, 5, 1437, 10, 2, 2016, 0},
                {1, 6, 1437, 10, 3, 2016, 0},
                {29, 6, 1437, 7, 4, 2016, 5},
                {1, 7, 1437, 8, 4, 2016, 4},
                {26, 7, 1437, 3, 5, 2016, 6},
                {1, 8, 1437, 8, 5, 2016, 0},
                {14, 8, 1437, 21, 5, 2016, 7},
                {1, 9, 1437, 6, 6, 2016, 8},
                {26, 9, 1437, 1, 7, 2016, 9},
                {29, 9, 1437, 4, 7, 2016, 10},
                {1, 10, 1437, 5, 7, 2016, 11},
                {2, 10, 1437, 6, 7, 2016, 12},
                {3, 10, 1437, 7, 7, 2016, 13},
                {1, 11, 1437, 4, 8, 2016, 0},
                {1, 12, 1437, 3, 9, 2016, 0},
                {9, 12, 1437, 11, 9, 2016, 14},
                {10, 12, 1437, 12, 9, 2016, 15},
                {11, 12, 1437, 13, 9, 2016, 16},
                {12, 12, 1437, 14, 9, 2016, 17},
                {13, 12, 1437, 15, 9, 2016, 18},
                {1, 1, 1438, 2, 10, 2016, 1},
                {10, 1, 1438, 11, 10, 2016, 2},
                {1, 2, 1438, 1, 11, 2016, 0},
                {1, 3, 1438, 1, 12, 2016, 0},
                {11, 3, 1438, 11, 12, 2016, 3},
                {1, 4, 1438, 30, 12, 2016, 0},
                {1, 5, 1438, 29, 1, 2017, 0},
                {1, 6, 1438, 28, 2, 2017, 0},
                {1, 7, 1438, 29, 3, 2017, 4},
                {2, 7, 1438, 30, 3, 2017, 5},
                {26, 7, 1438, 23, 4, 2017, 6},
                {1, 8, 1438, 27, 4, 2017, 0},
                {14, 8, 1438, 10, 5, 2017, 7},
                {1, 9, 1438, 27, 5, 2017, 8},
                {26, 9, 1438, 21, 6, 2017, 9},
                {29, 9, 1438, 24, 6, 2017, 10},
                {1, 10, 1438, 25, 6, 2017, 11},
                {2, 10, 1438, 26, 6, 2017, 12},
                {3, 10, 1438, 27, 6, 2017, 13},
                {1, 11, 1438, 24, 7, 2017, 0},
                {1, 12, 1438, 23, 8, 2017, 0},
                {9, 12, 1438, 31, 8, 2017, 14},
                {10, 12, 1438, 1, 9, 2017, 15},
                {11, 12, 1438, 2, 9, 2017, 16},
                {12, 12, 1438, 3, 9, 2017, 17},
                {13, 12, 1438, 4, 9, 2017, 18},
                {1, 1, 1439, 21, 9, 2017, 1},
                {10, 1, 1439, 30, 9, 2017, 2},
                {1, 2, 1439, 21, 10, 2017, 0},
                {1, 3, 1439, 20, 11, 2017, 0},
                {11, 3, 1439, 30, 11, 2017, 3},
                {1, 4, 1439, 19, 12, 2017, 0},
                {23, 4, 1439, 10, 1, 2018, 0},
                {1, 5, 1439, 18, 1, 2018, 0},
                {1, 6, 1439, 17, 2, 2018, 0},
                {1, 7, 1439, 19, 3, 2018, 4},
                {4, 7, 1439, 22, 3, 2018, 5},
                {26, 7, 1439, 13, 4, 2018, 6},
                {1, 8, 1439, 17, 4, 2018, 0},
                {14, 8, 1439, 30, 4, 2018, 7},
                {1, 9, 1439, 16, 5, 2018, 8},
                {26, 9, 1439, 10, 6, 2018, 9},
                {30, 9, 1439, 14, 6, 2018, 10},
                {1, 10, 1439, 15, 6, 2018, 11},
                {2, 10, 1439, 16, 6, 2018, 12},
                {3, 10, 1439, 17, 6, 2018, 13},
                {1, 11, 1439, 14, 7, 2018, 0},
                {1, 12, 1439, 12, 8, 2018, 0},
                {9, 12, 1439, 20, 8, 2018, 14},
                {10, 12, 1439, 21, 8, 2018, 15},
                {11, 12, 1439, 22, 8, 2018, 16},
                {12, 12, 1439, 23, 8, 2018, 17},
                {13, 12, 1439, 24, 8, 2018, 18},
                {1, 1, 1440, 11, 9, 2018, 1},
                {10, 1, 1440, 20, 9, 2018, 2},
                {1, 2, 1440, 10, 10, 2018, 0},
                {1, 3, 1440, 9, 11, 2018, 0},
                {11, 3, 1440, 19, 11, 2018, 3},
                {1, 4, 1440, 8, 12, 2018, 0},
                {1, 5, 1440, 7, 1, 2019, 0},
                {1, 6, 1440, 6, 2, 2019, 0},
                {30, 6, 1440, 7, 3, 2019, 5},
                {1, 7, 1440, 8, 3, 2019, 4},
                {26, 7, 1440, 2, 4, 2019, 6},
                {1, 8, 1440, 6, 4, 2019, 0},
                {14, 8, 1440, 19, 4, 2019, 7},
                {1, 9, 1440, 6, 5, 2019, 8},
                {26, 9, 1440, 31, 5, 2019, 9},
                {30, 9, 1440, 4, 6, 2019, 10},
                {1, 10, 1440, 5, 6, 2019, 11},
                {2, 10, 1440, 6, 6, 2019, 12},
                {3, 10, 1440, 7, 6, 2019, 13},
                {1, 11, 1440, 4, 7, 2019, 0},
                {1, 12, 1440, 2, 8, 2019, 0},
                {9, 12, 1440, 10, 8, 2019, 14},
                {10, 12, 1440, 11, 8, 2019, 15},
                {11, 12, 1440, 12, 8, 2019, 16},
                {12, 12, 1440, 13, 8, 2019, 17},
                {13, 12, 1440, 14, 8, 2019, 18},
                {1, 1, 1441, 31, 8, 2019, 1},
                {10, 1, 1441, 9, 9, 2019, 2},
                {1, 2, 1441, 30, 9, 2019, 0},
                {1, 3, 1441, 29, 10, 2019, 0},
                {11, 3, 1441, 8, 11, 2019, 3},
                {1, 4, 1441, 28, 11, 2019, 0},
                {1, 5, 1441, 27, 12, 2019, 0},
                {1, 6, 1441, 26, 1, 2020, 0},
                {1, 7, 1441, 25, 2, 2020, 4},
                {3, 7, 1441, 27, 2, 2020, 5},
                {26, 7, 1441, 21, 3, 2020, 6},
                {1, 8, 1441, 25, 3, 2020, 0},
                {14, 8, 1441, 7, 4, 2020, 7},
                {1, 9, 1441, 24, 4, 2020, 8},
                {26, 9, 1441, 19, 5, 2020, 9},
                {30, 9, 1441, 23, 5, 2020, 10},
                {1, 10, 1441, 24, 5, 2020, 11},
                {2, 10, 1441, 25, 5, 2020, 12},
                {3, 10, 1441, 26, 5, 2020, 13},
                {1, 11, 1441, 22, 6, 2020, 0},
                {1, 12, 1441, 22, 7, 2020, 0},
                {9, 12, 1441, 30, 7, 2020, 14},
                {10, 12, 1441, 31, 7, 2020, 15},
                {11, 12, 1441, 1, 8, 2020, 16},
                {12, 12, 1441, 2, 8, 2020, 17},
                {13, 12, 1441, 3, 8, 2020, 18},
                {1, 1, 1442, 20, 8, 2020, 1},
                {10, 1, 1442, 29, 8, 2020, 2},
                {1, 2, 1442, 18, 9, 2020, 0},
                {1, 3, 1442, 18, 10, 2020, 0},
                {11, 3, 1442, 28, 10, 2020, 3},
                {1, 4, 1442, 16, 11, 2020, 0},
                {1, 5, 1442, 16, 12, 2020, 0},
                {1, 6, 1442, 14, 1, 2021, 0},
                {1, 7, 1442, 13, 2, 2021, 4},
                {6, 7, 1442, 18, 2, 2021, 5},
                {26, 7, 1442, 10, 3, 2021, 6},
                {1, 8, 1442, 14, 3, 2021, 0},
                {14, 8, 1442, 27, 3, 2021, 7},
                {1, 9, 1442, 13, 4, 2021, 8},
                {26, 9, 1442, 8, 5, 2021, 9},
                {30, 9, 1442, 12, 5, 2021, 10},
                {1, 10, 1442, 13, 5, 2021, 11},
                {2, 10, 1442, 14, 5, 2021, 12},
                {3, 10, 1442, 15, 5, 2021, 13},
                {1, 11, 1442, 12, 6, 2021, 0},
                {1, 12, 1442, 11, 7, 2021, 0},
                {9, 12, 1442, 19, 7, 2021, 14},
                {10, 12, 1442, 20, 7, 2021, 15},
                {11, 12, 1442, 21, 7, 2021, 16},
                {12, 12, 1442, 22, 7, 2021, 17},
                {13, 12, 1442, 23, 7, 2021, 18},
                {1, 1, 1443, 10, 8, 2021, 1},
                {10, 1, 1443, 19, 8, 2021, 2},
                {1, 2, 1443, 8, 9, 2021, 0},
                {1, 3, 1443, 7, 10, 2021, 0},
                {11, 3, 1443, 17, 10, 2021, 3},
                {1, 4, 1443, 6, 11, 2021, 0},
                {1, 5, 1443, 5, 12, 2021, 0},
                {1, 6, 1443, 4, 1, 2022, 0},
                {1, 7, 1443, 2, 2, 2022, 4},
                {2, 7, 1443, 3, 2, 2022, 5},
                {26, 7, 1443, 27, 2, 2022, 6},
                {1, 8, 1443, 4, 3, 2022, 0},
                {14, 8, 1443, 17, 3, 2022, 7},
                {1, 9, 1443, 2, 4, 2022, 8},
                {26, 9, 1443, 27, 4, 2022, 9},
                {30, 9, 1443, 1, 5, 2022, 10},
                {1, 10, 1443, 2, 5, 2022, 11},
                {2, 10, 1443, 3, 5, 2022, 12},
                {3, 10, 1443, 4, 5, 2022, 13},
                {1, 11, 1443, 1, 6, 2022, 0},
                {1, 12, 1443, 30, 6, 2022, 0},
                {9, 12, 1443, 8, 7, 2022, 14},
                {10, 12, 1443, 9, 7, 2022, 15},
                {11, 12, 1443, 10, 7, 2022, 16},
                {12, 12, 1443, 11, 7, 2022, 17},
                {13, 12, 1443, 12, 7, 2022, 18},
                {1, 1, 1444, 30, 7, 2022, 1},
                {10, 1, 1444, 8, 8, 2022, 2},
                {1, 2, 1444, 28, 8, 2022, 0},
                {1, 3, 1444, 27, 9, 2022, 0},
                {11, 3, 1444, 7, 10, 2022, 3},
                {1, 4, 1444, 27, 10, 2022, 0},
                {1, 5, 1444, 25, 11, 2022, 0},
                {1, 6, 1444, 24, 12, 2022, 0}};
    }

    public static int isHolyday() {
        LocalDate day = LocalDate.now();
        for (int[] date : mDates) {
            if (date[GD] == day.getDayOfMonth() && (date[GM] == day.getMonthOfYear()) && (date[GY] == day.getYear())) {
                return date[DAY];
            }
        }
        return 0;
    }
}