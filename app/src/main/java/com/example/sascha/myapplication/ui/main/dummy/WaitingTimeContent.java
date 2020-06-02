package com.example.sascha.myapplication.ui.main.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class WaitingTimeContent {

    /**
     * An array of sample (dummy) items.
     */
    public static  List<WaitingTimeItem> ITEMS = new ArrayList<>();


    public static class WaitingTimeItem {
        public final String id;
        public final String content;
        public final String details;

        public WaitingTimeItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WaitingTimeItem dummyItem = (WaitingTimeItem) o;
            return Objects.equals(id, dummyItem.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
