import controller.Controller;
import controller.FileHandling;
import view.LayerSelector;

import java.awt.*;

/**
 *
 */
public class EQSIM {

        /**
         * Main method that starts the program that instantiates a new frame,
         * a new layer, and a new controller
         * @param args command line arguments
         */
        public static void main(String[] args) {
            boolean newDB = false;
            FileHandling fh = new FileHandling();

            if(newDB) {
                fh.convertDBToShort("resources/quarm_2025-04-12-16_11.sql");
            }

            Frame f = new Frame();
            new LayerSelector(f);

            Controller c = new Controller(fh);
        }
    }
