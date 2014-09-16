package co.notime.jarspliceplus;

import com.elezeta.jarspliceplus.JarSplicePlus;
import org.ninjacave.jarsplice.gui.JarSpliceFrame;

/**
 * User: lachlan.krautz
 * Date: 14/09/2014
 * Time: 8:03 AM
 */
public class Main {

    public static void main (String[] args) {
        if (args.length == 0) {
            new JarSpliceFrame();
        } else {
            JarSplicePlus j = new JarSplicePlus();
            j.runCmdJarSplice(args);
        }
    }

}
