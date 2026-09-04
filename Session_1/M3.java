public class M3 {

    static String getBmiStatus(double bmi) {

        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    static void printWellnessReport(double[] heights, double[] weights) {

        System.out.println("-----------------------------------------------------");
        System.out.println("Person | Height(m) | Weight(kg) | BMI  | Status");
        System.out.println("-----------------------------------------------------");

        for (int i = 0; i < heights.length; i++) {

            double bmi = weights[i] / (heights[i] * heights[i]);

            String status = getBmiStatus(bmi);

            System.out.printf(
                "%-6d | %-10.2f | %-10.2f | %-4.2f | %s%n",
                i + 1,
                heights[i],
                weights[i],
                bmi,
                status
            );
        }

        System.out.println("-----------------------------------------------------");
    }

    public static void main(String[] args) {

        double[] heights = {
            1.60, 1.70, 1.75, 1.80, 1.65
        };

        double[] weights = {
            45, 65, 80, 100, 55
        };

        printWellnessReport(heights, weights);
    }
}