package utils;
public final class DiscountUtils {

    private DiscountUtils() {
        // prevent object creation
    }

    /**
     * Resolves discount level based on input string
     * @return 0 = Transaction level, 1 = Line item level
     */
    public static int resolveDiscountLevel(String discountLevel) {

        if (discountLevel == null || discountLevel.trim().isEmpty()
                || "At Transaction Level".equalsIgnoreCase(discountLevel)) {
            return 0;
        }

        if ("At Line Item Level".equalsIgnoreCase(discountLevel)) {
            return 1;
        }

        // default safe fallback
        return 0;
    }
}