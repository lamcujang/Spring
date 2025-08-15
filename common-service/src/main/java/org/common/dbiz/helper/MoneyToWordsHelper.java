package org.common.dbiz.helper;

public final class MoneyToWordsHelper {

    private static final String[] NUMBER_TEXT = {
            "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"
    };

    private static final String[] UNIT_NAMES = {
            "", "nghìn", "triệu", "tỷ"
    };

    private MoneyToWordsHelper() {}

    public static String toVietnameseWords(double amount) {
        long money = (long) amount;
        if (money == 0) return "Không đồng";
        String result = readNumber(money).trim();
        return capitalizeFirst(result) + " đồng";
    }

    private static String readNumber(long number) {
        StringBuilder result = new StringBuilder();
        int unitIndex = 0;
        boolean hasPreviousGroup = false;

        // Tìm nhóm đầu tiên khác 0
        long temp = number;
        int highestNonZeroGroup = -1;
        int index = 0;
        while (temp > 0) {
            if (temp % 1000 != 0) highestNonZeroGroup = index;
            temp /= 1000;
            index++;
        }

        while (number > 0) {
            int threeDigits = (int) (number % 1000);
            boolean isFirstGroup = (unitIndex == highestNonZeroGroup);
            if (threeDigits > 0 || unitIndex == 0) {
                String part = readThreeDigits(threeDigits, hasPreviousGroup, isFirstGroup);
                if (!part.isEmpty()) {
                    result.insert(0, part + " " + UNIT_NAMES[unitIndex] + " ");
                    hasPreviousGroup = true;
                }
            }
            number /= 1000;
            unitIndex++;
        }

        return result.toString().replaceAll("\\s+", " ").trim();
    }

    private static String readThreeDigits(int number, boolean hasHigherGroup, boolean isFirstGroup) {
        int hundred = number / 100;
        int tenUnit = number % 100;
        int ten = tenUnit / 10;
        int unit = tenUnit % 10;
        StringBuilder sb = new StringBuilder();

        if (hundred > 0) {
            sb.append(NUMBER_TEXT[hundred]).append(" trăm");
        } else if (hasHigherGroup && tenUnit > 0) {
            sb.append("không trăm");
        } else if (!hasHigherGroup && !isFirstGroup && tenUnit > 0) {
            sb.append("không trăm");
        }

        if (ten > 1) {
            sb.append(" ").append(NUMBER_TEXT[ten]).append(" mươi");
            if (unit == 1) {
                sb.append(" mốt");
            } else if (unit == 5) {
                sb.append(" lăm");
            } else if (unit != 0) {
                sb.append(" ").append(NUMBER_TEXT[unit]);
            }
        } else if (ten == 1) {
            sb.append(" mười");
            if (unit == 5) {
                sb.append(" lăm");
            } else if (unit != 0) {
                sb.append(" ").append(NUMBER_TEXT[unit]);
            }
        } else if (unit != 0) {
            sb.append(" lẻ ").append(NUMBER_TEXT[unit]);
        }

        return sb.toString().trim();
    }


    private static String capitalizeFirst(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
