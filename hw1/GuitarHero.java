import synthesizer.GuitarString;

import java.util.ArrayList;
import java.util.List;

public class GuitarHero {
    private static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        List<GuitarString> guitarStrings = new ArrayList<>();
        for (int i = 0;i<keyboard.length();i++) {
            GuitarString temp = new GuitarString(getHZ(i));
            guitarStrings.add(temp);
        }
        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index != -1) {
                    guitarStrings.get(index).pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = guitarStrings.stream().mapToDouble(GuitarString::sample).sum();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString guitarString : guitarStrings) {
                guitarString.tic();
            }
        }
    }
    private static double getHZ(int index) {
        return 440.0 * Math.pow(2,(index-24.0) / 12.0);
    }
}
