import java.util.concurrent.TimeUnit;

/**
 * Implementation the simplest class of one-dimensional cellular automata. Elementary cellular automata have two
 * possible values for each cell (0 or 1), and rules that depend only on nearest neighbor values. As a result,
 * the evolution of an elementary cellular automaton can completely be described by a table specifying the state
 * a given cell will have in the next generation based on the value of the cell to its left, the value the cell itself,
 * and the value of the cell to its right.
 *
 * @author Jan Paw
 * @see <a href= "http://mathworld.wolfram.com/ElementaryCellularAutomaton.html">WolframMathWorld</a>
 * @see <a href= "https://github.com/Explicite">Explicite on GitHub</a>
 *      <p/>
 *      AGH - Modelowanie wieloskalowe 2012-04-03
 */
class CellularAutomaton {
    private boolean[] space;
    private boolean[] spaceTemp;
    private boolean[] rule;
    private boolean displayFlag;
    private volatile int iterationsCounter = 0;
    private Display display = new Display();
    private int maxIterations;

    /**
     * @param size          cells number
     * @param rule          stat integer number
     * @param boundaryValue value boundary cells
     * @param displayFlag   value ? continuous : single-line
     */
    public CellularAutomaton(int size, int rule, Boolean boundaryValue, Boolean displayFlag) {
        this.space = new boolean[size];
        this.spaceTemp = new boolean[this.space.length];
        this.rule = generateRule(rule);
        this.displayFlag = displayFlag;

        space[space.length / 2] = true;
        if (boundaryValue) {
            space[0] = true;
            space[space.length - 1] = true;
        }
    }


    /**
     * Computing next cellular population.
     */
    private synchronized void nextStep() {
        boolean next, actual, prev;
        for (int i = 0; i < space.length; i++) {

            prev = space[(((i - 1) % space.length) + space.length) % space.length];
            actual = space[i];
            next = space[(((i + 1) % space.length) + space.length) % space.length];

            if (prev && actual && next) {
                spaceTemp[i] = rule[0];
            } else if (prev && actual && !next) {
                spaceTemp[i] = rule[1];
            } else if (prev && !actual && next) {
                spaceTemp[i] = rule[2];
            } else if (prev && !actual && !next) {
                spaceTemp[i] = rule[3];
            } else if (!prev && actual && next) {
                spaceTemp[i] = rule[4];
            } else if (!prev && actual && !next) {
                spaceTemp[i] = rule[5];
            } else if (!prev && !actual && next) {
                spaceTemp[i] = rule[6];
            } else if (!prev && !actual && !next) {
                spaceTemp[i] = rule[7];
            }
        }

        System.arraycopy(spaceTemp, 0, space, 0, space.length);
    }

    /**
     * Running new thread and start computing.
     *
     * @param iterations number of iterations
     */
    public void iterations(int iterations) {
        maxIterations = iterations;
        display.run();
    }

    /**
     * Transform integer value to boolean array.
     *
     * @param rule Wolfram rule
     * @return new rule
     */
    public boolean[] generateRule(int rule) {
        boolean[] tmpRule = new boolean[8];
        String str = Integer.toBinaryString(rule);
        char[] chr = str.toCharArray();
        int div = 8 - chr.length;

        for (int i = 0; i < div; i++)
            tmpRule[i] = false;

        for (int i = 0; i < chr.length; i++)
            tmpRule[i + div] = (chr[i] == '1') ? true : false;

        return tmpRule;
    }

    /**
     * Display thread.
     *
     * @author Jan Paw
     */
    class Display extends Thread {
        private volatile boolean running = true;
        private int step = 0;

        /**
         * Method for display actual population.
         */
        private synchronized void show() {
            for (int i = 0; i < space.length; i++) {
                if (space[i]) {
                    //System.out.print("#");
                    System.out.print("■");
                } else {
                    //System.out.print(" ");
                    System.out.print("□");
                }
            }

            if (displayFlag)
                System.out.print("\r");
            else
                System.out.print("\n");
            iterationsCounter++;
        }

        @Override
        public void run() {
            this.step = (displayFlag) ? 500 : 1;
            while (running && (iterationsCounter < maxIterations)) {
                try {
                    show();
                    nextStep();
                    TimeUnit.MILLISECONDS.sleep(step);
                } catch (InterruptedException ie) {
                    running = false;
                }
            }
        }
    }
}