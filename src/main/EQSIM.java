package main;

import main.controller.Controller;
import main.controller.FileHandling;
import main.view.LayerSelector;

import java.awt.*;

/**
 *
 */
public class EQSIM {

        /**
         * Main method that starts the program that instantiates a new frame,
         * a new layer, and a new main.test.controller
         * @param args command line arguments
         */
        public static void main(String[] args) {
            boolean newDB = false;
            FileHandling fh = new FileHandling();

            if(newDB) {
                fh.convertDBToShort("resources/quarm_2026-01-01-13_56.sql");
            }

            Frame f = new Frame();
            new LayerSelector(f);

            Controller c = new Controller(fh);
        }
    }
