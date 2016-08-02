
package PlanGenerator;
/**
 *
 * @author navid roohi broojeni
 */
 /**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.*;

public class PlanGenerator {
   
    // Plan's dimension
    private int planDimension;
    // List of building setBacks
    private List<Double> setBacks;
    // Check if there is a wall to other sides ? 
    private boolean[][] up, right, down,left, stopped;    

     public static void main(String[] args) {
        //  main size
        int n = 5;  
        // plot size here (needs to adjust later)
        DrawLib.setPLotSize(600,400);
        PlanGenerator plan = new PlanGenerator(n);
        // turn on double buffering for graphics
        DrawLib.enableDoubleBuffering();
        plan.drawPlan();
    }
    
    // Constructor
    public PlanGenerator(int n) {
        this.planDimension = n;
        DrawLib.setHorizontalSize(0, n+2);
        DrawLib.setVerticalSize(0, n+2);
        startInit();
        produce();
        setGrid(n);
    }

    // Initialization stage
    private void startInit() {
        // initialize border block as already visited
        stopped = new boolean[planDimension+2][planDimension+2];
        for (int x = 0; x < planDimension+2; x++) {
            stopped[x][0] = true;
            stopped[x][planDimension+1] = true;
        }
        for (int y = 0; y < planDimension+2; y++) {
            stopped[0][y] = true;
            stopped[planDimension+1][y] = true;
        }

        // initialize all walls as 
        up = new boolean[planDimension+2][planDimension+2];
        right  = new boolean[planDimension+2][planDimension+2];
        down = new boolean[planDimension+2][planDimension+2];
        left  = new boolean[planDimension+2][planDimension+2];
        for (int x = 0; x < planDimension+2; x++) {
            for (int y = 0; y < planDimension+2; y++) {
                up[x][y] = true;
                right[x][y]  = true;
                down[x][y] = true;
                left[x][y]  = true;
            }
        }
    }


    // generate the plan
    private void generate(int x, int y) {
        stopped[x][y] = true;

        // as long as there is not stopped block
        while (!stopped[x][y+1] || !stopped[x+1][y] || !stopped[x][y-1] || !stopped[x-1][y]) {

            // select random block (This part needs to have some rules instead of being random) 
            while (true) {
                double r = RandomGenerator.uniform(4);
                if (r == 0 && !stopped[x][y+1]) {
                    up[x][y] = false;
                    down[x][y+1] = false;
                    generate(x, y + 1);
                    break;
                }
                else if (r == 1 && !stopped[x+1][y]) {
                    right[x][y] = false;
                    left[x+1][y] = false;
                    generate(x+1, y);
                    break;
                }
                else if (r == 2 && !stopped[x][y-1]) {
                    down[x][y] = false;
                    up[x][y-1] = false;
                    generate(x, y-1);
                    break;
                }
                else if (r == 3 && !stopped[x-1][y]) {
                    left[x][y] = false;
                    right[x-1][y] = false;
                    generate(x-1, y);
                    break;
                }
            }
        }
    }

    // produce the plan starting from lower left
    private void produce() {
        generate(1, 1);
          // build more walls to make it more closed space if designer wish to
//        for (int i = 0; i < 10; i++) {
//            int x = n/2 + RandomGenerator.uniform(n/2);
//            int y = n/2 + RandomGenerator.uniform(n/2);
//            right[x][y] = left[x+1][y] = true;
//        }
        // remove some walls to make it more open space
        for (int i = 0; i < planDimension; i++) {
            int x = 1 + RandomGenerator.uniform(planDimension-1);
            int y = 1 + RandomGenerator.uniform(planDimension-1);
            up[x][y] = down[x][y+1] = false;
        }
    }

    // draw the plan
    public void drawPlan() {
        // color
        DrawLib.setPenColor(DrawLib.GRAY);
        // wall thickness
        DrawLib.setPenRadius(.02); // 20 cm
        for (int x = 1; x <= planDimension; x++) {
            for (int y = 1; y <= planDimension; y++) {
                if (down[x][y]) DrawLib.wall(x, y, x+1, y);
                if (up[x][y]) DrawLib.wall(x, y+1, x+1, y+1);
                if (left[x][y])  DrawLib.wall(x, y, x, y+1);
                if (right[x][y])  DrawLib.wall(x+1, y, x+1, y+1);
            }
        }
        DrawLib.show();
    }
      // Grid
    public static void setGrid(int n){
        DrawLib.setPenColor(DrawLib.BLUE);
        DrawLib.setPenRadius(.001); // 20 cm
        //horizontal Lines
        for (int a=0; a<=n+2; a++){
              for (int b=0; b<n+2; b++){
                  DrawLib.wall(a, b, a+1, b);
                }
           }
        //Vertical 
        for (int a=0; a<=n+2; a++){
              for (int b=0; b<n+2; b++){
                  DrawLib.wall(a, b, a, b+1);
                }
           }
    }
}
