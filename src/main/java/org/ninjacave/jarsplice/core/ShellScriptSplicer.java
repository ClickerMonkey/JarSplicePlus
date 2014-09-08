package org.ninjacave.jarsplice.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

public class ShellScriptSplicer extends Splicer {
    String[] batchFile = {"#!/bin/sh", "FNAME=\"`readlink -f \"$0\"`\"", "java -jar \"$FNAME\"", "exit 0", ""};

    public void createFatJar (String[] jars, String[] natives, String output, String mainClass, String vmArgs)
            throws Exception {
        this.dirs.clear();

        FileOutputStream fos = new FileOutputStream(output);

        PrintStream pos = new PrintStream(fos);

        for (String aBatchFile : this.batchFile) {
            pos.println(aBatchFile);
        }

        pos.flush();
        fos.flush();

        Manifest manifest = getManifest(mainClass, vmArgs);
        JarOutputStream jos = new JarOutputStream(fos, manifest);
        try {
            addFilesFromJars(jars, jos);
            addNativesToJar(natives, jos);
            addJarSpliceLauncher(jos);
        } finally {
            jos.close();
            pos.close();
            fos.close();
        }
    }

    protected void addNativesToJar (String[] natives, JarOutputStream out)
            throws Exception {
        for (String aNative : natives) {
            if (aNative.endsWith(".so")) {
                InputStream in = new FileInputStream(aNative);

                out.putNextEntry(new ZipEntry(getFileName(aNative)));

                while ((this.bufferSize = in.read(this.buffer, 0, this.buffer.length)) != -1) {
                    out.write(this.buffer, 0, this.bufferSize);
                }

                in.close();
                out.closeEntry();
            }
        }
    }
}
