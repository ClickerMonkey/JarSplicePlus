package co.notime.maven.splice;

import com.elezeta.jarspliceplus.JarSplicePlus;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * User: lachlan.krautz
 * Date: 8/09/2014
 * Time: 8:01 AM
 */
public class Mojo extends AbstractMojo {

    @Override
    public void execute () throws MojoExecutionException, MojoFailureException {
        JarSplicePlus j = new JarSplicePlus();
        getLog().info("sup sup sup");
    }
}

