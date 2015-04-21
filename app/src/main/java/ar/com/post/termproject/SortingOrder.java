package ar.com.post.termproject;

import java.util.EnumSet;

import ar.com.post.termproject.db.ColoursTable;

public enum SortingOrder {
    HSV(1, ColoursTable.COLUMN_HUE + "," + ColoursTable.COLUMN_SATURATION + "," + ColoursTable.COLUMN_VALUE, R.id.radio_sort_hsv),
    HVS(2, ColoursTable.COLUMN_HUE + "," + ColoursTable.COLUMN_VALUE + "," + ColoursTable.COLUMN_SATURATION, R.id.radio_sort_hvs),
    SHV(3, ColoursTable.COLUMN_SATURATION + "," + ColoursTable.COLUMN_HUE + "," + ColoursTable.COLUMN_VALUE, R.id.radio_sort_shv),
    SVH(4, ColoursTable.COLUMN_SATURATION + "," + ColoursTable.COLUMN_VALUE + "," + ColoursTable.COLUMN_HUE, R.id.radio_sort_svh),
    VHS(5, ColoursTable.COLUMN_VALUE + "," + ColoursTable.COLUMN_HUE + "," + ColoursTable.COLUMN_SATURATION, R.id.radio_sort_vhs),
    VSH(6, ColoursTable.COLUMN_VALUE + "," + ColoursTable.COLUMN_SATURATION + "," + ColoursTable.COLUMN_HUE, R.id.radio_sort_vsh);

    private final static EnumSet<SortingOrder> sSet = EnumSet.allOf(SortingOrder.class);

    public final int mId;
    public final int mRadioId;
    public final String mSortQuery;

    SortingOrder(int id, String sortQuery, int radioId) {
        mId = id;
        mRadioId = radioId;
        mSortQuery = sortQuery;
    }

    public static SortingOrder findById(int id) {
        for (SortingOrder sortingOrder : sSet) {
            if (sortingOrder.mId == id) {
                return sortingOrder;
            }
        }
        return null;
    }

    public static SortingOrder findByRadioId(int radioId) {
        for (SortingOrder sortingOrder : sSet) {
            if (sortingOrder.mRadioId == radioId) {
                return sortingOrder;
            }
        }
        return null;
    }

    public interface SortingOrderActivity {
        public SortingOrder getSortingOrder();

        public void setSortingOrder(SortingOrder sortingOrder);
    }
}

