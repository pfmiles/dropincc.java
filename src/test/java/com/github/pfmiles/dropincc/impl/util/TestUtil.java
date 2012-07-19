package com.github.pfmiles.dropincc.impl.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import com.github.pfmiles.dropincc.Action;
import com.github.pfmiles.dropincc.impl.automataview.DotGenerator;

/**
 * @author pf-miles
 * 
 */
public class TestUtil extends TestCase {
    public void testResolveActionName() {
        Action a = new Action() {

            public Object act(Object... params) {
                return null;
            }
        };
        assertTrue("UtilTest$1".equals(Util.resolveActionName(a)));
    }

    public void testDumpCirclePath() {
        SetStack<String> s = new SetStack<String>();
        s.push("1");
        s.push("2");
        s.push("3");
        assertTrue("1 -> 2 -> 3 -> 1".equals(Util.dumpCirclePath(s, "1")));

        s = new SetStack<String>();
        s.push("1");
        assertTrue("1 -> 1".equals(Util.dumpCirclePath(s, "1")));
    }

    /**
     * Create image according to dotGen and imageName specified, in operating
     * system's temporary directory. (on linux, it's '/tmp' directory)
     * 
     * @param dotGen
     * @param imageName
     * @throws IOException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public static void createPng(DotGenerator dotGen, String imageName) throws IOException, UnsupportedEncodingException, FileNotFoundException, InterruptedException {
        File dotFile = File.createTempFile(imageName, ".dot");
        dotFile.deleteOnExit();
        OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(dotFile), "UTF-8");
        w.write(dotGen.toDotString(imageName, 50, 50));
        w.close();
        String dotFilePath = dotFile.getAbsolutePath();
        File pngFile = File.createTempFile(imageName, ".png");
        Process p = Runtime.getRuntime().exec("dot -Tpng " + dotFilePath);
        System.out.println(p.waitFor());
        byte[] buf = new byte[1024];
        FileOutputStream out = new FileOutputStream(pngFile);
        int read = -1;
        read = p.getInputStream().read(buf);
        while (read != -1) {
            out.write(buf, 0, read);
            read = p.getInputStream().read(buf);
        }
        out.close();
        p.getErrorStream().close();
        p.getInputStream().close();
        p.getOutputStream().close();
    }
}
