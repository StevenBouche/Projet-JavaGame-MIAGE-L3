
package share.utils;

        import java.util.Random;

/**
 * The type Handle random.
 */
public class HandleRandom {


    private Random random;

    public HandleRandom(){
        this.random = new Random();
    }

    /**
     * Get random between min max int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    public int getRandomBetweenMinMax(int min, int max){
        if(min < 0 || max < 0) {
            System.out.println(min +" "+max);
        }
        return this.random.nextInt(max - min + 1) + min;
    }

}